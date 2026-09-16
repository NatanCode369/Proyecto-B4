package org.library.system;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.library.system.utils.SceneManager;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("[MAIN] Iniciando aplicacion");

        // Registrar el stage en el SceneManager
        SceneManager.getInstanciaSceneManager().setPrimaryStage(stage);
        System.out.println("[MAIN] Stage registrado en SceneManager");

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/library/system/view/LoginView.fxml")
        );
        Scene scene = new Scene(loader.load());

        stage.setTitle("Sistema Bibliotecario");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setResizable(false);
        stage.show();

        System.out.println("[MAIN] Aplicacion iniciada");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
