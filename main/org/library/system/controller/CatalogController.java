package org.library.system.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.library.system.dao.BookDao;
import org.library.system.dao.LoanRequestDao;
import org.library.system.dao.LoanRequestDetailDao;
import org.library.system.enums.RequestStatus;
import org.library.system.enums.Role;
import org.library.system.model.Book;
import org.library.system.model.LoanApplication;
import org.library.system.model.RequestDetails;
import org.library.system.model.User;
import org.library.system.utils.SceneManager;
import org.library.system.utils.SessionManager;
import org.library.system.utils.Validations;

import java.sql.SQLException;
import java.util.List;

public class CatalogController {

    private static boolean editMode = true;

    @FXML private TextField txtSearchTitle;
    @FXML private TextField txtSearchIsbn;

    @FXML private VBox editSection;
    @FXML private TextField txtIsbn;
    @FXML private TextField txtTitle;
    @FXML private TextField txtAuthor;
    @FXML private TextField txtPublisher;
    @FXML private TextField txtYear;
    @FXML private TextField txtCopies;

    @FXML private TableView<Book> tableBooks;
    @FXML private TableColumn<Book, Integer> colBookId;
    @FXML private TableColumn<Book, String> colIsbn;
    @FXML private TableColumn<Book, String> colTitle;
    @FXML private TableColumn<Book, String> colAuthor;
    @FXML private TableColumn<Book, String> colPublisher;
    @FXML private TableColumn<Book, Integer> colYear;
    @FXML private TableColumn<Book, Integer> colTotalStock;
    @FXML private TableColumn<Book, Integer> colAvailableStock;

    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnClear;
    @FXML private Button btnNewBorrowing;
    @FXML private Button btnRequestBorrowing;
    @FXML private Button btnBack;

    private final BookDao bookDao = new BookDao();
    private final LoanRequestDao loanRequestDao = new LoanRequestDao();
    private final LoanRequestDetailDao loanRequestDetailDao = new LoanRequestDetailDao();
    private final Validations validations = new Validations();
    private final ObservableList<Book> bookList = FXCollections.observableArrayList();

    private Book selectedBook;

    public static void setEditMode(boolean editable) {
        editMode = editable;
    }

    @FXML
    public void initialize() {
        User user = SessionManager.getInstance().getCurrentUser();
        if (user == null) return;

        Role role = user.getUser_role();

        boolean canEdit = (role == Role.LIBRARIAN || role == Role.MANAGER);
        editSection.setVisible(canEdit);
        editSection.setManaged(canEdit);
        btnNewBorrowing.setVisible(canEdit);
        btnNewBorrowing.setManaged(canEdit);

        boolean isStudent = (role == Role.STUDENT);
        btnRequestBorrowing.setVisible(isStudent);
        btnRequestBorrowing.setManaged(isStudent);

        configureTable();
        loadBooks();
    }

