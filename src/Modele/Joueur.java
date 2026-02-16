package Modele;

import java.util.ArrayList;

public class Joueur {

    //Stats du joueur
    private static int hp;
    private static int attack;
    private static ArrayList<Object> inventaire;

    // Position
    private static int positionX;
    private static int positionY;

    // Constructeur de la classe Joueur, il initialise les données du joueur.
    public Joueur() { // on initialise la position en 0,0 dans le modèle
        positionX = 0;
        positionY = 0;
        hp = 100;
        attack = 10;
        inventaire = new ArrayList<>();
    }

    public static int getHp() {return hp;}

    public void setHp(int hp) {Joueur.hp = hp;}

    public static int getAttack() {return attack;}

    public void setAttack(int attack) {Joueur.attack = attack;}

    public static ArrayList<Object> getInventaire() {
        return inventaire;
    }

    public static void addToInventaire(Object item) {
        inventaire.add(item);
    }

    public static int getPositionX() {return positionX;}

    public void setPositionX(int positionX) {Joueur.positionX = positionX;}

    public static int getPositionY() {return positionY;}

    public void setPositionY(int positionY) {Joueur.positionY = positionY;}
}
