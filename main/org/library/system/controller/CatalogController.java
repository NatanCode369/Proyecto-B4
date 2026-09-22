package org.library.system.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.library.system.utils.AlertUtils;
import org.library.system.utils.AppStatus;
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

    public CatalogController() {
        Validations validations = new Validations();
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
    }

    @FXML
    private void handleAddBook() {
        if (!editMode)
            AlertUtils.instanceAlert().show(AppStatus.FORBIDDEN,
                    "No tiene permiso para añadir libros.");

        if ((txtTitle.getText().isEmpty() || txtTitle.getText().isBlank()) ||
                (txtIsbn.getText().isEmpty() || txtIsbn.getText().isBlank()) ||
                (txtAuthor.getText().isEmpty() || txtAuthor.getText().isBlank()) ||
                (txtPublisher.getText().isEmpty() || txtPublisher.getText().isBlank()) ||
                (txtYear.getText().isEmpty() || txtYear.getText().isBlank()) ||
                txtCopies.getText().isEmpty() || txtCopies.getText().isBlank()) {
            AlertUtils.instanceAlert().show(AppStatus.INVALID_INPUT,
                    "Campos obligatorios vacíos");
            return;
        }

        if (Validations.getInstancevalidations().validateIsbn(txtIsbn.getText())) {
            AlertUtils.instanceAlert().show(AppStatus.INVALID_INPUT,
                    "Formato incorrecto del ISBN.");
            return;
        }

        if (Validations.getInstancevalidations().validateInteger(txtIsbn.getText()) &&
                (Validations.getInstancevalidations().validateInteger(txtCopies.getText())) &&
                (Validations.getInstancevalidations().validateInteger(txtYear.getText()))) {
            AlertUtils.instanceAlert().show(AppStatus.INVALID_INPUT,
                    "Los campos númericos contienen letras o no son enteros");
            return;
        }

        if (Validations.getInstancevalidations().validateYear(txtYear.getText()) &&
                (Validations.getInstancevalidations().validatePositiveNumber(Integer.parseInt(txtYear.getText())))) {
            AlertUtils.instanceAlert().show(AppStatus.INVALID_INPUT, "El formato del año no es correcto.");
            return;
        }

        // TODO: Aquí iría el guardado en la base de datos

        clearFields();
    }

    @FXML
    private void handleNewBorrowing() {
        if (!editMode) {
            AlertUtils.instanceAlert().show(AppStatus.FORBIDDEN, "No tiene permisos para registrar préstamos.");
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