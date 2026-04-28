package Vue;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Système de textes flottants animés.
 * Il fournit un feedback visuel léger directement dans le monde de jeu,
 * avec une animation courte de montée puis de disparition.
 */
public class VueTexteFlottant {

    /** ---------- [Classe Interne - Élément animé] ---------- **/

    /**
     * Représente un texte flottant autonome avec son propre état d'animation.
     */
    private static class TexteFlottant {

        /** ---------- [Constantes de rendu] ---------- **/

        private static final Font FONT_TEXTE = new Font("Arial", Font.BOLD, 18);
        private static final double VITESSE_MONTEE = 0.9;
        private static float VITESSE_FONDU = 0.02f;

        /** ---------- [Propriétés] ---------- **/

        private Color couleur;

        final String texte;
        double x;
        double y;
        float alpha;

        /** ---------- [Constructeurs] ---------- **/

        /**
         * Initialise un texte flottant dans l'espace monde.
         *
         * @param texte - Message à afficher
         * @param x - Coordonnée X monde
         * @param y - Coordonnée Y monde
         * @param couleur - Couleur du texte, utilisée aussi pour adapter certains timings visuels
         */
        TexteFlottant(String texte, double x, double y, Color couleur) {
            this.texte = texte;
            this.x = x;
            this.y = y;
            this.alpha = 1.0f;
            this.couleur = couleur;

            // Les messages de gain doivent disparaître plus vite pour ne pas encombrer l'écran.
            if (couleur.equals(Color.YELLOW)) {
                VITESSE_FONDU = 0.1f;
            }
        }

        /** ---------- [Méthodes d'instance - Cycle de vie] ---------- **/

        /**
         * Fait progresser l'animation d'une frame.
         * Le texte monte légèrement tout en perdant progressivement son opacité.
         */
        void miseAJour() {
            y -= VITESSE_MONTEE;
            alpha -= VITESSE_FONDU;
        }

        /**
         * Indique si l'animation est terminée et que l'élément peut être retiré.
         *
         * @return true lorsque le texte est devenu totalement transparent
         */
        boolean estTermine() {
            return alpha <= 0f;
        }

        /**
         * Dessine le texte à son état courant.
         *
         * @param g2d - Contexte graphique principal
         */
        void dessiner(Graphics2D g2d) {
            g2d.setFont(FONT_TEXTE);

            FontMetrics fm = g2d.getFontMetrics();
            Rectangle2D rect = fm.getStringBounds(texte, g2d);

            // Le texte est centré sur son point d'ancrage pour un positionnement plus naturel.
            int textX = (int) x - (int) (rect.getWidth() / 2);
            int textY = (int) y;

            AlphaComposite oldComposite = (AlphaComposite) g2d.getComposite();
            g2d.setColor(couleur);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2d.drawString(texte, textX, textY);
            g2d.setComposite(oldComposite);
        }
    }

    /** ---------- [Propriétés - Gestionnaire] ---------- **/

    private final List textes = new ArrayList<>();

    /** ---------- [Méthodes Publiques - Gestion] ---------- **/

    /**
     * Ajoute un nouveau texte flottant à afficher dans le monde.
     *
     * @param texte - Message à afficher
     * @param mondeX - Coordonnée X monde
     * @param mondeY - Coordonnée Y monde
     * @param couleur - Couleur du feedback
     */
    public void ajouter(String texte, double mondeX, double mondeY, Color couleur) {
        textes.add(new TexteFlottant(texte, mondeX, mondeY, couleur));
    }

    /**
     * Met à jour tous les textes actifs et supprime ceux dont l'animation est terminée.
     * Cette méthode est conçue pour être appelée une fois par frame.
     */
    public void miseAJour() {
        Iterator it = textes.iterator();
        while (it.hasNext()) {
            TexteFlottant t = (TexteFlottant) it.next();
            t.miseAJour();

            if (t.estTermine()) it.remove();
        }
    }

    /**
     * Dessine l'ensemble des textes actifs.
     * L'appel doit être fait dans le repère monde, après application de la caméra.
     *
     * @param g2d - Contexte graphique principal
     */
    public void dessiner(Graphics2D g2d) {
        for (Object texte : textes) {
            TexteFlottant t = (TexteFlottant) texte;
            t.dessiner(g2d);
        }
    }
}