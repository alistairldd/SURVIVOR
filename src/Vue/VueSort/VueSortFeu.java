package Vue.VueSort;

import Modele.Items.SortFeu;
import java.awt.*;
import static Modele.Constantes.IMAGE_BOULE_FEU;

public class VueSortFeu {

    // Taille d'affichage de l'image (ajustez selon vos besoins)
    private static final int TAILLE_IMAGE = 300;

    /**
     * Dessine l'image du sort de feu à l'écran.
     */
    public void dessiner(Graphics g, SortFeu sort, double offsetX, double offsetY) {
        if (!sort.isActif()) return;

        Graphics2D g2d = (Graphics2D) g.create();

        // 1. Amélioration de la qualité
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // 2. Calcul de la position à l'écran (déjà translatée par la caméra dans Vue.java)
        int posX = (int) (sort.getX() - offsetX);
        int posY = (int) (sort.getY() - offsetY);

        // 3. Calcul de l'angle de rotation basé sur la direction du sort
        // On récupère les directions X et Y depuis l'objet sort
        double angle = Math.atan2(sort.getDirectionY(), sort.getDirectionX());

        // 4. Application de la rotation
        g2d.translate(posX, posY); // On déplace le point de pivot au centre du sort
        g2d.rotate(angle );         // On fait pivoter le "pinceau"

        // 5. Dessin de l'image
        if (IMAGE_BOULE_FEU != null) {
            // Note : On dessine à (-TAILLE/2) car le Graphics est déjà translaté au centre (posX, posY)
            g2d.drawImage(IMAGE_BOULE_FEU,
                    -TAILLE_IMAGE / 2,
                    -TAILLE_IMAGE / 2,
                    TAILLE_IMAGE,
                    TAILLE_IMAGE,
                    null);
        } else {
            g2d.setColor(Color.RED);
            g2d.fillOval(-10, -10, 20, 20);
        }

        g2d.dispose();

    }

}