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

    // Constructeur de la classe Vue, il initialise les données de la vue.
    public Vue(Modele modele) {
        this.vueCarte = new VueCarte(modele);
        this.vueJoueur = new VueJoueur();
    }

    /* ---- GETTERS ET SETTERS ---- */



    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        int mi_largeur = getWidth() / 2;
        int mi_hauteur = getHeight() / 2;


        vueCarte.dessiner(g, mi_largeur, mi_hauteur);
        vueJoueur.dessiner(g, mi_largeur, mi_hauteur);
    }

}
