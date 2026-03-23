package Vue;


import Modele.CycleJourNuit;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static Modele.Constantes.*;

/* Classe de la vue du jour et de la nuit */
/**
 * Composant graphique de l'interface (HUD) affichant l'état d'avancement du cycle temporel.
 * Charge des images locales (Soleil/Lune) et affiche le chronomètre dynamique.
 */
public class VueJourNuit{

    /* Taille de l'image du jour et de la nuit */
    // Constante définissant la largeur et la hauteur de l'icône affichée en bas de l'écran
    public final static int TAILLE_LS = 250;

    // Référence au modèle temporel pour lire l'état actuel (Jour/Nuit) et le temps restant
    private CycleJourNuit leCycle;

    /* Images du jour et de la nuit, elles sont utilisées pour dessiner le jour et la nuit. */
    // Objets permettant de stocker les images chargées depuis le disque dur en mémoire vive
    private BufferedImage lune;
    private BufferedImage soleil;

    // Constructeur de la classe VueJourNuit, il initialise les données du jour et de la nuit.
    public VueJourNuit(CycleJourNuit cycle) {
        // Enregistre la référence au chronomètre
        leCycle = cycle;

        /* Importe les images du jour et de la nuit, elles sont utilisées pour dessiner le jour et la nuit. */
        try {
            // Tente de lire les fichiers d'image dans le dossier spécifié
            lune = ImageIO.read(new File("src/images/LUNE.png"));
            soleil = ImageIO.read(new File("src/images/SOLEIL.png"));

        } catch (IOException e) {
            // Si le chemin est incorrect ou l'image introuvable, capture l'erreur pour éviter que le jeu ne plante
            e.printStackTrace();
            System.out.println("Erreur : Impossible de charger l'image.");
        }
    }

    /**
     * Dessine l'image correspondante au cycle actuel et le texte du compte à rebours.
     * @param g Le contexte graphique du HUD.
     * @param hauteurEcran La hauteur totale du panneau, utilisée pour ancrer l'image tout en bas.
     */
    public void dessiner (Graphics g, int hauteurEcran) {

        // Dessiner le jour et la nuit
        // On interroge le modèle pour savoir dans quelle phase on se trouve
        if (leCycle.isDay()) {
            // Dessine le jour en centrant l'image dans le panneau en bas
            // Calcul X : (Largeur du HUD - Taille image) / 2 pour centrer parfaitement
            // Calcul Y : Hauteur totale - Taille image pour coller au bas
            g.drawImage(soleil, (LARGEUR_HUD - TAILLE_LS) / 2, hauteurEcran - TAILLE_LS, TAILLE_LS, TAILLE_LS, null);
            // Prépare le pinceau en noir pour le texte qui sera affiché par-dessus l'image
            g.setColor(Color.BLACK);
        } else {
            // Dessine la lune exactement à la même place
            g.drawImage(lune, (LARGEUR_HUD - TAILLE_LS) / 2, hauteurEcran - TAILLE_LS, TAILLE_LS, TAILLE_LS, null); // Cercle pour la nuit
            // Prépare le pinceau en blanc pour que le texte ressorte sur le fond sombre de la nuit
            g.setColor(Color.WHITE);
        }

        /* Affiche le temps restant pour la phase actuelle */
        // Augmente la taille de la police pour une meilleure visibilité (Arial Gras, taille 20)
        g.setFont(new Font("Arial", Font.BOLD, 20));
        // Centrer le texte en bas du panneau
        // Interroge getTempsRestant() du modèle et dessine la chaîne de caractères avec un petit décalage pour l'esthétique
        g.drawString("Temps restant: " + leCycle.getTempsRestant() + "s", (LARGEUR_HUD - TAILLE_LS) / 2 + 30, hauteurEcran- TAILLE_LS - 10);
    }
}