package Modele.Batiments;
import Modele.GestionnaireBatiments;
import Modele.Localisable;

import static Modele.Constantes.*;

/**
 * Classe abstraite représentant une structure fixe sur la carte (HQ, Tours, etc.).
 * Gère les données de base communes à tous les bâtiments comme la position spatiale,
 * l'intégrité structurelle (HP) et les règles de réparation.
 */
public abstract class Batiment extends Thread implements Localisable {

    GestionnaireBatiments gBatiments;
    protected double x,y;
    protected int hp;
    protected final int reparationRange;

    protected int range;

    // NOUVEAU : Portée minimale (Angle mort). Vaut 0 par défaut pour les tours.
    protected int minRange = 0;

    protected boolean attaquable;
    private boolean fonctionnel = true;

    // --- NOUVELLES PROPRIÉTÉS DE COLLISION RECTANGULAIRES ---
    protected int largeurEncombrement;
    protected int hauteurEncombrement;
    protected int largeurHitbox;
    protected int hauteurHitbox;
    protected int offsetYHitbox;
    protected double angleRotation = 0;

    public Batiment(int x, int y, GestionnaireBatiments gBatiments, int range ) {
        this.gBatiments = gBatiments;
        this.x = x;
        this.y = y;
        this.reparationRange = REPARATION_RANGE;
        this.range = range;
        this.attaquable = true;
    }

    public int getHp() { return hp; }
    public void resetHp(int hp) { this.hp = hp; }
    public double getX(){ return x; }
    public double getY(){ return y; }
    public int getHealingRange() { return reparationRange; }

    public void setHp(int hp) {
        this.hp = hp;
        if (hp <= 0 ) setAttaquable(false);
    }

    public int getRange(){ return range; }

    // NOUVEAU : Getter pour l'angle mort
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
}