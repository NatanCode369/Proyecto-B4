package org.library.system.utils;

/**
 * Clase utilitaria para validaciones de datos en el sistema bibliotecario.
 * Todas las validaciones retornan boolean e imprimen mensajes de error por consola.
 */
public class Validations {

    /**
     * Valida si un texto está vacío o es null.
     *
     * @param text Texto a validar
     * @return true si está vacío o es null, false si tiene contenido
     */
    public boolean isEmpty(String text) {
        if (text == null) {
            System.out.println("Error: El texto es null");
            return true;
        }
        boolean result = text.trim().isEmpty();
        if (result) {
            System.out.println("Error: El campo está vacío");
        }
        return result;
    }

    /**
     * Valida si un texto no está vacío.
     *
     * @param text Texto a validar
     * @return true si tiene contenido, false si está vacío
     */
    public boolean isNotEmpty(String text) {
        return !isEmpty(text);
    }

    /**
     * Valida si dos textos son iguales.
     */
    public boolean equalsText(String textOriginal, String textCompare) {
        if (textOriginal == null || textCompare == null) {
            System.out.println("Error: Uno de los textos es null");
            return false;
        }
        boolean result = textOriginal.equals(textCompare);
        if (!result) {
            System.out.println("Error: Los textos no coinciden");
        }
        return result;
    }

    /**
     * Valida que un texto no supere la longitud máxima.
     */
    public boolean validateMaxLength(String text, int maxLength) {
        if (text == null) {
            System.out.println("Error: El texto es null");
            return false;
        }
        boolean result = text.length() <= maxLength;
        if (!result) {
            System.out.println("Error: El texto excede la longitud máxima de " + maxLength + " caracteres. Longitud actual: " + text.length());
        }
        return result;
    }

    /**
     * Valida que un texto cumpla con la longitud mínima.
     */
    public boolean validateMinLength(String text, int minLength) {
        if (text == null) {
            System.out.println("Error: El texto es null");
            return false;
        }
        boolean result = text.length() >= minLength;
        if (!result) {
            System.out.println("Error: El texto no alcanza la longitud mínima de " + minLength + " caracteres. Longitud actual: " + text.length());
        }
        return result;
    }

