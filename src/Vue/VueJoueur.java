package Vue;

import Modele.Joueur;

import java.awt.*;

public class VueJoueur {

    public static final int TAILLE = 20;

    // Constructeur de la classe VueJoueur
    public VueJoueur() {
    }

    // Méthode pour dessiner le joueur sur la carte
    public void dessiner(Graphics g2d) {
        double posX = Joueur.getPositionX();
        double posY = Joueur.getPositionY();
        g2d.setColor(Color.black);
        g2d.fillOval((int) posX - TAILLE/2, (int) posY - TAILLE/2 , TAILLE, TAILLE);
    }

}
