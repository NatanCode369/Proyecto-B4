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
public class Prestamo {
    private String idPrestamo;
    private String idEstudiante;
    private String isbn;
    private LocalDate fechaAdquisicion;
    private LocalDate fechaVencimiento;
    private Boolean aprobacionPrestamo;

    public Prestamo() {
    }

    public Prestamo(String idPrestamo, String idEstudiante, String isbn, LocalDate fechaAdquisicion, LocalDate fechaVencimiento, Boolean aprobacionPrestamo) {
        this.idPrestamo = idPrestamo;
        this.idEstudiante = idEstudiante;
        this.isbn = isbn;
        this.fechaAdquisicion = fechaAdquisicion;
        this.fechaVencimiento = fechaVencimiento;
        this.aprobacionPrestamo = aprobacionPrestamo;
    }

    public String getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(String idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public String getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(String idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public LocalDate getFechaAdquisicion() {
        return fechaAdquisicion;
    }

    public void setFechaAdquisicion(LocalDate fechaAdquisicion) {
        this.fechaAdquisicion = fechaAdquisicion;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Boolean getAprobacionPrestamo() {
        return aprobacionPrestamo;
    }

    public void setAprobacionPrestamo(Boolean aprobacionPrestamo) {
        this.aprobacionPrestamo = aprobacionPrestamo;
    }

    @Override
    public String toString() {
        return "Prestamo{" + "idPrestamo='" + idPrestamo + "', idEstudiante='" + idEstudiante + "', isbn='" + isbn + "', aprobacionPrestamo=" + aprobacionPrestamo + "}";
    }
}
