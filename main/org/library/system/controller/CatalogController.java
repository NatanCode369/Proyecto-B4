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
import org.library.system.enums.Role;
import org.library.system.model.Book;
import org.library.system.model.User;
import org.library.system.utils.SceneManager;
import org.library.system.utils.SessionManager;
import org.library.system.utils.Validations;

import org.library.system.dao.LoanRequestDao;
import org.library.system.dao.LoanRequestDetailDao;
import org.library.system.enums.RequestStatus;
import org.library.system.model.LoanApplication;
import org.library.system.model.RequestDetails;


import java.sql.SQLException;
import java.util.List;

public class CatalogController {

    private final LoanRequestDao loanRequestDao = new LoanRequestDao();
    private final LoanRequestDetailDao loanRequestDetailDao = new LoanRequestDetailDao();

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

    @FXML private Button btnNewBorrowing;
    @FXML private Button btnRequestBorrowing;
    @FXML private Button btnBack;

    private final BookDao bookDao = new BookDao();
    private final Validations validations = new Validations();
    private final ObservableList<Book> bookList = FXCollections.observableArrayList();

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
    }

    private void loadBooks() {
        try {
            List<Book> books = bookDao.search("");
            bookList.setAll(books);
        } catch (SQLException e) {
            System.err.println("Error al cargar libros: " + e.getMessage());
        }
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
            System.err.println("Error al buscar: " + e.getMessage());
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
            return;
        }
        if (!validations.validateInteger(yearStr) || !validations.validateInteger(copiesStr)) {
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
            loadBooks();
            clearFields();

        } catch (SQLException e) {
            System.err.println("Error al crear libro: " + e.getMessage());
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
        if (selected == null) return;

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

            bookList.setAll(bookDao.search(""));
            tableBooks.getSelectionModel().clearSelection();

        } catch (SQLException e) {
            System.err.println("Error al solicitar prestamo: " + e.getMessage());
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
    }
}