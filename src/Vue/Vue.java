package Vue;

import Modele.Joueur;
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

    // Vues
    private final VueCarte vueCarte;
    private final VueJoueur vueJoueur;

    private final Modele modele;

    // Constructeur de la classe Vue, il initialise les données de la vue.
    public Vue(Modele modele) {
        this.vueCarte = new VueCarte(modele);
        this.vueJoueur = new VueJoueur();

        this.modele = modele;
        new Redessine (this, modele);
    }

    /* ---- GETTERS ET SETTERS ---- */



    @Override
    public void paint(Graphics g) {

        super.paint(g);

        int mi_largeur = getWidth() / 2;
        int mi_hauteur = getHeight() / 2;

        int posX = Joueur.getPositionX();
        int posY = Joueur.getPositionY();

        System.out.println("Position du joueur dans vue: (" + posX + ", " + posY + ")");

        //vueCarte.dessiner(g, mi_largeur, mi_hauteur);
        //vueJoueur.dessiner(g, mi_largeur, mi_hauteur);

        vueCarte.dessiner(g);
        vueJoueur.dessiner(g);
    }

}
