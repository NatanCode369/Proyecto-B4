package org.library.system.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.library.system.dao.UserDao;
import org.library.system.enums.Role;
import org.library.system.model.User;
import org.library.system.utils.AlertUtils;
import org.library.system.utils.AppStatus;
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
    private final Validations validations = Validations.getInstancevalidations();

    @FXML
    public void initialize() {
        // Inicializacion del controlador
    }

    @FXML
    private void handleRegister() {
        if (txtName.getText().isEmpty()
                || txtEmail.getText().isEmpty()
                || txtPassword.getText().isEmpty()
                || txtConfirmPassword.getText().isEmpty()) {
            AlertUtils.instanceAlert().show(AppStatus.INVALID_INPUT,
                    "Campos obligatorios vacíos");
            return;
        }

        if (!validations.validatePasswordMatch(txtPassword.getText(), txtConfirmPassword.getText())) {
            AlertUtils.instanceAlert().show(AppStatus.INVALID_INPUT,
                    "Las contraseñas no coinciden, asegúrese de que ambas contraseñas coinciden.");
            return;
        }

        if (!validations.validatePasswordStrength(txtConfirmPassword.getText(), 8)) {
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

        if (!validations.validateEmail(txtEmail.getText().trim())) {
            AlertUtils.instanceAlert().show(AppStatus.INVALID_INPUT,
                    "Formato no válido para el email.");
            return;
        }

        String email = txtEmail.getText().trim().toLowerCase();

        // Solo se permiten correos con prefijo std. (estudiantes)
        if (!email.startsWith("std.")) {
            AlertUtils.instanceAlert().show(AppStatus.INVALID_INPUT,
                    "Solo se pueden registrar estudiantes.\n" +
                            "El correo debe iniciar con 'std.'");
            return;
        }

        try {
            String[] parts = txtName.getText().trim().split(" ", 2);
            String firstName = parts[0];
            String lastName = parts.length > 1 ? parts[1] : "";

            String emailWithoutPrefix = email.substring(email.indexOf('.') + 1);
            String userCode = emailWithoutPrefix.split("@")[0].toUpperCase();

            if (userDao.findByCode(userCode).isPresent()) {
                AlertUtils.instanceAlert().show(AppStatus.CONFLICT,
                        "El código de usuario ya existe: " + userCode);
                return;
            }
            if (userDao.findByEmail(email).isPresent()) {
                AlertUtils.instanceAlert().show(AppStatus.CONFLICT,
                        "El correo ya está registrado: " + email);
                return;
            }

            User user = new User();
            user.setUser_code(userCode);
            user.setFirst_name(firstName);
            user.setLast_name(lastName);
            user.setEmail(email);
            user.setPassword_hash(PasswordUtil.hash(txtPassword.getText()));
            user.setUser_role(Role.STUDENT);  // ← forzado a STUDENT
            user.setActive(true);

            userDao.create(user);

            AlertUtils.instanceAlert().show(AppStatus.CREATED,
                    "Estudiante registrado correctamente. Redirigiendo al login...");

            SceneManager.getInstanciaSceneManager().goTo(
                    "/org/library/system/view/LoginView.fxml"
            );

        } catch (SQLException e) {
            AlertUtils.instanceAlert().show(AppStatus.DATABASE_UNAVAILABLE,
                    "Error al registrar: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        SceneManager.getInstanciaSceneManager().goTo(
                "/org/library/system/view/LoginView.fxml"
        );
    }
}