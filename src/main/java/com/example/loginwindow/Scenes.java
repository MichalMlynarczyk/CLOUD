package com.example.loginwindow;

/**
 * Możliwe sceny do wybrania.
 */
public enum Scenes {
    /**
     * okno logowania
     */
    LOGIN("hello-view.fxml"),
    /**
     * okno rejestracji
     */
    REGISTRATION("registration.fxml"),
    /**
     * główne okno po zalogowaniu
     */
    MAINWINDOW("mainWindow.fxml");

    private final String fxmlLink;

    Scenes(String fxmlLink) {
        this.fxmlLink = fxmlLink;
    }

    public String toString() {
        return this.fxmlLink;
    }
}
