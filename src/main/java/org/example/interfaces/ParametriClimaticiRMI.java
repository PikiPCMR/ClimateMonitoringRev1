package org.example.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface ParametriClimaticiRMI extends Remote {
    boolean inserisciParametriClimatici(String nomecentro, String geoname, String vento, String umidita, String pressione, String temperatura, String precipitazioni, String altitudineGhiacciai, String massaGhiacciai) throws RemoteException;
}
