package Vue;

import Modele.Joueur;

import java.awt.*;

public class VueJoueur {

    // Position du joueur
    private int positionX;
    private int positionY;

    // Constructeur de la classe VueJoueur, il initialise les données du joueur.
    public VueJoueur() {
        positionX = 0;
        positionY = 0;
    }

    // Méthode pour dessiner le joueur sur la carte
    public void dessiner(Graphics g) {
        int posX = Joueur.getPositionX();
        int posY = Joueur.getPositionY();
        int taille = 20;
        g.setColor(Color.black);
        g.fillOval(posX - taille/2, posY - taille/2 , taille, taille);
    }

}
