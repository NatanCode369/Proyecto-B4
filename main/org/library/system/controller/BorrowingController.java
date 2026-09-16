package org.library.system.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.library.system.utils.SceneManager;

import java.io.IOException;
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
    @FXML private Button btnSearchStudent;
    @FXML private Button btnSearchBook;
    @FXML private Button btnGenerateBorrowing;
    @FXML private Button btnPrintReceipt;
    @FXML private Button btnBack;

    @FXML
    public void initialize() {
        System.out.println("[BORROWING] Inicializando ventana de prestamos");
        lblBorrowDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        colBarcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        loadTestCopies();
        System.out.println("[BORROWING] Ventana de prestamos lista");
    }

    @FXML
    private void handleSearchStudent() {
        System.out.println("[BORROWING] Iniciando busqueda de estudiante");
        String id = txtStudentId.getText();
        System.out.println("[BORROWING] Carnet ingresado: " + id);

        if (id == null || id.isBlank()) {
            System.out.println("[BORROWING] ERROR: El carnet esta vacio");
            return;
        }

        System.out.println("[BORROWING] (SIMULADO) Estudiante encontrado");
        lblStudentName.setText("Nombre: Juan Perez");
        lblStudentEmail.setText("Correo: juan.perez@universidad.edu");
        System.out.println("[BORROWING] Busqueda de estudiante finalizada");
    }

    @FXML
    private void handleSearchBook() {
        System.out.println("[BORROWING] Iniciando busqueda de libro");
        String isbn = txtIsbn.getText();
        String title = txtBookTitle.getText();
        System.out.println("[BORROWING] ISBN: " + isbn + " | Titulo: " + title);

        if ((isbn == null || isbn.isBlank()) && (title == null || title.isBlank())) {
            System.out.println("[BORROWING] ERROR: Ingrese ISBN o titulo");
            return;
        }

        System.out.println("[BORROWING] (SIMULADO) Ejemplares cargados");
        loadTestCopies();
        System.out.println("[BORROWING] Busqueda de libro finalizada");
    }

    @FXML
    private void handleGenerateBorrowing() {
        System.out.println("[BORROWING] Iniciando generacion de prestamo");

        if (dpDueDate.getValue() == null) {
            System.out.println("[BORROWING] ERROR: Seleccione una fecha limite");
            return;
        }
        if (lblStudentName.getText().equals("Nombre: -")) {
            System.out.println("[BORROWING] ERROR: Busque un estudiante primero");
            return;
        }

        System.out.println("[BORROWING] (SIMULADO) Prestamo registrado exitosamente");
        System.out.println("[BORROWING] Generacion de prestamo finalizada");
    }

    @FXML
    private void handlePrintReceipt() {
        System.out.println("[BORROWING] Iniciando impresion de comprobante");
        System.out.println("[BORROWING] (SIMULADO) Comprobante generado");
        System.out.println("[BORROWING] Impresion finalizada");
    }

    @FXML
    private void handleBack() {
        System.out.println("[BORROWING] Regresando al panel principal");
        goTo("/org/library/system/view/DashboardView.fxml");
    }

    private void loadTestCopies() {
        tableCopies.getItems().clear();
        tableCopies.getItems().addAll(
                new Copy("ABC-123", "Disponible"),
                new Copy("DEF-456", "Disponible"),
                new Copy("GHI-789", "Prestado")
        );
    }

    private void goTo(String path) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Scene scene = new Scene(loader.load());
            SceneManager.getInstanciaSceneManager().changeScene(scene);
        } catch (IOException e) {
            System.out.println("[BORROWING] ERROR al navegar: " + e.getMessage());
        }
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