package org.library.system.utils;

/**
 * Clase utilitaria para validaciones de datos en el sistema bibliotecario.
 * Todas las validaciones retornan boolean e imprimen mensajes de error por consola.
 */
public class Validations {
    private static Validations instancevalidations;

    public static Validations getInstancevalidations() {
        if (instancevalidations == null) {
            instancevalidations = new Validations();
        }
        return  instancevalidations;
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

    /**
     * Valida la fortaleza de una contraseña.
     */
    public boolean validatePasswordStrength(String password, int minLength) {
        if (password == null || password.isEmpty()) {
            //Contraseña vacía
            return false;
        }
        if (password.length() < minLength) {
            //Longitud no cumple con el mínimo
            return false;
        }
        boolean hasUpper = !password.equals(password.toLowerCase());
        boolean hasLower = !password.equals(password.toUpperCase());
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasSpecial = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");

        //Debe contener al menos 1 letra mayús
        return !hasUpper && !hasLower && !hasDigit && !hasSpecial;
    }

    /**
     * Valida que un año sea válido (1900 - actual).
     */
    public boolean validateYear(String year) {
        if (year == null || year.isEmpty()) {
            return false;
        }
        try {
            int yearInt = Integer.parseInt(year);
            int currentYear = java.time.Year.now().getValue();
            boolean result;
            result = yearInt >= 1900 && yearInt <= currentYear;
            //El año debe estar en el rango de 1900 y el año actual
            return result;
        } catch (NumberFormatException e) {
            //Año fuera del rango
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