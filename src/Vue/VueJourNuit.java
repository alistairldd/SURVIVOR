package Vue;

import Modele.CycleJourNuit;
import Modele.Modele;

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
    private Modele modele;
    private BufferedImage lune;
    private BufferedImage soleil;

    public VueJourNuit(Modele modele) {
        this.modele = modele;
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

        CycleJourNuit leCycle = modele.getLeCycleJourNuit();

        if (leCycle.isDay()) {
            g.drawImage(soleil, centreX, yCourant, TAILLE_LS, TAILLE_LS, null);
            g.setColor(Color.BLACK);
            yCourant += TAILLE_LS + 10; // On descend sous l'image pour le texte
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Temps restant: " + leCycle.getTempsRestant() + "s", centreX + 30, yCourant);
        } else {
            g.drawImage(lune, centreX, yCourant, TAILLE_LS, TAILLE_LS, null);
            g.setColor(Color.WHITE);
            yCourant += TAILLE_LS + 10;
            if (modele.getUpdateJN().getMonstresRestants() == 0) {
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.drawString("Nuit terminée !", centreX + 50, yCourant);
                yCourant += 30;
                g.setFont(new Font("Arial", Font.PLAIN, 13));
                g.drawString("Appuyez sur S pour passer au prochain jour", centreX - 5, yCourant);
            } else {
                g.setFont(new Font("Arial", Font.BOLD, 14));
                g.drawString("Temps restant avant le Game Over: ", centreX - 5, yCourant);
                yCourant += 20;
                g.drawString(leCycle.getTempsRestant() + " secondes", centreX + 60, yCourant);
                yCourant += 25;
                g.setFont(new Font("Arial", Font.BOLD, 13));
                g.drawString("Nombre de monstres restants à tuer: " + modele.getUpdateJN().getMonstresRestants(), centreX - 5, yCourant);
            }
        }



        yCourant += 40; // Marge finale pour que le scroll ne coupe pas le bas
        return yCourant;
    }
}