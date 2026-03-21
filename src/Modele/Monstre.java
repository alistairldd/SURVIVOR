package Modele;

import java.util.ArrayList;

/* * classe représentant les monstres du jeu
 * elle peut être utilisée pour créer différents types de monstres avec des comportements variés
 */
/**
 * Modèle de base pour toutes les entités hostiles (Ennemis).
 * Gère l'identification unique (ID) pour le suivi, les statistiques vitales,
 * et la logique mathématique des déplacements (parcours de vecteurs).
 */
public abstract class Monstre {

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
    protected int x,y;

    // vitesse de déplacement du monstre en pixels par mouvement
    private double vitesse;

    // Indicateur d'état (mort ou vivant) - Prévu pour nettoyer les entités
    private boolean isDead = false;

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
    public int getId() { return id; }

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
    public int getX() { return x; }

    public int getY() { return y; }


    /**
     * Calcule la distance brute (à vol d'oiseau) entre le monstre et une cible.
     * @param cibleX Coordonnée X de la cible.
     * @param cibleY Coordonnée Y de la cible.
     * @return La distance en pixels.
     */
    /* Méthode pour calculer la distance entre le monstre et une position cible, elle prend en paramètre les coordonnées de la position cible et retourne la distance entre le monstre et cette position. */
    public double distanceVers(double cibleX, double cibleY) {
        // Calcul de la distance à l'aide du théorème de Pythagore ( a² + b² = c² )
        return Math.sqrt(Math.pow(cibleX - x, 2) + Math.pow(cibleY - y, 2));
    }

    /**
     * Calcule le vecteur directeur vers une cible et modifie la position (X,Y)
     * du monstre en fonction de sa vitesse de déplacement.
     * @param cibleX Coordonnée X vers laquelle avancer.
     * @param cibleY Coordonnée Y vers laquelle avancer.
     */
    /* Méthode pour déplacer le monstre vers une position cible en fonction de la vitesse */
    public void deplacerVers(double cibleX, double cibleY) {
        // Détermine d'abord à quelle distance on se trouve
        double distance = distanceVers(cibleX, cibleY);

        // Si le monstre est assez loin (évite les tremblements quand il est quasiment sur la cible)
        if (distance > 2) {
            // Normalisation du vecteur : on divise la différence de position par la distance totale
            // Cela donne un vecteur de direction de longueur exactement égale à 1
            double directionX = (cibleX - x) / distance;
            double directionY = (cibleY - y) / distance;

            // Mise à jour de la position du monstre en multipliant ce vecteur normalisé par sa vitesse
            x += directionX * vitesse;
            y += directionY * vitesse;
        }
    }
}