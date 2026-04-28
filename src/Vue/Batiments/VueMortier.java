package Vue.Batiments;

import Modele.Batiments.Mortier;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import static Modele.Constantes.*;
import java.awt.*;

/**
 * Gère le rendu visuel du Mortier.
 * Ce bâtiment possède un traitement spécifique car il combine une zone d'effet
 * non circulaire simple, une phase de projectile et une phase d'explosion.
 */
public class VueMortier {

    /** ---------- [Méthodes Publiques - Rendu des auras] ---------- **/

    /**
     * Dessine l'aura d'attaque du Mortier au sol.
     * La zone utile est représentée sous forme d'anneau afin de matérialiser
     * à la fois la portée maximale et la zone morte minimale.
     *
     * @param g2d - Contexte graphique de dessin
     * @param m - Mortier dont il faut afficher la portée
     * @param x - Position X écran du centre du bâtiment
     * @param y - Position Y écran du centre du bâtiment
     */
    public static void dessinerAura(Graphics2D g2d, Mortier m, int x, int y) {
        int maxRange = m.getRange();
        int minRange = m.getMinRange();

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
        Color couleurAura = m.getHp() <= 0.1 * HP_MORTIER ? Color.RED : new Color(138, 43, 226);

        // La zone d'attaque est construite par soustraction géométrique pour obtenir un vrai anneau.
        Ellipse2D exterieur = new Ellipse2D.Double(x - maxRange, y - maxRange, maxRange * 2, maxRange * 2);
        Area zoneAttaque = new Area(exterieur);

        if (minRange > 0) {
            Ellipse2D interieur = new Ellipse2D.Double(x - minRange, y - minRange, minRange * 2, minRange * 2);
            zoneAttaque.subtract(new Area(interieur));
        }

        g2d.setColor(couleurAura);
        g2d.fill(zoneAttaque);

        // Une bordure plus opaque améliore la lecture précise des limites de tir.
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        g2d.setStroke(new BasicStroke(2));
        g2d.draw(zoneAttaque);

        g2d.setStroke(new BasicStroke(1));
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    /** ---------- [Méthodes Publiques - Rendu principal] ---------- **/

    /**
     * Dessine le Mortier, puis ses effets temporaires de tir lorsqu'ils sont actifs.
     * Le rendu combine trois états visuels : structure, projectile en vol et explosion
     * sur la cible pour refléter fidèlement la temporalité de l'attaque.
     *
     * @param g2d - Contexte graphique de dessin
     * @param m - Mortier à afficher
     * @param x - Position X écran du centre du bâtiment
     * @param y - Position Y écran du centre du bâtiment
     * @param minimap - Indique si le rendu cible la minimap
     */
    public static void dessinerSprite(Graphics2D g2d, Mortier m, int x, int y, boolean minimap) {
        if (minimap) {
            // Représentation minimale suffisante pour l'identification sur la minimap.
            g2d.setColor(new Color(75, 0, 130));
            g2d.fillRect(x - 3, y - 3, 6, 6);
            return;
        }

        Image img = m.getHp() < (HP_MORTIER / 2) ? IMAGE_MORTIER_ENDOMMAGE : IMAGE_MORTIER;
        if (img != null) {
            g2d.drawImage(img, x - (TAILLE_MORTIER / 2), y - (TAILLE_MORTIER / 2), null);
        }

        if (m.isEnTrainDeTirer()) {
            long tempsEcoule = System.currentTimeMillis() - m.getDebutTir();

            if (tempsEcoule < TEMPS_DE_VOL) {
                double progression = (double) tempsEcoule / TEMPS_DE_VOL;
                int projX = (int) (m.getX() + (m.getCibleX() - m.getX()) * progression);

                // La trajectoire suit une cloche visuelle pour donner une lecture immédiate d'un tir balistique.
                double hauteurCloche = 200.0;
                int baseProjY = (int) (m.getY() + (m.getCibleY() - m.getY()) * progression);
                int projY = (int) (baseProjY - Math.sin(progression * Math.PI) * hauteurCloche);

                int tailleObus = 50;
                if (IMAGE_MORTIER_PROJECTILE != null) {
                    // Le projectile est centré sur la position interpolée pour éviter tout décalage visuel.
                    g2d.drawImage(IMAGE_MORTIER_PROJECTILE, projX - (tailleObus / 2), projY - (tailleObus / 2), tailleObus, tailleObus, null);
                } else {
                    g2d.setColor(Color.BLACK);
                    g2d.fillOval(projX - 15, projY - 15, 30, 30);
                }
            }

            else if (tempsEcoule < TEMPS_DE_VOL + 300) {
                // L'explosion persiste brièvement après l'impact pour matérialiser la zone touchée.
                float opacite = 1.0f - ((float) (tempsEcoule - TEMPS_DE_VOL) / 300.0f);
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