package Vue;


import Modele.CycleJourNuit;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/* Classe de la vue du jour et de la nuit */
public class VueJourNuit{

    /* Taille de l'image du jour et de la nuit */
    public final static int TAILLE_LS = 250;

    private CycleJourNuit leCycle;

    /* Images du jour et de la nuit, elles sont utilisées pour dessiner le jour et la nuit. */
    private BufferedImage lune;
    private BufferedImage soleil;

    // Constructeur de la classe VueJourNuit, il initialise les données du jour et de la nuit.
    public VueJourNuit(CycleJourNuit cycle) {
        leCycle = cycle;

        /* Importe les images du jour et de la nuit, elles sont utilisées pour dessiner le jour et la nuit. */
        try {
            lune = ImageIO.read(new File("src/images/LUNE.png"));
            soleil = ImageIO.read(new File("src/images/SOLEIL.png"));

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur : Impossible de charger l'image.");
        }
    }

    public void dessiner (Graphics g) {

        // Dessiner le jour et la nuit
        if (leCycle.isDay()) {
            // Dessine le jour en centrant l'image dans le panneau en bas
            g.drawImage(soleil, (VueHUD.LARGEUR - TAILLE_LS) / 2, VueHUD.HAUTEUR - TAILLE_LS, TAILLE_LS, TAILLE_LS, null);
        } else {
            g.drawImage(lune, (VueHUD.LARGEUR - TAILLE_LS) / 2, VueHUD.HAUTEUR - TAILLE_LS, TAILLE_LS, TAILLE_LS, null); // Cercle pour la nuit
        }

        /* Affiche le temps restant pour la phase actuelle */
        g.setColor(Color.BLACK);
        // Augmente la taille de la police pour une meilleure visibilité
        g.setFont(new Font("Arial", Font.BOLD, 20));
        // Centrer le texte en bas du panneau
        g.drawString("Temps restant: " + leCycle.getTempsRestant() + "s", (VueHUD.LARGEUR - TAILLE_LS) / 2 + 30, VueHUD.HAUTEUR - TAILLE_LS - 10);
    }
}
