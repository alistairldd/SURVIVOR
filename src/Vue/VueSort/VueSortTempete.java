package Vue.VueSort;

import Modele.Items.SortTempete;
import java.awt.*;
import static Modele.Constantes.IMAGE_TEMPETE; // Remplacez par IMAGE_TEMPETE si disponible

public class VueSortTempete {

    // Taille d'affichage
    private static final int TAILLE_IMAGE = 250;

    /**
     * Dessine l'image du sort de tempête à l'écran.
     */
    public void dessiner(Graphics g, SortTempete sort, double offsetX, double offsetY) {
        if (!sort.isActif()) return;

        Graphics2D g2d = (Graphics2D) g.create();

        // Amélioration de la qualité
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // Position écran
        int posX = (int) (sort.getX() - offsetX);
        int posY = (int) (sort.getY() - offsetY);

        // Rotation vers la direction du mouvement
        double angle = Math.atan2(sort.getDirectionY(), sort.getDirectionX());

        g2d.translate(posX, posY);
        g2d.rotate(angle);

        // Dessin (on peut ajouter une transparence bleue pour l'effet vent/tempête)
        if (IMAGE_TEMPETE != null) {
            // Optionnel : Teinter en bleu/blanc pour différencier du feu
            // g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));

            g2d.drawImage(IMAGE_TEMPETE,
                    -TAILLE_IMAGE / 2,
                    -TAILLE_IMAGE / 2,
                    TAILLE_IMAGE,
                    TAILLE_IMAGE,
                    null);
        }

        g2d.dispose();
    }
}