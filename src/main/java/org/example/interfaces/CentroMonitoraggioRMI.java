package org.example.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface CentroMonitoraggioRMI extends Remote {
    // Metodo per registrare un centro di monitoraggio
    public boolean registraCentroAree(String nomecentro, String indirizzo, String operator_id) throws RemoteException;

    // Metodo per visualizzare i centri di monitoraggio registrati per un determinato operatore
    List<String[]> visualizzaCentriMonitoraggio(String operatore) throws RemoteException;

}

