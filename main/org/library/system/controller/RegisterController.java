package org.library.system.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.library.system.utils.SceneManager;

import java.io.IOException;

public class RegisterController {

    @FXML private TextField txtName;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtConfirmPassword;
    @FXML private Button btnRegister;
    @FXML private Button btnCancel;

    @FXML
    public void initialize() {
        System.out.println("[REGISTER] Inicializando ventana de registro");
        System.out.println("[REGISTER] Ventana de registro lista");
    }

    @FXML
    private void handleRegister() {
        System.out.println("[REGISTER] Iniciando proceso de registro");
        System.out.println("[REGISTER] Leyendo datos del formulario");

        String fullName = txtName.getText();
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        String confirm = txtConfirmPassword.getText();

        System.out.println("[REGISTER] Nombre: " + fullName);
        System.out.println("[REGISTER] Correo: " + email);

        // Validación
        System.out.println("[REGISTER] Validando campos vacios");

        if (fullName == null || fullName.isBlank()) {
            System.out.println("[REGISTER] ERROR: El nombre esta vacio");
            return;
        }
        if (email == null || email.isBlank()) {
            System.out.println("[REGISTER] ERROR: El correo esta vacio");
            return;
        }
        if (password == null || password.isBlank()) {
            System.out.println("[REGISTER] ERROR: La contrasena esta vacia");
            return;
        }
        if (confirm == null || confirm.isBlank()) {
            System.out.println("[REGISTER] ERROR: La confirmacion esta vacia");
            return;
        }

        System.out.println("[REGISTER] Campos validados correctamente");

        System.out.println("[REGISTER] Verificando que las contrasenas coincidan");

        if (!password.equals(confirm)) {
            System.out.println("[REGISTER] ERROR: Las contrasenas no coinciden");
            return;
        }

        System.out.println("[REGISTER] Contrasenas verificadas");

        //Simulación de giardar datos xd
        System.out.println("[REGISTER] (SIMULADO) Guardando usuario en la base de datos");
        System.out.println("[REGISTER] (SIMULADO) Rol asignado: MANAGER");
        System.out.println("[REGISTER] (SIMULADO) Usuario registrado exitosamente");

        System.out.println("[REGISTER] Proceso de registro finalizado");
        System.out.println("[REGISTER] Regresando a la ventana de login");

        goTo("/org/library/system/view/LoginView.fxml");
    }

    @FXML
    private void handleCancel() {
        System.out.println("[REGISTER] Cancelando registro");
        System.out.println("[REGISTER] Regresando a la ventana de login");
        goTo("/org/library/system/view/LoginView.fxml");
    }

    private void goTo(String path) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Scene scene = new Scene(loader.load());
            SceneManager.getInstanciaSceneManager().changeScene(scene);
        } catch (IOException e) {
            System.out.println("[REGISTER] ERROR al navegar: " + e.getMessage());
        }
    }
}