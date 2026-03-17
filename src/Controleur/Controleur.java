package Controleur;

/*
* La classe générale du contrôleur, elle contient les classes de données et les méthodes pour manipuler ces données.
* Elle est utilisée pour gérer les interactions entre la vue et le modèle, pour recevoir les événements
* de la vue et pour mettre à jour le modèle en conséquence. Elle est également utilisée pour gérer les threads du contrôleur.
 */

import Modele.Modele;
import Vue.Vue;

import javax.swing.*;

public class Controleur {

    private Modele monModele;
    private Vue maVue;

    public Controleur(Modele modele, Vue vue){
        monModele = modele;
        maVue = vue;

        ControleurSouris controleurSouris = new ControleurSouris(vue, modele);
        this.maVue.addMouseListener(controleurSouris);
        this.maVue.addMouseMotionListener(controleurSouris);

    }
}
