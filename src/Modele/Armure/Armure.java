package Modele.Armure;

import java.awt.*;
import java.util.List;

public abstract class Armure {
    // Nom affiché de l'armure
    private String nom;
    // Quantité de points de vie ajoutés au joueur lorsqu'il équipe cette armure
    private int reduction;
    // Image représentant l'arme (optionnel, peut être utilisé pour l'affichage)
    private Image image;
    // Liste des ressources nécessaires pour acheter / fabriquer l'armure
    private List ressourcesNecessaires;

    // Poids de l'armure (pour baisser la vitesse)
    private int vitesse;

    public Armure(String nom, int reduction, Image image, int vitesse, List ressourcesNecessaires) {
        this.nom = nom;
        this.reduction = reduction;
        this.image = image;
        this.vitesse = vitesse ;
        this.ressourcesNecessaires = ressourcesNecessaires;
    }

    // Retourne le bonus de vie de l'armure
    public int getReduction() {
        return reduction;
    }

    // Retourne le nom de l'armure
    public String getNom() {
        return nom;
    }

    // Retourne l'image de l'armure
    public Image getImage() {return image;}

    // Retourne le poids de l'armure
    public int getVitesse() {
        return vitesse;
    }

    // Retourne la liste des ressources nécessaires pour acheter / fabriquer l'armure
    public List<String> getRessourcesNecessaires() {return ressourcesNecessaires;}


}
