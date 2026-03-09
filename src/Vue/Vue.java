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
import Modele.Map;
import Modele.Monstre;

import javax.swing.*;
import java.awt.*;

public class Vue extends JPanel {

    // Taille de la fenêtre principale de l'application, elle est utilisée pour définir la taille de la fenêtre.
    public final static int LARGEUR = 1920;
    public final static int HAUTEUR = 1080;


    // Fenêtre principale de l'application, elle est utilisée pour afficher les composants de la vue.
    private JFrame maFenetre;

    // Vues
    private final VueHUD vueHUD;
    private final VueCarte vueCarte;
    private final VueJoueur vueJoueur;
    private final VueRessource vueRessource;
    private final VueMonstre vueMonstre;

    private final Modele modele;



    // Constructeur de la classe Vue, il initialise les données de la vue.
    public Vue(Modele modele) {
        /* Initialisation de la fenêtre principale de l'application, elle est utilisée pour afficher les composants de la vue. */
        maFenetre = new JFrame("survivor");
        maFenetre.setPreferredSize(new Dimension(LARGEUR,HAUTEUR));
        maFenetre.setExtendedState(JFrame.MAXIMIZED_BOTH); // Met la fenêtre en plein écran
        maFenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Ferme l'application lorsque la fenêtre est fermée
        maFenetre.setLayout(new BorderLayout()); // Utilise un BorderLayout pour organiser les composants
        maFenetre.setResizable(true);

        /* Initialisation du panneau droit de la fenêtre, il est utilisé pour afficher les informations du joueur et les ressources. */
        this.vueHUD = new VueHUD(modele);
        // Ajout de composants dans le panneau droite

        this.add(new JLabel("Le jeu le vrai"));
        // on ajoute les éléments en précisant les zones du BorderLayout
        maFenetre.add(this, BorderLayout.CENTER);
        maFenetre.add(vueHUD, BorderLayout.EAST);


        this.vueCarte = new VueCarte(modele);
        this.vueJoueur = new VueJoueur();

        this.addMouseListener(new controleurSouris(this, modele));

        this.modele = modele;
        new Redessine (this, modele);
        this.vueRessource = new VueRessource();

        this.vueMonstre = new VueMonstre();


        maFenetre.pack();
        maFenetre.setVisible(true);
    }

    /* ---- GETTERS ET SETTERS ---- */

    /* Getter pour l'HUD */
    public VueHUD getVueHUD() {
        return vueHUD;
    }


    protected void dessineMinimap(Graphics2D g2d) {
        // On peut dessiner une mini carte en haut à droite, qui montre la position du joueur et des ressources par rapport à la carte entière.
        // On peut faire ça en dessinant un petit rectangle qui représente la carte entière,
        // puis en dessinant un point pour le joueur et des points pour les ressources,
        // en utilisant les mêmes coordonnées que pour le monde, mais en les adaptant à la taille de la mini carte.
        // Par exemple, si la mini carte fait 200x200 pixels, et que la carte entière fait
        // 2000x2000 pixels, alors on peut dessiner le joueur à (joueurX / 10, joueurY / 10)
        // sur la mini carte, et les ressources à (ressourceX / 10, ressourceY / 10).
        // On peut aussi dessiner une bordure autour de la mini carte pour la différencier du
        // reste de l'interface.

        int tailleMinimap = 300;

        g2d.translate(getWidth() - tailleMinimap-10, 10); // On se place en haut à droite pour dessiner la mini carte
        g2d.setColor(new Color(50,70,50)); // du gris pour le fond de la mini carte
        g2d.fillRect(0, 0, tailleMinimap, tailleMinimap); // Dessine le fond de la mini carte
        g2d.setColor(Color.RED);
        g2d.drawRect(0, 0, tailleMinimap, tailleMinimap); // Dessine la bordure de la mini carte

        for (Ressource r : Modele.getMap().getRessources()) {
            int resX = modele.map (0, Map.LARGEUR_MAP, 0, tailleMinimap-4, r.getPositionX()); // Convertit les coordonnées de la ressource pour les adapter à la mini carte
            int resY = modele.map (0, Map.HAUTEUR_MAP, 0, tailleMinimap-4, r.getPositionY()); // idem
            VueRessource.dessinerRessource(g2d, r, resX, resY, true); // Dessine la ressource sur la mini carte
        }

        g2d.setColor(Color.BLACK);
        int posX = modele.map (0, Map.LARGEUR_MAP, 0, tailleMinimap-5, (int) Joueur.getPositionX()); // Convertit les coordonnées du joueur pour les adapter à la mini carte
        int posY = modele.map (0, Map.HAUTEUR_MAP, 0, tailleMinimap-5, (int) Joueur.getPositionY()); // idem
        g2d.fillOval(posX,posY, 5, 5);

        for (Monstre m : modele.getMonstres()) {
            int monstreX = modele.map (0, Map.LARGEUR_MAP, 0, tailleMinimap-4, m.getX()); // Convertit les coordonnées du monstre pour les adapter à la mini carte
            int monstreY = modele.map (0, Map.HAUTEUR_MAP, 0, tailleMinimap-4, m.getY()); // idem
            vueMonstre.dessiner(g2d, m, monstreX, monstreY, true);
        }

    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Nettoie l'écran
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // AMÉLIORE LA PRÉCISION DES POSITIONS
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        // --- CALCUL DE LA CAMÉRA ---
        // On veut que le joueur soit au centre du panneau (this)
        // camX/Y représentent le coin haut-gauche de ce que l'on voit dans le monde
        double camX = Joueur.getPositionX() - ((double) getWidth() / 2);
        double camY = Joueur.getPositionY() - ((double) getHeight() / 2);

        // --- DÉBUT DE LA ZONE MONDE ---
        // On demande à Graphics de décaler tout ce qu'on va dessiner ensuite
        g2d.translate(-camX, -camY);

        // 1. Dessiner le fond (VueCarte doit dessiner de 0,0 à LargeurMap, HauteurMap)
        vueCarte.dessiner(g2d);

        // 2. Dessiner les ressources
        // PLUS BESOIN de calculs compliqués : on utilise leurs vraies coordonnées X, Y
        for (Ressource r : Modele.getMap().getRessources()) {
            vueRessource.dessinerRessource(g2d, r, r.getPositionX(), r.getPositionY(), false);
        }

        // 3. Dessiner le joueur (à sa vraie position X, Y dans le monde)
        // Comme on a fait un translate(-camX, -camY), il apparaîtra pile au centre de l'écran
        vueJoueur.dessiner(g2d);

        // 4. Dessiner les monstres (si on en a)
        for (Monstre m : modele.getMonstres()) {
            vueMonstre.dessiner(g2d, m, m.getX(), m.getY(), false);
        }

        // --- FIN DE LA ZONE MONDE ---
        g2d.translate(camX, camY); // On remet à zéro pour l'interface si besoin


        dessineMinimap(g2d);

    }
}
