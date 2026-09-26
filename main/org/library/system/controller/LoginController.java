package org.library.system.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.library.system.dao.UserDao;
import org.library.system.model.User;
import org.library.system.utils.AlertUtils;
import org.library.system.utils.AppStatus;
import org.library.system.utils.PasswordUtil;
import org.library.system.utils.SceneManager;
import org.library.system.utils.SessionManager;
import org.library.system.utils.Validations;

import java.sql.SQLException;
import java.util.Optional;

public class LoginController {

    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnLogin;
    @FXML private Hyperlink hlRegister;

    private final UserDao userDao = new UserDao();
    private final Validations validations = Validations.getInstancevalidations();

    @FXML
    public void initialize() {
        // Inicialización del controlador
    }

    @FXML
    private void handleLogin() {
        String email = txtEmail.getText();
        String password = txtPassword.getText();

        // Validar campos vacíos
        if (email == null || email.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {
            AlertUtils.instanceAlert().show(AppStatus.INVALID_INPUT,
                    "Debe ingresar correo y contraseña.");
            return;
        }

        // Validar formato del correo
        if (!validations.validateEmail(email.trim())) {
            AlertUtils.instanceAlert().show(AppStatus.INVALID_INPUT,
                    "Formato no válido para el email.");
            return;
        }

        try {
            Optional<User> userOpt = userDao.loginByEmail(email.trim().toLowerCase());

            if (userOpt.isEmpty()) {
                AlertUtils.instanceAlert().show(AppStatus.NOT_FOUND,
                        "Usuario no encontrado o inactivo.");
                return;
            }

            User user = userOpt.get();

            if (!PasswordUtil.verify(password, user.getPassword_hash())) {
                AlertUtils.instanceAlert().show(AppStatus.UNAUTHORIZED,
                        "Credenciales incorrectas.");
                return;
            }

            SessionManager.getInstance().login(user, user.getPassword_hash());

            SceneManager.getInstanciaSceneManager().goTo(
                    "/org/library/system/view/DashboardView.fxml"
            );

        } catch (SQLException e) {
            AlertUtils.instanceAlert().show(AppStatus.DATABASE_UNAVAILABLE,
                    "Error de base de datos: " + e.getMessage());
        }
    }

    @FXML
    private void handleGoToRegister() {
        SceneManager.getInstanciaSceneManager().goTo(
                "/org/library/system/view/RegisterView.fxml"
        );
    }
}