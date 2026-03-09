package Modele;


/* * classe représentant les monstres du jeu
 * elle peut être utilisée pour créer différents types de monstres avec des comportements variés
 */
public abstract class Monstre {

    // Attributs du monstre

    // nom du monstre
    private final String nom;

    // points de vie du monstre
    private int hp;

    // points d'attaque du monstre
    private int attack;

    // position du monstre sur la carte
    private double x,y;

    // vitesse de déplacement du monstre
    private double vitesse;

    // Constructeur de la classe Monstre, il initialise les données du monstre.
    public Monstre(String nom, int hp, int attack) {
        this.nom = nom;
        this.hp = hp;
        this.attack = attack;
    }

    // Getters et setters pour les attributs du monstre
    public String getNom() { return nom; }

    // Getter HP
    public int getHp() { return hp; }

    // Setter HP
    public void setHp(int hp) { this.hp = hp; }

    // Getter Attack
    public int getAttack() { return attack; }

    // Setter Attack
    public void setAttack(int attack) { this.attack = attack; }

    // Getters pour la position du monstre
    public double getX() { return x; }

    public double getY() { return y; }

    /* Méthode pour calculer la distance entre le monstre et une position cible, elle prend en paramètre les coordonnées de la position cible et retourne la distance entre le monstre et cette position. */
    public double distanceVers(double cibleX, double cibleY) {
        // Calcul de la distance à l'aide du théorème de Pythagore
        return Math.sqrt(Math.pow(cibleX - x, 2) + Math.pow(cibleY - y, 2));
    }

    /* Méthode pour déplacer le monstre vers une position cible en fonction de la vitesse */
    public void deplacerVers(double cibleX, double cibleY) {
        double distance = distanceVers(cibleX, cibleY);
        if (distance > 2) {
            // Calcul de la direction du déplacement
            double directionX = (cibleX - x) / distance;
            double directionY = (cibleY - y) / distance;

            // Mise à jour de la position du monstre en fonction de la vitesse
            x += directionX * vitesse;
            y += directionY * vitesse;
        }
    }
}

