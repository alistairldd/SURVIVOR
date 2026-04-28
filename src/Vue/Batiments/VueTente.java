package Vue.Batiments;

import Modele.Batiments.TenteDeSoin;
import java.awt.*;

import static Modele.Constantes.*;

/**
 * Classe utilitaire dédiée au rendu de la Tente de Soin.
 * Sépare l'affichage de l'aura temporaire de guérison et celui de la structure
 * afin de s'intégrer proprement au pipeline de rendu 2.5D.
 */
public class VueTente {

    /** ---------- [Méthodes Publiques - Rendu des auras] ---------- **/

    /**
     * Dessine l'aura de soin au sol lorsqu'une guérison vient d'être déclenchée.
     * L'effet est volontairement bref afin de signaler l'activité de la tente
     * sans surcharger visuellement la scène en permanence.
     *
     * @param g2d - Contexte graphique de dessin
     * @param tente - Tente de soin concernée
     * @param x - Position X écran du centre du bâtiment
     * @param y - Position Y écran du centre du bâtiment
     */
    public static void dessinerAura(Graphics2D g2d, TenteDeSoin tente, int x, int y) {
        int portee = tente.getRange();

        // La pulsation n'est visible que juste après un soin pour traduire un événement récent.
        if (System.currentTimeMillis() - tente.getDernierTempsSoin() < 600) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.15f));
            g2d.setColor(Color.RED);
            g2d.fillOval(x - portee, y - portee, portee * 2, portee * 2);

            // Une bordure plus lisible aide à percevoir rapidement la zone couverte.
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(x - portee, y - portee, portee * 2, portee * 2);

            g2d.setStroke(new BasicStroke(1));
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }
    }

    /** ---------- [Méthodes Publiques - Rendu principal] ---------- **/

    /**
     * Dessine la tente dans la vue principale ou sous forme simplifiée dans la minimap.
     * Le sprite reflète l'état fonctionnel du bâtiment afin que son niveau de dégradation
     * soit identifiable directement dans la scène.
     *
     * @param g2d - Contexte graphique de dessin
     * @param tente - Tente de soin à afficher
     * @param x - Position X écran du point d'ancrage
     * @param y - Position Y écran du point d'ancrage
     * @param minimap - Indique si le rendu cible la minimap
     */
    public static void dessinerSprite(Graphics2D g2d, TenteDeSoin tente, int x, int y, boolean minimap) {
        if (minimap) {
            // Marqueur compact pour la lecture rapide sur la minimap.
            g2d.setColor(new Color(200, 200, 0));
            g2d.fillRect(x - (TAILLE_BATIMENT_MINIMAP / 2), y - (TAILLE_BATIMENT_MINIMAP / 2), TAILLE_BATIMENT_MINIMAP, TAILLE_BATIMENT_MINIMAP);
        } else {
            Image spriteAAfficher;

            if (!tente.isFonctionnel()) {
                spriteAAfficher = IMAGE_TENTE_ENDOMMAGE;
            } else {
                spriteAAfficher = IMAGE_TENTE;
            }

            if (spriteAAfficher != null) {
                // L'ancrage bas-milieu conserve l'illusion de profondeur 2.5D.
                g2d.drawImage(spriteAAfficher, x - (TAILLE_TENTE / 2), y - TAILLE_TENTE * 2 / 3, null);
            } else {
                // Rendu de secours si la texture n'est pas disponible.
                g2d.setColor(new Color(200, 200, 0));
                g2d.fillRect(x - (TAILLE_TENTE / 2), y - TAILLE_TENTE, TAILLE_TENTE, TAILLE_TENTE);
            }
        }
    }
}