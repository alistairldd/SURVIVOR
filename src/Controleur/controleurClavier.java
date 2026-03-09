package Controleur;

import Modele.Modele;
import Vue.Vue;
import Modele.Joueur;
import Modele.Map;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class controleurClavier implements KeyListener {

    private Modele modele;
    private Vue vue;

    public controleurClavier(Vue vue, Modele modele) {

        this.modele = modele;
        this.vue = vue;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("Action : La touche E a été pressée !");
        if (e.getKeyCode() == KeyEvent.VK_E) {
            modele.getJoueur().ramasse_ressource(Map.getRessources()) ;

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
