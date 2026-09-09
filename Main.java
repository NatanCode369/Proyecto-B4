package org.library.system;

import org.library.system.model.RequestDetails;
import org.library.system.model.LoanApplication;
import org.library.system.model.Book;
import org.library.system.model.LoanDetails;
import org.library.system.model.User;
import org.library.system.model.Loan;
import org.library.system.enums.LoanStatus;
import org.library.system.enums.RequestStatus;
import org.library.system.enums.Role;
import org.library.utils.SessionManager; // Importamos el manejador de sesiones

import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); 
        // Obtenemos la instancia única del SessionManager
        SessionManager session = SessionManager.getInstance();
        
        System.out.println("========================================");
        System.out.println("  LIBRARY MANAGEMENT SYSTEM            ");
        System.out.println("========================================");

        // 1. Creación de datos de prueba
        User estudiante = new User(1, "EST-2026", "Carlos", "Perez", "carlos@correo.com", "hash123", Role.STUDENT, true);
        User bibliotecario = new User(2, "BIB-007", "Ana", "Gomez", "ana@correo.com", "hash456", Role.LIBRARIAN, true);
        Book libro = new Book(101, "978-3-16-148410-0", "Design Patterns", "Erich Gamma", "Addison-Wesley", 1994, 5, 5, true);

        System.out.println("[INFO] Initial data created successfully.");
        System.out.println("----------------------------------------\n");

        // --- FASE 1: INICIO DE SESIÓN DEL ESTUDIANTE ---
        System.out.println("[AUTHENTICATION] Student login attempt...");
        // Intentamos iniciar sesión con el usuario estudiante y su contraseña
        boolean estudianteLogueado = session.login(estudiante, "hash123");

        // Validamos que el inicio de sesión fuera exitoso y que sea un ESTUDIANTE
        if (estudianteLogueado && session.hasRole(Role.STUDENT)) {
            System.out.println("----------------------------------------");
            System.out.println("[PROCESS] Creating loan request by active Student...");
            
            // Asociamos el ID de forma segura mediante session.getCurrentUser()
            LoanApplication solicitud = new LoanApplication(501, session.getCurrentUser().getUser_id(), LocalDate.now(), RequestStatus.PENDING, "None", null, null);
            RequestDetails solDetalle = new RequestDetails(901, solicitud.getRequest_id(), libro.getBook_id(), 1);

            System.out.println("Request #" + solicitud.getRequest_id() + " saved with status: " + solicitud.getStatus());
            System.out.println("----------------------------------------\n");

            // El estudiante termina su operación y cierra sesión
            session.logout();
            System.out.println("----------------------------------------\n");

            // --- FASE 2: INICIO DE SESIÓN DEL BIBLIOTECARIO ---
            System.out.println("Press ENTER to continue with Librarian review...");
            scanner.nextLine();

            System.out.println("[AUTHENTICATION] Librarian login attempt...");
            boolean bibliotecarioLogueado = session.login(bibliotecario, "hash456");

            // Validamos que el inicio de sesión fuera exitoso y que sea un BIBLIOTECARIO
            if (bibliotecarioLogueado && session.hasRole(Role.LIBRARIAN)) {
                System.out.println("----------------------------------------");
                System.out.println("[PROCESS] Processing approval...");

                // El bibliotecario en sesión aprueba la solicitud
                solicitud.setStatus(RequestStatus.APPROVED);
                solicitud.setLibrarian_id(session.getCurrentUser().getUser_id());
                solicitud.setResponse_date(LocalDate.now());
                solicitud.setObservation("Approved after manual stock check.");

                // Se genera el préstamo físico
                Loan prestamo = new Loan(701, solicitud.getRequest_id(), solicitud.getStudent_id(), solicitud.getLibrarian_id(), LocalDate.now(), LocalDate.now().plusDays(7), LoanStatus.ACTIVE);
                LoanDetails presDetalle = new LoanDetails(801, prestamo.getLoan_id(), libro.getBook_id(), solDetalle.getQuantity(), 0);

                // Descontamos del inventario disponible
                libro.setAvailable_stock(libro.getAvailable_stock() - presDetalle.getQuantity());

                System.out.println("[SUCCESS] Loan generated successfully!");
                System.out.println("Approved by Librarian: " + session.getCurrentUser().getFirst_name());
                System.out.println("Loan ID: " + prestamo.getLoan_id() + " | Status: " + prestamo.getStatus());
                System.out.println("New available stock for '" + libro.getTitle() + "': " + libro.getAvailable_stock());
                
                // El bibliotecario cierra su sesión al finalizar
                session.logout();
            }
        }
        
        System.out.println("========================================");
        scanner.close();
    }
}
