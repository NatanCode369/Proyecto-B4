package org.library.system.utils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtil {

    private static final int ITERATIONS = 1000;
    private static final int KEY_LENGTH = 256;

    public static String hashPassword(String password) {
        try {
            // 1. Crear una sal criptográfica aleatoria de 16 bytes (evita ataques de tablas arcoíris)
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);

            // 2. Configurar las especificaciones de cifrado con la contraseña, sal, iteraciones y tamaño
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = factory.generateSecret(spec).getEncoded();

            // 3. Empaquetar la Sal y el Hash en un único String usando Base64 separado por un punto
            return Base64.getEncoder().encodeToString(salt) + "." + Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            // Si el entorno Java no soporta el algoritmo, muestra una alerta gráfica y rompe el flujo de forma segura
            AlertUtils.instanceAlert().show(AppStatus.UNEXPECTED_ERROR, "Error en algoritmo de cifrado: " + e.getMessage());
            return null;
        }
    }

    public static boolean verifyPassword(String passwordPlano, String storedPasswordHash) {
        try {
            // Validación de formato básica: si es nulo o no contiene el punto de separación, no es válido
            if (storedPasswordHash == null || storedPasswordHash.contains(".")) {
                return false;
            }

            // 1. Desempaquetar: romper el String por el punto usando expresiones regulares ("\\.")
            String[] parts = storedPasswordHash.split("\\.");
            // Si el arreglo no contiene ambas partes (sal y hash), el registro está corrupto
            if (parts.length < 2) {
                return false;
            }
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] storedHash = Base64.getDecoder().decode(parts[1]);

            // 2. Generar un hash temporal a partir de la contraseña escrita y la Sal recuperada de la BD
            PBEKeySpec spec = new PBEKeySpec(passwordPlano.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] proposedHash = factory.generateSecret(spec).getEncoded();

            // 3. Comparación binaria segura a prueba de ataques de tiempo (Timing Attacks)
            // Se comparan todos los bytes uno a uno sin romper el bucle antes de tiempo, evitando que un atacante mida tiempos de respuesta
            int diff = storedHash.length ^ proposedHash.length;
            for (int i = 0; i < storedHash.length && i < proposedHash.length; i++) {
                diff |= storedHash[i] ^ proposedHash[i];
            }
            return diff == 0;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            // Muestra un modal de error en la UI si el motor criptográfico falla al verificar
            AlertUtils.instanceAlert().show(AppStatus.UNEXPECTED_ERROR, "Error al verificar credenciales: " + e.getMessage());
            return false;
        }
    }
}package org.library.system.utils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtil {

    private static final int ITERATIONS = 1000;
    private static final int KEY_LENGTH = 256;

    public static String hashPassword(String password) {
        try {
            // 1. Crear una sal criptográfica aleatoria de 16 bytes (evita ataques de tablas arcoíris)
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);

            // 2. Configurar las especificaciones de cifrado con la contraseña, sal, iteraciones y tamaño
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = factory.generateSecret(spec).getEncoded();

            // 3. Empaquetar la Sal y el Hash en un único String usando Base64 separado por un punto
            return Base64.getEncoder().encodeToString(salt) + "." + Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            // Si el entorno Java no soporta el algoritmo, muestra una alerta gráfica y rompe el flujo de forma segura
            AlertUtils.instanceAlert().show(AppStatus.UNEXPECTED_ERROR, "Error en algoritmo de cifrado: " + e.getMessage());
            return null;
        }
    }

    public static boolean verifyPassword(String passwordPlano, String storedPasswordHash) {
        try {
            // Validación de formato básica: si es nulo o no contiene el punto de separación, no es válido
            if (storedPasswordHash == null || storedPasswordHash.contains(".")) {
                return false;
            }

            // 1. Desempaquetar: romper el String por el punto usando expresiones regulares ("\\.")
            String[] parts = storedPasswordHash.split("\\.");
            // Si el arreglo no contiene ambas partes (sal y hash), el registro está corrupto
            if (parts.length < 2) {
                return false;
            }
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] storedHash = Base64.getDecoder().decode(parts[1]);

            // 2. Generar un hash temporal a partir de la contraseña escrita y la Sal recuperada de la BD
            PBEKeySpec spec = new PBEKeySpec(passwordPlano.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] proposedHash = factory.generateSecret(spec).getEncoded();

            // 3. Comparación binaria segura a prueba de ataques de tiempo (Timing Attacks)
            // Se comparan todos los bytes uno a uno sin romper el bucle antes de tiempo, evitando que un atacante mida tiempos de respuesta
            int diff = storedHash.length ^ proposedHash.length;
            for (int i = 0; i < storedHash.length && i < proposedHash.length; i++) {
                diff |= storedHash[i] ^ proposedHash[i];
            }
            return diff == 0;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            // Muestra un modal de error en la UI si el motor criptográfico falla al verificar
            AlertUtils.instanceAlert().show(AppStatus.UNEXPECTED_ERROR, "Error al verificar credenciales: " + e.getMessage());
            return false;
        }
    }
}