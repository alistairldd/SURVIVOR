package Modele.Items;

import java.util.List;

public class PotionVie extends Item{
    private int soin;

    public PotionVie() {
        super("Potion de Vie",
                50,
                null,
                List.of("Or:5")
        );
        this.soin = 50;
    }

    public int getSoin() {
        return soin;
    }
}
