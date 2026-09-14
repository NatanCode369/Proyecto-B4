package org.library.system.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.library.system.utils.Validations;

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

    private final Validations validations;

    public BorrowingController() {
        this.validations = new Validations();
    }

    @FXML
    public void initialize() {
        lblBorrowDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        colBarcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

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

        loadTestCopies();
        System.out.println("BorrowingController initialized");
    }

    @FXML
    private void handleSearchStudent() {
        String id = txtStudentId.getText();

        if (validations.isEmpty(id)) {
            System.out.println("Error: Ingrese un carnet");
            return;
        }

        System.out.println("Buscando estudiante con carnet: " + id);
        // Simulación
        lblStudentName.setText("Nombre: Juan Perez");
        lblStudentEmail.setText("Correo: juan.perez@universidad.edu");
        System.out.println("Estudiante encontrado (simulado)");
    }

    @FXML
    private void handleSearchBook() {
        String isbn = txtIsbn.getText();
        String title = txtBookTitle.getText();

        if (validations.isEmpty(isbn) && validations.isEmpty(title)) {
            System.out.println("Error: Ingrese ISBN o título para buscar");
            return;
        }

        System.out.println("Buscando libro: ISBN=" + isbn + ", Título=" + title);
        loadTestCopies();
        System.out.println("Libro encontrado (simulado)");
    }

    private void selectCopy(Copy copy) {
        System.out.println("Ejemplar seleccionado: " + copy.getBarcode());
    }

    @FXML
    private void handleGenerateBorrowing() {
        System.out.println("=== GENERANDO PRÉSTAMO ===");

        // Validar que se haya seleccionado una fecha
        if (dpDueDate.getValue() == null) {
            System.out.println("Error: Debe seleccionar una fecha límite");
            return;
        }

        // Validar que se haya buscado un estudiante
        if (lblStudentName.getText().equals("Nombre: -")) {
            System.out.println("Error: Debe buscar un estudiante");
            return;
        }

        // Validar que se haya seleccionado un ejemplar
        if (tableCopies.getSelectionModel().isEmpty()) {
            System.out.println("Error: Debe seleccionar un ejemplar de la tabla");
            return;
        }

        System.out.println("Préstamo generado exitosamente (simulado)");
        System.out.println("  Estudiante: " + lblStudentName.getText());
        System.out.println("  Fecha límite: " + dpDueDate.getValue().toString());

        // TODO: Aquí iría el guardado en la base de datos
    }

    @FXML
    private void handlePrintReceipt() {
        System.out.println("Imprimiendo comprobante...");
        // TODO: Generar comprobante
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