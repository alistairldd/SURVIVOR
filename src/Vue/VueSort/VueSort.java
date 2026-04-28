package Vue.VueSort;

import Modele.Items.Sort;
import Modele.Items.SortFeu;
import Modele.Items.SortTempete;

import java.awt.*;

/**
 * Orchestrateur graphique des projectiles magiques.
 * Agit comme une fabrique de rendu (Pattern Strategy léger) en déléguant
 * le dessin spécifique de chaque sort à sa classe de vue dédiée.
 */
public class VueSort {

    /** ---------- [Propriétés - Sous-Vues] ---------- **/

    private final VueSortFeu vueSortFeu = new VueSortFeu();
    private final VueSortTempete vueSortTempete = new VueSortTempete();

    /** ---------- [Méthodes Publiques - Rendu] ---------- **/

    /**
     * Intercepte l'instance générique du sort, prépare le contexte graphique
     * avec anticrénelage, puis délègue le dessin selon le type réel (RTTI).
     *
     * @param g - Le contexte graphique principal (Graphics)
     * @param sort - L'instance du sort à afficher
     * @param cameraX - Le décalage X lié à la position de la caméra
     * @param cameraY - Le décalage Y lié à la position de la caméra
     */
    public void dessiner(Graphics g, Sort sort, double cameraX, double cameraY) {
        if (sort == null || !sort.isActif()) return;

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (sort instanceof SortFeu) {
            vueSortFeu.dessiner(g, (SortFeu) sort, cameraX, cameraY);
        }
        if (sort instanceof SortTempete) {
            vueSortTempete.dessiner(g, (SortTempete) sort, cameraX, cameraY);
        }

        g2d.dispose();
    }
}