package org.library.system.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CatalogoController {

    @FXML
    private TextField txtISBN;

    @FXML
    private TextField txtTitulo;

    @FXML
    private TextField txtAutor;

    @FXML
    private TextField txtEditorial;

    @FXML
    private TextField txtAnio;

    @FXML
    private TextField txtCopias;

    @FXML
    private Button btnAgregar;

    @FXML
    private Button btnLimpiar;

    @FXML
    private Button btnNuevoPrestamo;

    @FXML
    private Button btnRegresar;

    @FXML
    private TableView<?> tablaLibros;

    @FXML
    private TableColumn<?, ?> colISBN;

    @FXML
    private TableColumn<?, ?> colTitulo;

    @FXML
    private TableColumn<?, ?> colAutor;

    @FXML
    private TableColumn<?, ?> colEditorial;

    @FXML
    private TableColumn<?, ?> colAnio;

    @FXML
    private TableColumn<?, ?> colCopias;

    @FXML
    public void initialize() {
        // Inicializacion del controlador
    }

    @FXML
    private void handleAgregarLibro() {
        System.out.println("Agregando libro: " + txtTitulo.getText());
        limpiarCampos();
    }

    @FXML
    private void handleLimpiar() {
        limpiarCampos();
    }

    @FXML
    private void handleNuevoPrestamo() {
        Stage stageActual = (Stage) btnNuevoPrestamo.getScene().getWindow();
        SceneManagerController.cambiarEscena(
                stageActual,
                "/org/library/system/view/PrestamoView.fxml",
                "Registrar Prestamo",
                800, 600
        );
    }

    @FXML
    private void handleRegresar() {
        Stage stageActual = (Stage) btnRegresar.getScene().getWindow();
        SceneManagerController.cambiarEscena(
                stageActual,
                "/org/library/system/view/DashboardView.fxml",
                "Panel Principal",
                800, 600
        );
    }

    private void limpiarCampos() {
        txtISBN.clear();
        txtTitulo.clear();
        txtAutor.clear();
        txtEditorial.clear();
        txtAnio.clear();
        txtCopias.clear();
    }
}