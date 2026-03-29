package Modele;
import static Modele.Constantes.*;

/**
 * Classe abstraite représentant une structure fixe sur la carte (HQ, Tours, etc.).
 * Gère les données de base communes à tous les bâtiments comme la position spatiale,
 * l'intégrité structurelle (HP) et les règles de réparation.
 */
public abstract class Batiment extends Thread implements Localisable {

    GestionnaireBatiments gBatiments;
    // Coordonnées de placement sur la grille/carte globale
    protected double x,y;
    // Points de vie actuels du bâtiment (diminue lors d'une attaque)
    protected int hp;
    // Rayon d'action dans lequel le joueur doit se trouver pour pouvoir interagir ou réparer
    protected final int healingRange;

    private int range;

    protected boolean attaquable;
    // Taille physique d'encombrement du bâtiment
    protected int rayonHitbox;


    /**
     * Initialise un bâtiment à une position spécifique avec ses points de vie maximum.
     * @param x Coordonnée X sur la carte globale.
     * @param y Coordonnée Y sur la carte globale.
     */
    public Batiment(int x, int y, GestionnaireBatiments gBatiments, int range ) {
        this.gBatiments = gBatiments;
        // Enregistre les coordonnées choisies pour la construction
        this.x = x;
        this.y = y;
        // Fixe la zone d'interaction par défaut à 10 pixels
        this.healingRange = HEALING_RANGE;
        this.range = range;
        this.start();
        this.attaquable = true;
    }

    // Récupère les points de vie actuels du bâtiment
    public int getHp() {
        return hp;
    }

    // Restaure instantanément les points de vie à leur valeur maximale par défaut
    public void resetHp(int hp) {
        this.hp = hp;
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

    public int getRange(){
        return range;
    }

    // Récupère le rayon d'encombrement physique du bâtiment
    public int getRayonHitbox() {
        return rayonHitbox;
    }

    public boolean isAttaquable() {
        return attaquable;
    }
}