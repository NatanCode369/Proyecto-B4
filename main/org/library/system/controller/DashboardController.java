package org.library.system.controller;

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
    private Button btnConsultarCatalogo;

    @FXML
    private Button btnMisPrestamos;

    @FXML
    private Button btnCerrarSesion;

    // Método estático para recibir el rol desde el LoginController
    public static void setRol(String rol) {
        rolActual = rol;
    }

    @FXML
    public void initialize() {
        // mensajito de bienvenida
        lblBienvenida.setText("Bienvenido al Sistema Bibliotecario");
        lblRol.setText("Rol: " + rolActual);

        // Ocultar todos los botones por defecto
        btnCatalogo.setVisible(false);
        btnCatalogo.setManaged(false);
        btnPrestamos.setVisible(false);
        btnPrestamos.setManaged(false);
        btnGestionBibliotecarios.setVisible(false);
        btnGestionBibliotecarios.setManaged(false);
        btnConsultarCatalogo.setVisible(false);
        btnConsultarCatalogo.setManaged(false);
        btnMisPrestamos.setVisible(false);
        btnMisPrestamos.setManaged(false);

        // Mostrar botones según el rol asignado por el sistema
        if (rolActual == null) {
            // Si no hay rol, no mostrar nada (o mostrar un mensaje de error, lo que venga primero :v)
            return;
        }

        switch (rolActual) {
            case "Bibliotecario Jefe":
                btnCatalogo.setVisible(true);
                btnCatalogo.setManaged(true);
                btnPrestamos.setVisible(true);
                btnPrestamos.setManaged(true);
                btnGestionBibliotecarios.setVisible(true);
                btnGestionBibliotecarios.setManaged(true);
                break;

            case "Bibliotecario":
                btnCatalogo.setVisible(true);
                btnCatalogo.setManaged(true);
                btnPrestamos.setVisible(true);
                btnPrestamos.setManaged(true);
                break;

            case "Estudiante":
                btnConsultarCatalogo.setVisible(true);
                btnConsultarCatalogo.setManaged(true);
                btnMisPrestamos.setVisible(true);
                btnMisPrestamos.setManaged(true);
                break;

            default:
                // Rol no reconocido, no mostrar nada
                break;
        }
    }

    @FXML
    private void handleAbrirCatalogo() {
        Stage stageActual = (Stage) btnCatalogo.getScene().getWindow();
        SceneManagerController.cambiarEscena(
                stageActual,
                "/org/library/system/view/CatalogoView.fxml",
                "Gestion de Catalogo",
                1000, 700
        );
    }

    @FXML
    private void handleAbrirPrestamos() {
        Stage stageActual = (Stage) btnPrestamos.getScene().getWindow();
        SceneManagerController.cambiarEscena(
                stageActual,
                "/org/library/system/view/PrestamoView.fxml",
                "Gestion de Prestamos",
                800, 600
        );
    }

    @FXML
    private void handleGestionBibliotecarios() {
        System.out.println("Abrir gestion de bibliotecarios (solo Jefe)");
        // Aqui se abrira la vista de gestion de bibliotecarios cuando exista
    }

    @FXML
    private void handleConsultarCatalogo() {
        System.out.println("Abrir consulta de catalogo (solo lectura)");
        // Aqui se abrira una vista de solo lectura del catalogo
        // Por ahora redirigimos al catalogo normal (pero en el futuro se deberia deshabilitar la edicion)
        Stage stageActual = (Stage) btnConsultarCatalogo.getScene().getWindow();
        SceneManagerController.cambiarEscena(
                stageActual,
                "/org/library/system/view/CatalogoView.fxml",
                "Consultar Catalogo",
                1000, 700
        );
    }

    @FXML
    private void handleMisPrestamos() {
        System.out.println("Abrir prestamos del estudiante (solo consulta)");
        // Aqui se abrira una vista de prestamos del estudiante
    }

    @FXML
    private void handleCerrarSesion() {
        Stage stageActual = (Stage) btnCerrarSesion.getScene().getWindow();
        SceneManagerController.cerrarYAbrir(
                stageActual,
                "/org/library/system/view/LoginView.fxml",
                "Iniciar Sesion",
                600, 400
        );
    }
}