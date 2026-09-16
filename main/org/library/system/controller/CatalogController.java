package org.library.system.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.library.system.enums.Role;
import org.library.system.model.User;
import org.library.system.utils.SceneManager;
import org.library.system.utils.SessionManager;

import java.io.IOException;

public class CatalogController {

    private static boolean editMode = true;

    // Barra de busqueda (siempre visible)
    @FXML private TextField txtSearchTitle;
    @FXML private TextField txtSearchIsbn;
    @FXML private Button btnSearch;
    @FXML private Button btnClearSearch;

    // Seccion de edicion (solo bibliotecarios)
    @FXML private VBox editSection;
    @FXML private TextField txtIsbn;
    @FXML private TextField txtTitle;
    @FXML private TextField txtAuthor;
    @FXML private TextField txtPublisher;
    @FXML private TextField txtYear;
    @FXML private TextField txtCopies;
    @FXML private Button btnAdd;
    @FXML private Button btnClear;

    // Tabla
    @FXML private TableView<?> tableBooks;
    @FXML private TableColumn<?, ?> colIsbn;
    @FXML private TableColumn<?, ?> colTitle;
    @FXML private TableColumn<?, ?> colAuthor;
    @FXML private TableColumn<?, ?> colPublisher;
    @FXML private TableColumn<?, ?> colYear;
    @FXML private TableColumn<?, ?> colCopies;

    // Botones de accion
    @FXML private Button btnNewBorrowing;
    @FXML private Button btnRequestBorrowing;
    @FXML private Button btnBack;

    public static void setEditMode(boolean editable) {
        editMode = editable;
    }

    @FXML
    public void initialize() {
        System.out.println("[CATALOG] Inicializando ventana de catalogo");
        System.out.println("[CATALOG] Modo edicion: " + editMode);

        // Obtener usuario de la sesion
        User user = SessionManager.getInstance().getCurrentUser();
        if (user == null) {
            System.out.println("[CATALOG] ERROR: No hay usuario en sesion");
            return;
        }

        Role role = user.getUser_role();
        System.out.println("[CATALOG] Rol del usuario: " + role);

        // Seccion de edicion: solo para LIBRARIAN y MANAGER
        boolean puedeEditar = (role == Role.LIBRARIAN || role == Role.MANAGER);
        editSection.setVisible(puedeEditar);
        editSection.setManaged(puedeEditar);

        // Boton "Nuevo Prestamo": solo para LIBRARIAN y MANAGER
        btnNewBorrowing.setVisible(puedeEditar);
        btnNewBorrowing.setManaged(puedeEditar);

        // Boton "Solicitar Prestamo": solo para STUDENT
        boolean esEstudiante = (role == Role.STUDENT);
        btnRequestBorrowing.setVisible(esEstudiante);
        btnRequestBorrowing.setManaged(esEstudiante);

        System.out.println("[CATALOG] Seccion de edicion visible: " + puedeEditar);
        System.out.println("[CATALOG] Boton Nuevo Prestamo visible: " + puedeEditar);
        System.out.println("[CATALOG] Boton Solicitar Prestamo visible: " + esEstudiante);
        System.out.println("[CATALOG] Ventana de catalogo lista");
    }

    @FXML
    private void handleSearch() {
        System.out.println("[CATALOG] Iniciando busqueda de libros");
        String title = txtSearchTitle.getText();
        String isbn = txtSearchIsbn.getText();

        System.out.println("[CATALOG] Filtros:");
        System.out.println("[CATALOG]   Titulo: " + title);
        System.out.println("[CATALOG]   ISBN: " + isbn);

        // TODO: Conectar con BookRepository.search(title, isbn)
        System.out.println("[CATALOG] (SIMULADO) Buscando libros...");
        System.out.println("[CATALOG] Busqueda finalizada");
    }

    @FXML
    private void handleClearSearch() {
        System.out.println("[CATALOG] Limpiando filtros de busqueda");
        txtSearchTitle.clear();
        txtSearchIsbn.clear();
    }

    @FXML
    private void handleAddBook() {
        System.out.println("[CATALOG] Iniciando registro de libro");
        System.out.println("[CATALOG] ISBN: " + txtIsbn.getText());
        System.out.println("[CATALOG] Titulo: " + txtTitle.getText());
        System.out.println("[CATALOG] Autor: " + txtAuthor.getText());

        // TODO: Conectar con BookRepository.create(book)
        System.out.println("[CATALOG] (SIMULADO) Libro registrado");
        clearFields();
    }

    @FXML
    private void handleClearFields() {
        clearFields();
        System.out.println("[CATALOG] Campos limpiados");
    }

    /**
     * Nuevo Prestamo (BIBLIOTECARIO)
     * Registra directamente un prestamo en la tabla loan.
     * Se usa cuando el estudiante pide el libro en persona.
     */
    @FXML
    private void handleNewBorrowing() {
        System.out.println("[CATALOG] Abriendo ventana de nuevo prestamo (Bibliotecario)");
        goTo("/org/library/system/view/BorrowingView.fxml");
    }

    /**
     * Solicitar Prestamo (ESTUDIANTE)
     * Crea un registro en loan_request (PENDING).
     * El bibliotecario debe aprobarlo.
     */
    @FXML
    private void handleRequestBorrowing() {
        System.out.println("[CATALOG] Iniciando solicitud de prestamo (Estudiante)");

        User user = SessionManager.getInstance().getCurrentUser();
        if (user == null) {
            System.out.println("[CATALOG] ERROR: No hay usuario en sesion");
            return;
        }

        // Validar que haya seleccionado un libro
        if (tableBooks.getSelectionModel().isEmpty()) {
            System.out.println("[CATALOG] ERROR: Seleccione un libro de la tabla");
            return;
        }

        System.out.println("[CATALOG] Estudiante: " + user.getUser_code());
        System.out.println("[CATALOG] Libro seleccionado: (pendiente)");

        // TODO: Conectar con LoanRequestRepository.create(request)
        System.out.println("[CATALOG] (SIMULADO) Solicitud creada con estado PENDING");
        System.out.println("[CATALOG] La solicitud debe ser aprobada por un bibliotecario");
    }

    @FXML
    private void handleBack() {
        System.out.println("[CATALOG] Regresando al panel principal");
        goTo("/org/library/system/view/DashboardView.fxml");
    }

    private void clearFields() {
        txtIsbn.clear();
        txtTitle.clear();
        txtAuthor.clear();
        txtPublisher.clear();
        txtYear.clear();
        txtCopies.clear();
    }

    private void goTo(String path) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Scene scene = new Scene(loader.load());
            SceneManager.getInstanciaSceneManager().changeScene(scene);
        } catch (IOException e) {
            System.out.println("[CATALOG] ERROR al navegar: " + e.getMessage());
        }
    }
}