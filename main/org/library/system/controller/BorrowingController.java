package org.library.system.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

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
    private TableView<Copy> tableCopies;

    @FXML
    private TableColumn<Copy, String> colBarcode;

    @FXML
    private TableColumn<Copy, String> colStatus;

    @FXML
    private TableColumn<Copy, Void> colSelect;

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

    @FXML
    public void initialize() {
        // Establecer fecha actual
        lblBorrowDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        // Configurar columnas de la tabla de ejemplares
        colBarcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Columna de selección (botón)
        colSelect.setCellFactory(param -> new TableCell<>() {
            private final Button selectBtn = new Button("Seleccionar");

            {
                selectBtn.setOnAction(event -> {
                    Copy copy = getTableView().getItems().get(getIndex());
                    selectCopy(copy);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(selectBtn);
                }
            }
        });

        // Cargar datos de prueba
        loadTestCopies();
    }

    @FXML
    private void handleSearchStudent() {
        String id = txtStudentId.getText();
        if (id == null || id.isEmpty()) {
            System.out.println("Ingrese un carnet");
            return;
        }
        System.out.println("Buscando estudiante con carnet: " + id);
        // Simulación
        lblStudentName.setText("Nombre: Juan Perez");
        lblStudentEmail.setText("Correo: juan.perez@universidad.edu");
    }

    @FXML
    private void handleSearchBook() {
        String isbn = txtIsbn.getText();
        String title = txtBookTitle.getText();
        System.out.println("Buscando libro: ISBN=" + isbn + ", Titulo=" + title);
        loadTestCopies();
    }

    private void selectCopy(Copy copy) {
        System.out.println("Ejemplar seleccionado: " + copy.getBarcode());
        // Aquí se guardaría el ejemplar seleccionado
    }

    @FXML
    private void handleGenerateBorrowing() {
        System.out.println("Generando préstamo...");
        if (dpDueDate.getValue() == null) {
            System.out.println("Debe seleccionar una fecha límite");
            return;
        }
        if (lblStudentName.getText().equals("Nombre: -")) {
            System.out.println("Debe buscar un estudiante");
            return;
        }
        System.out.println("Préstamo generado correctamente");
        // Aquí se guardaría en la base de datos
    }

    @FXML
    private void handlePrintReceipt() {
        System.out.println("Imprimiendo comprobante...");
        // Aquí se generaría el comprobante PDF
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
        tableCopies.getItems().addAll(
                new Copy("ABC-123", "Disponible"),
                new Copy("DEF-456", "Disponible"),
                new Copy("GHI-789", "Prestado")
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