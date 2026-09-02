/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.biblioteca;

import com.biblioteca.model.Bibliotecario;
import com.biblioteca.model.Estudiante;
import com.biblioteca.model.Libro;
import com.biblioteca.model.Prestamo;
import java.time.LocalDate;

/**
 * Clase principal para pruebas unitarias de la capa del Modelo.
 * @author informatica
 */
public class Main {

   
    public static void main(String[] args) {
        System.out.println(" SISTEMA CATALOGO BIBLIOGRAFICO DIGITAL ");
        System.out.println("Validando inicializacion de instancias del Modelo...\n");

        Libro libroPrueba = new Libro(
            "9786071514127", 
            "Calculo de una Variable", 
            "James Stewart", 
            "McGraw-Hill", 
            LocalDate.of(2018, 5, 20), 
            15
        );
        System.out.println("--> Datos de Libro Registrado:");
        System.out.println(libroPrueba.toString());
        System.out.println("Titulo: " + libroPrueba.getTituloLibro() + " | Stock Actual: " + libroPrueba.getStock());
        System.out.println("");

        Bibliotecario bibliotecarioPrueba = new Bibliotecario(
            "usr-uuid-8892-x", 
            "Adrian Natanael", 
            "Aguilar Cal", 
            "adrian.aguilar@universidad.edu", 
            "AdminPass123"
        );
        System.out.println("--> Datos de Bibliotecario Autenticado:");
        System.out.println(bibliotecarioPrueba.toString());
        System.out.println("Correo de contacto: " + bibliotecarioPrueba.getCorreo());
        System.out.println("");

        Estudiante estudiantePrueba = new Estudiante(
            "est-uuid-4412-z", 
            "Edmond Enrique", 
            "Dubon Agtun"
        );
        System.out.println("--> Datos de Estudiante Consultado:");
        System.out.println(estudiantePrueba.toString());
        System.out.println("ID Interno: " + estudiantePrueba.getIdEstudiante());
        System.out.println("");

        Prestamo prestamoPrueba = new Prestamo(
            "loan-uuid-9901-k",
            estudiantePrueba.getIdEstudiante(), 
            libroPrueba.getIsbn(),             
            LocalDate.now(),                    
            LocalDate.now().plusDays(5),        
            true                                
        );
        System.out.println("--> Transaccion de Prestamo Generada:");
        System.out.println(prestamoPrueba.toString());
        System.out.println("Fecha de Adquisicion: " + prestamoPrueba.getFechaAdquisicion());
        System.out.println("Fecha Limite de Vencimiento: " + prestamoPrueba.getFechaVencimiento());
        System.out.println("¿Estado Aprobado?: " + (prestamoPrueba.getAprobacionPrestamo() ? "SI" : "NO"));
        System.out.println("");

        System.out.println(" COMPILACION Y VERIFICACION DE CLASES EXITOSA ");
    }
}
