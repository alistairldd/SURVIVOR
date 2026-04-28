package Modele.Items;

import static Modele.Constantes.IMAGE_PIOCHE;

/**
 * Objet utilitaire débloquant la mécanique de minage passif (Bâtiment Mine).
 * Ne possède pas d'effet direct sur les statistiques du joueur.
 */
public class Pioche extends Item {

    /** ---------- [Propriétés] ---------- **/

    private boolean utilisee;

    /** ---------- [Constructeurs] ---------- **/

    public Pioche() {
        super(
                "Pioche",
                0,
                IMAGE_PIOCHE,
                30
        );
        this.utilisee = false;
    }

    /** ---------- [Accesseurs] ---------- **/

    public boolean getUtilisee() {
        return utilisee;
    }

    public void setUtilisee(boolean utilisee) {
        this.utilisee = utilisee;
    }
}