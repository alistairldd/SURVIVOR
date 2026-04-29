package Modele.Items;

import static Modele.Constantes.*;

/**
 * Consommable de soin basique.
 * Restaure une quantité modérée de Points de Vie (HP) au joueur.
 */
public class PotionVie extends Item {

    /** ---------- [Constructeurs] ---------- **/

    public PotionVie() {
        super(
                "Potion de Vie",
                15,
                IMAGE_POTION_DE_VIE,
                PRIX_POTION_VIE
        );
    }
}