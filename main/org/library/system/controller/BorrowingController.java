package org.library.system.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.library.system.model.Book;
import org.library.system.utils.AlertUtils;
import org.library.system.utils.AppStatus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BorrowingController {

    @FXML
    private TextField txtStudentId;

    @FXML
    private TextField txtIsbn;

    @FXML
    private TextField txtBookTitle;

    @FXML
    private Label lblStudentName;

    @FXML
    private Label lblStudentEmail;

    @FXML
    private Label lblBorrowDate;

    @FXML
    private DatePicker dpDueDate;

    @FXML
    private TableView<Book> tableCopies;

    @FXML
    private TableColumn<Book, String> colBarcode;

    @FXML
    private TableColumn<Book, String> colStatus;

    @FXML
    private TableColumn<Book, Void> colSelect;

    @FXML
    private Button btnSearchStudent;

    @FXML
    private Button btnSearchBook;

    @FXML
    private Button btnGenerateBorrowing;

    @FXML
    private Button btnPrintReceipt;

    @FXML
    private Button btnBack;

    public BorrowingController() {
    }

    @FXML
    public void initialize() {
        lblBorrowDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        //refactorizar el método initialize
    }

    @FXML
    private void handleSearchStudent() {
        if (txtStudentId.getText().isEmpty()) {
            AlertUtils.instanceAlert().show(AppStatus.INVALID_INPUT, "Ingrese el ID de un estudiante.");
        } else {
            //Consulta a la DB para buscar al estudiante y demás procesos requeridos.
        }
    }

    @FXML
    private void handleSearchBook() {
        if (txtIsbn.getText().isEmpty() && txtBookTitle.getText().isEmpty()) {
            AlertUtils.instanceAlert().show(AppStatus.INVALID_INPUT,
                    "Ingrese el ISBN o el título del libro que desea buscar.");
            return;
        } else {
            //Consulta a la DB para buscar el ISBN o el título del libro y demás procesos.
        }
    }

    @FXML
    private void handleGenerateBorrowing() {
        // Validar que se haya seleccionado una fecha
        if (dpDueDate.getValue() == null) {
            AlertUtils.instanceAlert().show(AppStatus.INVALID_INPUT,
                    "Debe agregar una fecha límite.");
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
        //Acá debe ir lo de JasperReports y la exportación a pdf.
    }

    @FXML
    private void handleBack() {
        Stage currentStage = (Stage) btnBack.getScene().getWindow();
        SceneManagerController.changeScene(
                currentStage,
                "/org/library/system/view/DashboardView.fxml",
                "Panel Principal"
        );
    }

    private void loadTestCopies() {
        tableCopies.getItems().clear();
    }

}