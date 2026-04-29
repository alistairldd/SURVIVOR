package Modele.Items;

import static Modele.Constantes.IMAGE_NUKE;

/**
 * Consommable ultime déclenchant un effet de nettoyage total (Wipe) de la carte.
 * Représente la fin de partie / l'arme de destruction massive.
 */
public class Armageddon extends Item {

    /** ---------- [Constructeurs] ---------- **/

    public Armageddon() {
        super(
                "Armageddon",
                0,
                IMAGE_NUKE,
                5000
        );
    }
}