package org.library.system.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.library.system.dao.BookDao;
import org.library.system.dao.LoanDao;
import org.library.system.dao.LoanDetailDao;
import org.library.system.dao.LoanRequestDao;
import org.library.system.dao.LoanRequestDetailDao;
import org.library.system.dao.UserDao;
import org.library.system.enums.LoanStatus;
import org.library.system.enums.RequestStatus;
import org.library.system.model.Book;
import org.library.system.model.Loan;
import org.library.system.model.LoanApplication;
import org.library.system.model.LoanDetails;
import org.library.system.model.RequestDetails;
import org.library.system.model.User;
import org.library.system.utils.AlertUtils;
import org.library.system.utils.AppStatus;
import org.library.system.utils.SceneManager;
import org.library.system.utils.SessionManager;
import org.library.system.utils.Validations;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class BorrowingController {

    @FXML private TextField txtStudentId;
    @FXML private TextField txtIsbn;
    @FXML private TextField txtBookTitle;
    @FXML private Label lblStudentName;
    @FXML private Label lblStudentEmail;
    @FXML private Label lblBorrowDate;
    @FXML private DatePicker dpDueDate;
    @FXML private TableView<Book> tableBooks;
    @FXML private TableColumn<Book, String> colIsbn;
    @FXML private TableColumn<Book, String> colTitle;
    @FXML private TableColumn<Book, String> colAuthor;
    @FXML private TableColumn<Book, Integer> colAvailable;
    @FXML private Button btnSearchStudent;
    @FXML private Button btnSearchBook;
    @FXML private Button btnGenerateBorrowing;
    @FXML private Button btnPrintReceipt;
    @FXML private Button btnBack;

    private final UserDao userDao = new UserDao();
    private final BookDao bookDao = new BookDao();
    private final LoanDao loanDao = new LoanDao();
    private final LoanRequestDao loanRequestDao = new LoanRequestDao();
    private final LoanDetailDao loanDetailDao = new LoanDetailDao();
    private final LoanRequestDetailDao loanRequestDetailDao = new LoanRequestDetailDao();
    private final Validations validations = Validations.getInstancevalidations();

    private final ObservableList<Book> bookList = FXCollections.observableArrayList();

    private User selectedStudent;
    private Book selectedBook;

    @FXML
    public void initialize() {
        lblBorrowDate.setText(LocalDate.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        colIsbn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getIsbn()));
        colTitle.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTitle()));
        colAuthor.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getAuthor()));
        colAvailable.setCellValueFactory(c ->
                new SimpleIntegerProperty(c.getValue().getAvailable_stock()).asObject());

        tableBooks.setItems(bookList);

        tableBooks.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        selectedBook = newVal;
                    }
                }
        );
    }

    @FXML
    private void handleSearchStudent() {
        String carnet = txtStudentId.getText();
        if (validations.isEmpty(carnet)) {
            AlertUtils.instanceAlert().show(AppStatus.INVALID_INPUT,
                    "Ingrese el carnet del estudiante.");
            return;
        }

        try {
            Optional<User> userOpt = userDao.findByCode(carnet.trim().toUpperCase());

            if (userOpt.isEmpty()) {
                selectedStudent = null;
                lblStudentName.setText("Nombre: -");
                lblStudentEmail.setText("Correo: -");
                AlertUtils.instanceAlert().show(AppStatus.NOT_FOUND,
                        "Estudiante no encontrado con carnet: " + carnet);
                return;
            }

            selectedStudent = userOpt.get();
            lblStudentName.setText("Nombre: " + selectedStudent.getFirst_name()
                    + " " + selectedStudent.getLast_name());
            lblStudentEmail.setText("Correo: " + selectedStudent.getEmail());

        } catch (SQLException e) {
            AlertUtils.instanceAlert().show(AppStatus.DATABASE_UNAVAILABLE,
                    "Error al buscar estudiante: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearchBook() {
        String isbn = txtIsbn.getText();
        String title = txtBookTitle.getText();

        String filter = "";
        if (!validations.isEmpty(isbn)) filter = isbn.trim();
        else if (!validations.isEmpty(title)) filter = title.trim();

        try {
            List<Book> results = bookDao.search(filter);
            bookList.setAll(results);

            if (results.isEmpty()) {
                AlertUtils.instanceAlert().show(AppStatus.NOT_FOUND,
                        "No se encontraron libros con ese criterio.");
            }
        } catch (SQLException e) {
            AlertUtils.instanceAlert().show(AppStatus.DATABASE_UNAVAILABLE,
                    "Error al buscar libros: " + e.getMessage());
        }
    }

    @FXML
    private void handleGenerateBorrowing() {
        if (selectedStudent == null) {
            AlertUtils.instanceAlert().show(AppStatus.INVALID_INPUT,
                    "Busque un estudiante primero.");
            return;
        }
        if (selectedBook == null) {
            AlertUtils.instanceAlert().show(AppStatus.INVALID_INPUT,
                    "Seleccione un libro de la tabla.");
            return;
        }
        if (dpDueDate.getValue() == null) {
            AlertUtils.instanceAlert().show(AppStatus.INVALID_INPUT,
                    "Seleccione la fecha límite de devolución.");
            return;
        }
        if (selectedBook.getAvailable_stock() <= 0) {
            AlertUtils.instanceAlert().show(AppStatus.INVALID_INPUT,
                    "No hay ejemplares disponibles de este libro.");
            return;
        }

        User librarian = SessionManager.getInstance().getCurrentUser();
        if (librarian == null) return;

        try {
            LoanApplication request = new LoanApplication();
            request.setStudent_id(selectedStudent.getUser_id());
            request.setStatus(RequestStatus.APPROVED);
            request.setLibrarian_id(librarian.getUser_id());
            request.setResponse_date(LocalDate.now());

            int requestId = loanRequestDao.create(request);

            RequestDetails requestDetail = new RequestDetails();
            requestDetail.setRequest_id(requestId);
            requestDetail.setBook_id(selectedBook.getBook_id());
            requestDetail.setQuantity(1);
            loanRequestDetailDao.create(requestDetail);

            Loan loan = new Loan();
            loan.setRequest_id(requestId);
            loan.setStudent_id(selectedStudent.getUser_id());
            loan.setLibrarian_id(librarian.getUser_id());
            loan.setDue_date(dpDueDate.getValue());
            loan.setStatus(LoanStatus.ACTIVE);

            int loanId = loanDao.create(loan);

            LoanDetails loanDetail = new LoanDetails();
            loanDetail.setLoan_id(loanId);
            loanDetail.setBook_id(selectedBook.getBook_id());
            loanDetail.setQuantity(1);
            loanDetail.setReturned_quantity(0);
            loanDetailDao.create(loanDetail);

            selectedBook.setAvailable_stock(selectedBook.getAvailable_stock() - 1);
            bookDao.update(selectedBook);

            AlertUtils.instanceAlert().show(AppStatus.OK,
                    "Préstamo #" + loanId + " registrado exitosamente.\n"
                            + "Estudiante: " + selectedStudent.getFirst_name() + "\n"
                            + "Libro: " + selectedBook.getTitle() + "\n"
                            + "Fecha límite: " + dpDueDate.getValue());

            bookList.setAll(bookDao.search(""));
            clearForm();

        } catch (SQLException e) {
            AlertUtils.instanceAlert().show(AppStatus.DATABASE_UNAVAILABLE,
                    "Error al generar préstamo: " + e.getMessage());
        }
    }

    @FXML
    private void handlePrintReceipt() {
        if (selectedStudent == null || selectedBook == null) {
            AlertUtils.instanceAlert().show(AppStatus.INVALID_INPUT,
                    "Genere un préstamo primero.");
            return;
        }

        // TODO: Integrar JasperReports aqui

        AlertUtils.instanceAlert().show(AppStatus.OK,
                "La generación del comprobante con JasperReports está pendiente de implementación.");
    }

    @FXML
    private void handleBack() {
        SceneManager.getInstanciaSceneManager().goTo(
                "/org/library/system/view/DashboardView.fxml"
        );
    }

    private void clearForm() {
        txtStudentId.clear();
        txtIsbn.clear();
        txtBookTitle.clear();
        lblStudentName.setText("Nombre: -");
        lblStudentEmail.setText("Correo: -");
        dpDueDate.setValue(null);
        selectedStudent = null;
        selectedBook = null;
        bookList.clear();
    }
}