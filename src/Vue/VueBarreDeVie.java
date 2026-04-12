package Vue;

import Modele.Localisable;
import java.awt.*;

/**
 * Composant de rendu pour les jauges de santé.
 * #DEV : Utilise l'interface Localisable pour une compatibilité universelle (Joueur, Monstre, Batiment).
 */
public class VueBarreDeVie {

    // Configuration visuelle (Dimensions en pixels)
    private static final int LARGEUR_BARRE = 45;
    private static final int HAUTEUR_BARRE = 6;
    private static final int DECALAGE_Y = 30; // Distance au-dessus de l'entité

    /**
     * Calcule la couleur et dessine la jauge de vie.
     * #TRD : Analyse du ratio HP pour déterminer le niveau de risque (Couleurs).
     */
    public static void dessiner(Graphics2D g2d, Localisable entite) {
        // 1. Extraction des data via Localisable
        int hpActuels = entite.getHp();
        int hpMax = entite.getMaxHp();

        // Sécurité : Si l'entité est déjà "Liquidée" ou invalide, on ne dessine rien
        if (hpMax <= 0 || hpActuels <= 0) return;

        // 2. Calcul du ratio de santé
        double ratio = (double) hpActuels / hpMax;
        if (ratio > 1.0) ratio = 1.0;

        // 3. Logique de couleur (Paliers demandés)
        Color couleurJauge;
        if (ratio >= 0.7) {
            couleurJauge = new Color(46, 204, 113); // Vert (Bullish)
        } else if (ratio >= 0.2) {
            couleurJauge = new Color(241, 196, 15); // Jaune (Side)
        } else if (ratio >= 0.1) {
            couleurJauge = new Color(230, 126, 34); // Orange (Warning)
        } else {
            couleurJauge = new Color(192, 57, 43);  // Rouge (Bearish/Panic)
        }

        // 4. Positionnement (Coordonnées relatives au monde)
        int x = (int) entite.getX() - (LARGEUR_BARRE / 2);
        int y = (int) entite.getY() - DECALAGE_Y;

        // 5. Rendu graphique (Double Rect)
        // Fond de la barre (Conteneur)
        g2d.setColor(new Color(30, 30, 30, 180));
        g2d.fillRect(x, y, LARGEUR_BARRE, HAUTEUR_BARRE);

        // Remplissage dynamique selon le ratio
        int largeurRemplie = (int) (LARGEUR_BARRE * ratio);
        g2d.setColor(couleurJauge);
        g2d.fillRect(x, y, largeurRemplie, HAUTEUR_BARRE);

        // Bordure de finition pour la netteté
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRect(x, y, LARGEUR_BARRE, HAUTEUR_BARRE);
    }
}