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

/**
 * Contrôleur dédié à la gestion des événements du clavier.
 * Gère les interactions rapides du joueur (changement d'arme, utilisation de compétences, navigation HUD).
 */
public class ControleurClavier implements KeyListener {

    /** ---------- [Propriétés] ---------- **/

    private Modele modele;
    private Vue vue;

    /** ---------- [Constructeurs] ---------- **/

    public ControleurClavier(Vue vue, Modele modele) {
        this.modele = modele;
        this.vue = vue;
    }

    /** ---------- [Méthodes Héritées - KeyListener] ---------- **/

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    /**
     * Détecte les touches enfoncées et déclenche les actions métier correspondantes
     * (Relance de jeu, switch d'équipement, interactions, navigation UI).
     *
     * @param e - L'événement de touche de clavier capturé
     */
    @Override
    public void keyPressed(KeyEvent e) {
        Joueur joueur = modele.getJoueur();
        double camX = joueur.getX();
        double camY = joueur.getY();

        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (modele.getPartieTerminee()) {
                modele.reinitialiserJeu();
                return;
            } else {
                if (modele.getJoueur().getArmePasEquipee() != null) {
                    if (!vue.getVueArme().getEnAnimation()) {
                        modele.getJoueur().switchArmes();
                    } else {
                        vue.afficherTexteErreur("Impossible de changer d'arme pendant une attaque !", camX, camY - (double) vue.getHeight() / 16, Color.RED);
                    }
                } else {
                    vue.afficherTexteErreur("Aucune autre arme à équiper !", camX, camY - (double) vue.getHeight() / 16, Color.RED);
                }
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_X) {
            modele.getJoueur().switchArmes();
        }

        if (e.getKeyCode() == KeyEvent.VK_C) {
            vue.getVueArme().setAffPortee(!vue.getVueArme().getAffPortee());
        }

        if (e.getKeyCode() == KeyEvent.VK_R) {
            modele.getJoueur().recolterMine();
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            int page = (modele.getHudPageActuelle() + 1) % 3 + 1;
            modele.setHudPageActuelle(page);
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            int page = (modele.getHudPageActuelle() % 3) + 1;
            modele.setHudPageActuelle(page);
        }

        if (e.getKeyCode() == KeyEvent.VK_P) {
            modele.toggleAffichagePV();
        }

        if (e.getKeyCode() == KeyEvent.VK_S) {
            modele.getUpdateJN().passerJour();
        }
    }
}