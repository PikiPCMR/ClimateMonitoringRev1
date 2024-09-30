package org.example.client;

import org.example.interfaces.AccessoRMI;
import org.example.interfaces.CentroMonitoraggioRMI;
import org.example.interfaces.ParametriClimaticiRMI;
import org.example.interfaces.RicercaRMI;
import javax.swing.*;
import java.awt.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Objects;

public class ClientUI {
    private static CardLayout cardLayout;
    private static JPanel mainPanel;
    private static String currentUser; // Nome utente corrente
    private static JPanel welcomePanel; // Riferimento al pannello di benvenuto
    private static JLabel welcomeLabel; // Campo di istanza per il JLabel di benvenuto
    private static AccessoRMI accessoRMI;
    private static RicercaRMI ricercaRMI;
    private static CentroMonitoraggioRMI centromonitoraggioRMI;
    private static ParametriClimaticiRMI parametriclimaticiRMI;

    public static void main(String[] args) {
        // Cambia look and feel per uno stile moderno
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            accessoRMI = (AccessoRMI) Naming.lookup("//localhost/AccessoRMI");  // Nome del servizio RMI
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore di connessione RMI", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            ricercaRMI = (RicercaRMI) Naming.lookup("//localhost/RicercaRMI");  // Nome del servizio RMI
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore di connessione RMI", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            parametriclimaticiRMI = (ParametriClimaticiRMI) Naming.lookup("//localhost/ParametriClimaticiRMI");  // Nome del servizio RMI
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore di connessione RMI", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            centromonitoraggioRMI = (CentroMonitoraggioRMI) Naming.lookup("//localhost/CentroMonitoraggioRMI");  // Nome del servizio RMI
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Errore di connessione RMI", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Inizializza il CardLayout e il pannello principale
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Aggiungi le varie schermate
        mainPanel.add(createHomePanel(), "Home");
        mainPanel.add(createCoordinateSearchPanel(), "CoordinateSearch");
        mainPanel.add(createDenominationSearchPanel(), "DenominationSearch");
        mainPanel.add(createLoginPanel(), "Login");
        mainPanel.add(createRegisterPanel(), "Register");

        welcomePanel = createWelcomePanel(); // Inizializza e memorizza il pannello di benvenuto
        mainPanel.add(welcomePanel, "Welcome"); // Aggiungi il Welcome Panel con nome "Welcome"
        mainPanel.add(createInsertCenterPanel(), "CenterPanel"); // Aggiungi il pannello di inserimento centri
        mainPanel.add(createClimateParamsPanel(), "ParamsPanel"); // Aggiungi il pannello di inserimento parametri

        // Crea il frame principale
        JFrame mainFrame = new JFrame("Client RMI");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(500, 700);

        ImageIcon icon = new ImageIcon(Objects.requireNonNull(ClientUI.class.getResource("/icon.png")));  // Sostituisci con il percorso della tua icona
        mainFrame.setIconImage(icon.getImage());

        // Aggiungi il pannello principale al frame
        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);
    }

    // Crea il pannello della schermata principale con sfondo azzurro chiaro
    private static JPanel createHomePanel() {
        BackgroundPanel panel = new BackgroundPanel("/immagine.png");
        panel.setLayout(new BorderLayout());
        panel.setOpaque(false);
    
        // Etichetta di benvenuto
        JLabel welcomeLabel = new JLabel("Benvenuto!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 40));
        welcomeLabel.setForeground(Color.BLACK);
    
        // Pannello per la scritta di benvenuto
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setOpaque(false);
        
        // Aggiungi uno spazio vuoto sopra la scritta di benvenuto
        welcomePanel.add(Box.createVerticalStrut(80), BorderLayout.NORTH); // Spazio sopra la scritta
        welcomePanel.add(welcomeLabel, BorderLayout.CENTER); // Centra la scritta
        
        // Aggiungi il pannello della scritta di benvenuto al pannello principale
        panel.add(welcomePanel, BorderLayout.NORTH);
    
        // Pannello per i pulsanti
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        buttonPanel.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Margini tra i pulsanti
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0; // Per fare in modo che i pulsanti si allarghino
        
        // Creazione e personalizzazione dei pulsanti
        JButton btnCoordinateSearch = createStyledButton("Ricerca per Coordinate");
        JButton btnDenominationSearch = createStyledButton("Ricerca per Denominazione");
        JButton btnLogin = createStyledButton("Login");
        JButton btnRegister = createStyledButton("Registrati");
        
        JButton[] buttons = {btnCoordinateSearch, btnDenominationSearch, btnLogin, btnRegister};
        for (int i = 0; i < buttons.length; i++) {
            JButton button = buttons[i];
            button.setPreferredSize(new Dimension(200, 60)); // Pulsanti leggermente più stretti
            button.setFont(new Font("Arial", Font.BOLD, 24)); // Font più grande per i pulsanti
            
            gbc.gridy = i; // Posiziona i pulsanti uno sotto l'altro
            buttonPanel.add(button, gbc);
        }
        
        panel.add(buttonPanel, BorderLayout.CENTER);
        
        // Aggiungi azioni ai pulsanti
        for (JButton button : buttons) {
            button.addActionListener(e -> {
                String actionCommand = e.getActionCommand();
                switch (actionCommand) {
                    case "Ricerca per Coordinate":
                        cardLayout.show(mainPanel, "CoordinateSearch");
                        break;
                    case "Ricerca per Denominazione":
                        cardLayout.show(mainPanel, "DenominationSearch");
                        break;
                    case "Login":
                        cardLayout.show(mainPanel, "Login");
                        break;
                    case "Registrati":
                        cardLayout.show(mainPanel, "Register");
                        break;
                }
            });
        }
        
        return panel;
    }

    // Crea il pannello per la ricerca per coordinate con sfondo azzurro chiaro
    private static JPanel createCoordinateSearchPanel() {
        BackgroundPanel panel = new BackgroundPanel("/immagine.png"); // Imposta il percorso dell'immagine
        panel.setOpaque(false); // Rendi il pannello trasparente per vedere lo sfondo
        panel.setLayout(new GridBagLayout()); // Usa GridBagLayout per un posizionamento flessibile
    
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Margini ridotti per un layout più compatto
        gbc.fill = GridBagConstraints.HORIZONTAL; // Stretch dei componenti orizzontalmente
        gbc.weightx = 1.0; // Permetti ai componenti di allungarsi orizzontalmente
    
        // Definisci un font comune per consistenza
        Font commonFont = new Font("Arial", Font.PLAIN, 20);
    
        // Crea l'etichetta con dimensioni del font aumentate
        JLabel label = new JLabel("Inserisci le coordinate:", SwingConstants.RIGHT);
        label.setFont(commonFont);
        label.setForeground(Color.BLACK);
    
        // Crea il campo di testo
        JTextField coordField = new JTextField(15);
        coordField.setFont(commonFont); // Aumenta la dimensione del font per il campo di testo
    
        // Crea i pulsanti
        JButton searchButton = createStyledButton("Cerca");
        searchButton.setFont(commonFont); // Aumenta la dimensione del font del pulsante
    
        JButton backButton = createStyledButton("Indietro");
        backButton.setFont(commonFont); // Aumenta la dimensione del font del pulsante
    
        searchButton.addActionListener(e -> {
            String coordText = coordField.getText();
            if (coordText.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Inserisci le coordinate per la ricerca", "Errore", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    // Chiamata al metodo per la ricerca delle coordinate
                    List<String[]> risultati = ricercaRMI.ricercaPerCoordinate(coordText);
                    if (risultati.isEmpty()) {
                        JOptionPane.showMessageDialog(panel, "Nessun risultato trovato per le coordinate: " + coordText, "Informazione", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        StringBuilder resultText = new StringBuilder();
    
                        for (String[] risultato : risultati) {
                            resultText.append(String.format("Geoname: %s, Denominazione: %s, Stato: %s, Coordinate: %s%n",
                                    risultato[0], risultato[1], risultato[2], risultato[3]));
    
                            // Chiamata al metodo getParametriClimatici per il geoname selezionato
                            List<String[]> parametriClimatici = ricercaRMI.getParametriClimatici(risultato[0]);
                            if (!parametriClimatici.isEmpty()) {
                                resultText.append("Parametri climatici:\n");
                                for (String[] parametro : parametriClimatici) {
                                    resultText.append(String.format("Vento: %s, Umidità: %s, Pressione: %s, Temperatura: %s, Precipitazioni: %s, Altitudine Ghiacciai: %s, Massa Ghiacciai: %s%n",
                                            parametro[0], parametro[1], parametro[2], parametro[3], parametro[4], parametro[5], parametro[6]));
                                }
                            } else {
                                resultText.append("Nessun parametro climatico associato.\n");
                            }
                        }
    
                        // Utilizzo di JTextArea e JScrollPane per mostrare i risultati in una finestra ridimensionabile
                        JTextArea textArea = new JTextArea(resultText.toString());
                        textArea.setEditable(false);
                        textArea.setLineWrap(true);
                        textArea.setWrapStyleWord(true);
    
                        // Imposta la dimensione preferita della JTextArea
                        textArea.setPreferredSize(new Dimension(500, 400));
    
                        // Aggiunge la JTextArea in un JScrollPane per abilitare lo scorrimento
                        JScrollPane scrollPane = new JScrollPane(textArea);
    
                        // Mostra la finestra di dialogo con lo JScrollPane
                        JOptionPane.showMessageDialog(panel, scrollPane, "Risultati", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(panel, "Errore durante la ricerca", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Home"));
    
        // Aggiungi le etichette e i campi di testo allineati a sinistra e vicini tra loro
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END; // Allinea le etichette a destra
        panel.add(label, gbc);
    
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START; // Allinea i campi di testo a sinistra, vicino alle etichette
        panel.add(coordField, gbc);
    
        // Pannello inferiore per i pulsanti, centrato
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Centra i pulsanti
        bottomPanel.setOpaque(false); // Rendi il pannello inferiore trasparente
        bottomPanel.add(searchButton);
        bottomPanel.add(backButton);
    
        // Aggiungi il pannello inferiore con i pulsanti
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2; // I pulsanti occupano entrambe le colonne
        gbc.anchor = GridBagConstraints.CENTER; // Centra il pannello dei pulsanti
        panel.add(bottomPanel, gbc);
    
        return panel;
    }

    private static JPanel createDenominationSearchPanel() {
        BackgroundPanel panel = new BackgroundPanel("/immagine.png"); // Imposta il percorso dell'immagine
        panel.setOpaque(false); // Rendi il pannello trasparente per vedere lo sfondo
        panel.setLayout(new GridBagLayout()); // Usa GridBagLayout per un posizionamento flessibile
    
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Margini ridotti per un layout più compatto
        gbc.fill = GridBagConstraints.HORIZONTAL; // Stretch dei componenti orizzontale
        gbc.weightx = 1.0; // Permetti ai componenti di allungarsi orizzontalmente
    
        // Definisci un font comune per consistenza
        Font commonFont = new Font("Arial", Font.PLAIN, 20);
    
        // Crea le etichette con dimensioni del font aumentate
        JLabel labelCity = new JLabel("Inserisci la città:", SwingConstants.RIGHT);
        labelCity.setFont(commonFont);
        labelCity.setForeground(Color.BLACK);
    
        JLabel labelState = new JLabel("Inserisci lo stato:", SwingConstants.RIGHT);
        labelState.setFont(commonFont);
        labelState.setForeground(Color.BLACK);
    
        // Crea i campi di testo
        JTextField cityField = new JTextField(15);
        cityField.setFont(commonFont); // Aumenta la dimensione del font per il campo di testo
    
        JTextField stateField = new JTextField(15);
        stateField.setFont(commonFont); // Aumenta la dimensione del font per il campo di testo
    
        // Crea i pulsanti
        JButton searchButton = createStyledButton("Cerca");
        searchButton.setFont(commonFont); // Aumenta la dimensione del font del pulsante
    
        JButton backButton = createStyledButton("Indietro");
        backButton.setFont(commonFont); // Aumenta la dimensione del font del pulsante
    
        searchButton.addActionListener(e -> {
            String denomText0 = cityField.getText();
            String denomText1 = stateField.getText();
            if (denomText0.isEmpty() && denomText1.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Inserisci la denominazione per la ricerca", "Errore", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    List<String[]> risultati = ricercaRMI.ricercaPerDenominazione(denomText0, denomText1);
                    if (risultati.isEmpty()) {
                        JOptionPane.showMessageDialog(panel, "Nessun risultato trovato per la denominazione: " + denomText0, "Informazione", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        StringBuilder resultText = new StringBuilder();
    
                        for (String[] risultato : risultati) {
                            resultText.append(String.format("Geoname: %s, Ascii: %s, Country Name: %s, Coordinates: %s%n",
                                    risultato[0], risultato[1], risultato[2], risultato[3]));
    
                            // Chiamata al metodo getParametriClimatici per visualizzare i dati climatici associati
                            List<String[]> parametriClimatici = ricercaRMI.getParametriClimatici(risultato[0]); // Passa il geoname al metodo
                            if (parametriClimatici.isEmpty()) {
                                resultText.append("Nessun parametro climatico associato per questo Geoname.\n");
                            } else {
                                resultText.append("Parametri Climatici:\n");
                                for (String[] parametro : parametriClimatici) {
                                    resultText.append(String.format(" Vento: %s, Umidità: %s, Pressione: %s, Temperatura: %s, Precipitazioni: %s, Altitudine Ghiacciai: %s, Massa Ghiacciai: %s%n",
                                            parametro[0], parametro[1], parametro[2], parametro[3], parametro[4], parametro[5], parametro[6]));
                                }
                            }
                        }
    
                        // Utilizzo di JTextArea e JScrollPane per mostrare i risultati in una finestra ridimensionabile
                        JTextArea textArea = new JTextArea(resultText.toString());
                        textArea.setEditable(false);
                        textArea.setLineWrap(true);
                        textArea.setWrapStyleWord(true);
    
                        // Imposta la dimensione preferita della JTextArea
                        textArea.setPreferredSize(new Dimension(500, 400));
    
                        // Aggiunge la JTextArea in un JScrollPane per abilitare lo scorrimento
                        JScrollPane scrollPane = new JScrollPane(textArea);
    
                        // Mostra la finestra di dialogo con lo JScrollPane
                        JOptionPane.showMessageDialog(panel, scrollPane, "Risultati", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(panel, "Errore durante la ricerca", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Home"));
    
        // Aggiungi le etichette e i campi di testo allineati a sinistra e vicini tra loro
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END; // Allinea le etichette a destra
        panel.add(labelCity, gbc);
    
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START; // Allinea i campi di testo a sinistra, vicino alle etichette
        panel.add(cityField, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END; // Allinea le etichette a destra
        panel.add(labelState, gbc);
    
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START; // Allinea i campi di testo a sinistra, vicino alle etichette
        panel.add(stateField, gbc);
    
        // Pannello inferiore per i pulsanti, centrato
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Centra i pulsanti
        bottomPanel.setOpaque(false); // Rendi il pannello inferiore trasparente
        bottomPanel.add(searchButton);
        bottomPanel.add(backButton);
    
        // Aggiungi il pannello inferiore con i pulsanti
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; // I pulsanti occupano entrambe le colonne
        gbc.anchor = GridBagConstraints.CENTER; // Centra il pannello dei pulsanti
        panel.add(bottomPanel, gbc);
    
        return panel;
    }

    // Crea il pannello per il login con sfondo azzurro chiaro
    private static JPanel createLoginPanel() {
        BackgroundPanel panel = new BackgroundPanel("/immagine.png"); // Imposta il percorso dell'immagine
        panel.setOpaque(false); // Rendi il pannello trasparente per vedere lo sfondo
        panel.setLayout(new GridBagLayout()); // Usa GridBagLayout per un posizionamento flessibile
    
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Spaziatura tra i componenti
        gbc.fill = GridBagConstraints.HORIZONTAL; // Stretch dei componenti orizzontalmente
        gbc.weightx = 1.0; // Permetti ai componenti di allungarsi orizzontalmente
    
        // Definisci un font comune per consistenza
        Font commonFont = new Font("Arial", Font.PLAIN, 20);
    
        // Configura e aggiungi l'etichetta per lo username
        JLabel userLabel = new JLabel("Username:", SwingConstants.RIGHT);
        userLabel.setFont(commonFont);
        userLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END; // Allinea a destra
        panel.add(userLabel, gbc);
    
        // Configura e aggiungi il campo di testo per lo username
        JTextField userField = new JTextField(20);
        userField.setFont(commonFont); // Aumenta la dimensione del font per il campo di testo
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START; // Allinea a sinistra
        panel.add(userField, gbc);
    
        // Configura e aggiungi l'etichetta per la password
        JLabel passLabel = new JLabel("Password:", SwingConstants.RIGHT);
        passLabel.setFont(commonFont);
        passLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END; // Allinea a destra
        panel.add(passLabel, gbc);
    
        // Configura e aggiungi il campo di password
        JPasswordField passField = new JPasswordField(20);
        passField.setFont(commonFont); // Aumenta la dimensione del font per il campo di testo
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_START; // Allinea a sinistra
        panel.add(passField, gbc);
    
        // Pannello inferiore per i pulsanti, centrato
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Centra i pulsanti
        bottomPanel.setOpaque(false); // Rendi il pannello inferiore trasparente
    
        // Configura e aggiungi il pulsante di login
        JButton loginButton = createStyledButton("Login");
        loginButton.setFont(commonFont); // Aumenta la dimensione del font del pulsante
        bottomPanel.add(loginButton);
    
        // Configura e aggiungi il pulsante di ritorno
        JButton backButton = createStyledButton("Indietro");
        backButton.setFont(commonFont); // Aumenta la dimensione del font del pulsante
        bottomPanel.add(backButton);
    
        // Aggiungi i pulsanti al pannello inferiore
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; // I pulsanti occupano entrambe le colonne
        gbc.anchor = GridBagConstraints.CENTER; // Centra il pannello dei pulsanti
        panel.add(bottomPanel, gbc);
    
        // Aggiungi azioni ai pulsanti
        loginButton.addActionListener(e -> {
            String operator_id = userField.getText();
            String password = new String(passField.getPassword());
    
            if (operator_id.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Inserisci username e password", "Errore", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    AccessoRMI accesso = (AccessoRMI) Naming.lookup("//localhost/AccessoRMI");
                    boolean success = accesso.eseguiLogin(operator_id, password);
                    if (success) {
                        currentUser = operator_id; // Imposta il nome utente corrente
                        cardLayout.show(mainPanel, "Welcome"); // Passa alla schermata di benvenuto
                        updateWelcomePanel(); // Aggiorna il pannello di benvenuto
                    } else {
                        JOptionPane.showMessageDialog(panel, "Username o password errati", "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(panel, "Errore nella connessione al server", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Home"));
    
        return panel;
    }
    
    private static JPanel createRegisterPanel() {
        BackgroundPanel panel = new BackgroundPanel("/immagine.png"); // Imposta il percorso dell'immagine
        panel.setOpaque(false); // Rendi il pannello trasparente per vedere lo sfondo
        panel.setLayout(new GridBagLayout()); // Usa GridBagLayout per un posizionamento flessibile
    
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Riduci la spaziatura tra i componenti
        gbc.fill = GridBagConstraints.HORIZONTAL; // Stretch dei componenti orizzontalmente
        gbc.weightx = 1.0; // Permetti ai componenti di allungarsi orizzontalmente
    
        // Definisci un font comune per consistenza
        Font commonFont = new Font("Arial", Font.PLAIN, 20);
    
        // Configura e aggiungi l'etichetta per il nome e cognome
        JLabel userLabel = new JLabel("Nome e cognome:");
        userLabel.setFont(commonFont);
        userLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END; // Allinea a destra
        gbc.weightx = 0.0; // Non permettere al componente di allungarsi orizzontalmente
        panel.add(userLabel, gbc);
    
        // Configura e aggiungi il campo di testo per il nome e cognome
        JTextField userField = new JTextField(20);
        userField.setFont(commonFont); // Aumenta la dimensione del font per il campo di testo
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START; // Allinea a sinistra
        gbc.weightx = 1.0; // Permetti al componente di allungarsi orizzontalmente
        panel.add(userField, gbc);
    
        // Configura e aggiungi l'etichetta per il codice fiscale
        JLabel cfLabel = new JLabel("Codice fiscale:");
        cfLabel.setFont(commonFont);
        cfLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END; // Allinea a destra
        gbc.weightx = 0.0; // Non permettere al componente di allungarsi orizzontalmente
        panel.add(cfLabel, gbc);
    
        // Configura e aggiungi il campo di testo per il codice fiscale
        JTextField cfField = new JTextField(20);
        cfField.setFont(commonFont); // Aumenta la dimensione del font per il campo di testo
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_START; // Allinea a sinistra
        gbc.weightx = 1.0; // Permetti al componente di allungarsi orizzontalmente
        panel.add(cfField, gbc);
    
        // Configura e aggiungi l'etichetta per la mail
        JLabel emailLabel = new JLabel("Mail:");
        emailLabel.setFont(commonFont);
        emailLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END; // Allinea a destra
        gbc.weightx = 0.0; // Non permettere al componente di allungarsi orizzontalmente
        panel.add(emailLabel, gbc);
    
        // Configura e aggiungi il campo di testo per la mail
        JTextField emailField = new JTextField(20);
        emailField.setFont(commonFont); // Aumenta la dimensione del font per il campo di testo
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_START; // Allinea a sinistra
        gbc.weightx = 1.0; // Permetti al componente di allungarsi orizzontalmente
        panel.add(emailField, gbc);
    
        // Configura e aggiungi l'etichetta per il nome utente
        JLabel usernameLabel = new JLabel("Nome utente:");
        usernameLabel.setFont(commonFont);
        usernameLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_END; // Allinea a destra
        gbc.weightx = 0.0; // Non permettere al componente di allungarsi orizzontalmente
        panel.add(usernameLabel, gbc);
    
        // Configura e aggiungi il campo di testo per il nome utente
        JTextField usernameField = new JTextField(20);
        usernameField.setFont(commonFont); // Aumenta la dimensione del font per il campo di testo
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_START; // Allinea a sinistra
        gbc.weightx = 1.0; // Permetti al componente di allungarsi orizzontalmente
        panel.add(usernameField, gbc);
    
        // Configura e aggiungi l'etichetta per la password
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(commonFont);
        passwordLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.LINE_END; // Allinea a destra
        gbc.weightx = 0.0; // Non permettere al componente di allungarsi orizzontalmente
        panel.add(passwordLabel, gbc);
    
        // Configura e aggiungi il campo di password
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(commonFont); // Aumenta la dimensione del font per il campo di testo
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.LINE_START; // Allinea a sinistra
        gbc.weightx = 1.0; // Permetti al componente di allungarsi orizzontalmente
        panel.add(passwordField, gbc);
    
        // Pannello inferiore per i pulsanti, centrato
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Centra i pulsanti
        bottomPanel.setOpaque(false); // Rendi il pannello inferiore trasparente
    
        // Configura e aggiungi il pulsante di registrazione
        JButton registerButton = createStyledButton("Registrati");
        registerButton.setFont(commonFont); // Aumenta la dimensione del font del pulsante
        bottomPanel.add(registerButton);
    
        // Configura e aggiungi il pulsante di ritorno
        JButton backButton = createStyledButton("Indietro");
        backButton.setFont(commonFont); // Aumenta la dimensione del font del pulsante
        bottomPanel.add(backButton);
    
        // Aggiungi i pulsanti al pannello inferiore
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2; // I pulsanti occupano entrambe le colonne
        gbc.anchor = GridBagConstraints.CENTER; // Centra il pannello dei pulsanti
        panel.add(bottomPanel, gbc);
    
        // Aggiungi azioni ai pulsanti
        registerButton.addActionListener(e -> {
            String nomeCognome = userField.getText().trim();
            String codiceFiscale = cfField.getText().trim();
            String email = emailField.getText().trim();
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
    
            if (nomeCognome.isEmpty() || codiceFiscale.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Compila tutti i campi", "Errore", JOptionPane.ERROR_MESSAGE);
            } else if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(panel, "Email non valida", "Errore", JOptionPane.ERROR_MESSAGE);
            } else if (!isValidPassword(password)) {
                JOptionPane.showMessageDialog(panel, "La password deve essere lunga almeno 8 caratteri e contenere una lettera maiuscola, un numero e un carattere speciale", "Errore", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    // Chiamata al metodo RMI per eseguire la registrazione
                    boolean registerSuccess = accessoRMI.eseguiRegistrazione(nomeCognome, codiceFiscale, email, username, password);
    
                    // Aggiunta di messaggi di log per debug
                    System.out.println("Risultato della registrazione: " + registerSuccess);
    
                    if (registerSuccess) {
                        // Messaggio di successo
                        JOptionPane.showMessageDialog(panel, "Registrazione effettuata per: " + username, "Successo", JOptionPane.INFORMATION_MESSAGE);
    
                        // Torna alla schermata precedente
                        cardLayout.show(mainPanel, "Home");
                    } else {
                        // Se il metodo restituisce false, mostra un messaggio di errore
                        JOptionPane.showMessageDialog(panel, "Errore durante la registrazione", "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(panel, "Errore di comunicazione con il server", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Home"));
    
        return panel;
    }

    private static JPanel createWelcomePanel() {
        BackgroundPanel panel = new BackgroundPanel("/immagine.png");
        panel.setLayout(new BorderLayout());
        panel.setOpaque(false);
    
        // Etichetta di benvenuto
        welcomeLabel = new JLabel("<html>Benvenuto nel Sistema</html>", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 40));
        welcomeLabel.setForeground(Color.BLACK);
        welcomeLabel.setPreferredSize(new Dimension(600, 100)); // Imposta una dimensione preferita
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
    
        // Pannello per la scritta di benvenuto
        welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setOpaque(false);
        welcomePanel.add(Box.createVerticalStrut(80), BorderLayout.NORTH); // Spazio sopra la scritta
        welcomePanel.add(welcomeLabel, BorderLayout.CENTER); // Centra la scritta
    
        // Pannello per i pulsanti
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        buttonPanel.setOpaque(false);
    
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Margini tra i pulsanti
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0; // Per fare in modo che i pulsanti si allarghino
    
        JButton btnRegisterCenter = createStyledButton("Registra Centro Aree");
        JButton btnViewCenters = createStyledButton("Visualizza Centri di Monitoraggio");
        JButton btnInsertClimateParams = createStyledButton("Inserisci Parametri Climatici");
        JButton btnBack = createStyledButton("Indietro");
    
        // Aggiungi azioni ai pulsanti
        btnRegisterCenter.addActionListener(e -> cardLayout.show(mainPanel, "CenterPanel"));
        btnViewCenters.addActionListener(e -> {
            try {
                List<String[]> risultati = centromonitoraggioRMI.visualizzaCentriMonitoraggio(currentUser);
                if (risultati.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Nessun Centro di monitoraggio registrato.", "Informazione", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    StringBuilder resultText = new StringBuilder("Risultati:\n");
                    for (String[] risultato : risultati) {
                        resultText.append(String.format("Nome Centro: %s, Indirizzo: %s, Geoname: %s%n",
                                risultato[0], risultato[1], risultato[2]));
                    }
    
                    // Utilizzo di JTextArea e JScrollPane per mostrare i risultati in una finestra ridimensionabile
                    JTextArea textArea = new JTextArea(resultText.toString());
                    textArea.setEditable(false);
                    textArea.setLineWrap(true);
                    textArea.setWrapStyleWord(true);
    
                    // Imposta la dimensione preferita della JTextArea
                    textArea.setPreferredSize(new Dimension(500, 400));
    
                    // Aggiunge la JTextArea in un JScrollPane per abilitare lo scorrimento
                    JScrollPane scrollPane = new JScrollPane(textArea);
    
                    // Mostra la finestra di dialogo con lo JScrollPane
                    JOptionPane.showMessageDialog(panel, scrollPane, "Centri di Monitoraggio", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(panel, "Errore durante la visualizzazione dei centri di monitoraggio", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });
        btnInsertClimateParams.addActionListener(e -> cardLayout.show(mainPanel, "ParamsPanel"));
        btnBack.addActionListener(e -> cardLayout.show(mainPanel, "Home")); // Ritorna alla schermata di login
    
        // Aggiungi i pulsanti al pannello dei pulsanti
        JButton[] buttons = {btnRegisterCenter, btnViewCenters, btnInsertClimateParams, btnBack};
        for (int i = 0; i < buttons.length; i++) {
            JButton button = buttons[i];
            button.setPreferredSize(new Dimension(200, 60)); // Pulsanti leggermente più stretti
            button.setFont(new Font("Arial", Font.BOLD, 24)); // Font più grande per i pulsanti
    
            gbc.gridy = i; // Posiziona i pulsanti uno sotto l'altro
            buttonPanel.add(button, gbc);
        }
    
        panel.add(welcomePanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
    
        return panel;
    }

    private static JPanel createClimateParamsPanel() {
        BackgroundPanel panel = new BackgroundPanel("/immagine.png"); // Imposta il percorso dell'immagine
        panel.setOpaque(false); // Rendi il pannello trasparente per vedere lo sfondo
        panel.setLayout(new GridBagLayout()); // Usa GridBagLayout per un posizionamento flessibile
    
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Spaziatura tra i componenti
        gbc.fill = GridBagConstraints.HORIZONTAL; // Stretch dei componenti orizzontalmente
        gbc.weightx = 1.0; // Permetti ai componenti di allungarsi orizzontalmente
    
        // Definisci un font comune per consistenza
        Font commonFont = new Font("Arial", Font.PLAIN, 16);
    
        // Campo 1: Nome Centro
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END; // Allinea a destra
        JLabel nomecentroLabel = new JLabel("Nome Centro:");
        nomecentroLabel.setFont(commonFont);
        nomecentroLabel.setForeground(Color.BLACK);
        panel.add(nomecentroLabel, gbc);
    
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START; // Allinea a sinistra
        JTextField nomecentroField = new JTextField(20);
        nomecentroField.setFont(commonFont);
        panel.add(nomecentroField, gbc);
    
        // Campo 2: Geoname
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel geonameLabel = new JLabel("Geoname:");
        geonameLabel.setFont(commonFont);
        geonameLabel.setForeground(Color.BLACK);
        panel.add(geonameLabel, gbc);
    
        gbc.gridx = 1;
        JTextField geonameField = new JTextField(20);
        geonameField.setFont(commonFont);
        panel.add(geonameField, gbc);
    
        // Campo 3: Vento
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel ventoLabel = new JLabel("Vento:");
        ventoLabel.setFont(commonFont);
        ventoLabel.setForeground(Color.BLACK);
        panel.add(ventoLabel, gbc);
    
        gbc.gridx = 1;
        JTextField ventoField = new JTextField(20);
        ventoField.setFont(commonFont);
        panel.add(ventoField, gbc);
    
        // Campo 4: Umidità
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel umiditaLabel = new JLabel("Umidità:");
        umiditaLabel.setFont(commonFont);
        umiditaLabel.setForeground(Color.BLACK);
        panel.add(umiditaLabel, gbc);
    
        gbc.gridx = 1;
        JTextField umiditaField = new JTextField(20);
        umiditaField.setFont(commonFont);
        panel.add(umiditaField, gbc);
    
        // Campo 5: Pressione
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel pressioneLabel = new JLabel("Pressione:");
        pressioneLabel.setFont(commonFont);
        pressioneLabel.setForeground(Color.BLACK);
        panel.add(pressioneLabel, gbc);
    
        gbc.gridx = 1;
        JTextField pressioneField = new JTextField(20);
        pressioneField.setFont(commonFont);
        panel.add(pressioneField, gbc);
    
        // Campo 6: Temperatura
        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel temperaturaLabel = new JLabel("Temperatura:");
        temperaturaLabel.setFont(commonFont);
        temperaturaLabel.setForeground(Color.BLACK);
        panel.add(temperaturaLabel, gbc);
    
        gbc.gridx = 1;
        JTextField temperaturaField = new JTextField(20);
        temperaturaField.setFont(commonFont);
        panel.add(temperaturaField, gbc);
    
        // Campo 7: Precipitazioni
        gbc.gridx = 0;
        gbc.gridy = 6;
        JLabel precipitazioneLabel = new JLabel("Precipitazioni:");
        precipitazioneLabel.setFont(commonFont);
        precipitazioneLabel.setForeground(Color.BLACK);
        panel.add(precipitazioneLabel, gbc);
    
        gbc.gridx = 1;
        JTextField precipitazioniField = new JTextField(20);
        precipitazioniField.setFont(commonFont);
        panel.add(precipitazioniField, gbc);
    
        // Campo 8: Altitudine Ghiacciai
        gbc.gridx = 0;
        gbc.gridy = 7;
        JLabel altitudineghiacciaiLabel = new JLabel("Altitudine Ghiacciai:");
        altitudineghiacciaiLabel.setFont(commonFont);
        altitudineghiacciaiLabel.setForeground(Color.BLACK);
        panel.add(altitudineghiacciaiLabel, gbc);
    
        gbc.gridx = 1;
        JTextField altitudineghiacciaiField = new JTextField(20);
        altitudineghiacciaiField.setFont(commonFont);
        panel.add(altitudineghiacciaiField, gbc);
    
        // Campo 9: Massa Ghiacciai
        gbc.gridx = 0;
        gbc.gridy = 8;
        JLabel massaghiacciaiLabel = new JLabel("Massa Ghiacciai:");
        massaghiacciaiLabel.setFont(commonFont);
        massaghiacciaiLabel.setForeground(Color.BLACK);
        panel.add(massaghiacciaiLabel, gbc);
    
        gbc.gridx = 1;
        JTextField massaghiacciaiField = new JTextField(20);
        massaghiacciaiField.setFont(commonFont);
        panel.add(massaghiacciaiField, gbc);
    
        // Pulsante Inserisci Parametri
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2; // I pulsanti occupano entrambe le colonne
        gbc.anchor = GridBagConstraints.CENTER; // Centra il pannello dei pulsanti
        JButton insparamButton = createStyledButton("Inserisci Parametri");
        insparamButton.setFont(commonFont);
        panel.add(insparamButton, gbc);
    
        // Pulsante Indietro
        gbc.gridy = 10;
        JButton backButton = createStyledButton("Indietro");
        backButton.setFont(commonFont);
        panel.add(backButton, gbc);
    
        // Azioni per i pulsanti
        insparamButton.addActionListener(e -> {
            try {
                // Ottieni i dati dai campi
                String nomecentro = nomecentroField.getText().trim();
                String geoname = geonameField.getText().trim();
                String vento = ventoField.getText().trim();
                String umidita = umiditaField.getText().trim();
                String pressione = pressioneField.getText().trim();
                String temperatura = temperaturaField.getText().trim();
                String precipitazioni = precipitazioniField.getText().trim();
                String altitudine = altitudineghiacciaiField.getText().trim();
                String massa = massaghiacciaiField.getText().trim();
    
                // Verifica se tutti i campi sono compilati
                if (nomecentro.isEmpty() || geoname.isEmpty() || vento.isEmpty() || umidita.isEmpty() || pressione.isEmpty() || temperatura.isEmpty() || precipitazioni.isEmpty() || altitudine.isEmpty() || massa.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Compila tutti i campi", "Errore", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Inserisci i parametri climatici
                    boolean result = parametriclimaticiRMI.inserisciParametriClimatici(nomecentro, geoname, vento, umidita, pressione, temperatura, precipitazioni, altitudine, massa);
                    if (result) {
                        JOptionPane.showMessageDialog(panel, "Parametri inseriti con successo", "Successo", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(panel, "Errore durante l'inserimento dei parametri", "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(panel, "Errore di comunicazione con il server", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });
    
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Welcome"));
    
        return panel;
    }
    
    private static JPanel createInsertCenterPanel() {
        // Crea il pannello con uno sfondo
        BackgroundPanel panel = new BackgroundPanel("/immagine.png"); // Imposta il percorso dell'immagine
        panel.setOpaque(false); // Rendi il pannello trasparente per vedere lo sfondo
        panel.setLayout(new GridBagLayout()); // Usa GridBagLayout per un posizionamento flessibile
    
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
    
        // Definisci un font comune per consistenza
        Font commonFont = new Font("Arial", Font.PLAIN, 16);
    
        // Nome centro
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END; // Allinea a destra
        JLabel nameLabel = new JLabel("Nome Centro:");
        nameLabel.setFont(commonFont);
        nameLabel.setForeground(Color.BLACK);
        panel.add(nameLabel, gbc);
    
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START; // Allinea a sinistra
        JTextField nameField = new JTextField(20);
        nameField.setFont(commonFont);
        panel.add(nameField, gbc);
    
        // Indirizzo
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel addressLabel = new JLabel("Indirizzo:");
        addressLabel.setFont(commonFont);
        addressLabel.setForeground(Color.BLACK);
        panel.add(addressLabel, gbc);
    
        gbc.gridx = 1;
        JTextField addressField = new JTextField(20);
        addressField.setFont(commonFont);
        panel.add(addressField, gbc);
    
        // Pulsante di inserimento
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // I pulsanti occupano entrambe le colonne
        gbc.anchor = GridBagConstraints.CENTER; // Centra il pannello dei pulsanti
        JButton insertButton = createStyledButton("Inserisci Centro");
        insertButton.setFont(commonFont);
        panel.add(insertButton, gbc);
    
        // Pulsante di ritorno
        gbc.gridy = 4;
        JButton backButton = createStyledButton("Indietro");
        backButton.setFont(commonFont);
        panel.add(backButton, gbc);
    
        // Azioni per i pulsanti
        insertButton.addActionListener(e -> {
            String nomeCentro = nameField.getText().trim();
            String indirizzo = addressField.getText().trim();
    
            if (nomeCentro.isEmpty() || indirizzo.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Compila tutti i campi", "Errore", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    boolean success = centromonitoraggioRMI.registraCentroAree(nomeCentro, indirizzo, currentUser);
                    if (success) {
                        JOptionPane.showMessageDialog(panel, "Centro inserito con successo", "Successo", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(panel, "Errore durante l'inserimento del centro", "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(panel, "Errore di comunicazione con il server", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Welcome"));
    
        return panel;
    }

    // Metodo per validare l'email
    private static boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    // Metodo per validare la password
    private static boolean isValidPassword(String password) {
        return password.length() >= 8 &&
                password.matches(".*[A-Z].*") && // Contiene almeno una lettera maiuscola
                password.matches(".*\\d.*") &&   // Contiene almeno un numero
                password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?`~].*"); // Contiene almeno un carattere speciale
    }

    // Metodo per aggiornare il testo del benvenuto
    private static void updateWelcomePanel() {
        SwingUtilities.invokeLater(() -> {
            if (welcomeLabel != null) {
                String message = "<html>Ciao " + (currentUser != null ? currentUser : "Utente") + ",<br>cosa vuoi fare oggi?</html>";
                welcomeLabel.setText(message);
                welcomePanel.revalidate(); // Rende il pannello visibile con le modifiche
                welcomePanel.repaint(); // Rende visibili le modifiche apportate al pannello
            } else {
                System.out.println("welcomeLabel is null."); // Debug
            }
        });
    }
   
    // Metodo per creare un pulsante con uno stile personalizzato
    private static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        
        button.setForeground(Color.BLACK); // Colore del testo nero
        button.setFont(new Font("Arial", Font.BOLD, 20)); // Font del pulsante
        button.setFocusPainted(false); // Rimuove il contorno quando cliccato
  

        return button;
    }
}