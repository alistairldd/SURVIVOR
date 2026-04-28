package Modele.Armes;

import java.awt.*;
import java.util.Map;

/**
 * Classe abstraite définissant la base de toute arme équipable par le joueur.
 * Centralise les statistiques utilisées par le Modèle pour calculer les collisions
 * et les dégâts lors des attaques (dégâts, zone d'effet, temps de recharge).
 */
public abstract class Arme {

    /** ---------- [Propriétés] ---------- **/

    private String nom;
    private int degats;
    private int portee;
    private int cadence;
    private double angle;
    private Image image;
    private Map<Integer, Integer> ressourcesNecessaires;

    /** ---------- [Constructeurs] ---------- **/

    /**
     * Constructeur de base d'une arme.
     * * @param nom - Le nom d'affichage de l'arme
     * @param degats - Les points de vie retirés à la cible touchée
     * @param portee - La distance maximale (en pixels) d'efficacité
     * @param cadence - Le temps de recharge ("cooldown") en millisecondes
     * @param angle - L'ouverture du cône d'attaque en radians
     * @param image - Image de l'arme pour l'affichage UI
     * @param ressourcesNecessaires - Dictionnaire des coûts de fabrication (ID Ressource : Quantité)
     */
    public Arme(String nom, int degats, int portee, int cadence, double angle, Image image, Map<Integer, Integer> ressourcesNecessaires) {
        this.nom = nom;
        this.degats = degats;
        this.portee = portee;
        this.cadence = cadence;
        this.angle = angle;
        this.image = image;
        this.ressourcesNecessaires = ressourcesNecessaires;
    }

    /** ---------- [Getters & Setters] ---------- **/

    public String getNom(){return nom;}
    public void setNom(String nom) {this.nom = nom;}

    public int getDegats(){return degats;}
    public void setDegats(int degats) {this.degats = degats;}

    public int getPortee(){return portee;}
    public void setPortee(int p){this.portee = p;}

    public int getCadence(){return cadence;}

    public double getAngle() {return angle;}

    public Map<Integer, Integer> getRessourcesNecessaires() {return ressourcesNecessaires;}

    public Image getImage() {return image;}
}