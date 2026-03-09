package Modele;

import java.util.ArrayList;

public class Map {
    public static final int LARGEUR_MAP = 3000;
    public static final int HAUTEUR_MAP = 3000;

    private int largeur;
    private int hauteur;
    private ArrayList<Ressource> ressources;

    public Map() {
        this.largeur = LARGEUR_MAP;
        this.hauteur = HAUTEUR_MAP;
        ressources = new ArrayList<>();
    }

    public void viderRessources() {
        this.ressources = new ArrayList<>();
    }

    public ArrayList<Ressource> getRessources() {
        return ressources;
    }

}
