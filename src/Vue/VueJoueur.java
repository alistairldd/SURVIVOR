package Vue;

import Modele.Joueur;

import java.awt.*;
import static Modele.Constantes.*;

/**
 * Responsable du dessin du personnage principal.
 * Traduit les coordonnées absolues du Modèle en une représentation visuelle basique (cercle).
 */
public class VueJoueur {

    // Constructeur de la classe VueJoueur
    public VueJoueur() {
    }

    /**
     * Dessine le joueur sur le contexte graphique.
     * @param g2d Le pinceau 2D (qui a déjà reçu la translation de caméra de la Vue principale).
     * @param joueur L'instance du joueur pour lire sa position exacte.
     */
    // Méthode pour dessiner le joueur sur la carte
    public void dessiner(Graphics g2d, Joueur joueur) {
        // Récupère les vraies coordonnées monde depuis le modèle
        double posX = joueur.getX();
        double posY = joueur.getY();

        // Définit la couleur de base de l'avatar (noir)
        g2d.setColor(Color.black);

        // Dessine un disque plein.
        // On soustrait la moitié de la taille (TAILLE/2) aux positions X et Y pour que
        // les coordonnées (posX, posY) représentent le CENTRE du joueur, et non son coin en haut à gauche.
        g2d.fillOval((int) posX - J_TAILLE/2, (int) posY - J_TAILLE/2 , J_TAILLE, J_TAILLE);
    }

}