package Modele;

import java.awt.*;
import java.util.List;


/**
 * Classe abstraite définissant la base de toute arme équipable par le joueur.
 * Centralise les statistiques utilisées par le Modèle pour calculer les collisions
 * et les dégâts lors des attaques (dégâts, zone d'effet, temps de recharge).
 */
public abstract class Arme {

    // Nom affiché de l'arme
    private String nom;
    // Quantité de points de vie retirés à la cible à chaque coup
    private int degats;
    // Distance maximale (en pixels) d'efficacité de l'arme
    private int portee;
    // Temps d'attente imposé (en ms) entre deux attaques successives
    private int cadence;
    // Angle d'ouverture du cône d'attaque (en radians)
    private double angle;
    // Image représentant l'arme (optionnel, peut être utilisé pour l'affichage)
    private Image image;
    // Liste des ressources nécessaires pour acheter / fabriquer l'arme
    private List ressourcesNecessaires;
    /**
     * Constructeur de base d'une arme.
     * @param nom Le nom d'affichage de l'arme.
     * @param degats Les points de vie retirés au monstre touché.
     * @param portee La distance maximale (en pixels) à laquelle une cible peut être touchée.
     * @param cadence Le temps de recharge ("cooldown") en millisecondes entre deux attaques.
     * @param angle L'ouverture du cône d'attaque en radians. Plus l'angle est grand, plus la zone balayée est large.
     */
    public Arme(String nom, int degats, int portee, int cadence, double angle, Image image, List ressourcesNecessaires) {
        // Affectation des statistiques de base lors de la création de l'arme
        this.nom = nom;
        this.degats = degats;
        this.portee = portee;
        this.cadence = cadence;
        this.angle = angle;
        this.image = image;
        this.ressourcesNecessaires = ressourcesNecessaires;
    }

    // Retourne la puissance d'attaque de l'arme
    public int getDegats(){return degats;};

    // Permet de modifier les dégâts de l'arme
    public void setDegats(int degats) {this.degats = degats;}

    // Retourne la distance d'attaque maximale
    public int getPortee(){return portee;};

    // Permet de modifier la portée de l'arme
    public void setPortee(int p){this.portee = p;};

    // Retourne le temps de recharge (cooldown) en millisecondes
    public int getCadence(){return cadence;};

    // Retourne le nom de l'arme
    public String getNom(){return nom;};

    // Permet de modifier le nom de l'arme
    public void setNom(String nom) {this.nom = nom;}

    // Retourne l'angle (la largeur) du cône de frappe en radians
    public double getAngle() {return angle;}

    // Retourne l'ensemble des ressources nécessaires pour acheter / fabriquer l'arme
    public List<String> getRessourcesNecessaires() {return ressourcesNecessaires;}

    // Retourne l'image de l'arme
    public Image getImage() {return image;}
}