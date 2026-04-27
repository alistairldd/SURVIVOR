package Modele.Items;

import static Modele.Constantes.*;

import static Modele.Constantes.IMAGE_SORT_FEU;

public class SortFeu extends Sort {

    public SortFeu(double x, double y, double dirX, double dirY) {
        super("Boule de Feu", 50, IMAGE_SORT_FEU, 20, x, y, dirX, dirY);
        this.vitesse = 6.0; // Plus rapide qu'un sort moyen
    }

    public SortFeu() {
        super("Boule de Feu", 50, IMAGE_SORT_FEU, 20);
    }
}