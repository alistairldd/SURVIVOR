package Vue.Batiments;

import Modele.Batiments.Mortier;
import static Modele.Constantes.*;
import java.awt.*;

/**
 * Classe utilitaire dédiée au rendu du Mortier.
 * Gère l'aura en "Donut" (avec zone morte), le sprite, et l'animation balistique de l'obus.
 */
public class VueMortier {

    /**
     * PASSE 1 : Dessine l'aura de portée au sol (Le Donut).
     * Appelée par VueBatiment AVANT le tri par profondeur.
     */
    public static void dessinerAura(Graphics2D g2d, Mortier m, int x, int y) {
        int maxRange = m.getRange();
        int minRange = m.getMinRange();

        // Configuration de la transparence
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));

        // Changement de couleur selon l'état de santé (Rouge si critique, Violet sinon)
        Color couleurAura = m.getHp() <= 0.1 * HP_MORTIER ? Color.RED : new Color(138, 43, 226);

        // 1. Grand cercle (Portée max)
        g2d.setColor(couleurAura);
        g2d.fillOval(x - maxRange, y - maxRange, maxRange * 2, maxRange * 2);

        // 2. Cercle intérieur (Zone morte / Angle mort)
        if (minRange > 0) {
            g2d.setColor(new Color(200, 0, 0, 150)); // Rouge translucide pour le danger
            g2d.fillOval(x - minRange, y - minRange, minRange * 2, minRange * 2);
        }

        // 3. Bordures pour délimiter clairement
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
        g2d.setStroke(new BasicStroke(2));

        g2d.setColor(couleurAura);
        g2d.drawOval(x - maxRange, y - maxRange, maxRange * 2, maxRange * 2);

        if (minRange > 0) {
            g2d.setColor(Color.RED);
            g2d.drawOval(x - minRange, y - minRange, minRange * 2, minRange * 2);
        }

        // Reset des paramètres
        g2d.setStroke(new BasicStroke(1));
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    /**
     * PASSE 2 : Dessine le sprite du Mortier, l'obus en vol et l'explosion AoE.
     * Appelée par VueBatiment APRÈS le tri par profondeur.
     */
    public static void dessinerSprite(Graphics2D g2d, Mortier m, int x, int y, boolean minimap) {
        // --- RENDU MINIMAP ---
        if (minimap) {
            g2d.setColor(new Color(75, 0, 130)); // Indigo/Violet foncé
            g2d.fillRect(x - TAILLE_BATIMENT_MINIMAP / 2, y - TAILLE_BATIMENT_MINIMAP / 2, TAILLE_BATIMENT_MINIMAP, TAILLE_BATIMENT_MINIMAP);
            return;
        }

        // --- 1. DESSIN DU BÂTIMENT ---
        Image img = m.getHp() < (HP_MORTIER / 2) ? IMAGE_MORTIER_ENDOMMAGE : IMAGE_MORTIER;
        if (img != null) {
            g2d.drawImage(img, x - (TAILLE_MORTIER / 2), y - (TAILLE_MORTIER / 2), null);
        } else {
            g2d.setColor(Color.DARK_GRAY);
            g2d.fillRect(x - 50, y - 50, 100, 100);
        }

        // --- 2. DESSIN DE LA BALISTIQUE & EXPLOSION ---
        if (m.isEnTrainDeTirer()) {
            long tempsEcoule = System.currentTimeMillis() - m.getDebutTir();

            // A) L'OBUS EST EN VOL
            if (tempsEcoule < TEMPS_DE_VOL) {
                // Progression du tir de 0.0 à 1.0
                double progression = (double) tempsEcoule / TEMPS_DE_VOL;

                // Interpolation linéaire pour X (Il avance droit de X vers CibleX)
                int projX = (int) (m.getX() + (m.getCibleX() - m.getX()) * progression);

                // Effet Parabolique (La Cloche) pour Y
                double hauteurCloche = 150.0; // Hauteur max de l'obus en pixels
                int baseProjY = (int) (m.getY() + (m.getCibleY() - m.getY()) * progression); // Le chemin droit
                // On soustrait un Sinus (qui fait 0 -> 1 -> 0) pour courber le tir vers le haut
                int projY = (int) (baseProjY - Math.sin(progression * Math.PI) * hauteurCloche);

                // Dessin de l'obus
                if (IMAGE_MORTIER_PROJECTILE != null) {
                    int tProj = IMAGE_MORTIER_PROJECTILE.getWidth(null);
                    g2d.drawImage(IMAGE_MORTIER_PROJECTILE, projX - (tProj/2), projY - (tProj/2), null);
                } else {
                    g2d.setColor(Color.BLACK);
                    g2d.fillOval(projX - 10, projY - 10, 20, 20);
                }
            }
            // B) L'IMPACT & L'EXPLOSION (Dure 300ms)
            else if (tempsEcoule < TEMPS_DE_VOL + 300) {
                // On calcule le fondu pour que l'explosion disparaisse doucement
                float opacite = 1.0f - ((float)(tempsEcoule - TEMPS_DE_VOL) / 300.0f);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacite));

                int cx = (int) m.getCibleX();
                int cy = (int) m.getCibleY();

                // Zone de Souffle (Outer) en Violet clair translucide
                g2d.setColor(new Color(138, 43, 226, 120));
                g2d.fillOval(cx - EXPLOSION_OUTER_RADIUS, cy - EXPLOSION_OUTER_RADIUS, EXPLOSION_OUTER_RADIUS * 2, EXPLOSION_OUTER_RADIUS * 2);

                // Zone Critique (Core) en Violet foncé
                g2d.setColor(new Color(75, 0, 130, 200));
                g2d.fillOval(cx - EXPLOSION_CORE_RADIUS, cy - EXPLOSION_CORE_RADIUS, EXPLOSION_CORE_RADIUS * 2, EXPLOSION_CORE_RADIUS * 2);

                // Reset de l'opacité
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            }
        }
    }
}