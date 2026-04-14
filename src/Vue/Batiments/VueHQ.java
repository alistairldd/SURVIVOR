package Vue.Batiments;

import Modele.Batiments.HQ;
import java.awt.*;
import static Modele.Constantes.*;

/**
 * Classe utilitaire dédiée au rendu du Quartier Général.
 * Gère l'affichage dynamique selon l'état de structure (Neuf/Endommagé).
 */
public class VueHQ {

    /**
     * Procédure de rendu du HQ.
     * @param g2d Le contexte graphique.
     * @param hq L'instance du HQ à dessiner.
     * @param x Coordonnée X (écran).
     * @param y Coordonnée Y (écran).
     * @param minimap Si vrai, dessine un symbole simplifié.
     */
    public static void dessiner(Graphics2D g2d, HQ hq, int x, int y, boolean minimap) {

        if (minimap) {
            // Sur la minimap, on reste sur une forme géométrique nette
            g2d.setColor(Color.WHITE);
            int tailleM = TAILLE_BATIMENT_MINIMAP + 2; // Légèrement plus gros car c'est le HQ
            g2d.fillRect(x - (tailleM / 2), y - (tailleM / 2), tailleM, tailleM);
        } else {
            // Logique d'état visuel pour le sprite
            Image spriteAAfficher;

            if (!hq.isFonctionnel()) {
                // État : Détruit / En panne
                spriteAAfficher = IMAGE_HQ_ENDOMMAGE;
            } else if (hq.getHp() <= (hq.getMaxHp() / 2)) {
                // État : Critique (Moins de 50% HP)
                spriteAAfficher = IMAGE_HQ_ENDOMMAGE;
            } else {
                // État : Opérationnel
                spriteAAfficher = IMAGE_HQ;
            }

            // Rendu de l'image centrée sur la position (x, y)
            if (spriteAAfficher != null) {
                g2d.drawImage(spriteAAfficher, x - (TAILLE_HQ / 2), y - (TAILLE_HQ / 2), null);
            } else {
                // Fallback de sécurité : Carré blanc si l'image est manquante
                g2d.setColor(Color.WHITE);
                g2d.fillRect(x - (TAILLE_HQ / 2), y - (TAILLE_HQ / 2), TAILLE_HQ, TAILLE_HQ);
            }
        }
    }
}