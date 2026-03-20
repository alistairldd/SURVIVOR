package Controleur;

import Modele.Modele;
import Vue.Vue;
import Modele.Joueur;
import Modele.DeplaceJoueur;
import Modele.Monstre;
import Modele.AnimationArme;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

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
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)){

            Joueur j = modele.getJoueur();
            if (j.peutAttaquer()){
                int centerX = vue.getWidth() / 2;
                int centerY = vue.getHeight() / 2;

                double angleAttaque = Math.atan2(mouseY - centerY, mouseX - centerX);
                j.attaquer(angleAttaque);
                j.setDernierTempsAttaque();
                int cadence = j.getArmeEquipee().getCadence();
                AnimationArme animation = new AnimationArme(vue.getVueArme(), cadence);
                vue.getVueArme().setEnAnimation(true);
                animation.start();
            }
        }
    }


    @Override
    public void mousePressed(MouseEvent e) {

        if (SwingUtilities.isRightMouseButton(e)){
            // Récupérer les coordonnées du clic
            int x = e.getX();
            int y = e.getY();

            // Calculer la position du joueur en fonction de la position du clic et de la position actuelle du joueur
            double camX = Joueur.getPositionX() - (double) vue.getWidth() / 2;
            double camY = Joueur.getPositionY() - (double) vue.getHeight() / 2;

            // Calculer les coordonnées de destination dans le monde en ajoutant les coordonnées du clic à la position de la caméra
            double destX = camX + x;
            double destY = camY + y;

            // Déplacer le joueur vers la position de destination
            DeplaceJoueur deplacement = new DeplaceJoueur(destX, destY);
            Joueur.setThreadActuel(deplacement);
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
