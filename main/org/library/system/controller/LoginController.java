package org.library.system.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.library.system.enums.Role;
import org.library.system.model.User;
import org.library.system.utils.SceneManager;
import org.library.system.utils.SessionManager;

import java.io.IOException;

public class LoginController {

    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnLogin;
    @FXML private Hyperlink hlRegister;

    @FXML
    public void initialize() {
        System.out.println("[LOGIN] Inicializando ventana de inicio de sesion");
        System.out.println("[LOGIN] Correos de prueba disponibles:");
        System.out.println("[LOGIN]   - estudiante@mail.com     (STUDENT)");
        System.out.println("[LOGIN]   - bibliotecario@mail.com  (LIBRARIAN)");
        System.out.println("[LOGIN]   - jefe@mail.com           (MANAGER)");
        System.out.println("[LOGIN] Contrasena para todos: admin123");
        System.out.println("[LOGIN] Ventana de inicio de sesion lista");
    }

    @FXML
    private void handleLogin() {
        System.out.println("[LOGIN] Iniciando proceso de autenticacion");

        String email = txtEmail.getText();
        String password = txtPassword.getText();

        System.out.println("[LOGIN] Correo ingresado: [" + email + "]");

        if (email == null || email.trim().isEmpty()) {
            System.out.println("[LOGIN] ERROR: El correo esta vacio");
            return;
        }
        if (password == null || password.trim().isEmpty()) {
            System.out.println("[LOGIN] ERROR: La contrasena esta vacia");
            return;
        }

        // Normalizar el correo
        email = email.trim().toLowerCase();
        System.out.println("[LOGIN] Correo normalizado: [" + email + "]");

        System.out.println("[LOGIN] Buscando usuario en la base de datos...");
        User user = simulateUserLookup(email);

        if (user == null) {
            System.out.println("[LOGIN] ERROR: Correo no registrado en el sistema");
            return;
        }

        System.out.println("[LOGIN] Usuario encontrado: " + user.getFirst_name()
                + " | Rol: " + user.getUser_role());

        System.out.println("[LOGIN] Enviando usuario al SessionManager");
        boolean success = SessionManager.getInstance().login(user, password);

        if (!success) {
            System.out.println("[LOGIN] ERROR: Contrasena incorrecta");
            return;
        }

        System.out.println("[LOGIN] Autenticacion completada exitosamente");
        System.out.println("[LOGIN] Navegando al panel principal");

        goTo("/org/library/system/view/DashboardView.fxml");

        System.out.println("[LOGIN] Proceso de login finalizado");
    }

    @FXML
    private void handleGoToRegister() {
        System.out.println("[LOGIN] Navegando al registro de bibliotecario jefe");
        goTo("/org/library/system/view/RegisterView.fxml");
    }

    private void goTo(String path) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Scene scene = new Scene(loader.load());
            SceneManager.getInstanciaSceneManager().changeScene(scene);
        } catch (IOException e) {
            System.out.println("[LOGIN] ERROR al navegar: " + e.getMessage());
        }
    }

    /**
     * Simulacion de busqueda de usuario mientras no exista UserRepository.
     * Detecta el rol usando contains() para ser tolerante a variaciones del correo.
     * Contrasena para todos: admin123
     */
    private User simulateUserLookup(String email) {
        System.out.println("[LOGIN] (SIMULADO) Consultando usuario por email");

        User user = new User();
        user.setUser_id(1);
        user.setEmail(email);
        user.setPassword_hash("admin123");
        user.setActive(true);

        if (email.contains("estudiante") || email.contains("student")) {
            user.setUser_code("EST001");
            user.setFirst_name("Juan");
            user.setLast_name("Perez");
            user.setUser_role(Role.STUDENT);
            System.out.println("[LOGIN] (SIMULADO) Rol detectado: STUDENT");
            return user;
        }

        if (email.contains("bibliotecario") || email.contains("librarian")) {
            user.setUser_code("BIB001");
            user.setFirst_name("Maria");
            user.setLast_name("Lopez");
            user.setUser_role(Role.LIBRARIAN);
            System.out.println("[LOGIN] (SIMULADO) Rol detectado: LIBRARIAN");
            return user;
        }

        if (email.contains("jefe") || email.contains("manager") || email.contains("admin")) {
            user.setUser_code("JEF001");
            user.setFirst_name("Carlos");
            user.setLast_name("Ramirez");
            user.setUser_role(Role.MANAGER);
            System.out.println("[LOGIN] (SIMULADO) Rol detectado: MANAGER");
            return user;
        }

        // No reconoce el correo
        System.out.println("[LOGIN] (SIMULADO) Correo no registrado: " + email);
        return null;
    }
}