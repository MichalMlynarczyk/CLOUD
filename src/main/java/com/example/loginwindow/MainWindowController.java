package com.example.loginwindow;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import static com.bazaDanych.BazaDanychRMI.*;
import static com.example.loginwindow.Scene.setScene;

/**
 * Klasa zarządzjąca plikiem XML - GŁÓWNE OKNO UŻYTKOWNIKA PO ZALOGOWANIU.
 */
public class MainWindowController implements Initializable {

    @FXML
    private Button signOutButton;
    @FXML
    private AnchorPane dragArea;
    @FXML
    private VBox userFileArea;
    @FXML
    private Label DragHere;
    @FXML
    private ImageView Logo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // wygląd przycisku "wyloguj się"
        this.signOutStyle();
        // zarządza działaniem - przeciagania pliku przez użytkonika
        this.activateDragArea();
        // wygląd miejsca gdzie pliki się przeciąga
        this.dragHereStyle();
        // towrzenia animacji loga
        new LogoStyle(Logo);
       // LogoStyle logoStyle = new LogoStyle(Logo);

        // wczytuje zpaisane pliki użytkownika i wyświetla w oknie
        try {
            this.readDataToFileExplorer();
        } catch (SQLException | RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * wczytuje dane z bazy danych i wyswietla je na koncie użytkownika
     * @throws SQLException, RemoteException
     */
    @FXML
    public void readDataToFileExplorer() throws SQLException, RemoteException, NotBoundException {
        ArrayList<ArrayList<String>> records = PobierzDaneZBazy();
        for(ArrayList<String> i : records){
            createFileInFileExplorer(i.get(0),
                    Integer.parseInt(String.valueOf(i.get(1))),
                    i.get(2)                         );
        }
    }

    /**
     * tworzy animacje na napisie "drag here file"
     */
    public void dragHereStyle(){
        Light.Distant lighting = new Light.Distant();

        Lighting lighting1 = new Lighting();
        lighting1.setSpecularConstant(1.25);
        lighting1.setLight(lighting);
        DragHere.setEffect(lighting1);


        AtomicBoolean effects = new AtomicBoolean(true);
        AtomicReference<AtomicInteger> brightness = new AtomicReference<>(new AtomicInteger(0));


        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(10), evt -> {
                        if (lighting.getAzimuth() > 360 ){
                            brightness.set(new AtomicInteger(0));
                            effects.set(true);
                        }
                        brightness.get().updateAndGet(v -> (v + 1));
                        lighting.setAzimuth(brightness.get().doubleValue());
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Tworzy miejsce do upuszczania plików przez użytkownika.
     */
    public void activateDragArea() {
        setDragOver();
        setDragDropped();
    }

    /**
     * Tworzy event działający podczas wykracia przeciągania pliku przez użytkownika,
     * do momentu upuszczenia, ale bez niego.
     */
    void setDragOver(){
        dragArea.setOnDragOver(event -> {
            if (event.getGestureSource() != dragArea
                    && event.getDragboard().hasFiles()) {
                dragArea.setStyle("-fx-background-color:  #0c162d;"+
                                  "-fx-border-width: 2;" +
                                  "-fx-border-color: blue"      );
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });
    }

    /**
     * Tworzy event do wykrycia upuszczenia pliku przez użytkownika.
     */
    void setDragDropped(){
        dragArea.setOnDragDropped(event -> {
            dragArea.setStyle("-fx-background-color: transparent;"+
                              "-fx-border-width: 2;" +
                              "-fx-border-color:  #55627e");
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                // odczytywanie ściezki absolutnej pliku na komputerze użytkownika
                String path = db.getFiles().toString(); // [C://...]
                path = path.substring(1, path.length()-1);
                // ścieżka do upuszczonego pliku przez użytkownika !
                Path filePath = Paths.get(path);  // -> ściezka absolutna
                //  -- uchwyt do pliku, służy do sprawdzenia czy to ->
                //  -- co przeciągnął użytkownik jest plikiem czy katalogiem
                File file = new File(path);

                // atrybuty pliku !
                String fileName; //                 -- NAZWA PLIKU
                int fileSize; //                    -- ROZMIAR PLIKU
                String dateAdded; //                -- DATA DODANIA PLIKU
                // ----------------------- //
                fileName = String.valueOf(filePath.getFileName()); // pobieranie nazwy pliku
                if(file.isFile()) {fileSize = (int) file.length();} // pobieranie rozmiaru pliku
                else {fileSize = folderSize(file);}                 // ^ ^ ^ ^ ^
                LocalDateTime timeAdded = LocalDateTime.now();  // pobieranie aktualnej daty
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                dateAdded  = dtf.format(timeAdded);

                // tworzenie nowego rekordu w bazie i w scroll panelu :
                createFileInFileExplorer(fileName, fileSize, dateAdded);
                // zapis do bazy danych


                try {
                    DodajPlikUżytkownika(fileName, String.valueOf(fileSize), dateAdded, filePath.toString());
                } catch (SQLException | RemoteException | NotBoundException e) {
                    throw new RuntimeException(e);
                }


                success = true;
            }
            /* let the source know whether the string was successfully
             * transferred and used */
            event.setDropCompleted(success);
            event.consume();
        });
    }

    /**
     * oblicza rommiar katalodu
     * @param directory ścieżka do katalogu
     * @return zwraca ilość KB
     */
    public int  folderSize(File directory) {
        int length = 0;
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isFile())
                length += file.length();
            else
                length += folderSize(file);
        }
        return length;
    }

    /**
     * tworzy rekord w oknie aplikacji
     * @param fileName nazwa pliku
     * @param fileSize rozmiar pliku
     * @param dateAdded data dodania
     */
    void createFileInFileExplorer(String fileName, int fileSize, String dateAdded){
        HBox verse = new HBox();
        Label fileNameLabel = new Label();
        Label sizeLabel = new Label();
        Label dateLabel = new Label();

        fileNameLabel.setText(fileName);
        sizeLabel.setText(fileSize + " bajtów");
        dateLabel.setText(dateAdded);

        setStyleFileNameLabel(fileNameLabel, "#2596be");
        setStyleSizeLabel(sizeLabel, "#2596be");
        setStyleDateLabel(dateLabel, "#2596be");

        verse.setCursor(Cursor.HAND);
        verse.setTranslateY(2);

        verse.addEventHandler(MouseEvent.MOUSE_ENTERED,
                e -> {
                    setStyleFileNameLabel(fileNameLabel, "#76b5c5");
                    setStyleSizeLabel(sizeLabel, "#76b5c5");
                    setStyleDateLabel(dateLabel, "#76b5c5");
                });

        verse.addEventHandler(MouseEvent.MOUSE_EXITED,
                e -> {
                    setStyleFileNameLabel(fileNameLabel, "#2596be");
                    setStyleSizeLabel(sizeLabel, "#2596be");
                    setStyleDateLabel(dateLabel, "#2596be");
                });

        // dodanie podpowiedzi -- po najechaniu myszki na rekord :
        Tooltip tooltip = new Tooltip("download file");
        tooltip.setShowDelay(Duration.millis(100));
        fileNameLabel.setTooltip(tooltip);
        sizeLabel.setTooltip(tooltip);
        dateLabel.setTooltip(tooltip);

        verse.getChildren().addAll(fileNameLabel, sizeLabel, dateLabel);

        verse.setStyle("-fx-border-width:  1 0 1 0;" +
                       "-fx-border-color:  #0c162d;" );
        userFileArea.getChildren().add(verse);
    }
    /**
     * @param fileNameLabel nazwa pliku
     * @param backgroundColor kolor tła
     */
    void setStyleFileNameLabel(Label fileNameLabel, String backgroundColor){
        fileNameLabel.setPrefSize(205, 25);
        fileNameLabel.setStyle("-fx-background-color:"+ backgroundColor+ ";" +
                "-fx-font-size: 15;" +
                "-fx-label-padding: 0 0 0 10;");
    }
    /**
     * @param sizeLabel wielkość pliku
     * @param backgroundColor kolor tła
     */
    void setStyleSizeLabel(Label sizeLabel, String backgroundColor){
        sizeLabel.setPrefSize(219, 25);
        sizeLabel.setStyle("-fx-background-color:" + backgroundColor + ";" +
                           "-fx-font-size: 15;" +
                           "-fx-label-padding: 0 0 0 10;" +
                           "-fx-border-color: black;" +
                           "-fx-border-width:  0 2 0 2"             );
    }
    /**
     * @param dateLabel infomacje o pliku
     * @param backgroundColor kolor tła
     */
    void setStyleDateLabel(Label dateLabel, String backgroundColor){
        dateLabel.setPrefSize(235, 25);
        dateLabel.setStyle( "-fx-background-color:" + backgroundColor + ";" +
                            "-fx-font-size: 15;" +
                            "-fx-label-padding: 0 0 0 10;"          );
    }

    /**
     * zmiana wygladu po przeciągnieciu myszki po za dragArea
     */
    @FXML
    void dragAreaMouseExited() {
        dragArea.setStyle(  "-fx-background-color: transparent;"+
                            "-fx-border-width: 2;" +
                            "-fx-border-color: #55627e;"               );
    }



    // SIGN OUT TERAZ !!
    @FXML
    void signOutMouseClicked() throws IOException {
        SetEmail("");
        setScene(Scenes.LOGIN);
    }

    @FXML
    void signOutStyle(){
        signOutButton.setStyle("-fx-font-size: 15px;-fx-background-color: #0c162d;-fx-border-width: 2;-fx-border-color: #55627e; -fx-alignment: center");
    }
    @FXML
    void signOutMouseEntered() {
        signOutButton.setStyle("-fx-font-size: 18px;-fx-background-color: #0c162d;-fx-border-width: 2;-fx-border-color: #55627e;  -fx-alignment: center");
    }

    @FXML
    void signOutMouseExited() {
        signOutButton.setStyle("-fx-font-size: 15px;-fx-background-color: #0c162d;-fx-border-width: 2;-fx-border-color: #55627e; -fx-alignment: center");
    }

}
