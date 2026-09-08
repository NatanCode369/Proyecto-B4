package org.catalogodigital.system.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class DashboardController {

    private static String rolActual;

    @FXML
    private Label lblBienvenida;

    @FXML
    private Label lblRol;

    @FXML
    private Button btnCatalogo;

    @FXML
    private Button btnPrestamos;

    @FXML
    private Button btnGestionBibliotecarios;

    @FXML
    private Button btnCerrarSesion;

    public static void setRol(String rol) {
        rolActual = rol;
    }

    @FXML
    public void initialize() {
        // Configurar mensaje de bienvenida y mostrar/ocultar botones segun rol que elgia el mongol al que le llamamos usuario ._.
        lblBienvenida.setText("Bienvenido al Sistema Bibliotecario");
        lblRol.setText("Rol: " + rolActual);

        // Si es Bibliotecario Jefe, muestra el boton de gestion de bibliotecarios
        // Si es Bibliotecario pos solo lo oculta, ni modo que hiciera más :v
        boolean esJefe = "Bibliotecario Jefe".equals(rolActual);
        btnGestionBibliotecarios.setVisible(esJefe);
        btnGestionBibliotecarios.setManaged(esJefe);
    }

    @FXML
    private void handleAbrirCatalogo() {
        Stage stageActual = (Stage) btnCatalogo.getScene().getWindow();
        SceneManagerController.cambiarEscena(
                stageActual,
                "/org/catalogodigital/system/view/CatalogoView.fxml",
                "Catalogo de Libros",
                1000, 700
        );
    }

    @FXML
    private void handleAbrirPrestamos() {
        Stage stageActual = (Stage) btnPrestamos.getScene().getWindow();
        SceneManagerController.cambiarEscena(
                stageActual,
                "/org/catalogodigital/system/view/PrestamoView.fxml",
                "Gestion de Prestamos",
                800, 600
        );
    }

    @FXML
    private void handleGestionBibliotecarios() {
        // Por ahora solo muestra un mensaje, ya que no hay vista de gestion de bibliotecarios (ni sé si me lo van a aprobar XDD)
        System.out.println("Abrir gestion de bibliotecarios (solo disponible para Jefe)");
        // Aqui se abrira una vista de gestion de bibliotecarios cuando ya la vaya a crear
    }

    @FXML
    private void handleCerrarSesion() {
        Stage stageActual = (Stage) btnCerrarSesion.getScene().getWindow();
        SceneManagerController.cerrarYAbrir(
                stageActual,
                "/org/catalogodigital/system/view/LoginView.fxml",
                "Iniciar Sesion",
                600, 400
        );
    }
}