package org.library.system.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.library.system.enums.Role;
import org.library.system.model.User;
import org.library.system.utils.SceneManager;
import org.library.system.utils.SessionManager;

import java.io.IOException;

public class DashboardController {

    @FXML private Label lblWelcome;
    @FXML private Label lblRole;
    @FXML private Button btnCatalog;
    @FXML private Button btnBorrowings;
    @FXML private Button btnManageLibrarians;
    @FXML private Button btnConsultCatalog;
    @FXML private Button btnMyBorrowings;
    @FXML private Button btnLogout;

    @FXML
    public void initialize() {
        System.out.println("[DASHBOARD] Inicializando panel principal");

        User user = SessionManager.getInstance().getCurrentUser();

        if (user == null) {
            System.out.println("[DASHBOARD] ERROR: No hay usuario en sesion");
            return;
        }

        System.out.println("[DASHBOARD] Usuario en sesion: " + user.getUser_code()
                + " | Rol: " + user.getUser_role());

        lblWelcome.setText("Bienvenido, " + user.getFirst_name() + " " + user.getLast_name());
        lblRole.setText("Rol: " + displayRole(user.getUser_role()));

        // Ocultar todos los botones
        hideAllButtons();

        switch (user.getUser_role()) {

            case MANAGER:
                // Bibliotecario Jefe: SOLO gestion de bibliotecarios
                System.out.println("[DASHBOARD] Mostrando opciones de Bibliotecario Jefe");
                show(btnManageLibrarians);
                break;

            case LIBRARIAN:
                // Bibliotecario: catalogo completo + prestamos
                System.out.println("[DASHBOARD] Mostrando opciones de Bibliotecario");
                show(btnCatalog);
                show(btnBorrowings);
                break;

            case STUDENT:
                // Estudiante: solo consultar catalogo + mis prestamos
                System.out.println("[DASHBOARD] Mostrando opciones de Estudiante");
                show(btnConsultCatalog);
                show(btnMyBorrowings);
                break;

            default:
                System.out.println("[DASHBOARD] Rol no reconocido");
                break;
        }

        System.out.println("[DASHBOARD] Panel principal listo");
    }

    private String displayRole(Role role) {
        return switch (role) {
            case MANAGER -> "Bibliotecario Jefe";
            case LIBRARIAN -> "Bibliotecario";
            case STUDENT -> "Estudiante";
        };
    }

    private void hideAllButtons() {
        for (Button b : new Button[]{
                btnCatalog, btnBorrowings, btnManageLibrarians,
                btnConsultCatalog, btnMyBorrowings}) {
            b.setVisible(false);
            b.setManaged(false);
        }
    }

    private void show(Button b) {
        b.setVisible(true);
        b.setManaged(true);
    }

    @FXML
    private void handleOpenCatalog() {
        System.out.println("[DASHBOARD] Abriendo gestion de catalogo (modo completo)");
        CatalogController.setEditMode(true);
        goTo("/org/library/system/view/CatalogView.fxml");
    }

    @FXML
    private void handleOpenBorrowings() {
        System.out.println("[DASHBOARD] Abriendo gestion de prestamos");
        goTo("/org/library/system/view/BorrowingView.fxml");
    }

    @FXML
    private void handleManageLibrarians() {
        System.out.println("[DASHBOARD] Abriendo gestion de bibliotecarios");
        goTo("/org/library/system/view/LibrarianView.fxml");
    }

    @FXML
    private void handleConsultCatalog() {
        System.out.println("[DASHBOARD] Abriendo consulta de catalogo (solo lectura)");
        CatalogController.setEditMode(false);
        goTo("/org/library/system/view/CatalogView.fxml");
    }

    @FXML
    private void handleMyBorrowings() {
        System.out.println("[DASHBOARD] Abriendo mis prestamos (pendiente)");
    }

    @FXML
    private void handleLogout() {
        System.out.println("[DASHBOARD] Cerrando sesion");
        SessionManager.getInstance().logout();
        goTo("/org/library/system/view/LoginView.fxml");
    }

    private void goTo(String path) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Scene scene = new Scene(loader.load());
            SceneManager.getInstanciaSceneManager().changeScene(scene);
        } catch (IOException e) {
            System.out.println("[DASHBOARD] ERROR al navegar: " + e.getMessage());
        }
    }
}