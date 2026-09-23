package org.library.system.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.library.system.dao.BookDao;
import org.library.system.dao.LoanDao;
import org.library.system.dao.LoanDetailDao;
import org.library.system.dao.LoanRequestDao;
import org.library.system.dao.LoanRequestDetailDao;
import org.library.system.dao.UserDao;
import org.library.system.enums.LoanStatus;
import org.library.system.enums.RequestStatus;
import org.library.system.model.Book;
import org.library.system.model.Loan;
import org.library.system.model.LoanApplication;
import org.library.system.model.LoanDetails;
import org.library.system.model.RequestDetails;
import org.library.system.model.User;
import org.library.system.utils.AlertUtils;
import org.library.system.utils.AppStatus;
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
                return new SimpleStringProperty(user.map(User::getUser_code).orElse("N/A"));
            } catch (SQLException e) {
                return new SimpleStringProperty("N/A");
            }
        });

        colStudentName.setCellValueFactory(c -> {
            try {
                var user = userDao.findById(c.getValue().getStudent_id());
                return new SimpleStringProperty(
                        user.map(u -> u.getFirst_name() + " " + u.getLast_name()).orElse("N/A"));
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
            AlertUtils.instanceAlert().show(AppStatus.DATABASE_UNAVAILABLE,
                    "Error al cargar solicitudes: " + e.getMessage());
        }
    }

    @FXML
    private void handleApprove() {
        LoanApplication selected = tableRequests.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.instanceAlert().show(AppStatus.INVALID_INPUT,
                    "Seleccione una solicitud de la tabla.");
            return;
        }

        User librarian = SessionManager.getInstance().getCurrentUser();
        if (librarian == null) return;

        try {
            selected.setStatus(RequestStatus.APPROVED);
            selected.setLibrarian_id(librarian.getUser_id());
            selected.setResponse_date(LocalDate.now());
            loanRequestDao.update(selected);

            Loan loan = new Loan();
            loan.setRequest_id(selected.getRequest_id());
            loan.setStudent_id(selected.getStudent_id());
            loan.setLibrarian_id(librarian.getUser_id());
            loan.setDue_date(LocalDate.now().plusDays(7));
            loan.setStatus(LoanStatus.ACTIVE);

            int loanId = loanDao.create(loan);

            List<RequestDetails> details = loanRequestDetailDao.search(
                    String.valueOf(selected.getRequest_id()));

            for (RequestDetails detail : details) {
                LoanDetails loanDetail = new LoanDetails();
                loanDetail.setLoan_id(loanId);
                loanDetail.setBook_id(detail.getBook_id());
                loanDetail.setQuantity(detail.getQuantity());
                loanDetail.setReturned_quantity(0);
                loanDetailDao.create(loanDetail);

                var bookOpt = bookDao.findById(detail.getBook_id());
                if (bookOpt.isPresent()) {
                    Book book = bookOpt.get();
                    book.setAvailable_stock(book.getAvailable_stock() - detail.getQuantity());
                    bookDao.update(book);
                }
            }

            AlertUtils.instanceAlert().show(AppStatus.OK,
                    "Se aprobó la solicitud y se creó el préstamo #" + loanId);

            loadPendingRequests();

        } catch (SQLException e) {
            AlertUtils.instanceAlert().show(AppStatus.DATABASE_UNAVAILABLE,
                    "Error al aprobar: " + e.getMessage());
        }
    }

    @FXML
    private void handleReject() {
        LoanApplication selected = tableRequests.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.instanceAlert().show(AppStatus.INVALID_INPUT,
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

            AlertUtils.instanceAlert().show(AppStatus.OK,
                    "La solicitud #" + selected.getRequest_id() + " fue rechazada.");

            loadPendingRequests();

        } catch (SQLException e) {
            AlertUtils.instanceAlert().show(AppStatus.DATABASE_UNAVAILABLE,
                    "Error al rechazar: " + e.getMessage());
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
}