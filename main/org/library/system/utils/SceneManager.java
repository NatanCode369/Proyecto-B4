package org.library.system.utils;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
    private static SceneManager instanciaSceneManager;
    private Stage primaryStage;

    private SceneManager() {
    }

    private Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public static SceneManager getInstanciaSceneManager() {
        if (instanciaSceneManager == null)
            instanciaSceneManager = new SceneManager();
        return instanciaSceneManager;
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
}
