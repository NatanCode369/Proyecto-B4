package com.biblioteca;

import biblioteca.enums.EstadoPrestamo;
import biblioteca.enums.EstadoSolicitud;
import biblioteca.enums.Rol;
import biblioteca.model.*;

import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); 
        
        System.out.println("========================================");
        System.out.println("  SISTEMA DE GESTIÓN DE BIBLIOTECA (EN) ");
        System.out.println("========================================");

        Usuario estudiante = new Usuario(1, "EST-2026", "Carlos", "Pérez", "carlos@correo.com", "hash123", Rol.ESTUDIANTE, true);
        Usuario bibliotecario = new Usuario(2, "BIB-007", "Ana", "Gómez", "ana@correo.com", "hash456", Rol.BIBLIOTECARIO, true);

        Libro libro = new Libro(101, "978-3-16-148410-0", "Patrones de Diseño", "Erich Gamma", "Addison-Wesley", 1994, 5, 5, true);

        System.out.println("[INFO] Datos iniciales creados.");
        System.out.println("Estudiante: " + estudiante.getFirst_name() + " " + estudiante.getLast_name());
        System.out.println("Libro disponible: '" + libro.getTitle() + "' (Stock: " + libro.getAvailable_stock() + ")");
        System.out.println("----------------------------------------\n");

        System.out.println("Presione ENTER para simular una Solicitud de Préstamo...");
        scanner.nextLine();

        SolicitudPrestamo solicitud = new SolicitudPrestamo(501, estudiante.getUser_id(), LocalDate.now(), EstadoSolicitud.PENDIENTE, "Ninguna", null, null);
        SolicitudDetalle solDetalle = new SolicitudDetalle(901, solicitud.getRequest_id(), libro.getBook_id(), 1);

        System.out.println("[PROCESO] Solicitud #" + solicitud.getRequest_id() + " generada por el estudiante.");
        System.out.println("Estado actual de la solicitud: " + solicitud.getStatus());
        System.out.println("----------------------------------------\n");

        System.out.println("Presione ENTER para que el Bibliotecario apruebe la solicitud...");
        scanner.nextLine();

        solicitud.setStatus(EstadoSolicitud.APROBADA);
        solicitud.setLibrarian_id(bibliotecario.getUser_id());
        solicitud.setResponse_date(LocalDate.now());
        solicitud.setObservation("Aprobado, stock verificado.");

        // Préstamo inicializado con 0 libros devueltos (returned_quantity = 0)
        Prestamo prestamo = new Prestamo(701, solicitud.getRequest_id(), solicitud.getStudent_id(), solicitud.getLibrarian_id(), LocalDate.now(), LocalDate.now().plusDays(7), EstadoPrestamo.ACTIVO);
        PrestamoDetalle presDetalle = new PrestamoDetalle(801, prestamo.getLoan_id(), libro.getBook_id(), solDetalle.getQuantity(), 0);

        libro.setAvailable_stock(libro.getAvailable_stock() - presDetalle.getQuantity());

        System.out.println("[ÉXITO] ¡Solicitud Aprobada y Préstamo Generado!");
        System.out.println("ID Préstamo: " + prestamo.getLoan_id());
        System.out.println("Estado del Préstamo: " + prestamo.getStatus());
        System.out.println("Fecha de Vencimiento: " + prestamo.getDue_date());
        System.out.println("Nuevo Stock Disponible del Libro '" + libro.getTitle() + "': " + libro.getAvailable_stock());
        System.out.println("========================================");
        
        scanner.close();
    }
}
