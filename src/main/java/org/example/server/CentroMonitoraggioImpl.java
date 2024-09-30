package org.example.server;

import org.example.interfaces.CentroMonitoraggioRMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CentroMonitoraggioImpl extends UnicastRemoteObject implements CentroMonitoraggioRMI {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/LaboratorioB";
    private static final String USER = "postgres";
    private static final String PASS = "password";

    public CentroMonitoraggioImpl() throws RemoteException {
        super();
    }

    @Override
    public boolean registraCentroAree(String nomecentro, String indirizzo, String operator_id) throws RemoteException {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "INSERT INTO centrimonitoraggio (nomecentro, indirizzo, operator_id) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, nomecentro);
                stmt.setString(2, indirizzo);
                stmt.setString(3, operator_id);
                stmt.executeUpdate();
                return true; // Return true if the insertion is successful
            }
        } catch (Exception e) {
            throw new RemoteException("Errore durante la registrazione del centro di monitoraggio.", e);
        }
    }
    

    @Override
    public List<String[]> visualizzaCentriMonitoraggio(String operator_id) throws RemoteException {
        List<String[]> risultati = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "SELECT c.nomecentro, c.indirizzo, p.geoname " +
                    "FROM centrimonitoraggio c " +
                    "LEFT JOIN parametriclimatici p ON c.nomecentro = p.nomecentro " +
                    "WHERE c.operator_id = ?";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, operator_id);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String[] centro = new String[3];
                        centro[0] = rs.getString("nomecentro");
                        centro[1] = rs.getString("indirizzo");
                        centro[2] = rs.getString("geoname");
                        risultati.add(centro);
                    }
                }
            }
        } catch (Exception e) {
            throw new RemoteException("Errore durante la visualizzazione dei centri di monitoraggio.", e);
        }

        return risultati;
    }
}

