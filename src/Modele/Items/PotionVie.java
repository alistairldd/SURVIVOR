package Modele.Items;

import java.util.List;

import static Modele.Constantes.IMAGE_POTION_DE_VIE;

public class PotionVie extends Item{
    private int soin;

    public PotionVie() {
        super("Potion de Vie",
                50,
                IMAGE_POTION_DE_VIE,
                15
        );
        this.soin = 50;
    }

    public int getSoin() {
        return soin;
    }
}