    private void configureTable() {
        colBookId.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getBook_id()).asObject());
        colIsbn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getIsbn()));
        colTitle.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTitle()));
        colAuthor.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getAuthor()));
        colPublisher.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPublisher()));
        colYear.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getPublication_year()).asObject());
        colTotalStock.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getTotal_stock()).asObject());
        colAvailableStock.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getAvailable_stock()).asObject());
        tableBooks.setItems(bookList);

        tableBooks.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        selectedBook = newVal;
                        loadBookToForm(newVal);
                    }
                }
        );
    }

    private void loadBooks() {
        try {
            List<Book> books = bookDao.search("");
            bookList.setAll(books);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    private void loadBookToForm(Book book) {
        txtIsbn.setText(book.getIsbn());
        txtTitle.setText(book.getTitle());
        txtAuthor.setText(book.getAuthor());
        txtPublisher.setText(book.getPublisher());
        txtYear.setText(String.valueOf(book.getPublication_year()));
        txtCopies.setText(String.valueOf(book.getTotal_stock()));
    }

    @FXML
    private void handleSearch() {
        String title = txtSearchTitle.getText();
        String isbn = txtSearchIsbn.getText();

        String filter = "";
        if (title != null && !title.isBlank()) filter = title.trim();
        else if (isbn != null && !isbn.isBlank()) filter = isbn.trim();

        try {
            List<Book> results = bookDao.search(filter);
            bookList.setAll(results);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    @FXML
    private void handleClearSearch() {
        txtSearchTitle.clear();
        txtSearchIsbn.clear();
        loadBooks();
    }

    @FXML
    private void handleAddBook() {
        String isbn = txtIsbn.getText();
        String title = txtTitle.getText();
        String author = txtAuthor.getText();
        String publisher = txtPublisher.getText();
        String yearStr = txtYear.getText();
        String copiesStr = txtCopies.getText();

        if (validations.isEmpty(isbn) || validations.isEmpty(title)
                || validations.isEmpty(author) || validations.isEmpty(publisher)
                || validations.isEmpty(yearStr) || validations.isEmpty(copiesStr)) {
            showAlert(Alert.AlertType.WARNING, "Campos vacios",
                    "Complete todos los campos.");
            return;
        }
        if (!validations.validateInteger(yearStr) || !validations.validateInteger(copiesStr)) {
            showAlert(Alert.AlertType.WARNING, "Datos invalidos",
                    "Anio y copias deben ser numeros.");
            return;
        }

        try {
            int year = Integer.parseInt(yearStr);
            int copies = Integer.parseInt(copiesStr);

            Book book = new Book();
            book.setIsbn(isbn.trim());
            book.setTitle(title.trim());
            book.setAuthor(author.trim());
            book.setPublisher(publisher.trim());
            book.setPublication_year(year);
            book.setTotal_stock(copies);
            book.setAvailable_stock(copies);
            book.setActive(true);

            bookDao.create(book);
            showAlert(Alert.AlertType.INFORMATION, "Libro creado",
                    "El libro se registro correctamente.");
            loadBooks();
            clearFields();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error al crear", e.getMessage());
        }
    }

    @FXML
    private void handleUpdateBook() {
        if (selectedBook == null) {
            showAlert(Alert.AlertType.WARNING, "Sin seleccion",
                    "Seleccione un libro de la tabla.");
            return;
        }

        try {
            selectedBook.setIsbn(txtIsbn.getText().trim());
            selectedBook.setTitle(txtTitle.getText().trim());
            selectedBook.setAuthor(txtAuthor.getText().trim());
            selectedBook.setPublisher(txtPublisher.getText().trim());
            selectedBook.setPublication_year(Integer.parseInt(txtYear.getText()));
            selectedBook.setTotal_stock(Integer.parseInt(txtCopies.getText()));

            bookDao.update(selectedBook);
            showAlert(Alert.AlertType.INFORMATION, "Libro actualizado",
                    "El libro se actualizo correctamente.");
            loadBooks();
            clearFields();

        } catch (SQLException | NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error al actualizar", e.getMessage());
        }
    }

    @FXML
    private void handleDeleteBook() {
        if (selectedBook == null) {
            showAlert(Alert.AlertType.WARNING, "Sin seleccion",
                    "Seleccione un libro de la tabla.");
            return;
        }

        try {
            bookDao.delete(selectedBook.getBook_id());
            showAlert(Alert.AlertType.INFORMATION, "Libro eliminado",
                    "El libro se elimino correctamente.");
            loadBooks();
            clearFields();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error al eliminar", e.getMessage());
        }
    }

    @FXML
    private void handleClearFields() {
        clearFields();
    }

    @FXML
    private void handleNewBorrowing() {
        SceneManager.getInstanciaSceneManager().goTo(
                "/org/library/system/view/BorrowingView.fxml"
        );
    }

    @FXML
    private void handleRequestBorrowing() {
        Book selected = tableBooks.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Sin seleccion",
                    "Debe seleccionar un libro de la tabla.");
            return;
        }

        User user = SessionManager.getInstance().getCurrentUser();
        if (user == null) return;

        try {
            LoanApplication request = new LoanApplication();
            request.setStudent_id(user.getUser_id());
            request.setStatus(RequestStatus.PENDING);

            int requestId = loanRequestDao.create(request);

            RequestDetails detail = new RequestDetails();
            detail.setRequest_id(requestId);
            detail.setBook_id(selected.getBook_id());
            detail.setQuantity(1);
            loanRequestDetailDao.create(detail);

            showAlert(Alert.AlertType.INFORMATION, "Solicitud enviada",
                    "Tu solicitud de prestamo ha sido enviada.\n" +
                            "Libro: " + selected.getTitle() + "\n" +
                            "Espera la aprobacion del bibliotecario.");

            bookList.setAll(bookDao.search(""));
            tableBooks.getSelectionModel().clearSelection();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error de base de datos",
                    e.getMessage());
        }
    }

    @FXML
    private void handleBack() {
        SceneManager.getInstanciaSceneManager().goTo(
                "/org/library/system/view/DashboardView.fxml"
        );
    }

    private void clearFields() {
        txtIsbn.clear();
        txtTitle.clear();
        txtAuthor.clear();
        txtPublisher.clear();
        txtYear.clear();
        txtCopies.clear();
        selectedBook = null;
        tableBooks.getSelectionModel().clearSelection();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}