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

        // Validar que se haya seleccionado un ejemplar
        if (tableCopies.getSelectionModel().isEmpty()) {
            AlertUtils.instanceAlert().show(AppStatus.INVALID_INPUT,
                    "Debe seleccionar un ejemplar para generar la solicitud.");
            return;
        }

        //Generación del préstamo
        // Guardado en la base de datos
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