package Vue.Batiments;

import Modele.Batiments.Tower;
import Modele.Monstres.Monstre;
import java.awt.*;
import static Modele.Constantes.*;

/**
 * Classe utilitaire dédiée au rendu de la Tour Défensive.
 * Gère séparément l'aura (au sol) et le sprite avec ses effets balistiques.
 */
public class VueTower {

    /**
     * PASSE 1 : Dessine uniquement l'aura de portée au sol.
     */
    public static void dessinerAura(Graphics2D g2d, Tower t, int x, int y) {
        int portee = t.getRange();

        // Configuration de la transparence pour l'aura
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));

        // Changement de couleur selon l'état de santé
        Color couleurAura = t.getHp() <= 0.1 * HP_TOWER ? Color.RED : Color.CYAN;
        g2d.setColor(couleurAura);

        // Remplissage du cercle
        g2d.fillOval(x - portee, y - portee, portee * 2, portee * 2);

        // Bordure un peu plus visible
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval(x - portee, y - portee, portee * 2, portee * 2);

        // Reset des paramètres pour ne pas affecter les autres dessins
        g2d.setStroke(new BasicStroke(1));
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    /**
     * PASSE 2 : Dessine le volume (Sprite) et les effets de tir.
     */
    public static void dessinerSprite(Graphics2D g2d, Tower t, int x, int y, boolean minimap) {
        if (minimap) {
            g2d.setColor(t.getHp() <= 0.1 * HP_TOWER ? Color.RED : Color.CYAN);
            g2d.fillRect(x - (TAILLE_BATIMENT_MINIMAP / 2), y - (TAILLE_BATIMENT_MINIMAP / 2), TAILLE_BATIMENT_MINIMAP, TAILLE_BATIMENT_MINIMAP);
        } else {
            // 1. DÉTERMINATION DU SPRITE SELON L'ÉTAT
            Image spriteAAfficher;
            if (!t.isFonctionnel() || t.getHp() <= (t.getMaxHp() / 2)) {
                spriteAAfficher = IMAGE_TOUR_ENDOMMAGE;
            } else {
                spriteAAfficher = IMAGE_TOUR;
            }

            // 2. RENDU DU SPRITE (Ancré au bas-milieu pour la 2.5D)
            if (spriteAAfficher != null) {
                g2d.drawImage(spriteAAfficher, x - (TAILLE_TOUR / 2), y - (TAILLE_TOUR * 4 / 5), null);
            } else {
                g2d.setColor(Color.CYAN);
                g2d.fillRect(x - (TAILLE_TOUR / 2), y - TAILLE_TOUR, TAILLE_TOUR, TAILLE_TOUR);
            }

            // 3. EFFET DE TIR (LASER)
            Monstre cible = t.getMonstreCible();
            if (cible != null && (System.currentTimeMillis() - t.getDernierTempsAttaque() < 150)) {
                dessinerLaser(g2d, t, cible, x, y);
            }
        }
    }

    /**
     * Méthode interne pour le rendu balistique du laser.
     */
    private static void dessinerLaser(Graphics2D g2d, Tower t, Monstre cible, int x, int y) {
        int cibleX = (int) cible.getX();
        int cibleY = (int) cible.getY();
        int tailleProjectile = 8;
        int demiTailleMonstre = TAILLE_MONSTRE / 2;

        // Calcul du sommet de la tour (point de départ du laser)
        int sommetX = x;
        int sommetY = y - (TAILLE_TOUR / 2);

        double dx = cibleX - sommetX;
        double dy = cibleY - sommetY;
        double distance = Math.hypot(dx, dy);

        if (distance > 1) {
            double tx = (dx == 0) ? Double.MAX_VALUE : Math.abs(demiTailleMonstre / dx);
            double ty = (dy == 0) ? Double.MAX_VALUE : Math.abs(demiTailleMonstre / dy);
            double tIntersection = Math.min(tx, ty);

            int impactX = (int) (sommetX + dx * (1 - tIntersection));
            int impactY = (int) (sommetY + dy * (1 - tIntersection));

            // Trait du laser
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            g2d.setColor(Color.YELLOW);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawLine(sommetX, sommetY, impactX, impactY);

            // Impact
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            g2d.fillOval(impactX - (tailleProjectile/2), impactY - (tailleProjectile/2), tailleProjectile, tailleProjectile);

            g2d.setStroke(new BasicStroke(1));
        }
    }
}