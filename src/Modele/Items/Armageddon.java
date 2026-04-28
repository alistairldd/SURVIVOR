package Modele.Items;

import static Modele.Constantes.IMAGE_BOULE_FEU; // Vous pouvez utiliser une image existante ou en ajouter une nouvelle
import static Modele.Constantes.IMAGE_NUKE;

public class Armageddon extends Item {
    public Armageddon() {
        super("Armageddon",
                0,
                IMAGE_NUKE,
                1000); // Prix élevé pour cet objet puissant
    }
}