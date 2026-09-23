package org.library.system.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
import org.library.system.utils.AlertUtils;
import org.library.system.utils.AppStatus;
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
    private final Validations validations = Validations.getInstancevalidations();
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
            AlertUtils.instanceAlert().show(AppStatus.DATABASE_UNAVAILABLE,
                    "Error al cargar libros: " + e.getMessage());
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
        String filter = "";
        if (!txtSearchTitle.getText().isEmpty()) {
            filter = txtSearchTitle.getText().trim();
        } else if (!txtSearchIsbn.getText().isEmpty()) {
            filter = txtSearchIsbn.getText().trim();
        }

        try {
            List<Book> results = bookDao.search(filter);
            bookList.setAll(results);

            if (results.isEmpty()) {
                AlertUtils.instanceAlert().show(AppStatus.NOT_FOUND,
                        "No se encontraron libros con ese criterio.");
            }
        } catch (SQLException e) {
            AlertUtils.instanceAlert().show(AppStatus.DATABASE_UNAVAILABLE,
                    "Error al buscar: " + e.getMessage());
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
        if (!editMode) {
            AlertUtils.instanceAlert().show(AppStatus.FORBIDDEN,
                    "No tiene permiso para añadir libros.");
            return;
        }

        if (txtTitle.getText().isEmpty() || txtTitle.getText().isBlank()
                || txtIsbn.getText().isEmpty() || txtIsbn.getText().isBlank()
                || txtAuthor.getText().isEmpty() || txtAuthor.getText().isBlank()
                || txtPublisher.getText().isEmpty() || txtPublisher.getText().isBlank()
                || txtYear.getText().isEmpty() || txtYear.getText().isBlank()
                || txtCopies.getText().isEmpty() || txtCopies.getText().isBlank()) {
            AlertUtils.instanceAlert().show(AppStatus.INVALID_INPUT,
                    "Campos obligatorios vacíos");
            return;
        }

        if (!validations.validateIsbn(txtIsbn.getText())) {
            AlertUtils.instanceAlert().show(AppStatus.INVALID_INPUT,
                    "Formato incorrecto del ISBN.");
            return;
        }

        if (!validations.validateInteger(txtYear.getText())
                || !validations.validateInteger(txtCopies.getText())) {
            AlertUtils.instanceAlert().show(AppStatus.INVALID_INPUT,
                    "Los campos numéricos contienen letras o no son enteros.");
            return;
        }

        if (!validations.validateYear(txtYear.getText())) {
            AlertUtils.instanceAlert().show(AppStatus.INVALID_INPUT,
                    "El formato del año no es correcto.");
            return;
        }

        if (!validations.validatePositiveNumber(Integer.parseInt(txtCopies.getText()))) {
            AlertUtils.instanceAlert().show(AppStatus.INVALID_INPUT,
                    "La cantidad de copias debe ser mayor a cero.");
            return;
        }

        try {
            Book book = getBook();
            bookDao.create(book);

            AlertUtils.instanceAlert().show(AppStatus.CREATED,
                    "El libro se registró correctamente.");

            loadBooks();
            clearFields();

        } catch (SQLException e) {
            AlertUtils.instanceAlert().show(AppStatus.DATABASE_UNAVAILABLE,
                    "Error al intentar crear el libro.");
        }
    }

    private Book getBook() {
        int year = Integer.parseInt(txtYear.getText());
        int copies = Integer.parseInt(txtCopies.getText());

        Book book = new Book();
        book.setIsbn(txtIsbn.getText().trim());
        book.setTitle(txtTitle.getText().trim());
        book.setAuthor(txtAuthor.getText().trim());
        book.setPublisher(txtPublisher.getText().trim());
        book.setPublication_year(year);
        book.setTotal_stock(copies);
        book.setAvailable_stock(copies);
        book.setActive(true);
        return book;
    }

    @FXML
    private void handleUpdateBook() {
        if (selectedBook == null) {
            AlertUtils.instanceAlert().show(AppStatus.INVALID_INPUT,
                    "Seleccione un libro de la tabla.");
            return;
        }

        if (txtIsbn.getText().isEmpty()
                || txtTitle.getText().isEmpty()
                || txtAuthor.getText().isEmpty()
                || txtPublisher.getText().isEmpty()
                || txtYear.getText().isEmpty()
                || txtCopies.getText().isEmpty()) {
            AlertUtils.instanceAlert().show(AppStatus.INVALID_INPUT,
                    "Complete todos los campos.");
            return;
        }

        if (!validations.validateInteger(txtYear.getText())
                || !validations.validateInteger(txtCopies.getText())) {
            AlertUtils.instanceAlert().show(AppStatus.INVALID_INPUT,
                    "Año y copias deben ser números.");
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

            AlertUtils.instanceAlert().show(AppStatus.OK,
                    "El libro se actualizó correctamente.");

            loadBooks();
            clearFields();

        } catch (SQLException e) {
            AlertUtils.instanceAlert().show(AppStatus.DATABASE_UNAVAILABLE,
                    "Error al actualizar el libro seleccionado.");
        }
    }

    @FXML
    private void handleDeleteBook() {
        if (selectedBook == null) {
            AlertUtils.instanceAlert().show(AppStatus.INVALID_INPUT,
                    "Seleccione un libro de la tabla.");
            return;
        }

        try {
            bookDao.delete(selectedBook.getBook_id());

            AlertUtils.instanceAlert().show(AppStatus.DELETED,
                    "El libro se eliminó correctamente.");

            loadBooks();
            clearFields();

        } catch (SQLException e) {
            AlertUtils.instanceAlert().show(AppStatus.DATABASE_UNAVAILABLE,
                    "Error al eliminar el libro seleccionado.");
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
        if (selectedBook == null) {
            AlertUtils.instanceAlert().show(AppStatus.INVALID_INPUT,
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
            detail.setBook_id(selectedBook.getBook_id());
            detail.setQuantity(1);
            loanRequestDetailDao.create(detail);

            AlertUtils.instanceAlert().show(AppStatus.OK,
                    "Tu solicitud de préstamo ha sido enviada.\n"
                            + "Libro: " + selectedBook.getTitle() + "\n"
                            + "Espera la aprobación del bibliotecario.");

            bookList.setAll(bookDao.search(""));
            tableBooks.getSelectionModel().clearSelection();
            selectedBook = null;

        } catch (SQLException e) {
            AlertUtils.instanceAlert().show(AppStatus.DATABASE_UNAVAILABLE,
                    "Error al solicitar el préstamo. Pruebe nuevamente.");
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
}