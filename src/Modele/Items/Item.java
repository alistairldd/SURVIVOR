package Modele.Items;

import java.awt.*;
import java.util.List;
import java.util.Objects;


public abstract class Item{
    // Nom affiché de l'armure
    private String nom;
    // Quantité de points de vie ajoutés au joueur lorsqu'il équipe cette armure
    private int effet;
    // Image représentant l'arme (optionnel, peut être utilisé pour l'affichage)
    private Image image;
    // Liste des ressources nécessaires pour acheter / fabriquer l'armure
    private int prix;

    public Item(String nom, int effet, Image image, int prix) {
        this.nom = nom;
        this.effet = effet;
        this.image = image;
        this.prix = prix;
    }

    // Retourne le bonus de vie de l'armure
    public int getEffet() {
        return effet;
    }

    // Retourne le nom de l'armure
    public String getNom() {
        return nom;
    }

    // Retourne l'image de l'armure
    public Image getImage() {return image;}

    // Retourne la liste des ressources nécessaires pour acheter / fabriquer l'armure
    public Integer getPrix() {return prix;}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(nom, item.nom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom);
    }

}

