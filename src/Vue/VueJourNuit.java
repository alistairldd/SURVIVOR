package Vue;

import Modele.CycleJourNuit;
import Modele.Modele;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import Modele.ResourceLoader;

import static Modele.Constantes.*;

/**
 * Composant HUD affichant l'état courant du cycle jour/nuit.
 * Il combine un repère visuel fort (soleil ou lune) et des informations
 * textuelles adaptées à la phase de jeu en cours.
 */
public class VueJourNuit {

    /** ---------- [Constantes] ---------- **/

    public final static int TAILLE_LS = 250;

    /** ---------- [Propriétés] ---------- **/

    private Modele modele;
    private BufferedImage lune;
    private BufferedImage soleil;

    /** ---------- [Constructeurs] ---------- **/

    /**
     * Initialise le composant et charge les ressources graphiques du cycle.
     *
     * @param modele - Modèle de jeu fournissant l'état temporel courant
     */
    public VueJourNuit(Modele modele) {
        this.modele = modele;
        try {
            lune = ResourceLoader.load("/images/LUNE.png", 0);
            soleil = ResourceLoader.load("/images/SOLEIL.png",0);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur : Impossible de charger l'image.");
        }
    }

    /** ---------- [Méthodes Publiques - Rendu] ---------- **/

    /**
     * Dessine le bloc d'information du cycle et retourne la prochaine coordonnée Y disponible.
     * Le contenu affiché varie selon qu'on soit en journée, en nuit active ou en fin de nuit.
     *
     * @param g - Contexte graphique HUD
     * @param yDebut - Position Y de départ du bloc
     * @return coordonnée Y finale après rendu du composant
     */
    public int dessiner(Graphics g, int yDebut) {
        int yCourant = yDebut;

        // L'illustration est centrée pour structurer visuellement le bloc dans la colonne HUD.
        int centreX = (LARGEUR_HUD - TAILLE_LS) / 2;

        CycleJourNuit leCycle = modele.getLeCycleJourNuit();

        if (leCycle.isDay()) {
            g.drawImage(soleil, centreX, yCourant, TAILLE_LS, TAILLE_LS, null);
            g.setColor(Color.BLACK);

            yCourant += TAILLE_LS + 10;
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Temps restant: " + leCycle.getTempsRestant() + "s", centreX + 30, yCourant);
        } else {
            g.drawImage(lune, centreX, yCourant, TAILLE_LS, TAILLE_LS, null);
            g.setColor(Color.WHITE);

            yCourant += TAILLE_LS + 10;

            if (modele.getUpdateJN().getMonstresRestants() == 0) {
                // Une nuit terminée n'affiche plus d'urgence mais une consigne de progression.
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.drawString("Nuit terminée !", centreX + 50, yCourant);

                yCourant += 30;
                g.setFont(new Font("Arial", Font.PLAIN, 13));
                g.drawString("Appuyez sur S pour passer au prochain jour", centreX - 5, yCourant);
            } else {
                // Tant que la nuit est active, le HUD rappelle à la fois la contrainte de temps et l'objectif restant.
                g.setFont(new Font("Arial", Font.BOLD, 14));
                g.drawString("Temps restant avant le Game Over: ", centreX - 5, yCourant);

                yCourant += 20;
                g.drawString(leCycle.getTempsRestant() + " secondes", centreX + 60, yCourant);

                yCourant += 25;
                g.setFont(new Font("Arial", Font.BOLD, 13));
                g.drawString("Nombre de monstres restants à tuer: " + modele.getUpdateJN().getMonstresRestants(), centreX - 5, yCourant);
            }
        }

        // Une marge finale protège le contenu suivant d'un collage visuel dans le HUD scrollable.
        yCourant += 40;
        return yCourant;
    }
}