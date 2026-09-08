package org.library.system.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterController {

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtCorreo;

    @FXML
    private PasswordField txtContrasena;

    @FXML
    private PasswordField txtConfirmarContrasena;

    @FXML
    private Button btnRegistrar;

    @FXML
    private Button btnCancelar;

    @FXML
    public void initialize() {
        // Inicializacion del controlador
    }

    @FXML
    private void handleRegistrar() {
        String nombre = txtNombre.getText();
        String correo = txtCorreo.getText();
        String contrasena = txtContrasena.getText();
        String confirmar = txtConfirmarContrasena.getText();

        System.out.println("Registro de bibliotecario jefe:");
        System.out.println("Nombre: " + nombre);
        System.out.println("Correo: " + correo);

        if (!contrasena.equals(confirmar)) {
            System.out.println("Las contrasenas no coinciden");
            return;
        }

        // Aqui se integrara el guardado real en la base de datos
        Stage stageActual = (Stage) btnRegistrar.getScene().getWindow();
        SceneManagerController.cerrarYAbrir(
                stageActual,
                "/org/library/system/view/LoginView.fxml",
                "Iniciar Sesion",
                600, 400
        );
    }

    @FXML
    private void handleCancelar() {
        Stage stageActual = (Stage) btnCancelar.getScene().getWindow();
        SceneManagerController.cerrarYAbrir(
                stageActual,
                "/org/library/system/view/LoginView.fxml",
                "Iniciar Sesion",
                600, 400
        );
    }
}