package Controleur;

import Modele.Modele;
import Vue.Vue;
import Vue.VueArme;

import Modele.GestionnaireShop;

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

        // --- RELANCE DU JEU ---
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (modele.getPartieTerminee()) {
                modele.reinitialiserJeu();
                return; // On bloque tout le reste des actions
            }
        }

        /*** System.out.println("Action : La touche E a été pressée !");
         if (e.getKeyCode() == KeyEvent.VK_E) {
         modele.getJoueur().ramasseRessource();
         }***/

        // --- CONSTRUCTIONS ET ARMES ---
        if (e.getKeyCode() == KeyEvent.VK_T) {
            modele.getJoueur().construireTour();
        }

        if (e.getKeyCode() == KeyEvent.VK_C) {
            vue.getVueArme().setAffPortee(!vue.getVueArme().getAffPortee()); // bascule l'affichage de la portée de l'arme
        }

        // --- INTERACTION BÂTIMENTS ---
        if (e.getKeyCode() == KeyEvent.VK_R) {
            modele.getJoueur().recolterMine();
        }

        /** --- RÉPARATION DES BÂTIMENTS ---
         if (e.getKeyCode() == KeyEvent.VK_F) {
         modele.getJoueur().lancerReparation();
         }**/

        // --- NAVIGATION DU HUD ---
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            // Flèche gauche : on recule (Formule pour un cycle 1-2-3 inversé)
            int page = (modele.getHudPageActuelle() + 1) % 3 + 1;
            modele.setHudPageActuelle(page);
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            // Flèche droite : on avance (Formule pour un cycle 1-2-3 standard)
            int page = (modele.getHudPageActuelle() % 3) + 1;
            modele.setHudPageActuelle(page);
        }

        // --- INTERFACE SYSTÈME (NOUVEAU) ---
        if (e.getKeyCode() == KeyEvent.VK_P) {
            modele.toggleAffichagePV();
        }

/**
 // --- ACHATS DANS LA BOUTIQUE (Page 3) ---
 if (modele.getHudPageActuelle() == 3) {
 // Utilisation du pavé numérique pour dissocier l'achat de la navigation
 switch (e.getKeyCode()) {
 case KeyEvent.VK_1 -> modele.getGestionnaireShop().acheterEpeeAcieree();
 case KeyEvent.VK_2 -> modele.getGestionnaireShop().acheterArmure();
 case KeyEvent.VK_3 -> modele.getGestionnaireShop().acheterArmureLourde();
 case KeyEvent.VK_4 -> modele.getGestionnaireShop().acheterEpee();
 case KeyEvent.VK_5 -> modele.getGestionnaireShop().acheterPotionDeVie();
 }
 }
 **/
        // --- PASSER LA NUIT ---
        if (e.getKeyCode() == KeyEvent.VK_S) {
            modele.getUpdateJN().passerJour();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}