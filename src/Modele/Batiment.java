package Modele;

/**
 * Classe abstraite représentant une structure fixe sur la carte (HQ, Tours, etc.).
 * Gère les données de base communes à tous les bâtiments comme la position spatiale,
 * l'intégrité structurelle (HP) et les règles de réparation.
 */
public abstract class Batiment implements Localisable{

    // Constante : Points de vie maximum d'un bâtiment neuf
    public final int BASE_HP = 100;
    // Coordonnées de placement sur la grille/carte globale
    double x,y;
    // Points de vie actuels du bâtiment (diminue lors d'une attaque)
    private int hp;
    // Rayon d'action dans lequel le joueur doit se trouver pour pouvoir interagir ou réparer
    private int healingRange;

    /**
     * Initialise un bâtiment à une position spécifique avec ses points de vie maximum.
     * @param x Coordonnée X sur la carte globale.
     * @param y Coordonnée Y sur la carte globale.
     */
    public Batiment(int x, int y) {
        // Enregistre les coordonnées choisies pour la construction
        this.x = x;
        this.y = y;
        // Initialise la structure "flambant neuve" avec tous ses PV
        this.hp = BASE_HP;
        // Fixe la zone d'interaction par défaut à 10 pixels
        this.healingRange = 10;
    }

    // Récupère les points de vie actuels du bâtiment
    public int getHp() {
        return hp;
    }

    // Restaure instantanément les points de vie à leur valeur maximale par défaut
    public void resetHp(int hp) {
        this.hp = BASE_HP;
    }

    // Récupère la position horizontale sur la carte
    public double getX(){ return x; }

    // Récupère la position verticale sur la carte
    public double getY(){ return y; }

    // Indique à quelle distance le joueur doit être pour initier une réparation
    public int getHealingRange() {
        return healingRange;
    }

    // Permet de forcer une valeur spécifique de points de vie (ex: lors de dégâts subis)
    public void setHp(int hp) {
        this.hp = hp;
    }

}