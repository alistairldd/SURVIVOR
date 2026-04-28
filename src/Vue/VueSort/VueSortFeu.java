package Vue.VueSort;

import Modele.Items.SortFeu;
import java.awt.*;
import static Modele.Constantes.IMAGE_BOULE_FEU;

/**
 * Rendu graphique spécifique pour le sort "Boule de Feu".
 * Gère l'interpolation visuelle, la transposition des coordonnées monde vers écran,
 * et la rotation dynamique du sprite selon le vecteur de direction.
 */
public class VueSortFeu {

    /** ---------- [Constantes de Rendu] ---------- **/

    private static final int TAILLE_IMAGE = 300;

    /** ---------- [Méthodes Publiques - Rendu] ---------- **/

    /**
     * Calcule la matrice de transformation pour dessiner la boule de feu
     * orientée dans le sens de sa trajectoire.
     *
     * @param g - Le contexte graphique 2D
     * @param sort - Le modèle contenant les données physiques (position, vecteur direction)
     * @param offsetX - Décalage de la caméra en X
     * @param offsetY - Décalage de la caméra en Y
     */
    public void dessiner(Graphics g, SortFeu sort, double offsetX, double offsetY) {
        if (!sort.isActif()) return;

        Graphics2D g2d = (Graphics2D) g.create();

        // Optimisation du rendu visuel (lissage des contours et des textures)
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // Transposition des coordonnées absolues du modèle en coordonnées relatives à l'écran
        int posX = (int) (sort.getX() - offsetX);
        int posY = (int) (sort.getY() - offsetY);

        // Résolution de l'angle de tir (en radians) depuis les vecteurs normalisés
        double angle = Math.atan2(sort.getDirectionY(), sort.getDirectionX());

        // Application des transformations géométriques
        g2d.translate(posX, posY);
        g2d.rotate(angle);

        // Dessin du sprite (centré sur le nouveau point d'origine) ou fallback visuel
        if (IMAGE_BOULE_FEU != null) {
            g2d.drawImage(
                    IMAGE_BOULE_FEU,
                    -TAILLE_IMAGE / 2,
                    -TAILLE_IMAGE / 2,
                    TAILLE_IMAGE,
                    TAILLE_IMAGE,
                    null
            );
        } else {
            g2d.setColor(Color.RED);
            g2d.fillOval(-10, -10, 20, 20);
        }

        g2d.dispose();
    }
}