package Modele.Items;

import static Modele.Constantes.IMAGE_POTION_GRANDE_VIE;

/**
 * Consommable de soin majeur.
 * Restaure une grande quantité de Points de Vie (HP) au joueur.
 */
public class PotionVieGrande extends Item {

    /** ---------- [Constructeurs] ---------- **/

    public PotionVieGrande() {
        super(
                "Grande potion de Vie",
                40,
                IMAGE_POTION_GRANDE_VIE,
                50
        );
    }
}