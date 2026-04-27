package Vue.VueSort;

import Modele.Items.Sort;
import Modele.Items.SortFeu;
import java.awt.*;

/**
 * Responsable du dessin de tous les sorts sur la carte.
 * Gère les différents types de sorts en déléguant au type approprié.
 */
public class VueSort {


    private static final int TAILLE_SORT = 20;
    private static final Color COULEUR_SORT = new Color(255, 100, 0, 200);
    private final VueSortFeu vueSortFeu = new VueSortFeu();

    /**
     * Dessine un sort à l'écran selon son type.
     * @param g Le contexte graphique
     * @param sort Le sort à afficher
     */
    public void dessiner(Graphics g, Sort sort, double cameraX, double cameraY) {
        if (sort == null || !sort.isActif()) return;

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (sort instanceof SortFeu) {
            vueSortFeu.dessiner(g, (SortFeu) sort, cameraX, cameraY);
        }

        g2d.dispose();
    }


}
