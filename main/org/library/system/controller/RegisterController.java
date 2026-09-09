package org.library.system.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

    @FXML
    public void initialize() {
        // Inicialización
    }

    @FXML
    private void handleRegister() {
        String name = txtName.getText();
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        String confirm = txtConfirmPassword.getText();

        System.out.println("Register: " + name + " - " + email);

        if (!password.equals(confirm)) {
            System.out.println("Las contraseñas no coinciden");
            return;
        }

        // Aquí se guardaría en la base de datos
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