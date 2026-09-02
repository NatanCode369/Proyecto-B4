package org.catalogodigital.system.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

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
    public void initialize() {
        System.out.println(" Catálogo iniciado correctamente");
        // Configurar columnas de la tabla
        // Cargar datos de prueba
    }

    @FXML
    private void handleAgregarLibro() {
        System.out.println("   Agregando libro: " + txtTitulo.getText());
        System.out.println("   ISBN: " + txtISBN.getText());
        System.out.println("   Autor: " + txtAutor.getText());
        System.out.println("   Editorial: " + txtEditorial.getText());
        System.out.println("   Año: " + txtAnio.getText());
        System.out.println("   Copias: " + txtCopias.getText());

        // Aquí irá la lógica para guardar en BD
        limpiarCampos();
    }

    @FXML
    private void handleLimpiar() {
        limpiarCampos();
        System.out.println("Campos limpiados");
    }

    @FXML
    private void handleNuevoPrestamo() {
        System.out.println("Abriendo ventana de préstamo");
        // Aquí irá la lógica para abrir PrestamoView después
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