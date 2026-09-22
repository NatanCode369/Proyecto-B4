package org.library.system.utils;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum AppStatus {
    OK(200, Severity.INFO, "Operación completada", "La operación se realizó con éxito."),
    BOOK_CREATED(201, Severity.INFO, "Libro creado", "Libro creado exitosamente."),
    BOOK_DELETED(204, Severity.INFO, "Libro eliminado", "Libro eliminado exitosamente."),
    USER_CREATED(201, Severity.INFO, "Usuario creado", "El usuario se creó correctamente."),
    USER_DELETED(204, Severity.INFO, "Usuario eliminado", "El usuario se eliminó correctamente."),

    INVALID_INPUT(400, Severity.WARNING, "Datos inválidos", "Revise los campos obligatorios."),
    UNAUTHORIZED(401, Severity.ERROR, "Sesión no válida", "Debe iniciar sesión."),
    FORBIDDEN(403, Severity.ERROR, "Acceso denegado", "No tiene permisos para esta operación."),
    NOT_FOUND(404, Severity.WARNING, "No encontrado", "No existe el recurso solicitado."),
    CONFLICT(409, Severity.WARNING, "Conflicto de datos", "El registro ya existe o no puede modificarse."),
    UNPROCESSABLE(422, Severity.WARNING, "Regla de negocio", "La operación no cumple las reglas del sistema."),

    DATABASE_UNAVAILABLE(503, Severity.ERROR, "Base de datos no disponible",
            "No fue posible conectarse con la base de datos."),
    UNEXPECTED_ERROR(500, Severity.ERROR, "Error inesperado",
            "Ocurrió un error interno. Intente nuevamente.");

    private final int code;
    private final Severity severity;
    private final String title;
    private final String descriptionMessage;

    private static final Map<Integer, AppStatus> BY_CODE =
            Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(
                    AppStatus::getCode,
                    Function.identity()
            ));

    AppStatus(int code, Severity severity, String title, String descriptionMessage) {
        this.code = code;
        this.severity = severity;
        this.title = title;
        this.descriptionMessage = descriptionMessage;
    }

    public static Optional<AppStatus> fromCode(int code) {
        return Optional.ofNullable(BY_CODE.get(code));
    }

    public int getCode() {
        return code;
    }

    public Severity getSeverity() {
        return severity;
    }

    public String getTitle() {
        return title;
    }

    public String getDescriptionMessage() {
        return descriptionMessage;
    }

    public enum Severity {
        INFO, WARNING, ERROR, CONFIRMATION
    }
}
