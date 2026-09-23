package org.library.system.utils;

/**
 * Clase utilitaria para validaciones de datos en el sistema bibliotecario.
 * Todas las validaciones retornan boolean. Los mensajes al usuario
 * son manejados por AlertUtils desde los controladores.
 */
public class Validations {

    private static Validations instance;

    private Validations() {
    }

    public static Validations getInstancevalidations() {
        if (instance == null) {
            instance = new Validations();
        }
        return instance;
    }

    /**
     * Valida formato de correo electrónico.
     */
    public boolean validateEmail(String email) {
        if (email == null) {
            return false;
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    /**
     * Valida formato ISBN (10 o 13 dígitos).
     */
    public boolean validateIsbn(String isbn) {
        if (isbn == null || isbn.isEmpty())
            return false;
        String cleanIsbn = isbn.replaceAll("[-\\s]", "");
        String isbnRegexNumbers = "^(97[8-9])?[0-9]{10}$|^[0-9]{13}$";
        String isbnRegexLetters = ".*[a-zA-Z].*";
        return cleanIsbn.matches(isbnRegexNumbers) || !cleanIsbn.matches(isbnRegexLetters) ;
    }


    /**
     * Valida que un número sea positivo.
     */
    public boolean validatePositiveNumber(int value) {
        return value > 0;
    }

    public boolean validatePasswordMatch(String password, String confirmPassword) {
        if (password == null || confirmPassword == null) return false;
        return password.equals(confirmPassword);
    }

    /**
     * Valida la fortaleza de una contraseña.
     * Requisitos: minLength caracteres, 1 mayúscula, 1 minúscula, 1 número, 1 especial.
     */
    public boolean validatePasswordStrength(String password, int minLength) {
        if (password == null || password.isEmpty()) return false;
        if (password.length() < minLength) return false;

        boolean hasUpper = !password.equals(password.toLowerCase());
        boolean hasLower = !password.equals(password.toUpperCase());
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasSpecial = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");

        return hasUpper && hasLower && hasDigit && hasSpecial;
    }

    /**
     * Valida que un año sea válido (1900 - actual).
     */
    public boolean validateYear(String year) {
        if (year == null || year.isEmpty()) return false;
        try {
            int yearInt = Integer.parseInt(year);
            int currentYear = java.time.Year.now().getValue();
            return yearInt >= 1900 && yearInt <= currentYear;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Valida que un texto sea un número entero.
     */
    public boolean validateInteger(String text) {
        if (text == null || text.isEmpty())
            return false;

        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}