package Modele.Items;

import static Modele.Constantes.IMAGE_BOULE_FEU; // Vous pouvez utiliser une image existante ou en ajouter une nouvelle

public class Armageddon extends Item {
    public Armageddon() {
        super("Armageddon",
                0,
                IMAGE_BOULE_FEU,
                200); // Prix élevé pour cet objet puissant
    }
}