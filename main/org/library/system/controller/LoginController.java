package org.library.system.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.library.system.utils.Validations;

public class LoginController {

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private Hyperlink hlRegister;

    private final Validations validations;

    public LoginController() {
        this.validations = new Validations();
    }

    @FXML
    public void initialize() {
        System.out.println("LoginController initialized");
    }

    @FXML
    private void handleLogin() {
        String userCode = txtEmail.getText();
        String password = txtPassword.getText();

        System.out.println("=== INICIANDO LOGIN ===");

        // Validar que los campos no estén vacíos
        if (validations.isEmpty(userCode)) {
            System.out.println("Error: El código de usuario está vacío");
            return;
        }
        if (validations.isEmpty(password)) {
            System.out.println("Error: La contraseña está vacía");
            return;
        }

        System.out.println("Usuario: " + userCode);
        System.out.println("Contraseña ingresada");

        // TODO: Aquí iría la validación con la base de datos
        // Por ahora, simulamos un login ._.
        System.out.println("Login exitoso (simulado)");

        // Asignar rol por defecto para pruebas
        String role = "Bibliotecario Jefe";
        DashboardController.setRole(role);

        // Redirigir al Dashboard
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