package Vue;

import Modele.Joueur;

import java.awt.*;

import static Modele.Constantes.*;

/**
 * Responsable du dessin du personnage principal.
 * Cette vue fournit un rendu minimal centré sur les coordonnées logiques du joueur.
 */
public class VueJoueur {

    /** ---------- [Constructeurs] ---------- **/

    public VueJoueur() {
    }

    /** ---------- [Méthodes Publiques - Rendu] ---------- **/

    /**
     * Dessine le joueur sur le contexte graphique courant.
     * Le rendu est centré sur la position monde du joueur afin de rester cohérent
     * avec les autres entités manipulées par la vue principale.
     *
     * @param g2d - Contexte graphique ayant déjà reçu la transformation caméra
     * @param joueur - Instance du joueur à afficher
     */
    public void dessiner(Graphics g2d, Joueur joueur) {
        double posX = joueur.getX();
        double posY = joueur.getY();

        g2d.setColor(Color.black);

        // Les coordonnées du modèle représentent le centre logique de l'entité, pas son coin supérieur gauche.
        g2d.fillOval((int) posX - J_TAILLE / 2, (int) posY - J_TAILLE / 2, J_TAILLE, J_TAILLE);
    }
}