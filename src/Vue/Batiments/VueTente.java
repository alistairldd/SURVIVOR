package Vue.Batiments;

import Modele.Batiments.TenteDeSoin;
import java.awt.*;
import static Modele.Constantes.*;

/**
 * Classe utilitaire dédiée au rendu de la Tente de Soin.
 * Gère l'affichage dynamique (Neuf/Endommagé) et l'effet visuel de l'aura de guérison.
 */
public class VueTente {

    /**
     * Procédure de rendu de la Tente.
     * @param g2d Le contexte graphique.
     * @param tente L'instance de la tente à dessiner.
     * @param x Coordonnée X (écran).
     * @param y Coordonnée Y (écran).
     * @param minimap Si vrai, dessine un symbole simplifié.
     */
    public static void dessiner(Graphics2D g2d, TenteDeSoin tente, int x, int y, boolean minimap) {

        if (minimap) {
            // Rendu Minimap : Un petit carré khaki/jaune foncé
            g2d.setColor(new Color(200, 200, 0));
            g2d.fillRect(x - (TAILLE_BATIMENT_MINIMAP / 2), y - (TAILLE_BATIMENT_MINIMAP / 2), TAILLE_BATIMENT_MINIMAP, TAILLE_BATIMENT_MINIMAP);
        } else {
            // --- 1. DESSIN DE L'AURA DE SOIN (VFX) ---
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

                // Reset de l'épaisseur du trait
                g2d.setStroke(new BasicStroke(1));
            }

            // --- 2. DESSIN DU SPRITE DE LA TENTE ---
            // On s'assure que l'opacité est bien revenue à 100% pour le bâtiment
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

            // Logique d'état visuel
            Image spriteAAfficher;
            if (!tente.isFonctionnel()) {
                spriteAAfficher = IMAGE_TENTE_ENDOMMAGE;
            } else if (tente.getHp() <= (tente.getMaxHp() / 2)) {
                spriteAAfficher = IMAGE_TENTE_ENDOMMAGE;
            } else {
                spriteAAfficher = IMAGE_TENTE;
            }

            // Rendu final
            if (spriteAAfficher != null) {
                g2d.drawImage(spriteAAfficher, x - (TAILLE_TENTE / 2), y - (TAILLE_TENTE / 2), null);
            } else {
                // Fallback si image introuvable
                g2d.setColor(new Color(200, 200, 0));
                g2d.fillRect(x - (TAILLE_TENTE / 2), y - (TAILLE_TENTE / 2), TAILLE_TENTE, TAILLE_TENTE);
            }
        }
    }
}