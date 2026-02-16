package Modele;

import java.util.ArrayList;

public static final int LARGEUR_MAP = 1000;
public static final int HAUTEUR_MAP = 1000;

public class Map {
    private int largeur;
    private int hauteur;
    private ArrayList<Ressource> ressources;

    public Map() {
        this.largeur = LARGEUR_MAP;
        this.hauteur = HAUTEUR_MAP;
        this.ressources = new ArrayList<>();
        this.ressources = genereRessources();
    }
}
