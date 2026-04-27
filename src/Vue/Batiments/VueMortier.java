package Vue.Batiments;

import Modele.Batiments.Mortier;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import static Modele.Constantes.*;
import java.awt.*;

/**
 * Rendu du Mortier : Aura "percée", Projectile imposant et Explosion visible.
 */
public class VueMortier {

    /**
     * PASSE 1 : Dessine l'aura au sol (Vrai Donut sans angle mort).
     */
    public static void dessinerAura(Graphics2D g2d, Mortier m, int x, int y) {
        int maxRange = m.getRange();
        int minRange = m.getMinRange();

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
        Color couleurAura = m.getHp() <= 0.1 * HP_MORTIER ? Color.RED : new Color(138, 43, 226);

        // --- LOGIQUE DU DONUT ---
        // On crée le grand cercle
        Ellipse2D exterieur = new Ellipse2D.Double(x - maxRange, y - maxRange, maxRange * 2, maxRange * 2);
        Area zoneAttaque = new Area(exterieur);

        // On soustrait le petit cercle (l'angle mort devient transparent)
        if (minRange > 0) {
            Ellipse2D interieur = new Ellipse2D.Double(x - minRange, y - minRange, minRange * 2, minRange * 2);
            zoneAttaque.subtract(new Area(interieur));
        }

        g2d.setColor(couleurAura);
        g2d.fill(zoneAttaque); // On remplit uniquement la "bande"

        // Bordures fines
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        g2d.setStroke(new BasicStroke(2));
        g2d.draw(zoneAttaque);

        g2d.setStroke(new BasicStroke(1));
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    /**
     * PASSE 2 : Dessine le sprite, l'obus (agrandi) et l'explosion.
     */
    public static void dessinerSprite(Graphics2D g2d, Mortier m, int x, int y, boolean minimap) {
        if (minimap) {
            g2d.setColor(new Color(75, 0, 130));
            g2d.fillRect(x - 3, y - 3, 6, 6);
            return;
        }

        // 1. Sprite du bâtiment
        Image img = m.getHp() < (HP_MORTIER / 2) ? IMAGE_MORTIER_ENDOMMAGE : IMAGE_MORTIER;
        if (img != null) {
            g2d.drawImage(img, x - (TAILLE_MORTIER / 2), y - (TAILLE_MORTIER / 2), null);
        }

        // 2. Projectile et Explosion
        if (m.isEnTrainDeTirer()) {
            long tempsEcoule = System.currentTimeMillis() - m.getDebutTir();

            if (tempsEcoule < TEMPS_DE_VOL) {
                double progression = (double) tempsEcoule / TEMPS_DE_VOL;
                int projX = (int) (m.getX() + (m.getCibleX() - m.getX()) * progression);
                double hauteurCloche = 200.0; // Augmenté pour plus de style
                int baseProjY = (int) (m.getY() + (m.getCibleY() - m.getY()) * progression);
                int projY = (int) (baseProjY - Math.sin(progression * Math.PI) * hauteurCloche);

                // --- RENDU DE L'OBUS (Agrandi à 50px, taille d'un monstre) ---
                int tailleObus = 50;
                if (IMAGE_MORTIER_PROJECTILE != null) {
                    // On centre l'image agrandie sur la position calculée
                    g2d.drawImage(IMAGE_MORTIER_PROJECTILE, projX - (tailleObus/2), projY - (tailleObus/2), tailleObus, tailleObus, null);
                } else {
                    g2d.setColor(Color.BLACK);
                    g2d.fillOval(projX - 15, projY - 15, 30, 30);
                }
            }
            // 3. Explosion (Visible grâce au délai ajouté dans Mortier.java)
            else if (tempsEcoule < TEMPS_DE_VOL + 300) {
                float opacite = 1.0f - ((float)(tempsEcoule - TEMPS_DE_VOL) / 300.0f);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacite));

                int cx = (int) m.getCibleX();
                int cy = (int) m.getCibleY();

                g2d.setColor(new Color(138, 43, 226, 150));
                g2d.fillOval(cx - EXPLOSION_OUTER_RADIUS, cy - EXPLOSION_OUTER_RADIUS, EXPLOSION_OUTER_RADIUS * 2, EXPLOSION_OUTER_RADIUS * 2);

                g2d.setColor(new Color(75, 0, 130, 220));
                g2d.fillOval(cx - EXPLOSION_CORE_RADIUS, cy - EXPLOSION_CORE_RADIUS, EXPLOSION_CORE_RADIUS * 2, EXPLOSION_CORE_RADIUS * 2);

                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            }
        }
    }
}