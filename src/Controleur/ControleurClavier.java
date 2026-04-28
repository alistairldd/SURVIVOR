package Controleur;

import Modele.Items.PotionDegats;
import Modele.Items.PotionVie;
import Modele.Items.PotionVitesse;
import Modele.Modele;
import Modele.Joueur;
import Vue.Vue;
import Vue.VueArme;

import Modele.GestionnaireShop;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.Key;

/**
 * Contrôleur dédié à la gestion des événements du clavier.
 * Fait le pont entre les frappes spécifiques de l'utilisateur et les actions du joueur
 * sur son environnement (interactions, gestion d'interface, etc.).
 */
public class ControleurClavier implements KeyListener {

    /** ---------- [Propriétés] ---------- **/

    private Modele modele;
    private Vue vue;

    /** ---------- [Constructeurs] ---------- **/

    /**
     * Initialise le contrôleur d'événements clavier.
     *
     * @param vue - L'instance de la vue pour la gestion des retours visuels
     * @param modele - L'instance du modèle contenant l'état du jeu et la logique métier
     */
    public ControleurClavier(Vue vue, Modele modele) {
        this.modele = modele;
        this.vue = vue;
    }

    /** ---------- [Méthodes Publiques / Écouteurs] ---------- **/

    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Détecte les touches enfoncées et déclenche les actions métier correspondantes.
     * Gère la relance de partie, la gestion de l'équipement, l'interaction avec le monde
     * et la navigation dans l'interface utilisateur.
     *
     * @param e - L'événement clavier contenant le code de la touche pressée
     */
    @Override
    public void keyPressed(KeyEvent e) {

        Joueur joueur = modele.getJoueur();
        double camX = joueur.getX();
        double camY = joueur.getY();

        // Gestion prioritaire de la relance de jeu ou de la boucle d'équipement
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (modele.getPartieTerminee()) {
                modele.reinitialiserJeu();
                return;
            } else {
                // Vérifie la disponibilité d'une arme secondaire avant le switch
                if (modele.getJoueur().getArmePasEquipee() != null){
                    // Empêche le changement d'arme si une animation d'attaque est en cours
                    if (!vue.getVueArme().getEnAnimation()) {
                        modele.getJoueur().switchArmes();
                    } else {
                        vue.afficherTexteErreur("Impossible de changer d'arme pendant une attaque !", camX, camY - (double) vue.getHeight() /16, Color.RED);
                    }
                } else {
                    vue.afficherTexteErreur("Aucune autre arme à équiper !", camX, camY - (double) vue.getHeight() /16, Color.RED);
                }
            }
        }

        // Raccourci alternatif pour le changement d'arme
        if (e.getKeyCode() == KeyEvent.VK_X){
            modele.getJoueur().switchArmes();
        }

        // Bascule l'affichage visuel de la portée de l'arme
        if (e.getKeyCode() == KeyEvent.VK_C) {
            vue.getVueArme().setAffPortee(!vue.getVueArme().getAffPortee());
        }

        // Interaction avec l'environnement : récolte de ressources
        if (e.getKeyCode() == KeyEvent.VK_R) {
            modele.getJoueur().recolterMine();
        }

        // Navigation cyclique dans les pages du HUD (inventaire/shop)
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            int page = (modele.getHudPageActuelle() + 1) % 3 + 1;
            modele.setHudPageActuelle(page);
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            int page = (modele.getHudPageActuelle() % 3) + 1;
            modele.setHudPageActuelle(page);
        }

        // Affichage ou masquage des points de vie (Interface Système)
        if (e.getKeyCode() == KeyEvent.VK_P) {
            modele.toggleAffichagePV();
        }

        // Avancement manuel du cycle temporel
        if (e.getKeyCode() == KeyEvent.VK_S) {
            modele.getUpdateJN().passerJour();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}