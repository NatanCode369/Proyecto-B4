package org.library.system;

import javafx.application.Application;
import javafx.stage.Stage;
import org.library.system.utils.SceneManager;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        SceneManager.getInstanciaSceneManager().setPrimaryStage(stage);
        SceneManager.getInstanciaSceneManager().goTo(
                "/org/library/system/view/LoginView.fxml"
        );
    }

    public static void main(String[] args) {
        launch(args);
    }
}

public class Main extends Application {
    public static void main(String[] args) {
        launch(Application.class, args);
    }

    @Override
    public void start(Stage stage) {

    }
}

