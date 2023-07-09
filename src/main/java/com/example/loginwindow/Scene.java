package com.example.loginwindow;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import java.io.IOException;


/**
 * Z każdego miejsca możemy zmieniać okno dzięki tej klasie.
 */
public class Scene {
    /**
     * uchwyt do głównego okna
     */
    static Stage stage;

    /**
     * @param stage przkazanie głównej sceny jako parametr
     */
    Scene(Stage stage){
        Scene.stage = stage;
    }

    /**
     * ustawia wybrane sceny w głównym oknie
     * @param sceneName nazwa Sceny
     * @throws IOException tag
     */
    public static void setScene(Scenes sceneName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(sceneName.toString()));
        javafx.scene.Scene scene = new javafx.scene.Scene(fxmlLoader.load(), 1000, 600);
        stage.setScene(scene);
        stage.show();
    }
}
