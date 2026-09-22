package org.library.system.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.library.system.dao.*;
import org.library.system.enums.LoanStatus;
import org.library.system.enums.RequestStatus;
import org.library.system.model.*;
import org.library.system.utils.SceneManager;
import org.library.system.utils.SessionManager;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PendingRequestsController {

    @FXML private TableView<LoanApplication> tableRequests;
    @FXML private TableColumn<LoanApplication, Integer> colRequestId;
    @FXML private TableColumn<LoanApplication, String> colStudentCode;
    @FXML private TableColumn<LoanApplication, String> colStudentName;
    @FXML private TableColumn<LoanApplication, String> colRequestDate;
    @FXML private TableColumn<LoanApplication, String> colStatus;

    @FXML private Button btnApprove;
    @FXML private Button btnReject;
    @FXML private Button btnRefresh;
    @FXML private Button btnBack;

    private final LoanRequestDao loanRequestDao = new LoanRequestDao();
    private final LoanRequestDetailDao loanRequestDetailDao = new LoanRequestDetailDao();
    private final LoanDao loanDao = new LoanDao();
    private final LoanDetailDao loanDetailDao = new LoanDetailDao();
    private final BookDao bookDao = new BookDao();
    private final UserDao userDao = new UserDao();

    private final ObservableList<LoanApplication> requestList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configureTable();
        loadPendingRequests();
    }

    private void configureTable() {
        colRequestId.setCellValueFactory(c ->
                new SimpleIntegerProperty(c.getValue().getRequest_id()).asObject());

        colStudentCode.setCellValueFactory(c -> {
            try {
                var user = userDao.findById(c.getValue().getStudent_id());
                return new SimpleStringProperty(
                        user.map(User::getUser_code).orElse("N/A"));
            } catch (SQLException e) {
                return new SimpleStringProperty("N/A");
            }
        });

        colStudentName.setCellValueFactory(c -> {
            try {
                var user = userDao.findById(c.getValue().getStudent_id());
                return new SimpleStringProperty(
                        user.map(u -> u.getFirst_name() + " " + u.getLast_name())
                                .orElse("N/A"));
            } catch (SQLException e) {
                return new SimpleStringProperty("N/A");
            }
        });

        colRequestDate.setCellValueFactory(c ->
                new SimpleStringProperty(String.valueOf(c.getValue().getRequest_date())));

        colStatus.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getStatus().name()));

        tableRequests.setItems(requestList);
    }

    private void loadPendingRequests() {
        try {
            List<LoanApplication> all = loanRequestDao.search("PENDING");
            all.removeIf(r -> r.getStatus() != RequestStatus.PENDING);
            requestList.setAll(all);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error de base de datos",
                    e.getMessage());
        }
    }

    @FXML
    private void handleApprove() {
        LoanApplication selected = tableRequests.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Sin seleccion",
                    "Seleccione una solicitud de la tabla.");
            return;
        }

        User librarian = SessionManager.getInstance().getCurrentUser();
        if (librarian == null) return;

        try {
            // 1. Actualizar estado de la solicitud
            selected.setStatus(RequestStatus.APPROVED);
            selected.setLibrarian_id(librarian.getUser_id());
            selected.setResponse_date(LocalDate.now());
            loanRequestDao.update(selected);

            // 2. Crear el prestamo
            Loan loan = new Loan();
            loan.setRequest_id(selected.getRequest_id());
            loan.setStudent_id(selected.getStudent_id());
            loan.setLibrarian_id(librarian.getUser_id());
            loan.setDue_date(LocalDate.now().plusDays(7));
            loan.setStatus(LoanStatus.ACTIVE);

            int loanId = loanDao.create(loan);

            // 3. Obtener los detalles de la solicitud
            List<RequestDetails> details = loanRequestDetailDao.search(
                    String.valueOf(selected.getRequest_id()));

            // 4. Crear detalles del prestamo + bajar stock
            for (RequestDetails detail : details) {
                LoanDetails loanDetail = new LoanDetails();
                loanDetail.setLoan_id(loanId);
                loanDetail.setBook_id(detail.getBook_id());
                loanDetail.setQuantity(detail.getQuantity());
                loanDetail.setReturned_quantity(0);
                loanDetailDao.create(loanDetail);

                // Bajar stock del libro
                var bookOpt = bookDao.findById(detail.getBook_id());
                if (bookOpt.isPresent()) {
                    Book book = bookOpt.get();
                    book.setAvailable_stock(book.getAvailable_stock() - detail.getQuantity());
                    bookDao.update(book);
                }
            }

            showAlert(Alert.AlertType.INFORMATION, "Solicitud aprobada",
                    "Se aprobo la solicitud y se creo el prestamo #" + loanId);

            loadPendingRequests();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error al aprobar",
                    e.getMessage());
        }
    }

    @FXML
    private void handleReject() {
        LoanApplication selected = tableRequests.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Sin seleccion",
                    "Seleccione una solicitud de la tabla.");
            return;
        }

        User librarian = SessionManager.getInstance().getCurrentUser();
        if (librarian == null) return;

        try {
            selected.setStatus(RequestStatus.REJECTED);
            selected.setLibrarian_id(librarian.getUser_id());
            selected.setResponse_date(LocalDate.now());
            loanRequestDao.update(selected);

            showAlert(Alert.AlertType.INFORMATION, "Solicitud rechazada",
                    "La solicitud #" + selected.getRequest_id() + " fue rechazada.");

            loadPendingRequests();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error al rechazar",
                    e.getMessage());
        }
    }

    @FXML
    private void handleRefresh() {
        loadPendingRequests();
    }

    @FXML
    private void handleBack() {
        SceneManager.getInstanciaSceneManager().goTo(
                "/org/library/system/view/DashboardView.fxml"
        );
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}