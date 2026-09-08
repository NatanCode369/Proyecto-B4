package org.library.system.utils;

import javafx.scene.control.Alert;

public class AlertUtils {
    private static Alert alert;

    public Alert getAlert() {
        return alert;
    }

    public static Alert instanceAlert() {
        if (alert == null)
            alert = new Alert(Alert.AlertType.NONE);
        return alert;
    }

    public void changeAlertType(String type) {
        switch (type.toUpperCase()) {
            case "CONFIRMATION" -> getAlert().setAlertType(Alert.AlertType.CONFIRMATION);
            case "INFORMATION" -> getAlert().setAlertType(Alert.AlertType.INFORMATION);
            case "ERROR" -> getAlert().setAlertType(Alert.AlertType.ERROR);
            case "WARNING" -> getAlert().setAlertType(Alert.AlertType.WARNING);
            default -> getAlert().setAlertType(Alert.AlertType.NONE);
        }
    }

    public void personalizeAlert(String preset, String... contentAlert) {
        switch (preset) {
            case "E1" -> {
                // Campos obligatorios vacíos
                getAlert().setTitle("Error");
                getAlert().setHeaderText("Campos vacíos");
                getAlert().setContentText(
                        "Debe completar todos los campos obligatorios."
                );
            }

            case "E2" -> {
                // Crendenciales inválidas
                getAlert().setTitle("Error");
                getAlert().setHeaderText("Credenciales Incorrectas");
                getAlert().setContentText("Ingrese nuevamente las credenciale");
            }

            case "E3" -> {
                // Usuario ya registrado
                getAlert().setTitle("Error");
                getAlert().setHeaderText("Usuario existente");
                getAlert().setContentText(
                        "El usuario que intenta registrar ya existe."
                );
            }

            case "E4" -> {
                // Contraseñas diferentes
                getAlert().setTitle("Error");
                getAlert().setHeaderText("Contraseñas no coinciden");
                getAlert().setContentText(
                        "La contraseña y su confirmación deben ser iguales."
                );
            }

            case "E*" -> {
                //Error genérico
                getAlert().setTitle("Error");
                getAlert().setHeaderText(contentAlert[0]);
                getAlert().setContentText(contentAlert[1]);
            }

            case "W1" -> {
                getAlert().setTitle("Advertencia");
                getAlert().setHeaderText("Contraseña no válida");
                getAlert().setContentText(
                        "La contraseña debe tener al menos 8 caracteres."
                );
            }

            case "W2" -> {
                getAlert().setTitle("Advertencia");
                getAlert().setHeaderText("Formato de correo inválido");
                getAlert().setContentText(
                        "No se ha agreado el dominio del correo."
                );
            }

            case "W3" -> {
                getAlert().setTitle("Advertencia");
                getAlert().setHeaderText("Formato de nombre inválido");
                getAlert().setContentText(
                        "Los nombres no deben poseer caracteres numéricos"
                );
            }

            case "I1" -> {
                getAlert().setTitle("Información");
                getAlert().setHeaderText("Registro completado");
                getAlert().setContentText(
                        "El usuario fue registrado correctamente."
                );
            }

            case "I2" -> {
                getAlert().setTitle("Información");
                getAlert().setHeaderText("Autenticación exitosa");
                getAlert().setContentText(
                        "Inicio de sesión realizado correctamente."
                );
            }

            case "C1" -> {
                alert.setTitle("Confirmación");
                alert.setHeaderText("Confirmar registro");
                alert.setContentText(
                        "¿Desea confirmar el registro del usuario?"
                );
            }

            default -> {
                alert.setTitle("Alerta");
                alert.setHeaderText(contentAlert[0]);
                alert.setContentText(contentAlert[1]);
            }
        }
    }
}
