package Controleur;

import Modele.Modele;
import Vue.Vue;

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
            int x = e.getX();
            int y = e.getY();
            modele.getJoueur().deplaceJoueur(x, y);
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
