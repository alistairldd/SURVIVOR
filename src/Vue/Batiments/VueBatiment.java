package Vue.Batiments;

import java.awt.*;

import Modele.Batiments.*;

import static Modele.Constantes.*;

/**
 * Aiguilleur de rendu des bâtiments.
 * Centralise la délégation vers la vue spécialisée de chaque type et découpe
 * le rendu en deux passes pour respecter la profondeur visuelle 2.5D.
 */
public class VueBatiment {

    /** ---------- [Constructeurs] ---------- **/

    public VueBatiment() {}

    /** ---------- [Rendu - Passes principales] ---------- **/

    /**
     * Dessine les éléments au sol d'un bâtiment avant le tri de profondeur.
     * Cette passe isole les auras et zones d'effet afin qu'elles restent sous
     * les entités et les volumes rendus ensuite.
     *
     * @param g2d - Contexte graphique de dessin
     * @param b - Bâtiment à rendre
     * @param x - Position X écran du bâtiment
     * @param y - Position Y écran du bâtiment
     */
    public static void dessinerAura(Graphics2D g2d, Batiment b, int x, int y) {
        if (b instanceof Tower) {
            VueTower.dessinerAura(g2d, (Tower) b, x, y);
        }

        else if (b instanceof TenteDeSoin) {
            VueTente.dessinerAura(g2d, (TenteDeSoin) b, x, y);
        }

        else if (b instanceof Mortier) {
            VueMortier.dessinerAura(g2d, (Mortier) b, x, y);
        }

        // Le HQ et la Mine n'ont pas d'effet de sol dédié dans cette passe.
    }

    /**
     * Dessine la représentation principale d'un bâtiment après tri visuel.
     * Cette passe gère le volume du bâtiment et ses éventuels overlays afin de
     * conserver une cohérence avec le Y-sorting du reste de la scène.
     *
     * @param g2d - Contexte graphique de dessin
     * @param b - Bâtiment à rendre
     * @param x - Position X écran du bâtiment
     * @param y - Position Y écran du bâtiment
     * @param minimap - Indique si le rendu cible la minimap plutôt que la vue monde
     */
    public static void dessinerSprite(Graphics2D g2d, Batiment b, int x, int y, boolean minimap) {
        if (b instanceof HQ) {
            VueHQ.dessiner(g2d, (HQ) b, x, y, minimap);
        }

        else if (b instanceof Tower) {
            VueTower.dessinerSprite(g2d, (Tower) b, x, y, minimap);
        }

        else if (b instanceof Mine) {
            VueMine.dessiner(g2d, (Mine) b, x, y, minimap);
        }

        else if (b instanceof TenteDeSoin) {
            VueTente.dessinerSprite(g2d, (TenteDeSoin) b, x, y, minimap);
        }

        else if (b instanceof Modele.Batiments.Abatis) {
            VueAbatis.dessinerSprite(g2d, (Modele.Batiments.Abatis) b, x, y, minimap);
        }

        else if (b instanceof Mortier) {
            VueMortier.dessinerSprite(g2d, (Mortier) b, x, y, minimap);
        }

        else {
            // Rendu de secours pour éviter une absence visuelle si un type n'est pas encore mappé.
            g2d.setColor(Color.GRAY);
            int taille = minimap ? TAILLE_BATIMENT_MINIMAP : TAILLE_TOUR;
            g2d.fillRect(x - (taille / 2), y - (taille / 2), taille, taille);
        }
    }

    /** ---------- [Compatibilité - Appels simplifiés] ---------- **/

    /**
     * Point d'entrée simplifié pour les rendus ne nécessitant pas la passe d'aura
     * ni de tri de profondeur avancé, notamment la minimap.
     *
     * @param g - Contexte graphique source
     * @param b - Bâtiment à rendre
     * @param x - Position X écran du bâtiment
     * @param y - Position Y écran du bâtiment
     * @param minimap - Indique si le rendu cible la minimap
     */
    public static void dessinerBatiment(Graphics g, Batiment b, int x, int y, boolean minimap) {
        Graphics2D g2d = (Graphics2D) g.create();
        dessinerSprite(g2d, b, x, y, minimap);
        g2d.dispose();
    }
}