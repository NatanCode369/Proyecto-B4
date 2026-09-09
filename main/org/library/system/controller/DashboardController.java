package org.library.system.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class DashboardController {

    private static String currentRole;

    @FXML
    private Label lblWelcome;

    @FXML
    private Label lblRole;

    @FXML
    private Button btnCatalog;

    @FXML
    private Button btnBorrowings;

    @FXML
    private Button btnManageLibrarians;

    @FXML
    private Button btnConsultCatalog;

    @FXML
    private Button btnMyBorrowings;

    @FXML
    private Button btnLogout;

    public static void setRole(String role) {
        currentRole = role;
    }

    @FXML
    public void initialize() {
        lblWelcome.setText("Bienvenido al Sistema Bibliotecario");
        lblRole.setText("Rol: " + currentRole);

        // Ocultar todos los botones por defecto
        btnCatalog.setVisible(false);
        btnCatalog.setManaged(false);
        btnBorrowings.setVisible(false);
        btnBorrowings.setManaged(false);
        btnManageLibrarians.setVisible(false);
        btnManageLibrarians.setManaged(false);
        btnConsultCatalog.setVisible(false);
        btnConsultCatalog.setManaged(false);
        btnMyBorrowings.setVisible(false);
        btnMyBorrowings.setManaged(false);

        // Mostrar según el rol
        if (currentRole != null) {
            switch (currentRole) {
                case "Bibliotecario Jefe":
                    btnCatalog.setVisible(true);
                    btnCatalog.setManaged(true);
                    btnBorrowings.setVisible(true);
                    btnBorrowings.setManaged(true);
                    btnManageLibrarians.setVisible(true);
                    btnManageLibrarians.setManaged(true);
                    break;

                case "Bibliotecario":
                    btnManageLibrarians.setVisible(true);
                    btnManageLibrarians.setManaged(true);
                    break;

                case "Estudiante":
                    btnConsultCatalog.setVisible(true);
                    btnConsultCatalog.setManaged(true);
                    btnMyBorrowings.setVisible(true);
                    btnMyBorrowings.setManaged(true);
                    break;

                default:
                    break;
            }
        }
    }

    @FXML
    private void handleOpenCatalog() {
        CatalogController.setEditMode(true);
        Stage currentStage = (Stage) btnCatalog.getScene().getWindow();
        SceneManagerController.changeScene(
                currentStage,
                "/org/library/system/view/CatalogView.fxml",
                "Gestion de Catalogo"
        );
    }

    @FXML
    private void handleOpenBorrowings() {
        Stage currentStage = (Stage) btnBorrowings.getScene().getWindow();
        SceneManagerController.changeScene(
                currentStage,
                "/org/library/system/view/BorrowingView.fxml",
                "Gestion de Prestamos"
        );
    }

    @FXML
    private void handleManageLibrarians() {
        System.out.println("Abrir gestion de bibliotecarios (solo Bibliotecario)");
        // Pendiente implementar
    }

    @FXML
    private void handleConsultCatalog() {
        CatalogController.setEditMode(false);
        Stage currentStage = (Stage) btnConsultCatalog.getScene().getWindow();
        SceneManagerController.changeScene(
                currentStage,
                "/org/library/system/view/CatalogView.fxml",
                "Consultar Catalogo"
        );
    }

    @FXML
    private void handleMyBorrowings() {
        System.out.println("Abrir prestamos del estudiante (solo consulta)");
        // Pendiente implementar
    }

    @FXML
    private void handleLogout() {
        Stage currentStage = (Stage) btnLogout.getScene().getWindow();
        SceneManagerController.closeAndOpen(
                currentStage,
                "/org/library/system/view/LoginView.fxml",
                "Iniciar Sesion"
        );
    }
}