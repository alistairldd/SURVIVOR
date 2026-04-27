package Vue;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Système de textes flottants animés (feedback visuel d'erreur).
 * Chaque texte apparaît en rouge au point de clic, monte progressivement
 * et disparaît en fondu — inspiré des jeux type Boom Beach.
 *
 * Cycle de vie d'un texte : Apparition → Montée + Fondu → Suppression.
 */
public class VueTexteFlottant {

    // ---------------------------------------------------------------
    // Entité interne : un texte flottant avec son propre état
    // ---------------------------------------------------------------
    private static class TexteFlottant {

        // Paramètres visuels
        private static final Font FONT_TEXTE  = new Font("Arial", Font.BOLD, 18);

        // Paramètres d'animation (par frame, ~60 fps)
        private static final double VITESSE_MONTEE  = 0.9;  // pixels/frame
        private static final float  VITESSE_FONDU   = 0.1f; // alpha/frame  → ~55 frames ≈ 0.9 s

        private Color couleur;

        final String texte;
        double x;
        double y;
        float  alpha;

        /**
         *
         * @param texte
         * @param x
         * @param y
         */
        TexteFlottant(String texte, double x, double y, Color couleur) {
            this.texte = texte;
            this.x     = x;
            this.y     = y;
            this.alpha = 1.0f;
            this.couleur = couleur;
        }

        /** Avance l'animation d'une frame. */
        void miseAJour() {
            y     -= VITESSE_MONTEE;
            alpha -= VITESSE_FONDU;
        }

        boolean estTermine() {
            return alpha <= 0f;
        }

        /** Dessine le texte avec son ombre portée. */
        void dessiner(Graphics2D g2d) {

            g2d.setFont(FONT_TEXTE);
            FontMetrics fm   = g2d.getFontMetrics();
            Rectangle2D rect = fm.getStringBounds(texte, g2d);
            int textX = (int) x - (int) (rect.getWidth()  / 2);
            int textY = (int) y;

            g2d.setColor(couleur);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha)); // Appliquer la transparence
            g2d.drawString(texte, textX, textY);

        }
    }

    // ---------------------------------------------------------------
    // Gestionnaire de la liste de textes actifs
    // ---------------------------------------------------------------
    private final List<TexteFlottant> textes = new ArrayList<>();

    /**
     * Ajoute un nouveau texte flottant en rouge.
     *
     * @param texte   Message à afficher (ex. "Impossible !", "Rechargement…")
     * @param mondeX  Coordonnée X dans l'espace monde (pas écran)
     * @param mondeY  Coordonnée Y dans l'espace monde (pas écran)
     */
    public void ajouter(String texte, double mondeX, double mondeY, Color couleur) {
        textes.add(new TexteFlottant(texte, mondeX, mondeY, couleur));
    }

    /**
     * Met à jour tous les textes actifs et supprime ceux qui ont disparu.
     * À appeler une fois par frame, dans paintComponent.
     */
    public void miseAJour() {
        Iterator<TexteFlottant> it = textes.iterator();
        while (it.hasNext()) {
            TexteFlottant t = it.next();
            t.miseAJour();
            if (t.estTermine()) it.remove();
        }
    }

    /**
     * Dessine tous les textes actifs.
     * Doit être appelé APRÈS la translation caméra (espace monde).
     */
    public void dessiner(Graphics2D g2d) {
        for (TexteFlottant t : textes) {
            t.dessiner(g2d);
        }
    }
}
