package Modele.Items;

import java.awt.*;
import java.util.Objects;

/**
 * Classe abstraite définissant tout objet stockable dans l'inventaire du joueur
 * (Consommables, Pièces de quête, Sorts, etc.).
 * Gère l'identification, le coût en boutique et l'effet générique.
 */
public abstract class Item {

    /** ---------- [Propriétés] ---------- **/

    private String nom;
    private int effet;
    private Image image;
    private int prix;

    /** ---------- [Constructeurs] ---------- **/

    /**
     * Initialise les propriétés de base d'un Item.
     *
     * @param nom - Le nom d'affichage de l'objet
     * @param effet - La valeur numérique de son action principale (PV rendus, dégâts, etc.)
     * @param image - L'icône affichée dans l'interface
     * @param prix - Le coût en pièces d'or dans la boutique
     */
    public Item(String nom, int effet, Image image, int prix) {
        this.nom = nom;
        this.effet = effet;
        this.image = image;
        this.prix = prix;
    }

    /** ---------- [Accesseurs / Getters] ---------- **/

    public int getEffet() {
        return effet;
    }

    public String getNom() {
        return nom;
    }

    public Image getImage() {
        return image;
    }

    public Integer getPrix() {
        return prix;
    }

    /** ---------- [Méthodes Utilitaires (Surcharge)] ---------- **/

    /**
     * Redéfinition de l'égalité basée strictement sur le nom de l'objet.
     * Permet le regroupement et la recherche d'items identiques dans l'inventaire.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(nom, item.nom);
    }

    public void inflation(){ this.prix += (int) ((double) this.prix * (20./100)); }

    @Override
    public int hashCode() {
        return Objects.hash(nom);
    }
}