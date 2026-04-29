package Modele.Items;

import static Modele.Constantes.*;

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
                PRIX_PIOCHE
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