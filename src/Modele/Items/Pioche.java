package Modele.Items;

import java.util.List;

import static Modele.Constantes.IMAGE_PIOCHE;

public class Pioche extends Item{
    private boolean utilisee;

    public Pioche() {
        super("Pioche",
                0,
                IMAGE_PIOCHE,
                30
        );
        this.utilisee = false;

    }

    public boolean getUtilisee() {
        return utilisee;
    }

    public void setUtilisee(boolean utilisee) {
        this.utilisee = utilisee;
    }
}
