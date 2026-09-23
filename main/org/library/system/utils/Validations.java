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
     * Valida si un texto está vacío o es null.
     */
    public boolean isEmpty(String text) {
        if (text == null) return true;
        return text.trim().isEmpty();
    }

    /**
     * Valida si un texto no está vacío.
     */
    public boolean isNotEmpty(String text) {
        return !isEmpty(text);
    }

    /**
     * Valida si dos textos son iguales.
     */
    public boolean equalsText(String textOriginal, String textCompare) {
        if (textOriginal == null || textCompare == null) return false;
        return textOriginal.equals(textCompare);
    }

    /**
     * Valida que un texto no supere la longitud máxima.
     */
    public boolean validateMaxLength(String text, int maxLength) {
        if (text == null) return false;
        return text.length() <= maxLength;
    }

    /**
     * Valida que un texto cumpla con la longitud mínima.
     */
    public boolean validateMinLength(String text, int minLength) {
        if (text == null) return false;
        return text.length() >= minLength;
    }

    /**
     * Valida formato de correo electrónico.
     */
    public boolean validateEmail(String email) {
        if (email == null) return false;
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    /**
     * Valida que un texto contenga solo números.
     */
    public boolean validateOnlyNumbers(String text) {
        if (text == null || text.isEmpty()) return false;
        return text.matches("^[0-9]+$");
    }

    /**
     * Valida que un texto contenga solo letras y espacios.
     */
    public boolean validateOnlyLetters(String text) {
        if (text == null || text.isEmpty()) return false;
        return text.matches("^[A-Za-záéíóúÁÉÍÓÚñÑ\\s]+$");
    }

    /**
     * Valida formato ISBN (10 o 13 dígitos).
     */
    public boolean validateIsbn(String isbn) {
        if (isbn == null || isbn.isEmpty()) return false;
        String cleanIsbn = isbn.replaceAll("[-\\s]", "");
        return cleanIsbn.matches("^(97[8-9])?[0-9]{10}$|^[0-9]{13}$");
    }

    /**
     * Valida que un número esté dentro de un rango.
     */
    public boolean validateRange(int value, int min, int max) {
        if (min > max) return false;
        return value >= min && value <= max;
    }

    /**
     * Valida que un número sea positivo.
     */
    public boolean validatePositiveNumber(int value) {
        return value > 0;
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
     * Valida formato de código de barras (6-20 caracteres alfanuméricos).
     */
    public boolean validateBarcode(String barcode) {
        if (barcode == null || barcode.isEmpty()) return false;
        return barcode.matches("^[A-Za-z0-9\\-]{6,20}$");
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
        if (text == null || text.isEmpty()) return false;
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}