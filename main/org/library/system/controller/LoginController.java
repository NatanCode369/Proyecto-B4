package org.library.system.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private Hyperlink hlRegister;

    @FXML
    public void initialize() {
        System.out.println("LoginController initialized");
    }

    @FXML
    private void handleLogin() {
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        System.out.println("Login attempt with email: " + email);

        // Simulación: asignar rol según el correo (para pruebas)
        String role;
        if (email.equalsIgnoreCase("estudiante@mail.com")) {
            role = "Estudiante";
        } else if (email.equalsIgnoreCase("bibliotecario@mail.com")) {
            role = "Bibliotecario";
        } else {
            role = "Bibliotecario Jefe";
        }

        DashboardController.setRole(role);

        Stage currentStage = (Stage) btnLogin.getScene().getWindow();
        SceneManagerController.closeAndOpen(
                currentStage,
                "/org/library/system/view/DashboardView.fxml",
                "Panel Principal"
        );
    }

    @FXML
    private void handleGoToRegister() {
        Stage currentStage = (Stage) hlRegister.getScene().getWindow();
        SceneManagerController.closeAndOpen(
                currentStage,
                "/org/library/system/view/RegisterView.fxml",
                "Registro de Bibliotecario Jefe"
        );
    }
}