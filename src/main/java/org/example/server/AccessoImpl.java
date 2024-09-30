package org.example.server;

import org.example.interfaces.AccessoRMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AccessoImpl extends UnicastRemoteObject implements AccessoRMI {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/LaboratorioB";
    private static final String USER = "postgres";
    private static final String PASS = "password";

    protected AccessoImpl() throws RemoteException {
        super(); // Necessario per l'esportazione dell'oggetto
    }

    // non funzionante
    public boolean eseguiLogin(String operator_id, String password) throws RemoteException {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "SELECT * FROM operatoriregistrati WHERE operator_id = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, operator_id);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Se c'è un risultato, il login è riuscito
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eseguiRegistrazione(String nome_cognome, String codice_fiscale, String mail, String operator_id, String password) throws RemoteException {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "INSERT INTO operatoriregistrati (nome_cognome, codice_fiscale, mail, operator_id, password) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome_cognome);
            stmt.setString(2, codice_fiscale);
            stmt.setString(3, mail);
            stmt.setString(4, operator_id);
            stmt.setString(5, password);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

