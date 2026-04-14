package Vue.Batiments;

import Modele.Batiments.Tower;
import Modele.Monstres.Monstre;
import java.awt.*;
import static Modele.Constantes.*;

/**
 * Classe utilitaire dédiée au rendu de la Tour Défensive.
 * Gère le sprite, l'aura de portée et les VFX balistiques (laser calculé par raycasting).
 */
public class VueTower {

    /**
     * Procédure de rendu de la Tour.
     * @param g2d Le contexte graphique.
     * @param t L'instance de la tour à dessiner.
     * @param x Coordonnée X (écran).
     * @param y Coordonnée Y (écran).
     * @param minimap Si vrai, dessine un symbole simplifié.
     */
    public static void dessiner(Graphics2D g2d, Tower t, int x, int y, boolean minimap) {

        if (minimap) {
            // Rendu Minimap : Un petit carré cyan (ou rouge si HP critique)
            g2d.setColor(t.getHp() <= 0.1 * HP_TOWER ? Color.RED : Color.CYAN);
            g2d.fillRect(x - (TAILLE_BATIMENT_MINIMAP / 2), y - (TAILLE_BATIMENT_MINIMAP / 2), TAILLE_BATIMENT_MINIMAP, TAILLE_BATIMENT_MINIMAP);
        } else {
            // --- 1. DESSIN DU CERCLE DE PORTÉE (AURA) ---
            int portee = t.getRange();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));

            // L'aura devient rouge si la tour est presque détruite
            Color couleurAura = t.getHp() <= 0.1 * HP_TOWER ? Color.RED : Color.CYAN;
            g2d.setColor(couleurAura);
            g2d.fillOval(x - portee, y - portee, portee * 2, portee * 2);

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(x - portee, y - portee, portee * 2, portee * 2);

            // On remet l'opacité à 100% pour la suite
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

            // --- 2. DESSIN DU SPRITE DE LA TOUR ---
            Image spriteAAfficher;
            if (!t.isFonctionnel() || t.getHp() <= (t.getMaxHp() / 2)) {
                spriteAAfficher = IMAGE_TOUR_ENDOMMAGE;
            } else {
                spriteAAfficher = IMAGE_TOUR;
            }

            if (spriteAAfficher != null) {
                g2d.drawImage(spriteAAfficher, x - (TAILLE_BATIMENT / 2), y - TAILLE_BATIMENT* 4/5, null);
            } else {
                g2d.setColor(couleurAura);
                g2d.fillRect(x - (TAILLE_BATIMENT / 2), y - TAILLE_BATIMENT, TAILLE_BATIMENT, TAILLE_BATIMENT);
            }

            // --- 3. EFFET D'ATTAQUE (LASER ET RAYCASTING) ---
            Monstre cible = t.getMonstreCible();

            // Le laser ne s'affiche que pendant 150ms après le tir
            if (cible != null && (System.currentTimeMillis() - t.getDernierTempsAttaque() < 150)) {
                int cibleX = (int) cible.getX();
                int cibleY = (int) cible.getY();
                int tailleProjectile = 8;
                int demiTailleMonstre = TAILLE_MONSTRE / 2;

                double dx = cibleX - x;
                double dy = cibleY - y;
                double distance = Math.hypot(dx, dy);

                if (distance > 1) { // Sécurité anti-division par zéro
                    // Mathématiques : calcul du point d'impact sur le bord du monstre
                    double tx = (dx == 0) ? Double.MAX_VALUE : Math.abs(demiTailleMonstre / dx);
                    double ty = (dy == 0) ? Double.MAX_VALUE : Math.abs(demiTailleMonstre / dy);
                    double tIntersection = Math.min(tx, ty);

                    int posBouleX = (int) (x + dx * (1 - tIntersection));
                    int posBouleY = (int) (y + dy * (1 - tIntersection));

                    // Traînée du laser (Jaune transparent 50%)
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                    g2d.setColor(Color.YELLOW);
                    g2d.setStroke(new BasicStroke(3));
                    g2d.drawLine(x, y, posBouleX, posBouleY);

                    // Impact / Boule d'énergie (Jaune opaque 100%)
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                    g2d.fillOval(posBouleX - (tailleProjectile/2), posBouleY - (tailleProjectile/2), tailleProjectile, tailleProjectile);
                }

                // Reset de l'épaisseur du trait pour ne pas polluer les autres dessins
                g2d.setStroke(new BasicStroke(1));
            }
        }
    }
}