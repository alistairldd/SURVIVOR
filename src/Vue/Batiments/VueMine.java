package Vue.Batiments;

import Modele.Batiments.Mine;
import java.awt.*;

import static Modele.Constantes.*;

public class VueMine {

    /** ---------- [Méthodes Publiques - Rendu] ---------- **/

    /**
     * Dessine la Mine dans la vue principale ou sur la minimap.
     * En vue monde, le bâtiment affiche également son stock courant pour rendre
     * l'état d'exploitation visible sans ouvrir d'interface dédiée.
     *
     * @param g2d - Contexte graphique de dessin
     * @param mine - Mine à afficher
     * @param x - Position X écran du point d'ancrage
     * @param y - Position Y écran du point d'ancrage
     * @param minimap - Indique si le rendu cible la minimap
     */
    public static void dessiner(Graphics2D g2d, Mine mine, int x, int y, boolean minimap) {

        if (minimap) {
            // Représentation compacte suffisante pour l'identification rapide sur la carte réduite.
            g2d.setColor(new Color(150, 75, 0));
            g2d.fillRect(x - (TAILLE_BATIMENT_MINIMAP / 2), y - (TAILLE_BATIMENT_MINIMAP / 2), TAILLE_BATIMENT_MINIMAP, TAILLE_BATIMENT_MINIMAP);
        } else {
            if (IMAGE_MINE != null) {
                // Le sprite est ancré au sol pour conserver la cohérence de perspective avec les autres bâtiments.
                g2d.drawImage(IMAGE_MINE, x - (TAILLE_MINE / 2), y - TAILLE_MINE, null);
            } else {
                // Rendu de secours si la ressource visuelle n'est pas disponible.
                g2d.setColor(new Color(150, 75, 0));
                g2d.fillRect(x - (TAILLE_MINE / 2), y - TAILLE_MINE, TAILLE_MINE, TAILLE_MINE);
            }

            // Le stock est affiché au-dessus du toit pour rester lisible sans masquer le bâtiment.
            int stock = mine.getRessources().size();
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            g2d.drawString(stock + " Minerais", x - 30, y - TAILLE_MINE - 10);
        }
    }
}