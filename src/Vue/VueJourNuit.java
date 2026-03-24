package Vue;

import Modele.CycleJourNuit;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static Modele.Constantes.*;

/**
 * Composant graphique de l'interface (HUD) affichant l'état d'avancement du cycle temporel.
 * Charge des images locales (Soleil/Lune) et affiche le chronomètre dynamique.
 */
public class VueJourNuit{

    public final static int TAILLE_LS = 250;
    private CycleJourNuit leCycle;
    private BufferedImage lune;
    private BufferedImage soleil;

    public VueJourNuit(CycleJourNuit cycle) {
        leCycle = cycle;
        try {
            lune = ImageIO.read(new File("src/images/LUNE.png"));
            soleil = ImageIO.read(new File("src/images/SOLEIL.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur : Impossible de charger l'image.");
        }
    }

    /**
     * Dessine l'image correspondante au cycle actuel et le texte du compte à rebours.
     * @return La coordonnée Y finale.
     */
    public int dessiner (Graphics g, int yDebut) {
        int yCourant = yDebut;
        // On centre l'image horizontalement
        int centreX = (LARGEUR_HUD - TAILLE_LS) / 2;

        if (leCycle.isDay()) {
            g.drawImage(soleil, centreX, yCourant, TAILLE_LS, TAILLE_LS, null);
            g.setColor(Color.BLACK);
        } else {
            g.drawImage(lune, centreX, yCourant, TAILLE_LS, TAILLE_LS, null);
            g.setColor(Color.WHITE);
        }

        yCourant += TAILLE_LS + 30; // On descend sous l'image pour le texte

        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Temps restant: " + leCycle.getTempsRestant() + "s", centreX + 30, yCourant);

        yCourant += 40; // Marge finale pour que le scroll ne coupe pas le bas
        return yCourant;
    }
}