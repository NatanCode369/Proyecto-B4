package org.library.system.service;

import org.library.system.enums.Role;

/**
 * Resuelve el rol del usuario segun el prefijo de su correo electronico.
 *
 * Convencion:
 *   std.*  -> STUDENT   (Estudiante)
 *   blt.*  -> LIBRARIAN (Bibliotecario)
 *   btj.*  -> MANAGER   (Bibliotecario Jefe)
 */
public class EmailRoleResolver {

    private EmailRoleResolver() {}

    public static Role resolve(String email) {
        if (email == null || email.isBlank()) return null;
        String normalized = email.trim().toLowerCase();
        if (normalized.startsWith("std.")) return Role.STUDENT;
        if (normalized.startsWith("blt.")) return Role.LIBRARIAN;
        if (normalized.startsWith("btj.")) return Role.MANAGER;
        return null;
    }

    public static boolean hasValidPrefix(String email) {
        return resolve(email) != null;
    }

    public static String expectedPrefix(Role role) {
        return switch (role) {
            case STUDENT -> "std.";
            case LIBRARIAN -> "blt.";
            case MANAGER -> "btj.";
        };
    }
}