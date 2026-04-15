package Modele.Armure;

import java.awt.*;
import java.util.Map;

/**
 * Classe abstraite définissant la base de toute armure équipable par le joueur.
 * Gère les statistiques de réduction de dégâts, le poids (vitesse) et le coût en ressources.
 */
public abstract class Armure {
    // Nom affiché de l'armure
    private String nom;
    // Quantité de dégâts réduits (ou bonus de PV selon l'implémentation)
    private int reduction;
    // Image représentant l'armure
    private Image image;
    // Dictionnaire des ressources nécessaires (ID Ressource : Quantité)
    private Map<Integer, Integer> ressourcesNecessaires;
    // Impact sur la vitesse du joueur (valeur négative pour ralentir)
    private int vitesse;

    public Armure(String nom, int reduction, Image image, int vitesse, Map<Integer, Integer> ressourcesNecessaires) {
        this.nom = nom;
        this.reduction = reduction;
        this.image = image;
        this.vitesse = vitesse;
        this.ressourcesNecessaires = ressourcesNecessaires;
    }

    // Retourne le bonus de réduction de l'armure
    public int getReduction() {
        return reduction;
    }

    // Retourne le nom de l'armure
    public String getNom() {
        return nom;
    }

    // Retourne l'image de l'armure
    public Image getImage() {
        return image;
    }

    // Retourne l'impact sur la vitesse
    public int getVitesse() {
        return vitesse;
    }

    /**
     * Retourne le dictionnaire des ressources nécessaires.
     * Clé : ID de la ressource (0:Bois, 1:Pierre, 2:Fer, 3:Or)
     * Valeur : Quantité requise
     */
    public Map<Integer, Integer> getRessourcesNecessaires() {
        return ressourcesNecessaires;
    }
}