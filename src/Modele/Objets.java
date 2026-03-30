package Modele;

public abstract class Objets{
    private String nom;
    private int prix;

    public Objets(String nom, int prix) {
        this.nom = nom;
        this.prix = prix;
    }

    public String getNom() {
        return nom;
    }

    public int getPrix() {
        return prix;
    }
}
