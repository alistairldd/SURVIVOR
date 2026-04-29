package Modele.Items;

import static Modele.Constantes.*;

/**
 * Projectile magique lourd (Sort de tempête).
 * Plus lent qu'une boule de feu, mais applique un effet de recul (Knockback).
 */
public class SortTempete extends Sort {

    /** ---------- [Constructeurs] ---------- **/

    /**
     * Instanciation lors du TIR.
     */
    public SortTempete(double x, double y, double dirX, double dirY) {
        super("Sort de Tempête", 75, IMAGE_SORT_TEMPETE, TEMPETE_KNOCKBACK_FORCE, x, y, dirX, dirY);
        this.vitesse = 4.0;
        this.porteeMax = 400.0;
    }

    /**
     * Instanciation pour l'INVENTAIRE.
     */
    public SortTempete() {
        super("Sort de Tempête", 100, IMAGE_SORT_TEMPETE, 300);
    }
}