package Modele;

public class Joueur {

    // Position
    private static int positionX;
    private static int positionY;

    // Constructeur de la classe Joueur, il initialise les données du joueur.
    public Joueur() { // on initialise la position en 0,0 dans le modèle
        positionX = 0;
        positionY = 0;
    }

    public static int getPositionX() {return positionX;}

    public void setPositionX(int positionX) {Joueur.positionX = positionX;}

    public static int getPositionY() {return positionY;}

    public void setPositionY(int positionY) {Joueur.positionY = positionY;}
}
