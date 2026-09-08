package org.library.system.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField txtCorreo;

    @FXML
    private PasswordField txtContrasena;

    @FXML
    private Button btnIniciarSesion;

    @FXML
    private Hyperlink hlRegistro;

    @FXML
    public void initialize() {
        // Inicializacion del controlador
        System.out.println("LoginController inicializado");
    }

    @FXML
    private void handleLogin() {
        String correo = txtCorreo.getText();
        String contrasena = txtContrasena.getText();

        System.out.println("Intento de login con correo: " + correo);

        // Por ahora, el sistema asigna un rol fijo (Bibliotecario Jefe)
        // En el futuro, este rol se obtendra de la base de datos
        String rol = "Bibliotecario Jefe";

        // Establecer el rol en el DashboardController
        DashboardController.setRol(rol);

        // Cerrar ventana actual y abrir Dashboard
        Stage stageActual = (Stage) btnIniciarSesion.getScene().getWindow();
        SceneManagerController.cerrarYAbrir(
                stageActual,
                "/org/library/system/view/DashboardView.fxml",
                "Panel Principal",
                800, 600
        );
    }

    @FXML
    private void handleIrARegistro() {
        Stage stageActual = (Stage) hlRegistro.getScene().getWindow();
        SceneManagerController.cerrarYAbrir(
                stageActual,
                "/org/library/system/view/RegisterView.fxml",
                "Registro de Bibliotecario Jefe",
                500, 400
        );
    }
}