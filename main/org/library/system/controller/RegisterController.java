package org.library.system.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.library.system.utils.AlertUtils;
import org.library.system.utils.AppStatus;
import org.library.system.utils.Validations;

public class RegisterController {

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private PasswordField txtConfirmPassword;

    @FXML
    private Button btnRegister;

    @FXML
    private Button btnCancel;

    public RegisterController() {}

    @FXML
    public void initialize() {

    }

    @FXML
    private void handleRegister() {
        if ((txtName.getText().isEmpty() || txtName.getText().isBlank()) ||
                (txtEmail.getText().isEmpty() || txtEmail.getText().isBlank()) ||
                (txtPassword.getText().isEmpty() || txtPassword.getText().isBlank()) ||
                (txtConfirmPassword.getText().isEmpty() || txtConfirmPassword.getText().isBlank()))
        {
            AlertUtils.instanceAlert().show(AppStatus.INVALID_INPUT,
                    "Campos obligatorios vacíos");
            return;
        }

        if (txtPassword.getText().equals(txtConfirmPassword.getText()))
        {
            AlertUtils.instanceAlert().show(AppStatus.INVALID_INPUT,
                    "Las contraseñas ingresadas no son idénticas");
            return;
        }

        if ((txtPassword.getText().equals(txtConfirmPassword.getText()))) {
            AlertUtils.instanceAlert().show(AppStatus.INVALID_INPUT,
                    "Las contraseñas no coinciden, asegúrese de que ambas contraseñas coinciden.");
            return;
        }

        if (Validations.getInstancevalidations()
                .validatePasswordStrength(txtConfirmPassword.getText(), 8)){
            AlertUtils.instanceAlert().show(AppStatus.INVALID_INPUT,
                    """
                            La contraseña no cumple los requisitos mínimos:
                            - 8 caracteres mínimo.
                            - Al menos 1 letra mayúscula y 1 minúscula.
                            - Al menos 1 caracter especial.
                            - Al menos 1 número.
                            """);
            return;
        }

        if (Validations.getInstancevalidations().validateEmail(
                txtEmail.getText()
        )) {
            AlertUtils.instanceAlert().show(AppStatus.INVALID_INPUT,
                    "Formato no válido para el email.");
            return;
        }

        // TODO: Aquí iría el guardado en la base de datos

        // Redirigir al Login
        Stage currentStage = (Stage) btnRegister.getScene().getWindow();
        SceneManagerController.closeAndOpen(
                currentStage,
                "/org/library/system/view/LoginView.fxml",
                "Iniciar Sesion"
        );
    }

    @FXML
    private void handleCancel() {
        Stage currentStage = (Stage) btnCancel.getScene().getWindow();
        SceneManagerController.closeAndOpen(
                currentStage,
                "/org/library/system/view/LoginView.fxml",
                "Iniciar Sesion"
        );
    }
}