package org.library.system.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {
    private static SceneManager instanciaSceneManager;
    private Stage primaryStage;

    private SceneManager() {
    }

    public static SceneManager getInstanciaSceneManager() {
        if (instanciaSceneManager == null)
            instanciaSceneManager = new SceneManager();
        return instanciaSceneManager;
    }

    // ← publico (antes era privado)
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void changeScene(Scene scene) {
        try {
            getPrimaryStage().setScene(scene);
            getPrimaryStage().sizeToScene();
            getPrimaryStage().show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void goTo(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SceneManager.class.getResource(fxmlPath)
            );
            Scene scene = new Scene(loader.load());
            changeScene(scene);
        } catch (IOException e) {
            System.err.println("Error al navegar a: " + fxmlPath);
            e.printStackTrace();
        }
    }
}