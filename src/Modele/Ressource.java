package Modele;

import java.util.ArrayList;

public class Ressource {
    public static final int[] TYPE_RESSOURCE = {0, 1, 2};

    private int positionX;
    private int positionY;
    private int type;

    public Ressource() {
        // MODIFICATION ICI :
        // On génère un nombre entre 0 et 2000, puis on retire la moitié (1000).
        // Résultat : on a des coordonnées entre -1000 et +1000.
        this.positionX = (int)(Math.random() * Map.LARGEUR_MAP) - (Map.LARGEUR_MAP / 2);
        this.positionY = (int)(Math.random() * Map.HAUTEUR_MAP) - (Map.HAUTEUR_MAP / 2);

        int index = (int)(Math.random() * TYPE_RESSOURCE.length);
        this.type = TYPE_RESSOURCE[index];
    }

    public static ArrayList<Ressource> genereRessources(int nbRessouces) {
        ArrayList<Ressource> ressources = new ArrayList<>();
        for (int i = 0; i < nbRessouces; i++) {
            ressources.add(new Ressource());
        }
        return ressources;
    }

    public int getPositionX() { return positionX; }
    public int getPositionY() { return positionY; }
    public int getType() { return type; }
}