package org.library.system;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        try {
            URL fxmlLocation = getClass().getResource("/org/library/system/view/LoginView.fxml");

            if (fxmlLocation == null) {
                System.err.println("No se encuentra LoginView.fxml");
                System.err.println("Verifica que el archivo existe en:");
                System.err.println("src/main/resources/org/library/system/view/LoginView.fxml");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Scene scene = new Scene(loader.load());

            stage.setTitle("Sistema Bibliotecario - Iniciar Sesion");
            stage.setScene(scene);
            stage.sizeToScene();
            stage.setResizable(false);
            stage.show();

            System.out.println("Aplicacion iniciada correctamente");

        } catch (IOException e) {
            System.err.println("Error al iniciar la aplicacion: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}