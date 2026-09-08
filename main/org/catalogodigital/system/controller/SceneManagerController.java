package org.catalogodigital.system.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManagerController {

    private SceneManagerController() {
        // Constructor privado para evitar instanciacion
    }

    public static void cambiarEscena(Stage stage, String rutaFxml, String titulo, int ancho, int alto) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SceneManagerController.class.getResource(rutaFxml)
            );
            Scene scene = new Scene(loader.load(), ancho, alto);
            stage.setTitle(titulo);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al cargar la vista: " + rutaFxml);
            e.printStackTrace();
        }
    }

    public static void cerrarYAbrir(Stage stageActual, String rutaFxml, String titulo, int ancho, int alto) {
        stageActual.close();
        cambiarEscena(new Stage(), rutaFxml, titulo, ancho, alto);
    }
}