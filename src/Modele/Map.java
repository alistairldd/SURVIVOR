package Modele;

import java.util.ArrayList;

public class Map {
    public static final int LARGEUR_MAP = 1600;
    public static final int HAUTEUR_MAP = 1100;

    private int largeur;
    private int hauteur;
    private ArrayList<Ressource> ressources;

    public Map() {
        this.largeur = LARGEUR_MAP;
        this.hauteur = HAUTEUR_MAP;
        this.ressources = new ArrayList<>();
        this.ressources = Ressource.genereRessources(100);
    }

    public ArrayList<Ressource> getRessources() {
        return ressources;
    }

}
