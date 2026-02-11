package Vue;

/*
* La classe générale de la vue, elle contient les classes de données et les méthodes pour manipuler ces données.
* Elle est utilisée pour afficher les données de l'application et pour recevoir les événements de l'utilisateur
* et pour les transmettre au contrôleur. Elle est également utilisée pour gérer les threads de la vue.
*
 */

import Controleur.controleurSouris;
import Modele.Modele;

import javax.swing.*;
import java.awt.*;

public class Vue extends JPanel {

    // Position du joueur
    private int positionX;
    private int positionY;

    // Vues
    private final VueCarte vueCarte;


    // Constructeur de la classe Vue, il initialise les données de la vue.
    public Vue(Modele modele) {
        this.vueCarte = new VueCarte(modele);
        positionX = 0;
        positionY = 0;
    }

    /* ---- GETTERS ET SETTERS ---- */

    // Position du joueur

    public int getPositionX() {return positionX;}

    public void setPositionX(int positionX) {this.positionX = positionX;}

    public int getPositionY() {return positionY;}

    public void setPositionY(int positionY) {this.positionY = positionY;}

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        vueCarte.dessiner(g);
    }

}
