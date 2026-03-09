package Controleur;

import Modele.Modele;
import Vue.Vue;
import Modele.Map;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ControleurClavier implements KeyListener {

    private Modele modele;
    private Vue vue;
    private Map map;

    public ControleurClavier(Vue vue, Modele modele) {

        this.modele = modele;
        this.vue = vue;
        this.map = modele.getMap();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("Action : La touche E a été pressée !");
        if (e.getKeyCode() == KeyEvent.VK_E) {
            modele.getJoueur().ramasse_ressource(map.getRessources()) ;

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
