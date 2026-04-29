package Modele.Items;

import static Modele.Constantes.*;

/**
 * Consommable offensif temporaire.
 * Augmente les dégâts du joueur pendant une durée limitée.
 */
public class PotionDegats extends Item {

    /** ---------- [Constructeurs] ---------- **/

    public PotionDegats() {
        super(
                "Potion de Dégâts",
                5,
                IMAGE_POTION_DEGATS,
                PRIX_POTION_DEGATS
        );
    }
}