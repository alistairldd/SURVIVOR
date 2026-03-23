package Controleur;

import Modele.Modele;
import Vue.Vue;
import Modele.Map;
import Vue.VueArme;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Contrôleur dédié à la gestion des événements du clavier.
 * Fait le pont entre les frappes spécifiques de l'utilisateur et les actions du joueur
 * sur son environnement (interactions avec les ressources et les bâtiments).
 */
public class ControleurClavier implements KeyListener {

    private Modele modele;
    private Vue vue;
    private Map map;

    public ControleurClavier(Vue vue, Modele modele) {

        this.modele = modele;
        this.vue = vue;
        this.map = modele.getMap();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * Détecte les touches enfoncées et déclenche les actions métier correspondantes.
     * - Touche 'E' : Ordonne au joueur de vérifier les collisions avec les ressources pour les ajouter à son inventaire.
     * - Touche 'T' : Ordonne au joueur de lancer la construction d'une tour défensive (vérification des prérequis gérée par le modèle).
     * * @param e L'événement de touche de clavier capturé.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        //System.out.println("Action : La touche E a été pressée !");
        if (e.getKeyCode() == KeyEvent.VK_E) {
            modele.joueurRamasseRessource();
        //getJoueur().ramasseRessource(map.getRessources()) ;

        }
        if (e.getKeyCode() == KeyEvent.VK_T) {
            modele.getJoueur().construireTour();
        }

        if (e.getKeyCode() == KeyEvent.VK_C) {
            vue.getVueArme().setAffPortee(!vue.getVueArme().getAffPortee()); // bascule l'affichage de la portée de l'arme
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}