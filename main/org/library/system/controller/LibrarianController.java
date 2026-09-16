package org.library.system.controller;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.library.system.enums.Role;
import org.library.system.model.User;
import org.library.system.utils.SceneManager;

import java.io.IOException;

public class LibrarianController {

    @FXML private TextField txtSearch;
    @FXML private Button btnSearch;
    @FXML private Button btnClearSearch;

    @FXML private TextField txtUserCode;
    @FXML private TextField txtFirstName;
    @FXML private TextField txtLastName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPassword;
    @FXML private CheckBox chkActive;

    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnClearFields;

    @FXML private TableView<User> tableLibrarians;
    @FXML private TableColumn<User, Integer> colUserId;
    @FXML private TableColumn<User, String> colUserCode;
    @FXML private TableColumn<User, String> colFirstName;
    @FXML private TableColumn<User, String> colLastName;
    @FXML private TableColumn<User, String> colEmail;
    @FXML private TableColumn<User, String> colRole;
    @FXML private TableColumn<User, Boolean> colActive;

    @FXML private Button btnBack;

    private final ObservableList<User> librarianList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        System.out.println("[LIBRARIAN] Inicializando ventana de gestion de bibliotecarios");

        colUserId.setCellValueFactory(cell ->
                new SimpleIntegerProperty(cell.getValue().getUser_id()).asObject());

