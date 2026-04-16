package Modele.Items;

import java.awt.*;
import java.util.List;

import static Modele.Constantes.IMAGE_POTION_DE_VIE;
import static Modele.Constantes.IMAGE_POTION_GRANDE_VIE;

public class PotionVieGrande extends Item{

    public PotionVieGrande() {
        super("Grande potion de Vie",
                40,
                IMAGE_POTION_GRANDE_VIE,
                50
        );
    }

}
