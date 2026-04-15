package Modele.Monstres;

import Modele.Joueur;
import Modele.Localisable;

import java.awt.*;

import static Modele.Constantes.*;


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

    // points de vie maximum du monstre (utile pour les soins ou les affichages de barre de vie)
    private int maxHp;

    // points d'attaque du monstre (dégâts qu'il inflige au joueur ou aux bâtiments)
    private int attack;

    // direction actuelle du monstre (utile pour les déplacements et les animations)
    protected double directionX;

    // indique si le monstre est en train de marcher ou d'attaquer (utile pour les animations)
    protected boolean marche = true;

    // portée d'attaque du monstre (à quelle distance il peut frapper)
    private int portee;

    // position horizontale et verticale sur la carte (protected pour que les sous-classes comme Slime y accèdent)
    protected double x,y;

    // vitesse de déplacement du monstre en pixels par mouvement
    private double vitesse;

    // nombre de pièces en récompense
    protected int drop;

    // Variable pour gérer l'animation du monstre
    protected double animation = 0;
    protected double animationAtt = 0;

    // Variable pour l'animation de marche
    protected boolean animationMarche = true;

    // Variable pour l'animation d'attaque
    protected boolean animationAttaque = false;

    // Variables pour gérer les temps d'attaque
    private double cadenceAttaque = 1.0; // 1 coup par seconde
    private double tempsDepuisDerniereAttaque = 0;

    // Constructeur de la classe Monstre, il initialise les données du monstre.
    public Monstre(String nom, int maxHp, int attack, int portee, int vitesse) {
        // Assigne la valeur actuelle du compteur comme ID unique, puis incrémente le compteur de 1 pour le prochain monstre
        this.id = compteurID++; // Attribue un ID unique au monstre et incrémente le compteur

        // Initialisation des statistiques
        this.nom = nom;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.attack = attack;
        this.portee = portee;
        this.vitesse = vitesse;
        this.drop = DEFAULT_DROP; // Valeur par défaut, peut être modifiée par les sous-classes
    }

    // Getter id
    public int getID() { return id; }

    // Getters et setters pour les attributs du monstre
    public String getNom() { return nom; }

    // Getter HP
    public int getHp() { return hp; }

    // Getter HP max
    public int getMaxHp() { return maxHp; }

    public boolean estVivant() { return hp > 0; }

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

    public boolean regardeGauche() { return directionX < 0; }

    public double getY() { return y; }

    public Image getImage() { return null; } // Getter d'image par défaut, les sous-classes comme Slime le redéfiniront pour fournir leur propre sprite

    public int getDrop() { return drop; }

    public void ajouterAnimation(double delta) { this.animation += delta; }

    public double getAnimation() { return this.animation; }

    public boolean isMarche () { return marche; }

    public boolean getAnimationMarche() { return animationMarche; }

    public boolean getAnimationAttaque() { return animationAttaque; }

    public void mettreAJourPosition(Localisable cible, double dt) {
        double diffX = cible.getX() - this.x;
        double diffY = cible.getY() - this.y;
        double distance = Math.sqrt(diffX * diffX + diffY * diffY);


        if (distance > this.portee) {
            // Marcher
            this.marche = true;
            // Calculer la direction en normalisant le vecteur de différence
            this.directionX = diffX / distance;

            this.x += (diffX / distance) * this.vitesse;
            this.y += (diffY / distance) * this.vitesse;
        } else {
            // S'arrêter et attaquer
            this.marche = false;
            attaquer(cible, dt);
        }
    }

    private void attaquer(Localisable cible, double dt) {
        // On incrémente le compteur de temps
        tempsDepuisDerniereAttaque += dt;

        // Si assez de temps est passé (1 seconde)
        if (tempsDepuisDerniereAttaque >= cadenceAttaque) {
            if (cible instanceof Joueur){
                Joueur j = (Joueur) cible;
                int reduction = j.getReductionDegats();
                cible.setHp(Math.max(cible.getHp() - this.attack*(100-reduction)/100, 0)); // La cible perd des PV en tenant compte de la réduction de dégâts
            }
            else {
                cible.setHp(Math.max(cible.getHp() - this.attack, 0)); // La cible perd des PV
            }
            tempsDepuisDerniereAttaque = 0;    // On réinitialise le timer

            //System.out.println("Le monstre tape ! PV restants : " + cible.getHp());
        }
    }

}