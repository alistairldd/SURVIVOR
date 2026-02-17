package Controleur;

import Modele.Modele;
import Vue.Vue;
import Modele.Joueur;
import Modele.DeplaceJoueur;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class controleurSouris implements MouseListener {

    private Modele modele;
    private Vue vue;


    public controleurSouris(Vue vue, Modele modele) {

        this.modele = modele;
        this.vue = vue;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println("Clic détecté à la position : (" + e.getX() + ", " + e.getY() + ")");
        if (SwingUtilities.isRightMouseButton(e)){
            // Récupérer les coordonnées du clic
            int x = e.getX();
            int y = e.getY();

            // Calculer la position du joueur en fonction de la position du clic et de la position actuelle du joueur
            int camX = Joueur.getPositionX() - vue.getWidth() / 2;
            int camY = Joueur.getPositionY() - vue.getHeight() / 2;

            // Calculer les coordonnées de destination dans le monde en ajoutant les coordonnées du clic à la position de la caméra
            int destX = camX + x;
            int destY = camY + y;

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
}
