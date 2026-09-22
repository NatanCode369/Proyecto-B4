package org.library.system.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.library.system.dao.UserDao;
import org.library.system.model.User;
import org.library.system.utils.PasswordUtil;
import org.library.system.utils.SceneManager;
import org.library.system.utils.SessionManager;

import java.sql.SQLException;
import java.util.Optional;

public class LoginController {

    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnLogin;
    @FXML private Hyperlink hlRegister;

    private final UserDao userDao = new UserDao();


    @FXML
    public void initialize() {
        // Inicializacion del controlador
    }

    @FXML
    private void handleLogin() {
        String email = txtEmail.getText();
        String password = txtPassword.getText();

        if (email == null || email.isBlank()) return;
        if (password == null || password.isBlank()) return;

        try {
            Optional<User> userOpt = userDao.loginByEmail(email.trim().toLowerCase());

            if (userOpt.isEmpty()) return;

            User user = userOpt.get();

            if (!PasswordUtil.verify(password, user.getPassword_hash())) return;

            SessionManager.getInstance().login(user, user.getPassword_hash());

            SceneManager.getInstanciaSceneManager().goTo(
                    "/org/library/system/view/DashboardView.fxml"
            );

        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
        }
    }

    @FXML
    private void handleGoToRegister() {
        SceneManager.getInstanciaSceneManager().goTo(
                "/org/library/system/view/RegisterView.fxml"
        );
    }
}