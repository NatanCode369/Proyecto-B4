/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.biblioteca.model;

/**
 *
 * @author informatica
 */
public class Bibliotecario {
    private String idBibliotecario;
    private String nombreBibliotecario;
    private String apellidoBibliotecario;
    private String correo;
    private String contrasena;

    public Bibliotecario() {
    }

    public Bibliotecario(String idBibliotecario, String nombreBibliotecario, String apellidoBibliotecario, String correo, String contrasena) {
        this.idBibliotecario = idBibliotecario;
        this.nombreBibliotecario = nombreBibliotecario;
        this.apellidoBibliotecario = apellidoBibliotecario;
        this.correo = correo;
        this.contrasena = contrasena;
    }

    public String getIdBibliotecario() {
        return idBibliotecario;
    }

    public void setIdBibliotecario(String idBibliotecario) {
        this.idBibliotecario = idBibliotecario;
    }

    public String getNombreBibliotecario() {
        return nombreBibliotecario;
    }

    public void setNombreBibliotecario(String nombreBibliotecario) {
        this.nombreBibliotecario = nombreBibliotecario;
    }

    public String getApellidoBibliotecario() {
        return apellidoBibliotecario;
    }

    public void setApellidoBibliotecario(String apellidoBibliotecario) {
        this.apellidoBibliotecario = apellidoBibliotecario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    @Override
    public String toString() {
        return "Bibliotecario{" + "idBibliotecario='" + idBibliotecario + "', nombreBibliotecario='" + nombreBibliotecario + "', apellidoBibliotecario='" + apellidoBibliotecario + "', correo='" + correo + "'}";
    }
}
