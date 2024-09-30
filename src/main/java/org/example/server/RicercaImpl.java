package org.example.server;

import org.example.interfaces.RicercaRMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RicercaImpl extends UnicastRemoteObject implements RicercaRMI {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/LaboratorioB";
    private static final String USER = "postgres";
    private static final String PASS = "password";

    public RicercaImpl() throws RemoteException {
        super();
    }

    @Override
    public List<String[]> ricercaPerDenominazione(String denominazione, String stato) throws RemoteException {
        List<String[]> risultati = new ArrayList<>();

        String sql = "SELECT geoname, ascii, country_name, coordinates " +
                "FROM geonamesandcoordinates " +
                "WHERE ascii = ? AND country_name = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, denominazione);
            stmt.setString(2, stato);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String[] risultato = new String[4];
                risultato[0] = rs.getString("geoname");
                risultato[1] = rs.getString("ascii");
                risultato[2] = rs.getString("country_name");
                risultato[3] = rs.getString("coordinates");

                risultati.add(risultato);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return risultati;
    }

    @Override
    public List<String[]> ricercaPerCoordinate(String coordinate) throws RemoteException {
        List<String[]> risultati = new ArrayList<>();

        String sql = "SELECT geoname, ascii, country_name, coordinates " +
                "FROM geonamesandcoordinates " +
                "WHERE coordinates = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, coordinate);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String[] risultato = new String[4];
                risultato[0] = rs.getString("geoname");
                risultato[1] = rs.getString("ascii");
                risultato[2] = rs.getString("country_name");
                risultato[3] = rs.getString("coordinates");

                risultati.add(risultato);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return risultati;
    }

    @Override
    public List<String[]> getParametriClimatici(String geoname) throws RemoteException {
        List<String[]> risultati = new ArrayList<>();

        String sql = "SELECT vento, umidita, pressione, temperatura, precipitazioni, altitudineghiacciai, massaghiacciai " +
                "FROM parametriclimatici " +
                "WHERE geoname = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Imposta il parametro della query
            stmt.setString(1, geoname);

            // Esegui la query
            ResultSet rs = stmt.executeQuery();

            // Cicla tra i risultati
            while (rs.next()) {
                String[] parametro = new String[7];  // Creiamo un array con 7 elementi per i parametri climatici
                parametro[0] = rs.getString("vento");
                parametro[1] = rs.getString("umidita");
                parametro[2] = rs.getString("pressione");
                parametro[3] = rs.getString("temperatura");
                parametro[4] = rs.getString("precipitazioni");
                parametro[5] = rs.getString("altitudineghiacciai");
                parametro[6] = rs.getString("massaghiacciai");

                risultati.add(parametro);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return risultati;
    }
}


