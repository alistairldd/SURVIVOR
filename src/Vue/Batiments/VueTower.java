package Vue.Batiments;

import Modele.Batiments.Tower;
import Modele.Monstres.Monstre;
import java.awt.*;

import static Modele.Constantes.*;

/**
 * Classe utilitaire dédiée au rendu de la Tour Défensive.
 * Sépare le dessin de la portée, du volume du bâtiment et de l'effet de tir
 * pour s'intégrer proprement au pipeline de rendu du jeu.
 */
public class VueTower {

    /** ---------- [Méthodes Publiques - Rendu des auras] ---------- **/

    /**
     * Dessine l'aura de portée de la tour au sol.
     * La couleur bascule en rouge lorsque la tour est proche de la destruction
     * afin de rendre immédiatement visible son état critique.
     *
     * @param g2d - Contexte graphique de dessin
     * @param t - Tour concernée
     * @param x - Position X écran du centre du bâtiment
     * @param y - Position Y écran du centre du bâtiment
     */
    public static void dessinerAura(Graphics2D g2d, Tower t, int x, int y) {
        int portee = t.getRange();

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));

        Color couleurAura = t.getHp() <= 0.1 * HP_TOWER ? Color.RED : Color.CYAN;
        g2d.setColor(couleurAura);
        g2d.fillOval(x - portee, y - portee, portee * 2, portee * 2);

        // Une bordure plus marquée aide à lire la limite exacte de la zone couverte.
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval(x - portee, y - portee, portee * 2, portee * 2);

        // Réinitialisation explicite pour ne pas propager l'état graphique aux rendus suivants.
        g2d.setStroke(new BasicStroke(1));
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    /** ---------- [Méthodes Publiques - Rendu principal] ---------- **/

    /**
     * Dessine la tour dans la vue principale ou sa représentation simplifiée sur la minimap.
     * Le rendu prend en charge l'état visuel de la structure ainsi que l'effet de tir
     * lorsqu'une cible vient d'être attaquée.
     *
     * @param g2d - Contexte graphique de dessin
     * @param t - Tour à afficher
     * @param x - Position X écran du point d'ancrage
     * @param y - Position Y écran du point d'ancrage
     * @param minimap - Indique si le rendu cible la minimap
     */
    public static void dessinerSprite(Graphics2D g2d, Tower t, int x, int y, boolean minimap) {
        if (minimap) {
            g2d.setColor(t.getHp() <= 0.1 * HP_TOWER ? Color.RED : Color.CYAN);
            g2d.fillRect(x - (TAILLE_BATIMENT_MINIMAP / 2), y - (TAILLE_BATIMENT_MINIMAP / 2), TAILLE_BATIMENT_MINIMAP, TAILLE_BATIMENT_MINIMAP);
        } else {
            Image spriteAAfficher;

            if (!t.isFonctionnel()) {
                spriteAAfficher = IMAGE_TOUR_ENDOMMAGE;
            } else {
                spriteAAfficher = IMAGE_TOUR;
            }

            if (spriteAAfficher != null) {
                // L'ancrage bas-milieu maintient la cohérence de perspective 2.5D.
                g2d.drawImage(spriteAAfficher, x - (TAILLE_TOUR / 2), y - (TAILLE_TOUR * 4 / 5), null);
            } else {
                // Rendu de secours si la texture n'est pas disponible.
                g2d.setColor(Color.CYAN);
                g2d.fillRect(x - (TAILLE_TOUR / 2), y - TAILLE_TOUR, TAILLE_TOUR, TAILLE_TOUR);
            }

            Monstre cible = t.getMonstreCible();
            if (cible != null && (System.currentTimeMillis() - t.getDernierTempsAttaque() < 150)) {
                dessinerLaser(g2d, t, cible, x, y);
            }
        }
    }

    /** ---------- [Méthodes Privées - Effets de tir] ---------- **/

    /**
     * Dessine le tir instantané de la tour jusqu'au point d'impact apparent sur la cible.
     * Le calcul s'arrête sur le bord visuel du monstre plutôt qu'en son centre pour éviter
     * un rendu qui donnerait l'impression que le projectile traverse la cible.
     *
     * @param g2d - Contexte graphique de dessin
     * @param t - Tour à l'origine du tir
     * @param cible - Monstre visé
     * @param x - Position X écran de la tour
     * @param y - Position Y écran de la tour
     */
    private static void dessinerLaser(Graphics2D g2d, Tower t, Monstre cible, int x, int y) {
        int cibleX = (int) cible.getX();
        int cibleY = (int) cible.getY();
        int tailleProjectile = 8;
        int demiTailleMonstre = TAILLE_MONSTRE / 2;

        // Le départ du tir est fixé près du sommet visuel de la tour pour un rendu plus naturel.
        int sommetX = x;
        int sommetY = y - (TAILLE_TOUR / 2);

        double dx = cibleX - sommetX;
        double dy = cibleY - sommetY;
        double distance = Math.hypot(dx, dy);

        if (distance > 1) {
            // Approximation simple du point d'impact sur le contour de la cible.
            double tx = (dx == 0) ? Double.MAX_VALUE : Math.abs(demiTailleMonstre / dx);
            double ty = (dy == 0) ? Double.MAX_VALUE : Math.abs(demiTailleMonstre / dy);
            double tIntersection = Math.min(tx, ty);

            int impactX = (int) (sommetX + dx * (1 - tIntersection));
            int impactY = (int) (sommetY + dy * (1 - tIntersection));

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            g2d.setColor(Color.YELLOW);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawLine(sommetX, sommetY, impactX, impactY);

            // Le point d'impact renforce la lisibilité du hit pendant sa très courte fenêtre d'affichage.
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            g2d.fillOval(impactX - (tailleProjectile / 2), impactY - (tailleProjectile / 2), tailleProjectile, tailleProjectile);

            g2d.setStroke(new BasicStroke(1));
        }
    }
}