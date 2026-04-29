package Modele.Items;

import static Modele.Constantes.*;

/**
 * Consommable de mobilité temporaire.
 * Augmente la vitesse de déplacement du joueur.
 */
public class PotionVitesse extends Item {

    /** ---------- [Constructeurs] ---------- **/

    public PotionVitesse() {
        super(
                "Potion de Vitesse",
                5,
                IMAGE_POTION_VITESSE,
                PRIX_POTION_VITESSE
        );
    }
}