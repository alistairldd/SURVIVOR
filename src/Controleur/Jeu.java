package Controleur;

import Modele.Modele;
import Vue.Vue;

import javax.swing.*;
import java.awt.*;

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

        maFenetre.setVisible(true);
    }


    private void initaliserFenetre(){

        maFenetre = new JFrame("survivor");
        maFenetre.setExtendedState(JFrame.MAXIMIZED_BOTH); // Met la fenêtre en plein écran
        maFenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Ferme l'application lorsque la fenêtre est fermée
        maFenetre.setLayout(new BorderLayout()); // Utilise un BorderLayout pour organiser les composants
        maFenetre.setResizable(false);
    }

    private void initialiserVue() {
        // creation du panneau de droite
        JPanel panelDroite = new JPanel();
        panelDroite.setPreferredSize(new Dimension(300,0));
        panelDroite.setBackground(Color.RED); // Couleur de fond pour différencier le panneau            <- On peut modifier la couleur ici

        // Ajout de composants dans le panneau droite
        panelDroite.add(new JLabel("Menu de Contrôle"));

        // init vue principale
        vue = new Vue();
        vue.add(new JLabel("Le jeu le vrai"));
        // on ajoute les éléments en précisant les zones du BorderLayout
        maFenetre.add(vue, BorderLayout.CENTER);
        maFenetre.add(panelDroite, BorderLayout.EAST);



        maFenetre.revalidate(); // Rafraîchit la structure
        maFenetre.repaint();    // Redessine les composants
    }

     private void initialiserModele(){
        modele = new Modele();
     }

        private void initialiserControleur(){
            new controleurSouris(modele);
        }

}
