package Modele.Items;

import static Modele.Constantes.*;

/**
 * Projectile magique standard (Boule de feu).
 * Rapide, dégâts modérés.
 */
public class SortFeu extends Sort {

    /** ---------- [Constructeurs] ---------- **/

    /**
     * Instanciation lors du TIR.
     */
    public SortFeu(double x, double y, double dirX, double dirY) {
        super("Boule de Feu", PRIX_SORTS_FEU, IMAGE_SORT_FEU, 500, x, y, dirX, dirY);
        this.vitesse = 6.0;
    }

    /**
     * Instanciation pour l'INVENTAIRE.
     */
    public SortFeu() {
        super("Boule de Feu", PRIX_SORTS_FEU, IMAGE_SORT_FEU, 500);
    }
}