package org.library.core.model;

public class LoanDetails {
    private Integer loan_detail_id;
    private Integer loan_id;
    private Integer book_id;
    private Integer quantity;
    private Integer returned_quantity; // Añadido según tus requerimientos

    public LoanDetails() {}

    public LoanDetails(Integer loan_detail_id, Integer loan_id, Integer book_id, Integer quantity, Integer returned_quantity) {
        this.loan_detail_id = loan_detail_id;
        this.loan_id = loan_id;
        this.book_id = book_id;
        this.quantity = quantity;
        this.returned_quantity = returned_quantity;
    }

    public Integer getLoan_detail_id() { return loan_detail_id; }
    public void setLoan_detail_id(Integer loan_detail_id) { this.loan_detail_id = loan_detail_id; }
    public Integer getLoan_id() { return loan_id; }
    public void setLoan_id(Integer loan_id) { this.loan_id = loan_id; }
    public Integer getBook_id() { return book_id; }
    public void setBook_id(Integer book_id) { this.book_id = book_id; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Integer getReturned_quantity() { return returned_quantity; }
    public void setReturned_quantity(Integer returned_quantity) { this.returned_quantity = returned_quantity; }
}
