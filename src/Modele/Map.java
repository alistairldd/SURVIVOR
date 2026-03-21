package Modele;

import java.util.ArrayList;

public class Map {
    public static final int LARGEUR_MAP = 3000;
    public static final int HAUTEUR_MAP = 3000;

    private int largeur;
    private int hauteur;
    private ArrayList<Ressource> ressources;
    private ArrayList<Batiment> batiments;

    public Map() {
        this.largeur = LARGEUR_MAP;
        this.hauteur = HAUTEUR_MAP;
        this.ressources = new ArrayList<>();
        this.batiments = new ArrayList<>();
        this.batiments.add(new HQ());
        //test en vif
        // On stocke la tour dans une variable avant de l'ajouter
        Tower maTourBlessee = new Tower(largeur/2 + 100, hauteur/2+100);
        maTourBlessee.setHp(10); // On lui met 10 HP
        this.batiments.add(maTourBlessee); // On l'ajoute à la liste

        this.batiments.add(new Tower(largeur/2 - 100, hauteur/2 - 100));
        this.batiments.add(new Tower(largeur/2,  70));
    }

    public void viderRessources() {
        this.ressources = new ArrayList<>();
    }

    // supprime une ressource de la map
    public void deleteResources(Ressource ressource){ this.ressources.remove(ressource);}

    public ArrayList<Ressource> getRessources() {
        return ressources;
    }

    public ArrayList<Batiment> getBatiments() { return batiments; }

}
