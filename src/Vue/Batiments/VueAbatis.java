package Vue.Batiments;

import Modele.Batiments.Abatis;
import java.awt.*;

import static Modele.Constantes.*;

/**
 * Gère le rendu visuel de l'Abatis.
 * Sélectionne la ressource graphique à afficher selon son orientation logique
 * et son état fonctionnel, avec un rendu simplifié pour la minimap.
 */
public class VueAbatis {

    /** ---------- [Méthodes Publiques - Rendu] ---------- **/

    /**
     * Dessine le sprite de l'Abatis dans la vue principale ou sa version simplifiée
     * dans la minimap.
     *
     * @param g2d - Contexte graphique de dessin
     * @param a - Instance d'Abatis à afficher
     * @param x - Position X écran du centre du bâtiment
     * @param y - Position Y écran du centre du bâtiment
     * @param minimap - Indique si le rendu cible la minimap
     */
    public static void dessinerSprite(Graphics2D g2d, Abatis a, int x, int y, boolean minimap) {
        if (minimap) {
            // Représentation compacte suffisante pour l'identification sur la minimap.
            g2d.setColor(new Color(139, 69, 19));
            g2d.fillRect(x - 3, y - 3, 6, 6);
            return;
        }

        Image imgAffichee;
        boolean estEndommage = (!a.isFonctionnel());

        // Le choix du sprite dépend à la fois de l'orientation posée en jeu et de l'état de dégradation.
        if (!a.isRotation()) {
            imgAffichee = estEndommage ? IMAGE_ABATIS_1_ENDOMMAGE : IMAGE_ABATIS_1;
        } else {
            imgAffichee = estEndommage ? IMAGE_ABATIS_2_ENDOMMAGE : IMAGE_ABATIS_2;
        }

        if (imgAffichee != null) {
            // Le rendu est centré sur la position logique du bâtiment pour rester cohérent avec le reste du moteur.
            int offsetX = x - (TAILLE_ABATIS / 2);
            int offsetY = y - (TAILLE_ABATIS / 2);
            g2d.drawImage(imgAffichee, offsetX, offsetY, null);
        }
    }
}