package Controleur;

import Modele.Modele;
import Vue.Vue;

import javax.swing.*;

public class Jeu {

    private JFrame maFenetre;
    private JPanel panel;
    private Vue vue;
    private Modele modele;


    public Jeu() {
        initaliserFenetre();
        initialiserVue();
        initialiserModele();
        initialiserControleur();
    }


    private void initaliserFenetre(){

        maFenetre = new JFrame("survivor");
        maFenetre.setExtendedState(JFrame.MAXIMIZED_BOTH); // Met la fenêtre en plein écran
        maFenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Ferme l'application lorsque la fenêtre est fermée
        maFenetre.setVisible(true);
        maFenetre.setResizable(false);
    }

     private void initialiserVue(){
        vue = new Vue();
        maFenetre.add(vue);
        maFenetre.pack();
    }

     private void initialiserModele(){
        modele = new Modele();
     }

        private void initialiserControleur(){
            new controleurSouris(modele);
        }

}
