/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.biblioteca.model;

import java.time.LocalDate;

/**
 *
 * @author informatica
 */
public class Libro {
    private String isbn;
    private String tituloLibro;
    private String autorLibro;
    private String editorialLibro;
    private LocalDate añoPublicacion; 
    private int stock;

    public Libro() {
    }

    public Libro(String isbn, String tituloLibro, String autorLibro, String editorialLibro, LocalDate añoPublicacion, int stock) {
        this.isbn = isbn;
        this.tituloLibro = tituloLibro;
        this.autorLibro = autorLibro;
        this.editorialLibro = editorialLibro;
        this.añoPublicacion = añoPublicacion;
        this.stock = stock;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTituloLibro() {
        return tituloLibro;
    }

    public void setTituloLibro(String tituloLibro) {
        this.tituloLibro = tituloLibro;
    }

    public String getAutorLibro() {
        return autorLibro;
    }

    public void setAutorLibro(String autorLibro) {
        this.autorLibro = autorLibro;
    }

    public String getEditorialLibro() {
        return editorialLibro;
    }

    public void setEditorialLibro(String editorialLibro) {
        this.editorialLibro = editorialLibro;
    }

    public LocalDate getAnioPublicacion() {
        return añoPublicacion;
    }

    public void setAnioPublicacion(LocalDate añoPublicacion) {
        this.añoPublicacion = añoPublicacion;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
    
    @Override
    public String toString() {
        return "Libro{" + "isbn='" + isbn + "', tituloLibro='" + tituloLibro + "', autorLibro='" + autorLibro + "', añoPublicacion=" + añoPublicacion + ", stock=" + stock + "}";
    }
}
