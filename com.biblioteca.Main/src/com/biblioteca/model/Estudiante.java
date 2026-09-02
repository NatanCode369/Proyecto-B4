/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.biblioteca.model;

/**
 *
 * @author informatica
 */
public class Estudiante {
    private String idEstudiante;
    private String nombreEstudiante;
    private String apellidoEstudiante;

    public Estudiante() {
    }

    public Estudiante(String idEstudiante, String nombreEstudiante, String apellidoEstudiante) {
        this.idEstudiante = idEstudiante;
        this.nombreEstudiante = nombreEstudiante;
        this.apellidoEstudiante = apellidoEstudiante;
    }

    public String getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(String idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    public String getApellidoEstudiante() {
        return apellidoEstudiante;
    }

    public void setApellidoEstudiante(String apellidoEstudiante) {
        this.apellidoEstudiante = apellidoEstudiante;
    }

    @Override
    public String toString() {
        return "Estudiante{" + "idEstudiante='" + idEstudiante + "', nombreEstudiante='" + nombreEstudiante + "', apellidoEstudiante='" + apellidoEstudiante + "'}";
    }
}
