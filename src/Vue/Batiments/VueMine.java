package Vue.Batiments;

import Modele.Batiments.Mine;
import java.awt.*;
import static Modele.Constantes.*;

/**
 * Classe utilitaire dédiée au rendu de la Mine.
 * Gère l'affichage du sprite (Neuf/Endommagé) et l'overlay textuel des ressources stockées.
 */
public class VueMine {

    /**
     * Procédure de rendu de la Mine.
     * @param g2d Le contexte graphique.
     * @param mine L'instance de la mine à dessiner.
     * @param x Coordonnée X (écran).
     * @param y Coordonnée Y (écran).
     * @param minimap Si vrai, dessine un symbole simplifié.
     */
    public static void dessiner(Graphics2D g2d, Mine mine, int x, int y, boolean minimap) {

        if (minimap) {
            // Rendu Minimap : Un petit carré marron/orange foncé
            g2d.setColor(new Color(150, 75, 0));
            g2d.fillRect(x - (TAILLE_BATIMENT_MINIMAP / 2), y - (TAILLE_BATIMENT_MINIMAP / 2), TAILLE_BATIMENT_MINIMAP, TAILLE_BATIMENT_MINIMAP);
        } else {
            // --- 1. DESSIN DU SPRITE DE LA MINE ---
            Image spriteAAfficher;

            // Logique d'état visuel
            if (!mine.isFonctionnel()) {
                spriteAAfficher = IMAGE_MINE_ENDOMMAGE;
            } else if (mine.getHp() <= (mine.getMaxHp() / 2)) {
                spriteAAfficher = IMAGE_MINE_ENDOMMAGE;
            } else {
                spriteAAfficher = IMAGE_MINE;
            }

            // Rendu de l'image
            if (spriteAAfficher != null) {
                g2d.drawImage(spriteAAfficher, x - (TAILLE_BATIMENT / 2), y - (TAILLE_BATIMENT /3), null);
            } else {
                // Fallback de sécurité si l'image manque
                g2d.setColor(new Color(150, 75, 0));
                g2d.fillRect(x - (TAILLE_BATIMENT / 2), y - (TAILLE_BATIMENT / 2), TAILLE_BATIMENT, TAILLE_BATIMENT);
            }

            // --- 2. AFFICHAGE DU TEXTE DES MINERAIS ---
            // On récupère dynamiquement la taille de la liste des ressources
            int stock = mine.getRessources().size();

            // Configuration de la police
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 12));

            // Dessin du texte juste au-dessus du sprite de la mine
            g2d.drawString(stock + " Minerais", x - 30, y - (TAILLE_BATIMENT / 2) - 8);
        }
    }
}