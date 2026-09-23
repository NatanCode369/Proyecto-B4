package org.library.system.controller;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.library.system.dao.UserDao;
import org.library.system.enums.Role;
import org.library.system.model.User;
import org.library.system.utils.AlertUtils;
import org.library.system.utils.AppStatus;
import org.library.system.utils.PasswordUtil;
import org.library.system.utils.SceneManager;
import org.library.system.utils.Validations;

import java.sql.SQLException;
import java.util.List;

public class LibrarianController {

    @FXML private TextField txtSearch;
    @FXML private TextField txtUserCode;
    @FXML private TextField txtFirstName;
    @FXML private TextField txtLastName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPassword;
    @FXML private CheckBox chkActive;

    @FXML private TableView<User> tableLibrarians;
    @FXML private TableColumn<User, Integer> colUserId;
    @FXML private TableColumn<User, String> colUserCode;
    @FXML private TableColumn<User, String> colFirstName;
    @FXML private TableColumn<User, String> colLastName;
    @FXML private TableColumn<User, String> colEmail;
    @FXML private TableColumn<User, String> colRole;
    @FXML private TableColumn<User, Boolean> colActive;

    @FXML private Button btnBack;

    private final UserDao userDao = new UserDao();
    private final Validations validations = Validations.getInstancevalidations();
    private final ObservableList<User> librarianList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configureTable();
        tableLibrarians.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if (newVal != null) loadToForm(newVal);
                }
        );
        loadLibrarians();
    }

    private void configureTable() {
        colUserId.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getUser_id()).asObject());
        colUserCode.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getUser_code()));
        colFirstName.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFirst_name()));
        colLastName.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getLast_name()));
        colEmail.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEmail()));
        colRole.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getUser_role().name()));
        colActive.setCellValueFactory(c -> new SimpleBooleanProperty(c.getValue().getActive()).asObject());
        tableLibrarians.setItems(librarianList);
    }

    private void loadLibrarians() {
        try {
            List<User> users = userDao.search("");
            users.removeIf(u -> u.getUser_role() != Role.LIBRARIAN);
            librarianList.setAll(users);
        } catch (SQLException e) {
            AlertUtils.instanceAlert().show(AppStatus.DATABASE_UNAVAILABLE,
                    "Error al cargar bibliotecarios. Intente nuevamente. ");
        }
    }

    @FXML
    private void handleSearch() {
        String filter = txtSearch.getText();
        try {
            List<User> results = userDao.search(filter == null ? "" : filter.trim());
            results.removeIf(u -> u.getUser_role() != Role.LIBRARIAN);
            librarianList.setAll(results);

            if (results.isEmpty()) {
                AlertUtils.instanceAlert().show(AppStatus.NOT_FOUND,
                        "No se encontraron bibliotecarios con los criterio establecidos.");
            }
        } catch (SQLException e) {
            AlertUtils.instanceAlert().show(AppStatus.DATABASE_UNAVAILABLE,
                    "Error al realizar la búsqueda.");
        }
    }

    @FXML
    private void handleClearSearch() {
        txtSearch.clear();
        loadLibrarians();
    }

    @FXML
    private void handleAdd() {
        if (txtUserCode.getText().isEmpty()
                || txtFirstName.getText().isEmpty()
                || txtLastName.getText().isEmpty()
                || txtEmail.getText().isEmpty()
                || txtPassword.getText().isEmpty()) {
            AlertUtils.instanceAlert().show(AppStatus.INVALID_INPUT,
                    "Complete todos los campos obligatorios.");
            return;
        }

        if (!validations.validateEmail(txtEmail.getText().trim())) {
            AlertUtils.instanceAlert().show(AppStatus.INVALID_INPUT,
                    "Formato no válido para el email.");
            return;
        }

        try {
            String userCode = txtUserCode.getText().trim();
            String email = txtEmail.getText().trim().toLowerCase();

            if (userDao.findByCode(userCode).isPresent()) {
                AlertUtils.instanceAlert().show(AppStatus.CONFLICT,
                        "El código ya existe: " + userCode);
                return;
            }
            if (userDao.findByEmail(email).isPresent()) {
                AlertUtils.instanceAlert().show(AppStatus.CONFLICT,
                        "El correo ya existe: " + email);
                return;
            }

            User user = new User();
            user.setUser_code(userCode);
            user.setFirst_name(txtFirstName.getText().trim());
            user.setLast_name(txtLastName.getText().trim());
            user.setEmail(email);
            user.setPassword_hash(PasswordUtil.hash(txtPassword.getText()));
            user.setUser_role(Role.LIBRARIAN);
            user.setActive(chkActive.isSelected());

            userDao.create(user);

            AlertUtils.instanceAlert().show(AppStatus.CREATED,
                    "Bibliotecario registrado correctamente.");

            loadLibrarians();
            clearFields();

        } catch (SQLException e) {
            AlertUtils.instanceAlert().show(AppStatus.DATABASE_UNAVAILABLE,
                    "Error al crear bibliotecario: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdate() {
        User selected = tableLibrarians.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.instanceAlert().show(AppStatus.INVALID_INPUT,
                    "Seleccione un bibliotecario de la tabla.");
            return;
        }

        if (txtUserCode.getText().isEmpty()
                || txtFirstName.getText().isEmpty()
                || txtLastName.getText().isEmpty()
                || txtEmail.getText().isEmpty()) {
            AlertUtils.instanceAlert().show(AppStatus.INVALID_INPUT,
                    "Complete todos los campos obligatorios.");
            return;
        }

        try {
            selected.setUser_code(txtUserCode.getText().trim());
            selected.setFirst_name(txtFirstName.getText().trim());
            selected.setLast_name(txtLastName.getText().trim());
            selected.setEmail(txtEmail.getText().trim().toLowerCase());
            if (!txtPassword.getText().isBlank()) {
                selected.setPassword_hash(PasswordUtil.hash(txtPassword.getText()));
            }
            selected.setActive(chkActive.isSelected());
            userDao.update(selected);

            AlertUtils.instanceAlert().show(AppStatus.OK,
                    "Bibliotecario actualizado correctamente.");

            loadLibrarians();
            clearFields();

        } catch (SQLException e) {
            AlertUtils.instanceAlert().show(AppStatus.DATABASE_UNAVAILABLE,
                    "Error al actualizar: " + e.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        User selected = tableLibrarians.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.instanceAlert().show(AppStatus.INVALID_INPUT,
                    "Seleccione un bibliotecario de la tabla.");
            return;
        }

        try {
            userDao.delete(selected.getUser_id());

            AlertUtils.instanceAlert().show(AppStatus.DELETED,
                    "Bibliotecario eliminado correctamente.");

            loadLibrarians();
            clearFields();

        } catch (SQLException e) {
            AlertUtils.instanceAlert().show(AppStatus.DATABASE_UNAVAILABLE,
                    "Error al intentar eliminar. Pruebe nuevamente. ");
        }
    }

    @FXML
    private void handleClearFields() {
        clearFields();
    }

    @FXML
    private void handleBack() {
        SceneManager.getInstanciaSceneManager().goTo(
                "/org/library/system/view/DashboardView.fxml"
        );
    }

    private void loadToForm(User user) {
        txtUserCode.setText(user.getUser_code());
        txtFirstName.setText(user.getFirst_name());
        txtLastName.setText(user.getLast_name());
        txtEmail.setText(user.getEmail());
        txtPassword.clear();
        chkActive.setSelected(user.getActive());
    }

    private void clearFields() {
        txtUserCode.clear();
        txtFirstName.clear();
        txtLastName.clear();
        txtEmail.clear();
        txtPassword.clear();
        chkActive.setSelected(true);
        tableLibrarians.getSelectionModel().clearSelection();
    }
}