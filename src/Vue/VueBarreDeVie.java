package Vue;

import Modele.Localisable;
import java.awt.*;

/**
 * Composant de rendu pour les jauges de santé.
 * S'appuie sur l'interface Localisable afin de rester réutilisable pour toutes
 * les entités affichables disposant d'un état de vie.
 */
public class VueBarreDeVie {

    /** ---------- [Constantes de rendu] ---------- **/

    private static final int LARGEUR_BARRE = 45;
    private static final int HAUTEUR_BARRE = 6;
    private static final int DECALAGE_Y = 30;

    /** ---------- [Méthodes Publiques - Rendu] ---------- **/

    /**
     * Dessine la barre de vie d'une entité à partir de son état courant.
     * La couleur change par paliers pour donner une lecture immédiate du niveau
     * de danger sans nécessiter la lecture d'une valeur numérique.
     *
     * @param g2d - Contexte graphique principal
     * @param entite - Entité dont la jauge doit être affichée
     */
    public static void dessiner(Graphics2D g2d, Localisable entite) {
        int hpActuels = entite.getHp();
        int hpMax = entite.getMaxHp();

        // Les entités invalides ou déjà détruites n'ont plus besoin de feedback visuel de santé.
        if (hpMax <= 0 || hpActuels <= 0) return;

        double ratio = (double) hpActuels / hpMax;
        if (ratio > 1.0) ratio = 1.0;

        Color couleurJauge;
        if (ratio >= 0.7) {
            couleurJauge = new Color(46, 204, 113);
        } else if (ratio >= 0.2) {
            couleurJauge = new Color(241, 196, 15);
        } else if (ratio >= 0.1) {
            couleurJauge = new Color(230, 126, 34);
        } else {
            couleurJauge = new Color(192, 57, 43);
        }

        int x = (int) entite.getX() - (LARGEUR_BARRE / 2);
        int y = (int) entite.getY() - DECALAGE_Y;

        // Le fond sert de conteneur fixe pour rendre la perte de vie perceptible en un coup d'œil.
        g2d.setColor(new Color(30, 30, 30, 180));
        g2d.fillRect(x, y, LARGEUR_BARRE, HAUTEUR_BARRE);

        int largeurRemplie = (int) (LARGEUR_BARRE * ratio);
        g2d.setColor(couleurJauge);
        g2d.fillRect(x, y, largeurRemplie, HAUTEUR_BARRE);

        // La bordure améliore la lisibilité sur les décors chargés ou contrastés.
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRect(x, y, LARGEUR_BARRE, HAUTEUR_BARRE);
    }
}