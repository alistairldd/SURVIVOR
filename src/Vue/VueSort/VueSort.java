package Vue.VueSort;

import Modele.Items.Sort;
import Modele.Items.SortFeu;
import Modele.Items.SortTempete;

import java.awt.*;

/**
 * Responsable du dessin de tous les sorts sur la carte.
 * Gère les différents types de sorts en déléguant au type approprié.
 */
public class VueSort {

    private final VueSortFeu vueSortFeu = new VueSortFeu();
    private final VueSortTempete vueSortTempete = new VueSortTempete();
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
        if (sort instanceof SortTempete) { // Nouveau bloc pour la tempête
            vueSortTempete.dessiner(g, (SortTempete) sort, cameraX, cameraY);
        }

        g2d.dispose();
    }


}
