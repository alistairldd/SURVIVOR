package Modele;

import java.util.ArrayList;

import static Vue.VueJoueur.TAILLE;

public class Ressource {
    public static final int[] TYPE_RESSOURCE = {0, 1, 2};

    private int positionX;
    private int positionY;
    private int type;

    public Ressource() {
        // MODIFICATION ICI :
        // On génère un nombre entre 0 et 2000, puis on retire la moitié (1000).
        // Résultat : on a des coordonnées entre -1000 et +1000.

        // Cependant, pour éviter que les ressources soient trop proches des bords de la carte,
        int offsetDecale = 10 + TAILLE /2;

        this.positionX = offsetDecale + (int)(Math.random() * (Map.LARGEUR_MAP - 2* (double) offsetDecale));
        this.positionY = offsetDecale + (int)(Math.random() * (Map.HAUTEUR_MAP - 2* (double) offsetDecale));

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