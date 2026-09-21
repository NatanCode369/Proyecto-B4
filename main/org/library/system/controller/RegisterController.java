package org.library.system.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.library.system.dao.UserDao;
import org.library.system.enums.Role;
import org.library.system.model.User;
import org.library.system.service.EmailRoleResolver;
import org.library.system.utils.PasswordUtil;
import org.library.system.utils.SceneManager;
import org.library.system.utils.Validations;

import java.sql.SQLException;

public class RegisterController {

    @FXML private TextField txtName;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtConfirmPassword;
    @FXML private Button btnRegister;
    @FXML private Button btnCancel;

    private final UserDao userDao = new UserDao();
    private final Validations validations = new Validations();

    @FXML
    public void initialize() {
        // Inicializacion del controlador
    }

    @FXML
    private void handleRegister() {
        String fullName = txtName.getText();
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        String confirm = txtConfirmPassword.getText();

        if (validations.isEmpty(fullName)) return;
        if (validations.isEmpty(email)) return;
        if (validations.isEmpty(password)) return;
        if (validations.isEmpty(confirm)) return;
        if (!validations.validatePasswordMatch(password, confirm)) return;
        if (!validations.validateEmail(email)) return;

        Role role = EmailRoleResolver.resolve(email);
        if (role == null) return;

        try {
            String[] parts = fullName.trim().split(" ", 2);
            String firstName = parts[0];
            String lastName = parts.length > 1 ? parts[1] : "";

            String emailWithoutPrefix = email.substring(email.indexOf('.') + 1);
            String userCode = emailWithoutPrefix.split("@")[0].toUpperCase();

            if (userDao.findByCode(userCode).isPresent()) return;
            if (userDao.findByEmail(email.trim()).isPresent()) return;

            User user = new User();
            user.setUser_code(userCode);
            user.setFirst_name(firstName);
            user.setLast_name(lastName);
            user.setEmail(email.trim().toLowerCase());
            user.setPassword_hash(PasswordUtil.hash(password));
            user.setUser_role(role);
            user.setActive(true);

            userDao.create(user);

            SceneManager.getInstanciaSceneManager().goTo(
                    "/org/library/system/view/LoginView.fxml"
            );

        } catch (SQLException e) {
            System.err.println("Error al registrar: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        SceneManager.getInstanciaSceneManager().goTo(
                "/org/library/system/view/LoginView.fxml"
        );
    }
}