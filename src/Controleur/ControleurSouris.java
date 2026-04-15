package Controleur;

import Modele.Modele;
import Vue.Vue;
import Modele.Joueur;

import Modele.Armes.Arme;
import Modele.Armure.Armure;
import Modele.Items.Item;

import Vue.AnimationArme;
import Modele.DeplaceJoueur;
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Contrôleur dédié à la gestion des événements de la souris.
 * Gère le mode RTS (Construction) ou les actions classiques (Attaque/Déplacement).
 */
public class ControleurSouris implements MouseListener, MouseMotionListener {

    private Modele modele;
    private Vue vue;

    private int mouseX = 0;
    private int mouseY = 0;

    public ControleurSouris(Vue vue, Modele modele) {
        this.modele = modele;
        this.vue = vue;
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        // 1. Priorité UI : Vérifier si on clique dans la boutique
        Object cible = vue.identifierElementClique(e.getX(), e.getY(), e.getSource());

        if (cible != null) {
            if (cible instanceof Arme) {
                modele.getGestionnaireShop().acheterArme((Arme) cible);
            } else if (cible instanceof Armure) {
                modele.getGestionnaireShop().acheterArmure((Armure)cible);
            } else if (cible instanceof Item) {
                modele.getGestionnaireShop().acheterItem((Item) cible);
            }
            return;
        }

        Joueur joueur = modele.getJoueur();
        double camX = joueur.getX() - (double) vue.getWidth() / 2;
        double camY = joueur.getY() - (double) vue.getHeight() / 2;
        double destX = camX + e.getX();
        double destY = camY + e.getY();

        // --- 1. DÉPLACEMENT (Clic Droit) ---
        if (SwingUtilities.isRightMouseButton(e)){
            if (!modele.getPartieTerminee()){
                DeplaceJoueur deplacement = new DeplaceJoueur(destX, destY, joueur);
                joueur.setThreadActuel(deplacement);
                deplacement.start();
            }
        }
        // --- 2. ACTION (Clic Gauche) ---
        else if (SwingUtilities.isLeftMouseButton(e)){
            if (!modele.getPartieTerminee()){

                // A. MODE CONSTRUCTION (RTS)
                if (modele.getModeConstruction() != Modele.TypeConstruction.AUCUN) {
                    modele.finaliserConstruction(destX, destY);
                }
                // B. MODE COMBAT (Classique)
                else {
                    if (joueur.peutAttaquer()){
                        int centerX = vue.getWidth() / 2;
                        int centerY = vue.getHeight() / 2;
                        double angleAttaque = Math.atan2(mouseY - centerY, mouseX - centerX);

                        modele.joueurAttaque(angleAttaque);
                        joueur.setDernierTempsAttaque();

                        int cadence = joueur.getArmeEquipee().getCadence();
                        AnimationArme animation = new AnimationArme(vue.getVueArme(), cadence, modele);
                        vue.getVueArme().setEnAnimation(true);
                        animation.start();
                    }
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!modele.getPartieTerminee()) {
            Joueur joueur = modele.getJoueur();

            mouseX = e.getX();
            mouseY = e.getY();

            double camX = joueur.getX() - (double) vue.getWidth() / 2;
            double camY = joueur.getY() - (double) vue.getHeight() / 2;

            double sourisMondeX = camX + mouseX;
            double sourisMondeY = camY + mouseY;

            // Transmission des coordonnées au Modèle pour le Fantôme et l'UI
            modele.setPositionSourisMonde(sourisMondeX, sourisMondeY);
            modele.verifierSurvol(sourisMondeX, sourisMondeY);
        }
    }

    public int getMX(){ return this.mouseX; }
    public int getMY(){ return this.mouseY; }
}