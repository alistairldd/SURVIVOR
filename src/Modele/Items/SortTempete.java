package Modele.Items;

import Modele.Constantes;

import static Modele.Constantes.*;

public class SortTempete extends Sort {

    // Constructeur pour le lancement du sort (appelé par le Gestionnaire de Sorts)
    public SortTempete(double x, double y, double dirX, double dirY) {
        // "Sort de Tempête", dégâts, image, prix, positionInit, direction
        super("Sort de Tempête", 100, IMAGE_SORT_TEMPETE, TEMPETE_KNOCKBACK_FORCE, x, y, dirX, dirY);
        this.vitesse = 4.0;    // Plus lent qu'une boule de feu pour simuler une tempête lourde
        this.porteeMax = 400.0; // Portée plus courte
    }

    // Constructeur pour l'inventaire (utilisé par le Shop)
    public SortTempete() {
        super("Sort de Tempête", 100, IMAGE_SORT_TEMPETE, 300);
    }
}