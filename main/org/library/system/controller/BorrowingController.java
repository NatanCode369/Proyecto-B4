package org.library.system.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.library.system.utils.SceneManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BorrowingController {

    @FXML private TextField txtStudentId;
    @FXML private TextField txtIsbn;
    @FXML private TextField txtBookTitle;
    @FXML private Label lblStudentName;
    @FXML private Label lblStudentEmail;
    @FXML private Label lblBorrowDate;
    @FXML private DatePicker dpDueDate;
    @FXML private TableView<Copy> tableCopies;
    @FXML private TableColumn<Copy, String> colBarcode;
    @FXML private TableColumn<Copy, String> colStatus;
    @FXML private TableColumn<Copy, Void> colSelect;
    @FXML private Button btnBack;

    @FXML
    public void initialize() {
        lblBorrowDate.setText(LocalDate.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        colBarcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        colSelect.setCellFactory(param -> new TableCell<>() {
            private final Button selectBtn = new Button("Seleccionar");

            {
                selectBtn.setOnAction(event -> {
                    Copy copy = getTableView().getItems().get(getIndex());
                    // TODO: Guardar seleccion
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : selectBtn);
            }
        });
    }

    @FXML
    private void handleSearchStudent() {
        // TODO: Conectar con UserDao para buscar estudiante
    }

    @FXML
    private void handleSearchBook() {
        // TODO: Conectar con BookDao para buscar libro
    }

    @FXML
    private void handleGenerateBorrowing() {
        // TODO: Conectar con LoanDao y LoanRequestDao
    }

    @FXML
    private void handlePrintReceipt() {
        // TODO: Generar comprobante
    }

    @FXML
    private void handleBack() {
        SceneManager.getInstanciaSceneManager().goTo(
                "/org/library/system/view/DashboardView.fxml"
        );
    }

    public static class Copy {
        private final String barcode;
        private final String status;

        public Copy(String barcode, String status) {
            this.barcode = barcode;
            this.status = status;
        }

        public String getBarcode() { return barcode; }
        public String getStatus() { return status; }
    }
}