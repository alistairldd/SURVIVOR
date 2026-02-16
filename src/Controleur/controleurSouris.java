package Controleur;

import Modele.Modele;
import Vue.Vue;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class controleurSouris implements MouseListener {

    private Modele modele;

    public controleurSouris(Modele modele) {
        this.modele = modele;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)){
            int x = e.getX();
            int y = e.getY();
            modele.deplaceJoueur(x, y);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

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
