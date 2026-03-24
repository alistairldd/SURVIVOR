package Controleur;

import Modele.Modele;
import Vue.Vue;
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

    public ControleurClavier(Vue vue, Modele modele) {
        this.modele = modele;
        this.vue = vue;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * Détecte les touches enfoncées et déclenche les actions métier correspondantes.
     * - Touche 'E' : Ordonne au joueur de vérifier les collisions avec les ressources pour les ajouter à son inventaire.
     * - Touche 'T' : Ordonne au joueur de lancer la construction d'une tour défensive (vérification des prérequis gérée par le modèle).
     * - Touches '1', '2', '3' : Navigation entre les pages du HUD.
     * * @param e L'événement de touche de clavier capturé.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        //System.out.println("Action : La touche E a été pressée !");
        if (e.getKeyCode() == KeyEvent.VK_E) {
            modele.getJoueur().ramasseRessource();
        }

        if (e.getKeyCode() == KeyEvent.VK_T) {
            modele.getJoueur().construireTour();
        }

        if (e.getKeyCode() == KeyEvent.VK_C) {
            vue.getVueArme().setAffPortee(!vue.getVueArme().getAffPortee()); // bascule l'affichage de la portée de l'arme
        }

        // --- NAVIGATION DU HUD ---
        if (e.getKeyCode() == KeyEvent.VK_1) {
            modele.setHudPageActuelle(1); // Page État du jeu
        }
        else if (e.getKeyCode() == KeyEvent.VK_2) {
            modele.setHudPageActuelle(2); // Page Action / Inventaire
        }
        else if (e.getKeyCode() == KeyEvent.VK_3) {
            modele.setHudPageActuelle(3); // Page Boutique
        }

        // --- ACHATS DANS LA BOUTIQUE (Page 3) ---
        if (modele.getHudPageActuelle() == 3) {
            // Utilisation du pavé numérique pour dissocier l'achat de la navigation
            switch (e.getKeyCode()) {
                case KeyEvent.VK_NUMPAD1 -> modele.getGestionnaireShop().acheterEpeeAcieree();
                case KeyEvent.VK_NUMPAD2 -> modele.getGestionnaireShop().acheterArmure();
                case KeyEvent.VK_NUMPAD3 -> modele.getGestionnaireShop().acheterArmureLourde();
                case KeyEvent.VK_NUMPAD4 -> modele.getGestionnaireShop().acheterEpee();
                case KeyEvent.VK_NUMPAD5 -> modele.getGestionnaireShop().acheterPotionDeVie();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}