package org.example.server;

import org.example.interfaces.ParametriClimaticiRMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ParametriClimaticiImpl extends UnicastRemoteObject implements ParametriClimaticiRMI {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/LaboratorioB";
    private static final String USER = "postgres";
    private static final String PASS = "password";

    public ParametriClimaticiImpl() throws RemoteException {
        super();
    }

    public boolean inserisciParametriClimatici(String nomecentro, String geoname, String vento, String umidita, String pressione, String temperatura, String precipitazioni, String altitudineghiacciai, String massaghiacciai) throws RemoteException {
        System.out.println("Parametri climatici: nomecentro=" + nomecentro + ", geoname=" + geoname + ", vento=" + vento + ", umidita=" + umidita + ", pressione=" + pressione + ", temperatura=" + temperatura + ", precipitazioni=" + precipitazioni + ", altitudineghiacciai=" + altitudineghiacciai + ", massaghiacciai=" + massaghiacciai);

        // Verifica che nomecentro esista nella tabella centrimonitoraggio
        if (!esisteNomeCentro(nomecentro)) {
            System.err.println("Errore: Nome Centro non esistente.");
            return false;
        }

        // Verifica che geoname esista nella tabella geonamesandcoordinates
        if (!esisteGeoname(geoname)) {
            System.err.println("Errore: Geoname non esistente.");
            return false;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO parametriclimatici (nomecentro, geoname, vento, umidita, pressione, temperatura, precipitazioni, altitudineghiacciai, massaghiacciai) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

            insertStmt.setString(1, nomecentro);
            insertStmt.setString(2, geoname);
            insertStmt.setString(3, vento);
            insertStmt.setString(4, umidita);
            insertStmt.setString(5, pressione);
            insertStmt.setString(6, temperatura);
            insertStmt.setString(7, precipitazioni);
            insertStmt.setString(8, altitudineghiacciai);
            insertStmt.setString(9, massaghiacciai);

            int rowsInserted = insertStmt.executeUpdate();
            System.out.println("Righe inserite: " + rowsInserted);
            return rowsInserted > 0;

        } catch (Exception e) {
            System.err.println("Errore: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private boolean esisteNomeCentro(String nomecentro) {
        String query = "SELECT COUNT(*) FROM centrimonitoraggio WHERE nomecentro = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nomecentro);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Errore nella verifica di nomecentro: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    private boolean esisteGeoname(String geoname) {
        String query = "SELECT COUNT(*) FROM geonamesandcoordinates WHERE geoname = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, geoname);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Errore nella verifica di geoname: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }





   
}

