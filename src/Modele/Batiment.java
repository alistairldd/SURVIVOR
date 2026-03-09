package Modele;

/*
* La classe générale du bâtiment, elle contient les classes de données et les méthodes pour manipuler ces données.
* On aura des sous classes pour les différents batiments (remparts, tour, etc)
*
 */

public abstract class Batiment {

    public final int BASE_HP = 100; //Points de vie de base pour tous les bâtiments.
    int x,y; //Position de la tour sur la carte.
    private int hp; //Points de vie du batiment.
    private int healingRange; //Portée de position pour reparer le batiment.

    public Batiment(int x, int y) {
        this.x = x;
        this.y = y;
        this.hp = BASE_HP;
        this.healingRange = 10;
    }

    public int getHp() {
        return hp;
    }

    public void resetHp(int hp) {
        this.hp = BASE_HP;
    }

    public int getX(){ return x; }
    public int getY(){ return y; }

    public int getHealingRange() {
        return healingRange;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }



}
