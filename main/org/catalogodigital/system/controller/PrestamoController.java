package org.catalogodigital.system.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PrestamoController {

    @FXML
    private TextField txtCarnet;

    @FXML
    private TextField txtISBN;

    @FXML
    private Label lblNombre;

    @FXML
    private Label lblTitulo;

    @FXML
    private Label lblFechaPrestamo;

    @FXML
    private Label lblFechaLimite;

    @FXML
    private Button btnBuscarEstudiante;

    @FXML
    private Button btnBuscarLibro;

    @FXML
    private Button btnGenerarPrestamo;

    @FXML
    private Button btnImprimir;

    @FXML
    private Button btnRegresar;

    @FXML
    public void initialize() {
        // Inicializacion del controlador
    }

    @FXML
    private void handleBuscarEstudiante() {
        String carnet = txtCarnet.getText();
        System.out.println("Buscando estudiante con carnet: " + carnet);
        // Aqui se integrara la consulta real
        lblNombre.setText("Nombre del estudiante");
    }

    @FXML
    private void handleBuscarLibro() {
        String isbn = txtISBN.getText();
        System.out.println("Buscando libro con ISBN: " + isbn);
        // Aqui se integrara la consulta real
        lblTitulo.setText("Titulo del libro");
    }

    @FXML
    private void handleGenerarPrestamo() {
        System.out.println("Generando prestamo...");
        // Aqui se integrara la logica de guardado
    }

    @FXML
    private void handleImprimir() {
        System.out.println("Imprimiendo comprobante...");
        // Aqui se integrara la generacion del comprobante (cuansdo ya lo haya hecho)
    }

    @FXML
    private void handleRegresar() {
        Stage stageActual = (Stage) btnRegresar.getScene().getWindow();
        SceneManagerController.cambiarEscena(
                stageActual,
                "/org/catalogodigital/system/view/DashboardView.fxml",
                "Panel Principal",
                800, 600
        );
    }
}