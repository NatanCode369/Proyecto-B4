package org.library.system.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
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

    private final Validations validations;

    public RegisterController() {
        this.validations = new Validations();
    }

    @FXML
    public void initialize() {
        System.out.println("RegisterController initialized");
    }

    @FXML
    private void handleRegister() {
        String name = txtName.getText();
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        String confirm = txtConfirmPassword.getText();

        System.out.println("INICIANDO REGISTRO");

        if (validations.isEmpty(name)) {
            System.out.println("Error: El nombre está vacío");
            return;
        }
        if (validations.isEmpty(email)) {
            System.out.println("Error: El correo está vacío");
            return;
        }
        if (validations.isEmpty(password)) {
            System.out.println("Error: La contraseña está vacía");
            return;
        }
        if (validations.isEmpty(confirm)) {
            System.out.println("Error: La confirmación de contraseña está vacía");
            return;
        }

        if (!validations.validatePasswordMatch(password, confirm)) {
            System.out.println("Error: Las contraseñas no coinciden");
            return;
        }

        if (!validations.validateEmail(email)) {
            System.out.println("Error: El correo no tiene un formato válido");
            return;
        }

        if (!validations.validateOnlyLetters(name)) {
            System.out.println("Error: El nombre solo debe contener letras");
            return;
        }

        System.out.println("Registro exitoso (simulado)");
        System.out.println("  Nombre: " + name);
        System.out.println("  Correo: " + email);

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