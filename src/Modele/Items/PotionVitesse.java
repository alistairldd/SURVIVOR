package Modele.Items;

import static Modele.Constantes.IMAGE_POTION_VITESSE;

public class PotionVitesse extends Item{

    public PotionVitesse() {
        super("Potion de Vitesse",
                5,
                IMAGE_POTION_VITESSE, // Image à définir
                20
        );
    }
}
