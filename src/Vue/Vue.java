package Vue;

import Modele.Joueur;
/*
* La classe générale de la vue, elle contient les classes de données et les méthodes pour manipuler ces données.
* Elle est utilisée pour afficher les données de l'application et pour recevoir les événements de l'utilisateur
* et pour les transmettre au contrôleur. Elle est également utilisée pour gérer les threads de la vue.
*
 */

import Controleur.controleurSouris;
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

    private final Modele modele;

    // Constructeur de la classe Vue, il initialise les données de la vue.
    public Vue(Modele modele) {
        /* Initialisation de la fenêtre principale de l'application, elle est utilisée pour afficher les composants de la vue. */
        maFenetre = new JFrame("survivor");
        maFenetre.setVisible(true);
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


        this.vueCarte = new VueCarte(modele);
        this.vueJoueur = new VueJoueur();

        this.addMouseListener(new controleurSouris(this, modele));

        this.modele = modele;
        new Redessine (this, modele);
        this.vueRessource = new VueRessource();
    }

    /* ---- GETTERS ET SETTERS ---- */





    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Nettoie l'écran
        Graphics2D g2d = (Graphics2D) g;

        // --- CALCUL DE LA CAMÉRA ---
        // On veut que le joueur soit au centre du panneau (this)
        // camX/Y représentent le coin haut-gauche de ce que l'on voit dans le monde
        int camX = Joueur.getPositionX() - (getWidth() / 2);
        int camY = Joueur.getPositionY() - (getHeight() / 2);

        // --- DÉBUT DE LA ZONE MONDE ---
        // On demande à Graphics de décaler tout ce qu'on va dessiner ensuite
        g2d.translate(-camX, -camY);

        // 1. Dessiner le fond (VueCarte doit dessiner de 0,0 à LargeurMap, HauteurMap)
        vueCarte.dessiner(g2d);

        // 2. Dessiner les ressources
        // PLUS BESOIN de calculs compliqués : on utilise leurs vraies coordonnées X, Y
        for (Ressource r : Modele.getMap().getRessources()) {
            vueRessource.dessinerRessource(g2d, r, r.getPositionX(), r.getPositionY());
        }

        // 3. Dessiner le joueur (à sa vraie position X, Y dans le monde)
        // Comme on a fait un translate(-camX, -camY), il apparaîtra pile au centre de l'écran
        vueJoueur.dessiner(g2d);

        // --- FIN DE LA ZONE MONDE ---
        g2d.translate(camX, camY); // On remet à zéro pour l'interface si besoin
    }
}