    /**
     * Valida formato de correo electrónico.
     */
    public boolean validateEmail(String email) {
        if (email == null) {
            System.out.println("Error: El correo es null");
            return false;
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        boolean result = email.matches(emailRegex);
        if (!result) {
            System.out.println("Error: El correo '" + email + "' no tiene un formato válido");
        }
        return result;
    }

    /**
     * Valida que un texto contenga solo números.
     */
    public boolean validateOnlyNumbers(String text) {
        if (text == null || text.isEmpty()) {
            System.out.println("Error: El texto es null o está vacío");
            return false;
        }
        String numberRegex = "^[0-9]+$";
        boolean result = text.matches(numberRegex);
        if (!result) {
            System.out.println("Error: '" + text + "' contiene caracteres no numéricos");
        }
        return result;
    }

    /**
     * Valida que un texto contenga solo letras y espacios.
     */
    public boolean validateOnlyLetters(String text) {
        if (text == null || text.isEmpty()) {
            System.out.println("Error: El texto es null o está vacío");
            return false;
        }
        String letterRegex = "^[A-Za-záéíóúÁÉÍÓÚñÑ\\s]+$";
        boolean result = text.matches(letterRegex);
        if (!result) {
            System.out.println("Error: '" + text + "' contiene caracteres no alfabéticos");
        }
        return result;
    }

    /**
     * Valida formato ISBN (10 o 13 dígitos).
     */
    public boolean validateIsbn(String isbn) {
        if (isbn == null || isbn.isEmpty()) {
            System.out.println("Error: El ISBN está vacío");
            return false;
        }
        String cleanIsbn = isbn.replaceAll("[-\\s]", "");
        String isbnRegex = "^(97[8-9])?[0-9]{10}$|^[0-9]{13}$";
        boolean result = cleanIsbn.matches(isbnRegex);
        if (!result) {
            System.out.println("Error: El ISBN '" + isbn + "' no es válido (debe ser 10 o 13 dígitos)");
        }
        return result;
    }

    /**
     * Valida que un número esté dentro de un rango.
     */
    public boolean validateRange(int value, int min, int max) {
        if (min > max) {
            System.out.println("Error: El mínimo (" + min + ") es mayor que el máximo (" + max + ")");
            return false;
        }
        boolean result = value >= min && value <= max;
        if (!result) {
            System.out.println("Error: El valor " + value + " no está en el rango [" + min + ", " + max + "]");
        }
        return result;
    }

    /**
     * Valida que un número sea positivo.
     */
    public boolean validatePositiveNumber(int value) {
        boolean result = value > 0;
        if (!result) {
            System.out.println("Error: El valor " + value + " debe ser mayor que cero");
        }
        return result;
    }

    /**
     * Valida que dos contraseñas coincidan.
     */
    public boolean validatePasswordMatch(String password, String confirmPassword) {
        if (password == null || confirmPassword == null) {
            System.out.println("Error: Una de las contraseñas es null");
            return false;
        }
        boolean result = password.equals(confirmPassword);
        if (!result) {
            System.out.println("Error: Las contraseñas no coinciden");
        }
        return result;
    }

    /**
     * Valida la fortaleza de una contraseña.
     */
    public boolean validatePasswordStrength(String password, int minLength) {
        if (password == null || password.isEmpty()) {
            System.out.println("Error: La contraseña está vacía");
            return false;
        }
        if (password.length() < minLength) {
            System.out.println("Error: La contraseña debe tener al menos " + minLength + " caracteres");
            return false;
        }
        boolean hasUpper = !password.equals(password.toLowerCase());
        boolean hasLower = !password.equals(password.toUpperCase());
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasSpecial = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");

        if (!hasUpper) {
            System.out.println("Error: La contraseña debe tener al menos una mayúscula");
            return false;
        }
        if (!hasLower) {
            System.out.println("Error: La contraseña debe tener al menos una minúscula");
            return false;
        }
        if (!hasDigit) {
            System.out.println("Error: La contraseña debe tener al menos un número");
            return false;
        }
        if (!hasSpecial) {
            System.out.println("Error: La contraseña debe tener al menos un carácter especial");
            return false;
        }
        return true;
    }

    /**
     * Valida formato de código de barras.
     */
    public boolean validateBarcode(String barcode) {
        if (barcode == null || barcode.isEmpty()) {
            System.out.println("Error: El código de barras está vacío");
            return false;
        }
        String barcodeRegex = "^[A-Za-z0-9\\-]{6,20}$";
        boolean result = barcode.matches(barcodeRegex);
        if (!result) {
            System.out.println("Error: El código de barras '" + barcode + "' no es válido (6-20 caracteres alfanuméricos)");
        }
        return result;
    }

    /**
     * Valida que un año sea válido (1900 - actual).
     */
    public boolean validateYear(String year) {
        if (year == null || year.isEmpty()) {
            System.out.println("Error: El año está vacío");
            return false;
        }
        try {
            int yearInt = Integer.parseInt(year);
            int currentYear = java.time.Year.now().getValue();
            boolean result = yearInt >= 1900 && yearInt <= currentYear;
            if (!result) {
                System.out.println("Error: El año debe estar entre 1900 y " + currentYear);
            }
            return result;
        } catch (NumberFormatException e) {
            System.out.println("Error: El año '" + year + "' no es un número válido");
            return false;
        }
    }

    /**
     * Valida que un texto sea un número entero.
     */
    public boolean validateInteger(String text) {
        if (text == null || text.isEmpty()) {
            System.out.println("Error: El campo está vacío");
            return false;
        }
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Error: '" + text + "' no es un número entero válido");
            return false;
        }
    }
}