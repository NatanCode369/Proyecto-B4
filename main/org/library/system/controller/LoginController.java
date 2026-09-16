package org.library.system.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.library.system.utils.AlertUtils;
import org.library.system.utils.AppStatus;
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

    public LoginController() {}

    @FXML
    public void initialize() {
        System.out.println("LoginController initialized");
    }

    @FXML
    private void handleLogin() {
        if ((txtEmail.getText().isEmpty() || txtEmail.getText().isBlank()) ||
                (txtPassword.getText().isEmpty() || txtPassword.getText().isBlank())) {
            AlertUtils.instanceAlert().show(AppStatus.INVALID_INPUT,
                    "Campos obligatorios vacíos");
            return;
        }

        // TODO: Aquí iría la validación con la base de datos



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