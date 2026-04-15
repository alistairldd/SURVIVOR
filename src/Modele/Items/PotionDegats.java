package Modele.Items;

import static Modele.Constantes.IMAGE_POTION_DEGATS;

public class PotionDegats extends Item{

    public PotionDegats() {
        super("Potion de Dégâts",
                5,
                IMAGE_POTION_DEGATS, // Image à définir
                20
        );
    }

}
