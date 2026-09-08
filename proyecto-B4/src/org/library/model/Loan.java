package org.library.model;

import java.time.LocalDate;
import org.library.enums.LoanStatus;

public class Loan {
    private Integer loan_id;
    private Integer request_id;
    private Integer student_id;
    private Integer librarian_id;
    private LocalDate loan_date;
    private LocalDate due_date;
    private LoanStatus status;

    public Loan() {}

    public Loan(Integer loan_id, Integer request_id, Integer student_id, Integer librarian_id, LocalDate loan_date, LocalDate due_date, LoanStatus status) {
        this.loan_id = loan_id;
        this.request_id = request_id;
        this.student_id = student_id;
        this.librarian_id = librarian_id;
        this.loan_date = loan_date;
        this.due_date = due_date;
        this.status = status;
    }

    public Integer getLoan_id() { return loan_id; }
    public void setLoan_id(Integer loan_id) { this.loan_id = loan_id; }
    public Integer getRequest_id() { return request_id; }
    public void setRequest_id(Integer request_id) { this.request_id = request_id; }
    public Integer getStudent_id() { return student_id; }
    public void setStudent_id(Integer student_id) { this.student_id = student_id; }
    public Integer getLibrarian_id() { return librarian_id; }
    public void setLibrarian_id(Integer librarian_id) { this.librarian_id = librarian_id; }
    public LocalDate getLoan_date() { return loan_date; }
    public void setLoan_date(LocalDate loan_date) { this.loan_date = loan_date; }
    public LocalDate getDue_date() { return due_date; }
    public void setDue_date(LocalDate due_date) { this.due_date = due_date; }
    public LoanStatus getStatus() { return status; }
    public void setStatus(LoanStatus status) { this.status = status; }
}
