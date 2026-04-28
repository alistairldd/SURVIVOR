package Vue.VueSort;

import Modele.Items.SortTempete;
import java.awt.*;
import static Modele.Constantes.IMAGE_TEMPETE;

/**
 * Rendu graphique spécifique pour le sort "Tempête".
 * Gère l'interpolation visuelle, la transposition spatiale (caméra) et la rotation dynamique.
 */
public class VueSortTempete {

    /** ---------- [Constantes de Rendu] ---------- **/

    private static final int TAILLE_IMAGE = 250;

    /** ---------- [Méthodes Publiques - Rendu] ---------- **/

    /**
     * Applique les transformations graphiques (translation, rotation) pour rendre
     * l'effet visuel de la tempête en cohérence avec son vecteur de déplacement.
     *
     * @param g - Le contexte graphique 2D
     * @param sort - Le modèle contenant la physique du sort
     * @param offsetX - Décalage de la caméra en X
     * @param offsetY - Décalage de la caméra en Y
     */
    public void dessiner(Graphics g, SortTempete sort, double offsetX, double offsetY) {
        if (!sort.isActif()) return;

        Graphics2D g2d = (Graphics2D) g.create();

        // Optimisation du rendu visuel
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // Calcul du positionnement relatif à la fenêtre utilisateur
        int posX = (int) (sort.getX() - offsetX);
        int posY = (int) (sort.getY() - offsetY);

        // Alignement de l'axe de l'image sur le vecteur de mouvement
        double angle = Math.atan2(sort.getDirectionY(), sort.getDirectionX());

        // Modification de la matrice du contexte graphique
        g2d.translate(posX, posY);
        g2d.rotate(angle);

        // Rendu final centré sur le point de translation
        if (IMAGE_TEMPETE != null) {
            // Note d'implémentation future potentielle : AlphaComposite pour effet de transparence
            // g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));

            g2d.drawImage(
                    IMAGE_TEMPETE,
                    -TAILLE_IMAGE / 2,
                    -TAILLE_IMAGE / 2,
                    TAILLE_IMAGE,
                    TAILLE_IMAGE,
                    null
            );
        }

        g2d.dispose();
    }
}