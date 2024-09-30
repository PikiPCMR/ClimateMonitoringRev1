package org.example.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RicercaRMI extends Remote {
    List<String[]> ricercaPerDenominazione(String denominazione, String stato) throws RemoteException;
    List<String[]> ricercaPerCoordinate(String coordinate) throws RemoteException;
    List<String[]> getParametriClimatici(String geoname) throws RemoteException;
}

