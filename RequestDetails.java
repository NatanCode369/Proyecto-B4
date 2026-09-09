package org.library.system.model;

public class RequestDetails {
    private Integer request_detail_id;
    private Integer request_id;
    private Integer book_id;
    private Integer quantity;

    public RequestDetails() {}

    public RequestDetails(Integer request_detail_id, Integer request_id, Integer book_id, Integer quantity) {
        this.request_detail_id = request_detail_id;
        this.request_id = request_id;
        this.book_id = book_id;
        this.quantity = quantity;
    }

    public Integer getRequest_detail_id() { return request_detail_id; }
    public void setRequest_detail_id(Integer request_detail_id) { this.request_detail_id = request_detail_id; }
    public Integer getRequest_id() { return request_id; }
    public void setRequest_id(Integer request_id) { this.request_id = request_id; }
    public Integer getBook_id() { return book_id; }
    public void setBook_id(Integer book_id) { this.book_id = book_id; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
