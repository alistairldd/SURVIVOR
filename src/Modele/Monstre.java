package Modele;

import java.awt.*;
import java.util.ArrayList;

/* * classe représentant les monstres du jeu
 * elle peut être utilisée pour créer différents types de monstres avec des comportements variés
 */
/**
 * Modèle de base pour toutes les entités hostiles (Ennemis).
 * Gère l'identification unique (ID) pour le suivi, les statistiques vitales,
 * et la logique mathématique des déplacements (parcours de vecteurs).
 */
public abstract class Monstre extends Thread implements Localisable {

    // Attributs du monstre
    // Variable statique (partagée par tous les monstres) servant de générateur d'identifiants
    private static int compteurID = 0; // Compteur pour générer des IDs uniques

    // Identifiant unique, fixe et propre à cette instance (pratique pour le debug)
    private final int id; // ID unique du monstre

    // nom du monstre (ex: "Slime")
    private final String nom;

    // points de vie actuels du monstre (tombe à 0 = mort)
    private int hp;

    // points d'attaque du monstre (dégâts qu'il inflige au joueur ou aux bâtiments)
    private int attack;

    // portée d'attaque du monstre (à quelle distance il peut frapper)
    private int portee;

    // position horizontale et verticale sur la carte (protected pour que les sous-classes comme Slime y accèdent)
    protected double x,y;

    // vitesse de déplacement du monstre en pixels par mouvement
    private double vitesse;

    // Indique si le monstre est actuellement en train d'attaquer (true) ou de se déplacer (false)
    private boolean estEnTrainDAttaquer = false;

    // Variable pour gérer l'animation du monstre
    private double animation = 0;

    // Variables pour gérer les temps d'attaque
    private double cadenceAttaque = 1.0; // 1 coup par seconde
    private double tempsDepuisDerniereAttaque = 0;

    // Constructeur de la classe Monstre, il initialise les données du monstre.
    public Monstre(String nom, int hp, int attack, int portee, int vitesse) {
        // Assigne la valeur actuelle du compteur comme ID unique, puis incrémente le compteur de 1 pour le prochain monstre
        this.id = compteurID++; // Attribue un ID unique au monstre et incrémente le compteur

        // Initialisation des statistiques
        this.nom = nom;
        this.hp = hp;
        this.attack = attack;
        this.portee = portee;
        this.vitesse = vitesse;

    }

    // Getter id
    public int getID() { return id; }

    // Getters et setters pour les attributs du monstre
    public String getNom() { return nom; }

    // Getter HP
    public int getHp() { return hp; }

    // Setter HP
    public void setHp(int hp) { this.hp = hp; }

    // Retire des points de vie (appelé lors d'une attaque du joueur ou d'une tour)
    public void perdreHp(int hpPerdus) { this.hp -= hpPerdus; }

    // Getter Attack
    public int getAttack() { return attack; }

    // Setter Attack
    public void setAttack(int attack) { this.attack = attack; }

    // Getter Portée
    public int getPortee() { return portee; }

    // Getters pour la position du monstre
    public double getX() { return x; }

    public double getY() { return y; }

    public Image getImage() { return null; } // Getter d'image par défaut, les sous-classes comme Slime le redéfiniront pour fournir leur propre sprite

    public void ajouterAnimation(double delta) { this.animation += delta; }

    public double getAnimation() { return this.animation; }

    public void mettreAJourPosition(Localisable cible, double dt) {
        double diffX = cible.getX() - this.x;
        double diffY = cible.getY() - this.y;
        double distance = Math.sqrt(diffX * diffX + diffY * diffY);


        if (distance > this.portee) {
            // Marcher
            this.x += (diffX / distance) * this.vitesse;
            this.y += (diffY / distance) * this.vitesse;
            this.estEnTrainDAttaquer = false;
        } else {
            // S'arrêter et attaquer
            this.estEnTrainDAttaquer = true;
            attaquer(cible, dt);
        }
    }

    private void attaquer(Localisable cible, double dt) {
        // On incrémente le compteur de temps
        tempsDepuisDerniereAttaque += dt;

        // Si assez de temps est passé (1 seconde)
        if (tempsDepuisDerniereAttaque >= cadenceAttaque) {
            cible.setHp(cible.getHp() - this.attack); // La cible perd des PV
            tempsDepuisDerniereAttaque = 0;    // On réinitialise le timer

            System.out.println("Le monstre tape ! PV restants : " + cible.getHp());
        }
    }

}