        colUserCode.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getUser_code()));

        colFirstName.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getFirst_name()));

        colLastName.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getLast_name()));

        colEmail.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getEmail()));

        colRole.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getUser_role().name()));

        colActive.setCellValueFactory(cell ->
                new SimpleBooleanProperty(cell.getValue().getActive()).asObject());

        tableLibrarians.setItems(librarianList);

        tableLibrarians.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        loadUserToForm(newSelection);
                    }
                }
        );

        loadTestData();

        System.out.println("[LIBRARIAN] Ventana de gestion lista");
    }

    @FXML
    private void handleSearch() {
        System.out.println("[LIBRARIAN] Iniciando busqueda");
        String filter = txtSearch.getText();
        System.out.println("[LIBRARIAN] Filtro: " + filter);

        // TODO: Conectar con UserRepository.search(filter)
        System.out.println("[LIBRARIAN] (SIMULADO) Busqueda finalizada");
    }

    @FXML
    private void handleClearSearch() {
        System.out.println("[LIBRARIAN] Limpiando busqueda");
        txtSearch.clear();
    }

    @FXML
    private void handleAdd() {
        System.out.println("[LIBRARIAN] Iniciando registro de bibliotecario");

        if (!validateFields()) {
            return;
        }

        System.out.println("[LIBRARIAN] Codigo: " + txtUserCode.getText());
        System.out.println("[LIBRARIAN] Nombre: " + txtFirstName.getText());
        System.out.println("[LIBRARIAN] Apellido: " + txtLastName.getText());
        System.out.println("[LIBRARIAN] Correo: " + txtEmail.getText());
        System.out.println("[LIBRARIAN] Rol: LIBRARIAN");
        System.out.println("[LIBRARIAN] Activo: " + chkActive.isSelected());

        // TODO: Conectar con UserRepository.create(user)

        System.out.println("[LIBRARIAN] (SIMULADO) Bibliotecario registrado");
        clearFields();
    }

    @FXML
    private void handleUpdate() {
        System.out.println("[LIBRARIAN] Iniciando actualizacion de bibliotecario");

        User selected = tableLibrarians.getSelectionModel().getSelectedItem();
        if (selected == null) {
            System.out.println("[LIBRARIAN] ERROR: Seleccione un bibliotecario de la tabla");
            return;
        }

        if (!validateFields()) {
            return;
        }

        System.out.println("[LIBRARIAN] ID: " + selected.getUser_id());
        System.out.println("[LIBRARIAN] Nuevo codigo: " + txtUserCode.getText());
        System.out.println("[LIBRARIAN] Nuevo nombre: " + txtFirstName.getText());

        // TODO: Conectar con UserRepository.update(user)

        System.out.println("[LIBRARIAN] (SIMULADO) Bibliotecario actualizado");
        clearFields();
    }

    @FXML
    private void handleDelete() {
        System.out.println("[LIBRARIAN] Iniciando eliminacion de bibliotecario");

        User selected = tableLibrarians.getSelectionModel().getSelectedItem();
        if (selected == null) {
            System.out.println("[LIBRARIAN] ERROR: Seleccione un bibliotecario de la tabla");
            return;
        }

        System.out.println("[LIBRARIAN] Eliminando: " + selected.getUser_code());

        // TODO: Conectar con UserRepository.delete(user_id)

        System.out.println("[LIBRARIAN] (SIMULADO) Bibliotecario eliminado");
        librarianList.remove(selected);
        clearFields();
    }

    @FXML
    private void handleClearFields() {
        System.out.println("[LIBRARIAN] Limpiando campos del formulario");
        clearFields();
    }

    @FXML
    private void handleBack() {
        System.out.println("[LIBRARIAN] Regresando al panel principal");
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/library/system/view/DashboardView.fxml")
            );
            Scene scene = new Scene(loader.load());
            SceneManager.getInstanciaSceneManager().changeScene(scene);
        } catch (IOException e) {
            System.out.println("[LIBRARIAN] ERROR al navegar: " + e.getMessage());
        }
    }

    /**
     * Carga los datos de un usuario en el formulario.
     */
    private void loadUserToForm(User user) {
        System.out.println("[LIBRARIAN] Cargando datos al formulario: " + user.getUser_code());
        txtUserCode.setText(user.getUser_code());
        txtFirstName.setText(user.getFirst_name());
        txtLastName.setText(user.getLast_name());
        txtEmail.setText(user.getEmail());
        txtPassword.setText("");
        chkActive.setSelected(user.getActive());
    }

    /**
     * Limpia todos los campos del formulario.
     */
    private void clearFields() {
        txtUserCode.clear();
        txtFirstName.clear();
        txtLastName.clear();
        txtEmail.clear();
        txtPassword.clear();
        chkActive.setSelected(true);
        tableLibrarians.getSelectionModel().clearSelection();
    }

    /**
     * Valida que los campos obligatorios esten llenos.
     */
    private boolean validateFields() {
        if (txtUserCode.getText() == null || txtUserCode.getText().isBlank()) {
            System.out.println("[LIBRARIAN] ERROR: El codigo es obligatorio");
            return false;
        }
        if (txtFirstName.getText() == null || txtFirstName.getText().isBlank()) {
            System.out.println("[LIBRARIAN] ERROR: El nombre es obligatorio");
            return false;
        }
        if (txtLastName.getText() == null || txtLastName.getText().isBlank()) {
            System.out.println("[LIBRARIAN] ERROR: El apellido es obligatorio");
            return false;
        }
        if (txtEmail.getText() == null || txtEmail.getText().isBlank()) {
            System.out.println("[LIBRARIAN] ERROR: El correo es obligatorio");
            return false;
        }
        return true;
    }

    /**
     * Carga datos de prueba (mientras no exista UserRepository).
     */
    private void loadTestData() {
        System.out.println("[LIBRARIAN] Cargando datos de prueba");

        User bib1 = new User();
        bib1.setUser_id(2);
        bib1.setUser_code("BIB001");
        bib1.setFirst_name("Maria");
        bib1.setLast_name("Lopez");
        bib1.setEmail("bibliotecario@biblioteca.edu");
        bib1.setUser_role(Role.LIBRARIAN);
        bib1.setActive(true);

        User bib2 = new User();
        bib2.setUser_id(3);
        bib2.setUser_code("BIB002");
        bib2.setFirst_name("Pedro");
        bib2.setLast_name("Garcia");
        bib2.setEmail("pedro.garcia@biblioteca.edu");
        bib2.setUser_role(Role.LIBRARIAN);
        bib2.setActive(true);

        User bib3 = new User();
        bib3.setUser_id(4);
        bib3.setUser_code("BIB003");
        bib3.setFirst_name("Ana");
        bib3.setLast_name("Martinez");
        bib3.setEmail("ana.martinez@biblioteca.edu");
        bib3.setUser_role(Role.LIBRARIAN);
        bib3.setActive(false);

        librarianList.addAll(bib1, bib2, bib3);
        System.out.println("[LIBRARIAN] " + librarianList.size() + " bibliotecarios cargados");
    }
}