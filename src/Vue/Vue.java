package Vue;

import Modele.Joueur;
import Modele.Modele;
import Modele.Ressource;

import javax.swing.*;
import java.awt.*;

public class Vue extends JPanel {


    private JFrame maFenetre;


    // Vues
    private final VueCarte vueCarte;
    private final VueJoueur vueJoueur;
    private final VueRessource vueRessource;

    // Constructeur de la classe Vue, il initialise les données de la vue.
    public Vue(Modele modele) {
        /* Initialisation de la fenêtre principale de l'application, elle est utilisée pour afficher les composants de la vue. */
        maFenetre = new JFrame("survivor");
        maFenetre.setExtendedState(JFrame.MAXIMIZED_BOTH); // Met la fenêtre en plein écran
        maFenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Ferme l'application lorsque la fenêtre est fermée
        maFenetre.setLayout(new BorderLayout()); // Utilise un BorderLayout pour organiser les composants
        maFenetre.setResizable(false);


        // creation du panneau de droite
        JPanel panelDroite = new JPanel();
        panelDroite.setPreferredSize(new Dimension(300,0));
        panelDroite.setBackground(new Color (74, 9, 9)); // Couleur de fond pour différencier le panneau            <- On peut modifier la couleur ici

        // Ajout de composants dans le panneau droite
        panelDroite.add(new JLabel("Menu de Contrôle"));

        this.add(new JLabel("Le jeu le vrai"));
        // on ajoute les éléments en précisant les zones du BorderLayout
        maFenetre.add(this, BorderLayout.CENTER);
        maFenetre.add(panelDroite, BorderLayout.EAST);

        maFenetre.pack();
        maFenetre.setVisible(true);



        this.vueCarte = new VueCarte(modele);
        this.vueJoueur = new VueJoueur();
        this.vueRessource = new VueRessource();
    }

    /* ---- GETTERS ET SETTERS ---- */




    @Override
    public void paint(Graphics g) {

        super.paint(g);

        int mi_largeur = getWidth() / 2;
        int mi_hauteur = getHeight() / 2;

        // 1. Dessiner le fond
        vueCarte.dessiner(g, mi_largeur, mi_hauteur);

        // 2. Dessiner les ressources
        // On récupère la liste des ressources de la map
        for (Ressource r : Modele.getMap().getRessources()) {

            // Calcul de la position relative :
            // Centre écran + Position Ressource - Position Joueur
            int x = mi_largeur + r.getPositionX() - Joueur.getPositionX();
            int y = mi_hauteur + r.getPositionY() - Joueur.getPositionY();

            // On demande à VueRessource de dessiner CETTE ressource à CES coordonnées
            vueRessource.dessinerRessource(g, r, x, y);
        }

        // 3. Dessiner le joueur (toujours au centre)
        vueJoueur.dessiner(g, mi_largeur, mi_hauteur);
    }

}
