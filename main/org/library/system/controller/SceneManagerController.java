package org.library.system.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controlador central para la gestión de cambios de escena.
 */
public final class SceneManagerController {

    private SceneManagerController() {}

    /**
     * Cambia la escena actual del Stage por una nueva vista.
     *
     * @param stage     Stage actual
     * @param fxmlPath  Ruta al archivo FXML (ej: /org/library/system/view/LoginView.fxml)
     * @param title     Título de la ventana
     */
    public static void changeScene(Stage stage, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SceneManagerController.class.getResource(fxmlPath)
            );
            Scene scene = new Scene(loader.load());
            stage.setTitle(title);
            stage.setScene(scene);
            stage.sizeToScene();
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al cargar la vista: " + fxmlPath);
            e.printStackTrace();
        }
    }

    /**
     * Cierra el Stage actual y abre uno nuevo con la vista indicada.
     *
     * @param currentStage Stage que se va a cerrar
     * @param fxmlPath     Ruta al FXML de la nueva vista
     * @param title        Título de la nueva ventana
     */
    public static void closeAndOpen(Stage currentStage, String fxmlPath, String title) {
        currentStage.close();
        changeScene(new Stage(), fxmlPath, title);
    }
}