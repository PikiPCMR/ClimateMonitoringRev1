package org.example.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface AccessoRMI extends Remote {
    boolean eseguiLogin(String operator_id, String password) throws RemoteException;
    boolean eseguiRegistrazione(String nome_cognome, String codice_fiscale, String mail, String operator_id, String password) throws RemoteException;
}
