package com.bazaDanych;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.ArrayList;

// ----- INTERTFACE ----- //   << ----------------------

/**
 * Interface do zdalnych metod serwera
 */
interface RMI_SERWER extends Remote {
    void dodajUżytkownika(String  email, String imie, String nazwisko, String haslo) throws RemoteException, SQLException;
    boolean sprawdzCzyEmailJestUnikalny(String email) throws RemoteException, SQLException;
    void dodajPlikUżytkownika(String Email, String fileName, String fileSize, String date, String filePath) throws RemoteException, SQLException;
    boolean sprwadzLogowanie(String login, String haslo) throws RemoteException, SQLException;
    ArrayList<ArrayList<String>> pobierzDaneZBazy(String Email) throws RemoteException, SQLException;
    void makeConnectionAgain() throws RemoteException, SQLException;
}

/**
 * służy do wysyłania danych na serwer który z kolei łączy się z baządanych
 */
public class BazaDanychRMI {

    static String Email;

    /**
     * ustawia email z którego będzie pobierać dane
     * @param email zpaisuje email obecnie zalogowanego użytkownika
     */
    public static void SetEmail(String email){
        Email = email;
    }

    /**
     * wpisuje dane użyttkownika do bazy, tworzy jego konto
     * @param email email
     * @param imie imie
     * @param nazwisko nazwisko
     * @param haslo hasło
     * @throws RemoteException tag
     * @throws SQLException tag
     */
    public static void DodajUżytkownika(String  email, String imie, String nazwisko, String haslo) throws RemoteException, SQLException {
        RMI_SERWER stub = StworzPoloczenie();
        try{
             stub.dodajUżytkownika(email, imie, nazwisko, haslo);
        }catch (Exception e){
            stub.makeConnectionAgain();
            DodajUżytkownika(email, imie, nazwisko, haslo);
            }
    }

    /**
     * dodaje plik użytkownika
     * @param fileName nazwa pliku
     * @param fileSize rozmiar pliku
     * @param date data
     * @param filePath ścieżka do pliku
     * @throws RemoteException tag
     * @throws SQLException tag
     * @throws NotBoundException tag
     */
    public static void DodajPlikUżytkownika(String fileName, String fileSize, String date, String filePath) throws RemoteException, SQLException, NotBoundException {
        RMI_SERWER stub = StworzPoloczenie();
        try{
            stub.dodajPlikUżytkownika(Email, fileName, fileSize, date, filePath);
        }catch (Exception e){
            stub.makeConnectionAgain();
            DodajPlikUżytkownika(fileName, fileSize, date, filePath);
        }
    }

    /**
     * sprawdza logowanie
     * @param login login
     * @param haslo hasło
     * @return boolen
     * @throws RemoteException tag
     * @throws SQLException tag
     */
    public static boolean SprwadzLogowanie(String login, String haslo) throws RemoteException, SQLException {
        boolean result = false;
        RMI_SERWER stub = StworzPoloczenie();
        try {
            result = stub.sprwadzLogowanie(login, haslo);
        }catch (Exception e){
            stub.makeConnectionAgain();
            SprwadzLogowanie(login, haslo);
        }
        if (result){SetEmail(login);}
        return result;
    }

    /**
     * pobiera dane z bazy
     * @return ArrayList<ArrayList<String>> dynamiczna tablica dwuwymiarowa
     * @throws RemoteException tag
     * @throws SQLException tag
     */
    public static ArrayList<ArrayList<String>> PobierzDaneZBazy() throws RemoteException, SQLException {
        ArrayList<ArrayList<String>> result = null;
        RMI_SERWER stub = StworzPoloczenie();
        try{
             result = stub.pobierzDaneZBazy(Email);
        }catch (Exception e){
            stub.makeConnectionAgain();
            PobierzDaneZBazy();
             }
        return result;
    }

    /**
     * sparwdza czy email jest unikalny
     * @param email email użytkonwika
     * @return boolen true -> jesli jest unikalny
     * @throws SQLException tag
     * @throws RemoteException tag
     */
    public static boolean SprawdzCzyEmailJestUnikalny(String email) throws SQLException, RemoteException {
        boolean result = false;
        RMI_SERWER stub = StworzPoloczenie();
        try{
            result = stub.sprawdzCzyEmailJestUnikalny(email);
        }catch (Exception e){
            stub.makeConnectionAgain();
            SprawdzCzyEmailJestUnikalny(email);
        }
        return result;
    }

    /**
     * tworzy połączenie z serwerem RMI
     * @return RMI_SERWER
     */
    public static RMI_SERWER StworzPoloczenie() {
        RMI_SERWER stub = null;
        System.setProperty("java.security.policy","src/main/java/com/bazaDanych/test.policy");
        try {
            Registry reg = LocateRegistry.getRegistry("89.116.228.48", 1099);
            stub = (RMI_SERWER) reg.lookup("RemoteRMIServer");
        }//catch (RemoteException e) {
        catch (Exception e) {
            System.out.println("BRAK POŁĄCZENIA Z INTERNETEM");
        }
        return stub;
    }
}
