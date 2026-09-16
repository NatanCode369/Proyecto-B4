package org.library.system.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.library.system.enums.Role;
import org.library.system.model.User;
import org.library.system.utils.SceneManager;
import org.library.system.utils.SessionManager;

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
        User user = SessionManager.getInstance().getCurrentUser();

        if (user == null) {
            SceneManager.getInstanciaSceneManager().goTo(
                    "/org/library/system/view/LoginView.fxml"
            );
            return;
        }

        lblWelcome.setText("Bienvenido, "
                + user.getFirst_name() + " " + user.getLast_name());
        lblRole.setText("Rol: " + displayRole(user.getUser_role()));

        hideAll();

        switch (user.getUser_role()) {
            case MANAGER -> show(btnManageLibrarians);
            case LIBRARIAN -> {
                show(btnCatalog);
                show(btnBorrowings);
            }
            case STUDENT -> {
                show(btnConsultCatalog);
                show(btnMyBorrowings);
            }
        }
    }

    private String displayRole(Role role) {
        return switch (role) {
            case MANAGER -> "Bibliotecario Jefe";
            case LIBRARIAN -> "Bibliotecario";
            case STUDENT -> "Estudiante";
        };
    }

    private void hideAll() {
        for (Button b : new Button[]{btnCatalog, btnBorrowings,
                btnManageLibrarians, btnConsultCatalog, btnMyBorrowings}) {
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
        CatalogController.setEditMode(true);
        SceneManager.getInstanciaSceneManager().goTo(
                "/org/library/system/view/CatalogView.fxml"
        );
    }

    @FXML
    private void handleOpenBorrowings() {
        SceneManager.getInstanciaSceneManager().goTo(
                "/org/library/system/view/BorrowingView.fxml"
        );
    }

    @FXML
    private void handleManageLibrarians() {
        SceneManager.getInstanciaSceneManager().goTo(
                "/org/library/system/view/LibrarianView.fxml"
        );
    }

    @FXML
    private void handleConsultCatalog() {
        CatalogController.setEditMode(false);
        SceneManager.getInstanciaSceneManager().goTo(
                "/org/library/system/view/CatalogView.fxml"
        );
    }

    @FXML
    private void handleMyBorrowings() {
        SceneManager.getInstanciaSceneManager().goTo(
                "/org/library/system/view/BorrowingView.fxml"
        );
    }

    @FXML
    private void handleLogout() {
        SessionManager.getInstance().logout();
        SceneManager.getInstanciaSceneManager().goTo(
                "/org/library/system/view/LoginView.fxml"
        );
    }
}