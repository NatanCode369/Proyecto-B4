package org.library.system.model;

public class Book {
    private Integer book_id;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private Integer publication_year;
    private Integer total_stock;
    private Integer available_stock;
    private Boolean active;

    public Book() {}

    public Book(Integer book_id, String isbn, String title, String author, String publisher, Integer publication_year, Integer total_stock, Integer available_stock, Boolean active) {
        this.book_id = book_id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publication_year = publication_year;
        this.total_stock = total_stock;
        this.available_stock = available_stock;
        this.active = active;
    }

    public Integer getBook_id() { return book_id; }
    public void setBook_id(Integer book_id) { this.book_id = book_id; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    public Integer getPublication_year() { return publication_year; }
    public void setPublication_year(Integer publication_year) { this.publication_year = publication_year; }
    public Integer getTotal_stock() { return total_stock; }
    public void setTotal_stock(Integer total_stock) { this.total_stock = total_stock; }
    public Integer getAvailable_stock() { return available_stock; }
    public void setAvailable_stock(Integer available_stock) { this.available_stock = available_stock; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
