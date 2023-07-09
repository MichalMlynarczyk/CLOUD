package com.example.loginwindow;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import static com.bazaDanych.BazaDanychRMI.*;
import static com.example.loginwindow.Scene.*;

/**
 * Klasa zarządzjąca plikiem XML - REJESTRACJA.
 */
public class RegistrationController implements Initializable {

    @FXML
    private PasswordField ConfirmPassword;
    @FXML
    private TextField Email;
    @FXML
    private TextField FirstName;
    @FXML
    private TextField LastName;
    @FXML
    private PasswordField Password;
    @FXML
    private Label incorrectData;
    @FXML
    private ImageView Logo;
    @FXML
    private AnchorPane backGround;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new Snow(backGround);
        new LogoStyle(Logo);
    }


    /**
     * Zmienia scene po nakliknięciu na przyski "zaloguj się".
     * @throws IOException tag
     */
    @FXML
    void signInButtonClicked() throws IOException {
        setScene(Scenes.LOGIN);
    }

    /**
     * Akcja po naciśnięciu przycisku "zarejestruj się".
     */
    @FXML
    void RegisterButtonClicked() throws SQLException, IOException {
        // ważne dane
        String firstName = FirstName.getText(); //            -- IMIE UZYRKOWNIKA
        String lastName = LastName.getText(); //              -- NAZWISKO UZYTKOWNIKA
        String email = Email.getText();//                     -- EMAIL UŻYTKONIKA
        String password = Password.getText();//               -- HASŁO UZYTKOWNIKA
        String confirmPassword = ConfirmPassword.getText();// -- POWTÓRZONE HASŁO UŻYTKONWIKA


        // sprawdzenie czy nie ma pustych pól
        if(firstName.equals("") ||
           lastName.equals("") ||
           email.equals("") ||
           password.equals("") ||
           confirmPassword.equals("")){
            incorrectData.setText("Complete all data!");
            incorrectData.setVisible(true);
            return;
        }

        // sprawdzenie czy hasła nie są za krótkie
        if (password.length() < 6){
            incorrectData.setText("Too short pasword!");
            incorrectData.setVisible(true);
            Password.setText("");
            ConfirmPassword.setText("");
            return;
        }

        // sparwdzenie czy email posiada "@"
        int czyToEmail = email.indexOf("@");
        if(czyToEmail == -1){
            incorrectData.setText("Email is not correct!");
            incorrectData.setVisible(true);
            Email.setText("");
            return;
        }
        // sprawdzanie danych --------------- >>
        if(!checkPassword(password,confirmPassword)){
            incorrectData.setText("Passwords are not identical!");
            incorrectData.setVisible(true);
            Password.setText("");
            ConfirmPassword.setText("");
        }else {
          //  if(sprawdzCzyEmailJestUnikalny(email)){
            if(SprawdzCzyEmailJestUnikalny(email)){
               // dodajUżytkownika(email, firstName, lastName, password);
                DodajUżytkownika(email, firstName, lastName, password);
                SetEmail(email);
                setScene(Scenes.MAINWINDOW);
            }else{
                incorrectData.setText("This email already exists!");
                incorrectData.setVisible(true);
                Email.setText("");
                Password.setText("");
                ConfirmPassword.setText("");
            }
        }
    }

    /**
     * Sprawdza czy hasła są identyczne.
     * @param password hasło
     * @param confirmPassword powtórzone hasło
     * @return prawda/fałsz
     */
    boolean checkPassword(String password, String confirmPassword){
        return password.equals(confirmPassword);
    }
}
