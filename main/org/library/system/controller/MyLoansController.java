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
import org.library.system.model.Book;
import org.library.system.model.Loan;
import org.library.system.model.LoanDetails;
import org.library.system.model.User;
import org.library.system.utils.AlertUtils;
import org.library.system.utils.AppStatus;
import org.library.system.utils.SceneManager;
import org.library.system.utils.SessionManager;

import java.sql.SQLException;
import java.util.List;

public class MyLoansController {

    @FXML private TableView<Loan> tableLoans;
    @FXML private TableColumn<Loan, Integer> colLoanId;
    @FXML private TableColumn<Loan, String> colBookTitle;
    @FXML private TableColumn<Loan, String> colLoanDate;
    @FXML private TableColumn<Loan, String> colDueDate;
    @FXML private TableColumn<Loan, String> colStatus;

    @FXML private Button btnBack;

    private final LoanDao loanDao = new LoanDao();
    private final LoanDetailDao loanDetailDao = new LoanDetailDao();
    private final BookDao bookDao = new BookDao();

    private final ObservableList<Loan> loanList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configureTable();
        loadMyLoans();
    }

    private void configureTable() {
        colLoanId.setCellValueFactory(c ->
                new SimpleIntegerProperty(c.getValue().getLoan_id()).asObject());

        colBookTitle.setCellValueFactory(c -> {
            try {
                List<LoanDetails> details = loanDetailDao.search(
                        String.valueOf(c.getValue().getLoan_id()));
                if (details.isEmpty()) return new SimpleStringProperty("N/A");

                var bookOpt = bookDao.findById(details.get(0).getBook_id());
                return new SimpleStringProperty(bookOpt.map(Book::getTitle).orElse("N/A"));
            } catch (SQLException e) {
                return new SimpleStringProperty("N/A");
            }
        });

        colLoanDate.setCellValueFactory(c ->
                new SimpleStringProperty(String.valueOf(c.getValue().getLoan_date())));

        colDueDate.setCellValueFactory(c ->
                new SimpleStringProperty(String.valueOf(c.getValue().getDue_date())));

        colStatus.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getStatus().name()));

        tableLoans.setItems(loanList);
    }

    private void loadMyLoans() {
        User user = SessionManager.getInstance().getCurrentUser();
        if (user == null) return;

        try {
            List<Loan> all = loanDao.search("");
            all.removeIf(l -> !l.getStudent_id().equals(user.getUser_id()));
            loanList.setAll(all);
        } catch (SQLException e) {
            AlertUtils.instanceAlert().show(AppStatus.DATABASE_UNAVAILABLE,
                    "Error al cargar préstamos: " + e.getMessage());
        }
    }

    @FXML
    private void handleBack() {
        SceneManager.getInstanciaSceneManager().goTo(
                "/org/library/system/view/DashboardView.fxml"
        );
    }
}