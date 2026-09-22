package org.library.system.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.library.system.dao.*;
import org.library.system.enums.LoanStatus;
import org.library.system.enums.RequestStatus;
import org.library.system.model.*;
import org.library.system.utils.SceneManager;
import org.library.system.utils.SessionManager;

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
        if (carnet == null || carnet.isBlank()) {
            showAlert(Alert.AlertType.WARNING, "Sin datos",
                    "Ingrese el carnet del estudiante.");
            return;
        }

        try {
            Optional<User> userOpt = userDao.findByCode(carnet.trim().toUpperCase());

            if (userOpt.isEmpty()) {
                selectedStudent = null;
                lblStudentName.setText("Nombre: -");
                lblStudentEmail.setText("Correo: -");
                showAlert(Alert.AlertType.WARNING, "No encontrado",
                        "Estudiante no encontrado con carnet: " + carnet);
                return;
            }

            selectedStudent = userOpt.get();
            lblStudentName.setText("Nombre: " + selectedStudent.getFirst_name()
                    + " " + selectedStudent.getLast_name());
            lblStudentEmail.setText("Correo: " + selectedStudent.getEmail());

            showAlert(Alert.AlertType.INFORMATION, "Estudiante encontrado",
                    "Estudiante: " + selectedStudent.getFirst_name()
                            + " " + selectedStudent.getLast_name());

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error de base de datos",
                    e.getMessage());
        }
    }

    @FXML
    private void handleSearchBook() {
        String isbn = txtIsbn.getText();
        String title = txtBookTitle.getText();

        String filter = "";
        if (isbn != null && !isbn.isBlank()) filter = isbn.trim();
        else if (title != null && !title.isBlank()) filter = title.trim();

        try {
            List<Book> results = bookDao.search(filter);
            bookList.setAll(results);

            if (results.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Sin resultados",
                        "No se encontraron libros con ese criterio.");
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Resultados",
                        "Se encontraron " + results.size() + " libro(s).");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error de base de datos",
                    e.getMessage());
        }
    }

    @FXML
    private void handleGenerateBorrowing() {
        if (selectedStudent == null) {
            showAlert(Alert.AlertType.WARNING, "Sin estudiante",
                    "Busque un estudiante primero.");
            return;
        }
        if (selectedBook == null) {
            showAlert(Alert.AlertType.WARNING, "Sin libro",
                    "Seleccione un libro de la tabla.");
            return;
        }
        if (dpDueDate.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Sin fecha",
                    "Seleccione la fecha limite de devolucion.");
            return;
        }
        if (selectedBook.getAvailable_stock() <= 0) {
            showAlert(Alert.AlertType.WARNING, "Sin stock",
                    "No hay ejemplares disponibles de este libro.");
            return;
        }

        User librarian = SessionManager.getInstance().getCurrentUser();
        if (librarian == null) return;

        try {
            // 1. Crear solicitud aprobada
            LoanApplication request = new LoanApplication();
            request.setStudent_id(selectedStudent.getUser_id());
            request.setStatus(RequestStatus.APPROVED);
            request.setLibrarian_id(librarian.getUser_id());
            request.setResponse_date(LocalDate.now());

            int requestId = loanRequestDao.create(request);

            // 2. Detalle de la solicitud
            RequestDetails requestDetail = new RequestDetails();
            requestDetail.setRequest_id(requestId);
            requestDetail.setBook_id(selectedBook.getBook_id());
            requestDetail.setQuantity(1);
            loanRequestDetailDao.create(requestDetail);

            // 3. Crear el prestamo
            Loan loan = new Loan();
            loan.setRequest_id(requestId);
            loan.setStudent_id(selectedStudent.getUser_id());
            loan.setLibrarian_id(librarian.getUser_id());
            loan.setDue_date(dpDueDate.getValue());
            loan.setStatus(LoanStatus.ACTIVE);

            int loanId = loanDao.create(loan);

            // 4. Detalle del prestamo
            LoanDetails loanDetail = new LoanDetails();
            loanDetail.setLoan_id(loanId);
            loanDetail.setBook_id(selectedBook.getBook_id());
            loanDetail.setQuantity(1);
            loanDetail.setReturned_quantity(0);
            loanDetailDao.create(loanDetail);

            // 5. Bajar stock
            selectedBook.setAvailable_stock(selectedBook.getAvailable_stock() - 1);
            bookDao.update(selectedBook);

            showAlert(Alert.AlertType.INFORMATION, "Prestamo creado",
                    "Prestamo #" + loanId + " registrado exitosamente.\n" +
                            "Estudiante: " + selectedStudent.getFirst_name() + "\n" +
                            "Libro: " + selectedBook.getTitle() + "\n" +
                            "Fecha limite: " + dpDueDate.getValue());

            bookList.setAll(bookDao.search(""));
            clearForm();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error al generar prestamo",
                    e.getMessage());
        }
    }

    @FXML
    private void handlePrintReceipt() {
        if (selectedStudent == null || selectedBook == null) {
            showAlert(Alert.AlertType.WARNING, "Sin datos",
                    "Genere un prestamo primero.");
            return;
        }

        String receipt = "COMPROBANTE DE PRESTAMO\n\n" +
                "Estudiante: " + selectedStudent.getFirst_name() + " "
                + selectedStudent.getLast_name() + "\n" +
                "Carnet: " + selectedStudent.getUser_code() + "\n" +
                "Libro: " + selectedBook.getTitle() + "\n" +
                "ISBN: " + selectedBook.getIsbn() + "\n" +
                "Fecha prestamo: " + LocalDate.now() + "\n" +
                "Fecha limite: " + dpDueDate.getValue();

        showAlert(Alert.AlertType.INFORMATION, "Comprobante", receipt);
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

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}