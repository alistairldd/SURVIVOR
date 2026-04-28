package Modele.Batiments;

import Modele.GestionnaireBatiments;
import Modele.Localisable;

import static Modele.Constantes.*;

/**
 * Classe abstraite représentant une structure fixe sur la carte (HQ, Tours, etc.).
 * Gère les données de base communes à tous les bâtiments comme la position spatiale,
 * l'intégrité structurelle (HP) et les règles de collision.
 */
public abstract class Batiment extends Thread implements Localisable {

    /** ---------- [Propriétés - Générales & Combat] ---------- **/

    protected GestionnaireBatiments gBatiments;
    protected double x,y;
    protected int hp;
    protected final int reparationRange;
    protected int range;
    protected int minRange = 0;
    protected boolean attaquable;
    private boolean fonctionnel = true;

    /** ---------- [Propriétés - Moteur Physique (Collision 2.5D)] ---------- **/

    protected int largeurEncombrement;
    protected int hauteurEncombrement;
    protected int largeurHitbox;
    protected int hauteurHitbox;
    protected int offsetYHitbox;
    protected double angleRotation = 0;

    /** ---------- [Constructeurs] ---------- **/

    /**
     * Initialise la logique spatiale et structurelle de base d'un bâtiment.
     *
     * @param x - Coordonnée X du centre du bâtiment
     * @param y - Coordonnée Y du centre du bâtiment
     * @param gBatiments - Le gestionnaire orchestrant tous les bâtiments
     * @param range - La portée maximale de l'effet du bâtiment (0 pour passif)
     */
    public Batiment(int x, int y, GestionnaireBatiments gBatiments, int range) {
        this.gBatiments = gBatiments;
        this.x = x;
        this.y = y;
        this.reparationRange = REPARATION_RANGE;
        this.range = range;
        this.attaquable = true;
    }

    /** ---------- [Accesseurs / Getters & Setters] ---------- **/

    public double getX() { return x; }
    public double getY() { return y; }

    public int getHp() { return hp; }

    public void setHp(int hp) {
        this.hp = hp;
        if (hp <= 0) setAttaquable(false);
    }

    public void resetHp(int hp) { this.hp = hp; }

    public int getHealingRange() { return reparationRange; }
    public int getRange() { return range; }
    public int getMinRange() { return minRange; }

    public boolean isAttaquable() { return attaquable; }
    public void setAttaquable(boolean attaquable) { this.attaquable = attaquable; }

    public boolean isFonctionnel() { return fonctionnel; }
    public void setFonctionnel(boolean fonctionnel) { this.fonctionnel = fonctionnel; }

    public int getLargeurEncombrement() { return largeurEncombrement; }
    public int getHauteurEncombrement() { return hauteurEncombrement; }
    public int getLargeurHitbox() { return largeurHitbox; }
    public int getHauteurHitbox() { return hauteurHitbox; }
    public int getOffsetYHitbox() { return offsetYHitbox; }
    public double getAngleRotation() { return angleRotation; }

    /** ---------- [Méthodes Abstraites] ---------- **/

    public abstract int getMaxHp();
    public abstract String getNom();
}