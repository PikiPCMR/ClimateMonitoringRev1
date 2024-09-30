package org.example.client;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.awt.Graphics;

public class BackgroundPanel extends JPanel {
    private BufferedImage image;
    public BackgroundPanel(String imagePath)  {
        try {
            // Usa getResourceAsStream per caricare l'immagine da una risorsa interna al JAR
            InputStream imageStream = ClientUI.class.getResourceAsStream("/immagine.png");
            if (imageStream != null) {
                image = ImageIO.read(imageStream); // Carica l'immagine dallo stream
            } else {
                System.err.println("Immagine non trovata: " + imagePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this); // Disegna l'immagine su tutto il pannello
        }
    }
}
