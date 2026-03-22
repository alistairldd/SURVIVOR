package Controleur;

import Modele.Modele;
import Vue.Vue;
import Modele.Joueur;
import Modele.DeplaceJoueur;
import Modele.AnimationArme;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Contrôleur dédié à la gestion des événements de la souris.
 * Gère les interactions fondamentales du joueur : l'attaque (clic gauche),
 * le déplacement (clic droit), et l'orientation de l'arme (mouvement).
 */
public class ControleurSouris implements MouseListener, MouseMotionListener {

    private Modele modele;
    private Vue vue;
    private Joueur joueur;

    private int mouseX = 0;
    private int mouseY = 0;

    public ControleurSouris(Vue vue, Modele modele) {

        this.modele = modele;
        this.vue = vue;
        this.joueur = modele.getJoueur();

    }

    /**
     * Gère les clics de la souris.
     * Sur un clic gauche : Vérifie le temps de recharge (cooldown) de l'arme. Si l'attaque est possible,
     * calcule l'angle de tir depuis le centre de l'écran, applique les dégâts via le modèle
     * et déclenche le Thread d'animation de l'arme côté Vue.
     * * @param e L'événement de clic de souris capturé.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)){

            Joueur j = modele.getJoueur();
            if (j.peutAttaquer()){
                int centerX = vue.getWidth() / 2;
                int centerY = vue.getHeight() / 2;

                double angleAttaque = Math.atan2(mouseY - centerY, mouseX - centerX);

                //System.out.println("angleAttaque=" + angleAttaque + " mouse=(" + mouseX + "," + mouseY + ")");

                // Attaquer dans la direction de la souris
                modele.joueurAttaque(angleAttaque);
                j.setDernierTempsAttaque();
                int cadence = j.getArmeEquipee().getCadence();
                AnimationArme animation = new AnimationArme(vue.getVueArme(), cadence, modele);
                vue.getVueArme().setEnAnimation(true);
                animation.start();
            }
        }
    }

    /**
     * Gère la pression des boutons de la souris.
     * Sur un clic droit : Convertit les coordonnées du clic à l'écran en coordonnées "Monde"
     * (en appliquant l'offset de la caméra centré sur le joueur), puis lance un Thread autonome
     * (DeplaceJoueur) pour gérer le mouvement de manière fluide.
     * * @param e L'événement de pression de souris capturé.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)){
            // Récupérer les coordonnées du clic
            int x = e.getX();
            int y = e.getY();

            // Calculer la position du joueur en fonction de la position du clic et de la position actuelle du joueur
            double camX = joueur.getX() - (double) vue.getWidth() / 2;
            double camY = joueur.getY() - (double) vue.getHeight() / 2;

            // Calculer les coordonnées de destination dans le monde en ajoutant les coordonnées du clic à la position de la caméra
            double destX = camX + x;
            double destY = camY + y;

            // Déplacer le joueur vers la position de destination
            DeplaceJoueur deplacement = new DeplaceJoueur(destX, destY, joueur);
            joueur.setThreadActuel(deplacement);
            deplacement.start();
            //modele.getJoueur().deplaceJoueur(destX, destY);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    /**
     * Met à jour en continu les coordonnées de la souris.
     * Ces coordonnées sont lues par la VueArme pour orienter le dessin de l'arme en temps réel
     * vers le curseur de l'utilisateur.
     * * @param e L'événement de mouvement de souris capturé.
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    // Getters pour les coordonnées de la souris

    public int getMX(){
        return this.mouseX;
    }

    public int getMY(){
        return this.mouseY;
    }
}