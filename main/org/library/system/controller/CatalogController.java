package org.library.system.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

    public static void setEditMode(boolean editable) {
        editMode = editable;
    }

    @FXML
    public void initialize() {
        // Mostrar u ocultar botones de edición según el modo
        btnAdd.setVisible(editMode);
        btnAdd.setManaged(editMode);
        btnClear.setVisible(editMode);
        btnClear.setManaged(editMode);
        btnNewBorrowing.setVisible(editMode);
        btnNewBorrowing.setManaged(editMode);

        // Aquí se cargarían los datos de la base de datos
    }

    @FXML
    private void handleAddBook() {
        if (!editMode) {
            System.out.println("No tiene permisos para agregar libros");
            return;
        }
        System.out.println("Agregando libro: " + txtTitle.getText());
        clearFields();
    }

    @FXML
    private void handleClearFields() {
        clearFields();
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