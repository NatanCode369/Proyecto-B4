package org.library.system.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.library.system.utils.Validations;

public class CatalogController {

    private static boolean editMode = true;

    @FXML
    private TextField txtIsbn;

    @FXML
    private TextField txtTitle;

    @FXML
    private TextField txtAuthor;

    @FXML
    private TextField txtPublisher;

    @FXML
    private TextField txtYear;

    @FXML
    private TextField txtCopies;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnNewBorrowing;

    @FXML
    private Button btnBack;

    @FXML
    private TableView<?> tableBooks;

    @FXML
    private TableColumn<?, ?> colIsbn;

    @FXML
    private TableColumn<?, ?> colTitle;

    @FXML
    private TableColumn<?, ?> colAuthor;

    @FXML
    private TableColumn<?, ?> colPublisher;

    @FXML
    private TableColumn<?, ?> colYear;

    @FXML
    private TableColumn<?, ?> colCopies;

    private final Validations validations;

    public CatalogController() {
        this.validations = new Validations();
    }

    public static void setEditMode(boolean editable) {
        editMode = editable;
    }

    @FXML
    public void initialize() {
        btnAdd.setVisible(editMode);
        btnAdd.setManaged(editMode);
        btnClear.setVisible(editMode);
        btnClear.setManaged(editMode);
        btnNewBorrowing.setVisible(editMode);
        btnNewBorrowing.setManaged(editMode);
        System.out.println("CatalogController initialized - EditMode: " + editMode);
    }

    @FXML
    private void handleAddBook() {
        if (!editMode) {
            System.out.println("No tiene permisos para agregar libros");
            return;
        }

        String isbn = txtIsbn.getText();
        String title = txtTitle.getText();
        String author = txtAuthor.getText();
        String publisher = txtPublisher.getText();
        String year = txtYear.getText();
        String copies = txtCopies.getText();

        System.out.println("=== AGREGANDO LIBRO ===");

        if (validations.isEmpty(isbn)) {
            System.out.println("Error: El ISBN está vacío");
            return;
        }
        if (validations.isEmpty(title)) {
            System.out.println("Error: El título está vacío");
            return;
        }
        if (validations.isEmpty(author)) {
            System.out.println("Error: El autor está vacío");
            return;
        }
        if (validations.isEmpty(publisher)) {
            System.out.println("Error: La editorial está vacía");
            return;
        }
        if (validations.isEmpty(year)) {
            System.out.println("Error: El año está vacío");
            return;
        }
        if (validations.isEmpty(copies)) {
            System.out.println("Error: La cantidad de copias está vacía");
            return;
        }

        if (!validations.validateInteger(year)) {
            System.out.println("Error: El año debe ser un número");
            return;
        }

        if (!validations.validateInteger(copies)) {
            System.out.println("Error: La cantidad de copias debe ser un número");
            return;
        }
        int copiesInt = Integer.parseInt(copies);
        if (!validations.validatePositiveNumber(copiesInt)) {
            System.out.println("Error: La cantidad de copias debe ser mayor que cero");
            return;
        }

        System.out.println("Libro agregado exitosamente (simulado)");
        System.out.println("  ISBN: " + isbn);
        System.out.println("  Título: " + title);
        System.out.println("  Autor: " + author);
        System.out.println("  Editorial: " + publisher);
        System.out.println("  Año: " + year);
        System.out.println("  Copias: " + copies);

        // TODO: Aquí iría el guardado en la base de datos

        clearFields();
    }

    @FXML
    private void handleClearFields() {
        clearFields();
        System.out.println("Campos limpiados");
    }

    @FXML
    private void handleNewBorrowing() {
        if (!editMode) {
            System.out.println("No tiene permisos para registrar préstamos");
            return;
        }
        Stage currentStage = (Stage) btnNewBorrowing.getScene().getWindow();
        SceneManagerController.changeScene(
                currentStage,
                "/org/library/system/view/BorrowingView.fxml",
                "Registrar Prestamo"
        );
    }

    @FXML
    private void handleBack() {
        Stage currentStage = (Stage) btnBack.getScene().getWindow();
        SceneManagerController.changeScene(
                currentStage,
                "/org/library/system/view/DashboardView.fxml",
                "Panel Principal"
        );
    }

    private void clearFields() {
        txtIsbn.clear();
        txtTitle.clear();
        txtAuthor.clear();
        txtPublisher.clear();
        txtYear.clear();
        txtCopies.clear();
    }
}