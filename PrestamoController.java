package com.franciscoalvarez.system.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PrestamoController {

    @FXML
    private TextField txtCarnet, txtISBN;

    @FXML
    private Label lblNombre, lblTitulo, lblFechaPrestamo, lblFechaLimite;

    @FXML
    private Button btnBuscarEstudiante, btnBuscarLibro;
    @FXML
    private Button btnGenerarPrestamo, btnImprimir;

    @FXML
    public void initialize() {
        // Establecer fechas automáticas
        // java.time.LocalDate.now() para fecha actual
        // java.time.LocalDate.now().plusDays(7) para fecha límite
    }

    @FXML
    private void handleBuscarEstudiante() {
        String carnet = txtCarnet.getText();
        System.out.println("Buscando estudiante: " + carnet);
        // Simular búsqueda
        lblNombre.setText("Estudiante: Juan Pérez");
    }

    @FXML
    private void handleBuscarLibro() {
        String isbn = txtISBN.getText();
        System.out.println("Buscando libro: " + isbn);
        // Simular búsqueda
        lblTitulo.setText("Libro: El Quijote");
    }

    @FXML
    private void handleGenerarPrestamo() {
        System.out.println("Generando préstamo...");
        // Lógica para guardar el préstamo
    }

    @FXML
    private void handleImprimir() {
        System.out.println("Imprimiendo comprobante...");
        // Lógica para imprimir en media carta
    }
}