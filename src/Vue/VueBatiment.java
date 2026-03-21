package Vue;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import Modele.Batiment;
import Modele.HQ;
import Modele.Tower;
import Modele.Monstre; // Import nécessaire !

public class VueBatiment {

    public VueBatiment() {
    }

    public static void dessinerBatiment(Graphics g, Batiment b, int x, int y, boolean minimap) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int taille = 30;

        if (minimap) {
            taille = 6;
        }

        // On calcule la moitié de la taille pour pouvoir centrer le dessin
        int demiTaille = taille / 2;

        if (b instanceof HQ) {
            g2d.setColor(Color.WHITE);

            int tailleHQ = minimap ? taille : 45;
            int demiTailleHQ = tailleHQ / 2;

            // On décale de la moitié de la taille pour que (x, y) soit le centre exact
            g2d.fillRect(x - demiTailleHQ, y - demiTailleHQ, tailleHQ, tailleHQ);

        } else if (b instanceof Tower) {
            Tower t = (Tower) b;

            // --- DESSIN DU CERCLE DE PORTÉE ---
            if (!minimap) {
                int portee = t.getRange();

                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));

                if (t.getHp() <= 0.1 * t.BASE_HP) {
                    g2d.setColor(Color.RED);
                } else {
                    g2d.setColor(Color.CYAN);
                }

                g2d.fillOval(x - portee, y - portee, portee * 2, portee * 2);

                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawOval(x - portee, y - portee, portee * 2, portee * 2);

                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            }

            // --- DESSIN DE LA TOUR ---
            if (t.getHp() <= 0.1 * t.BASE_HP) {
                g2d.setColor(Color.RED);
            } else {
                g2d.setColor(Color.CYAN);
            }
            g2d.fillRect(x - demiTaille, y - demiTaille, taille, taille);


            // =====================================================================
            // --- AJOUT : EFFET D'ATTAQUE (Traînée et Boule touchant le bord) ---
            // =====================================================================
            if (!minimap) {
                Monstre cible = t.getMonstreCible();

                // On affiche l'effet uniquement pendant 150ms après le tir
                if (cible != null && (System.currentTimeMillis() - t.getDernierTempsAttaque() < 150)) {

                    int cibleX = cible.getX(); // Centre X du monstre
                    int cibleY = cible.getY(); // Centre Y du monstre
                    int tailleProjectile = 8;

                    // --- AJOUT : Taille du monstre pour l'intersection ---
                    // Supposons que ton monstre est un carré de 24 pixels.
                    // Remplace par la vraie valeur si disponible (ex: cible.getTaille())
                    int tailleMonstre = 24;
                    int demiTailleMonstre = tailleMonstre / 2;

                    // -- CALCUL DES COORDONNÉES --
                    double dx = cibleX - x;
                    double dy = cibleY - y;
                    double distance = Math.hypot(dx, dy);

                    if (distance > 1) { // Sécurité

                        // --- AJOUT : Calcul du point d'impact sur le bord du carré ---
                        // On cherche le facteur 't' d'intersection avec les bords
                        double tx = (dx == 0) ? Double.MAX_VALUE : Math.abs(demiTailleMonstre / dx);
                        double ty = (dy == 0) ? Double.MAX_VALUE : Math.abs(demiTailleMonstre / dy);
                        double tIntersection = Math.min(tx, ty); // On prend la première intersection trouvée

                        // Coordonnées du centre de la boule (exactement sur le bord du monstre)
                        // Note : '1 - tIntersection' car le vecteur dx part de la tour, on recule vers l'arrière
                        int posBouleX = (int) (x + dx * (1 - tIntersection));
                        int posBouleY = (int) (y + dy * (1 - tIntersection));

                        // 2. Dessiner la trajectoire/traînée (JAUNE, translucent)
                        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                        g2d.setColor(Color.YELLOW);
                        g2d.setStroke(new BasicStroke(3));
                        // On trace du centre de la tour (x,y) vers le centre de la boule (bords du monstre)
                        g2d.drawLine(x, y, posBouleX, posBouleY);

                        // 3. Dessiner la "boule" (le projectile jaune, opaque)
                        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                        g2d.fillOval(posBouleX - (tailleProjectile/2), posBouleY - (tailleProjectile/2), tailleProjectile, tailleProjectile);
                    }

                    g2d.setStroke(new BasicStroke(1)); // Reset du trait
                }
            }
            // =====================================================================

        } else {
            // Bâtiment générique
            g2d.setColor(Color.GRAY);
            g2d.fillRect(x - demiTaille, y - demiTaille, taille, taille);
        }

        g2d.dispose();
    }
}