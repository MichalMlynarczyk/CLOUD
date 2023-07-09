package com.example.loginwindow;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import static com.bazaDanych.BazaDanychRMI.SprwadzLogowanie;
import static com.example.loginwindow.Scene.*;

/**
 * Klasa zarządzjąca plikiem XML - LOGOWANIEM.
 */
public class HelloController implements Initializable {

    @FXML
    private Button forgotPasswordButton;
    @FXML
    private PasswordField passwordLabel;
    @FXML
    private TextField usernameLabel;
    @FXML
    private Label incorrectData;
    @FXML
    private ImageView Logo;
    @FXML
    private AnchorPane backGround;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       this.fButtonStyle();
       // DODAJE PŁATKI ŚNIEGU W TLE
       new Snow(backGround);
       // TWORZY ANIMACJE LOGO
        new LogoStyle(Logo);
    }

    /**
     * Obsługuje event po naciśnięciu przycisku "SIGN UP".
     * Zmienia scnene na rejstracje po nakliknięciu na przycisk "SIGN UP".
     * @throws IOException tag
     */
    @FXML
    void signUpButtonClicked() throws IOException {
        /*
          @link Scene#setScene(Scenes)
         */
        setScene(Scenes.REGISTRATION);
    }

    /**
     * Obsługuje event po naciśnięciu przycisku "LOGIN".
     */
    @FXML
    void loginButtonClicked() throws SQLException, IOException {
        // pobierania danych
        String username = usernameLabel.getText();
        String password = passwordLabel.getText();
        // kasowanie napisów
        usernameLabel.setText("");
        passwordLabel.setText("");

        // sprawdza czy któreś z pól nie jest puste
        if (username.equals("") || password.equals(""))
        {
            incorrectData.setVisible(true);
            return;
        }

        // sparwdza dane logowania
        if (SprwadzLogowanie(username, password)) {
            setScene(Scenes.MAINWINDOW);
        } else {
            incorrectData.setVisible(true);
        }
    }

    /**
     * Forgot Password Button Style - ustawia deafultowy wygląd przycisku.
     */
    @FXML
    void fButtonStyle(){ // #2D3447
        forgotPasswordButton.setStyle(  "-fx-background-color: #0c162d;" +
                                        "-fx-text-fill: #2D3447;" +
                                        "-fx-font-size: 18px;"
        );
    }

    /**
     * Zmienia wygląd przyciku (forgot password) po najechaniu muszką na niego.
     */
    @FXML
    void fButtonStyleMouseEntered() {
        forgotPasswordButton.setStyle("-fx-background-color: #0c162d;" +
                                      "-fx-text-fill: #eeeeee;" +
                                      "-fx-font-size: 18px;"
        );
    }
    /**
     * Zmienia wygląd przyciku (forgot password) po zjechaniu muszką z niego.
     */
    @FXML
    void fButtonStyleMouseExited() {
        forgotPasswordButton.setStyle("-fx-background-color: #0c162d;" +
                                      "-fx-text-fill: #2D3447;" +
                                      "-fx-font-size: 18px;"
        );
    }
}