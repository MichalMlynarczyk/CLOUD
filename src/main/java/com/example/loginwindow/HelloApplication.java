package com.example.loginwindow;

import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;


/**
 * klasa rozpoczynająca program
 * @author Młynarczyk, Śmierciak, Nowicki, Snochowski, Żurek
 * @version 1.0
 */
public class HelloApplication extends Application {
    Scene window; // -> zmienia sceny w programie
    @Override
    public void start(Stage stage) throws IOException {
        /*
          @link Scene#Scene()
         */
       // window = new com.example.loginwindow.Scene(stage);
        window = new Scene(stage);
        /*
          @link Scene#setScene(Scenes)
         */
        Scene.setScene(Scenes.LOGIN);
    }

    public static void main(String[] args) {
        launch();
    }
}