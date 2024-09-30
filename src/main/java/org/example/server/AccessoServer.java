package org.example.server;

import org.example.interfaces.AccessoRMI;
import org.example.interfaces.CentroMonitoraggioRMI;
import org.example.interfaces.ParametriClimaticiRMI;
import org.example.interfaces.RicercaRMI;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class AccessoServer {
    public static void main(String[] args) {
        try {
            // Crea e avvia il registro RMI sulla porta 1099 (default)
            LocateRegistry.createRegistry(1099);

            // Crea e registra l'oggetto remoto AccessoRMI
            AccessoRMI accesso = new AccessoImpl();
            Naming.rebind("rmi://localhost/AccessoRMI", accesso);

            // Crea e registra l'oggetto remoto RicercaRMI
            RicercaRMI ricerca = new RicercaImpl();
            Naming.rebind("rmi://localhost/RicercaRMI", ricerca);

            // Crea e registra l'oggetto remoto CentroMonitoraggioRMI
            CentroMonitoraggioRMI centroMonitoraggio = new CentroMonitoraggioImpl();
            Naming.rebind("rmi://localhost/CentroMonitoraggioRMI", centroMonitoraggio);

            ParametriClimaticiRMI parametriClimatici = new ParametriClimaticiImpl();
            Naming.rebind("rmi://localhost/ParametriClimaticiRMI", parametriClimatici);

            System.out.println("Server RMI pronto.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


