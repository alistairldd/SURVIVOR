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
                // On utilise directement la portée brute, sans multiplicateur
                int portee = t.getRange();

                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));

                if (t.getHp() <= 0.1 * t.BASE_HP) {
                    // Si la tour a perdu des points de vie, on la dessine en rouge
                    g2d.setColor(Color.RED);
                } else {
                    // Sinon, elle est en bon état, on la dessine en cyan
                    g2d.setColor(Color.CYAN);
                }

                // Puisque (x, y) est maintenant le vrai centre de la tour,
                // on dessine le cercle directement autour de ce point (x, y).
                g2d.fillOval(x - portee, y - portee, portee * 2, portee * 2);

                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawOval(x - portee, y - portee, portee * 2, portee * 2);

                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            }

            // --- DESSIN DE LA TOUR ---
            if (t.getHp() <= 0.1 * t.BASE_HP) {
                // Si la tour a perdu des points de vie, on la dessine en rouge
                g2d.setColor(Color.RED);
            } else {
                // Sinon, elle est en bon état, on la dessine en cyan
            g2d.setColor(Color.CYAN);
            }
            // On décale le carré pour le centrer sur (x, y)
            g2d.fillRect(x - demiTaille, y - demiTaille, taille, taille);

        } else {
            // Bâtiment générique
            g2d.setColor(Color.GRAY);
            g2d.fillRect(x - demiTaille, y - demiTaille, taille, taille);
        }

        g2d.dispose();
    }
}