package Vue;

import Modele.Joueur;

import java.awt.*;

public class VueJoueur {

    // Position du joueur
    private int positionX;
    private int positionY;

    // Constructeur de la classe VueJoueur, il initialise les données du joueur.
    public VueJoueur() {
        positionX = Joueur.getPositionX();
        positionY = Joueur.getPositionY();
    }

    public void dessiner(Graphics g, int xCentre, int yCentre) {
        int taille = 20;
        g.setColor(Color.black);
        g.fillOval(xCentre - taille/2, yCentre - taille/2, taille, taille);
    }

}
