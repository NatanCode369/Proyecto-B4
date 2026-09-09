package org.library.system.utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;

public class AlertUtils {
    private Alert alert = new Alert(Alert.AlertType.NONE);
    private static AlertUtils alertUtils;

    public static AlertUtils instanceAlert() {
        if (alertUtils == null)
            alertUtils = new AlertUtils();
        return alertUtils;
    }

    private Alert.AlertType toAlertType(AppStatus.Severity severity) {
        return switch (severity) {
            case INFO -> Alert.AlertType.INFORMATION;
            case WARNING -> Alert.AlertType.WARNING;
            case ERROR -> Alert.AlertType.ERROR;
            case CONFIRMATION -> Alert.AlertType.CONFIRMATION;
        };
    }

    public void show(AppStatus status, String detail) {
        // Setea los valores para la alerta.
        Runnable display = () -> {
            alert.setAlertType(toAlertType(status.getSeverity()));
            alert.setTitle(String.valueOf(status.getCode()));
            alert.setHeaderText(status.getTitle());
            alert.setContentText(
                    detail == null || detail.isBlank()?
                            status.getDescriptionMessage(): detail
            );
        };
        alert.show();

        // Verifica si el hilo actual es el hilo de JavaFX
        if (Platform.isFxApplicationThread()) {
            display.run(); // Si lo es, correo el display.
        } else {
            Platform.runLater(display); // Si no, el proceso se mantiene en segundo plano.
        }
    }
}
