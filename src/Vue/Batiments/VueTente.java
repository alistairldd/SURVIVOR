package Vue.Batiments;

import Modele.Batiments.TenteDeSoin;
import java.awt.*;
import static Modele.Constantes.*;

/**
 * Classe utilitaire dédiée au rendu de la Tente de Soin.
 * Gère séparément l'aura de guérison (au sol) et le sprite.
 */
public class VueTente {

    /**
     * PASSE 1 : Dessine uniquement l'aura de soin au sol (si active).
     */
    public static void dessinerAura(Graphics2D g2d, TenteDeSoin tente, int x, int y) {
        int portee = tente.getRange();

        // Si la tente a soigné il y a moins de 600ms, on dessine la pulsation rouge
        if (System.currentTimeMillis() - tente.getDernierTempsSoin() < 600) {
            // Fond rouge transparent (15%)
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.15f));
            g2d.setColor(Color.RED);
            g2d.fillOval(x - portee, y - portee, portee * 2, portee * 2);

            // Bordure rouge plus marquée (40%)
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(x - portee, y - portee, portee * 2, portee * 2);

            // Reset
            g2d.setStroke(new BasicStroke(1));
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }
    }

    /**
     * PASSE 2 : Dessine le sprite de la tente.
     */
    public static void dessinerSprite(Graphics2D g2d, TenteDeSoin tente, int x, int y, boolean minimap) {
        if (minimap) {
            g2d.setColor(new Color(200, 200, 0));
            g2d.fillRect(x - (TAILLE_BATIMENT_MINIMAP / 2), y - (TAILLE_BATIMENT_MINIMAP / 2), TAILLE_BATIMENT_MINIMAP, TAILLE_BATIMENT_MINIMAP);
        } else {
            // Logique d'état visuel
            Image spriteAAfficher;
            if (!tente.isFonctionnel()) {
                spriteAAfficher = IMAGE_TENTE_ENDOMMAGE;
            } else {
                spriteAAfficher = IMAGE_TENTE;
            }

            // Rendu final (Ancré au bas-milieu pour la 2.5D : y - TAILLE_TENTE)
            if (spriteAAfficher != null) {
                g2d.drawImage(spriteAAfficher, x - (TAILLE_TENTE / 2), y - TAILLE_TENTE * 2/3, null);
            } else {
                g2d.setColor(new Color(200, 200, 0));
                g2d.fillRect(x - (TAILLE_TENTE / 2), y - TAILLE_TENTE, TAILLE_TENTE, TAILLE_TENTE);
            }
        }
    }
}