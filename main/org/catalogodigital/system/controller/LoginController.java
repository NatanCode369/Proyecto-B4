package org.catalogodigital.system.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
    private ComboBox<String> cbRol;

    @FXML
    private Button btnIniciarSesion;

    @FXML
    private Hyperlink hlRegistro;

    @FXML
    private void handleLogin() {
        String correo = txtCorreo.getText();
        String contrasena = txtContrasena.getText();
        String rol = cbRol.getSelectionModel().getSelectedItem();

        System.out.println("Intento de login con correo: " + correo + " y rol: " + rol);

        /*
         * xd aquí va la validación de verdad cuando sirva, por mientras
         * solo te manda al Dashboard pasando el rol y ya :v
         */
        Stage stageActual = (Stage) btnIniciarSesion.getScene().getWindow();
        /**
         * Cerramos el Login y abrimos el Dashboard pasando el rol ._.
         * Para esto usamos el SceneManager, pero como hay que mandar el rol,
         * o inventamos un método específico ahí o lo hacemos a la fuerza...
         * La opción rápida era meter un método estático en DashboardController
         * o arreglar SceneManager para que acepte parámetros, pero por pura flojera
         * y para no complicarnos la vida, usamos una variable estática ahí xd
         */
        DashboardController.setRol(rol);
        SceneManagerController.cerrarYAbrir(
                stageActual,
                "/org/catalogodigital/system/view/DashboardView.fxml",
                "Panel Principal",
                800, 600
        );
    }

    @FXML
    private void handleIrARegistro() {
        Stage stageActual = (Stage) hlRegistro.getScene().getWindow();
        SceneManagerController.cerrarYAbrir(
                stageActual,
                "/org/catalogodigital/system/view/RegisterView.fxml",
                "Registro de Bibliotecario Jefe",
                500, 400
        );
    }
}