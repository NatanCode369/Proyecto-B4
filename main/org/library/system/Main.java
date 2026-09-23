package org.library.system;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.library.system.utils.SceneManager;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        SceneManager.getInstanciaSceneManager().setPrimaryStage(stage);

        // Cargar el icono
        try {
            java.net.URL iconUrl = getClass().getResource(
                    "/org/library/system/resources/images/library-book.png"
            );

            if (iconUrl == null) {
                System.err.println(">>> ICONO NO ENCONTRADO");
            } else {
                Image icon = new Image(iconUrl.toExternalForm());
                stage.getIcons().add(icon);
                System.out.println(">>> ICONO APLICADO");
            }
        } catch (Exception e) {
            System.err.println(">>> ERROR al cargar icono: " + e.getMessage());
        }

        // Cargar la vista inicial (Login)
        SceneManager.getInstanciaSceneManager().goTo(
                "/org/library/system/view/LoginView.fxml"
        );
    }

    public static void main(String[] args) {
        launch(args);
    }
}