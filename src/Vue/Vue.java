package Vue;

import static Modele.Constantes.*;
import Modele.*;
/*
 * La classe générale de la vue, elle contient les classes de données et les méthodes pour manipuler ces données.
 * Elle est utilisée pour afficher les données de l'application et pour recevoir les événements de l'utilisateur
 * et pour les transmettre au contrôleur. Elle est également utilisée pour gérer les threads de la vue.
 *
 */
import Controleur.ControleurClavier;
import Controleur.ControleurSouris;


import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

/**
 * Fenêtre et zone de dessin principale du jeu.
 * Gère la fenêtre (JFrame), instancie toutes les sous-vues (Joueur, Monstres, Carte),
 * et s'occupe de la logique de Caméra (centrage sur le joueur) et de la Minimap.
 */
public class Vue extends JPanel {

    // Fenêtre principale de l'application, elle est utilisée pour afficher les composants de la vue.
    private JFrame maFenetre;

    // Vues
    // Déclaration de tous les "pinceaux" spécifiques responsables de dessiner chaque type d'entité
    private final VueHUD vueHUD;
    private final VueCarte vueCarte;
    //private final VueJoueur vueJoueur;
    private final VueArme vueArme;
    private final VueRessource vueRessource;
    private final VueBatiment vueBatiment;
    private final VueMonstre vueMonstre;


    // Modèle
    private final Modele modele;

    // Le gestionnaire visuel des particules de soin
    private VueEffetSoin vueEffetSoin;



    // Constructeur de la classe Vue, il initialise les données de la vue.
    public Vue(Modele modele) {
        /* Initialisation de la fenêtre principale de l'application, elle est utilisée pour afficher les composants de la vue. */
        maFenetre = new JFrame("survivor");
        maFenetre.setPreferredSize(new Dimension(LARGEUR,HAUTEUR));
        maFenetre.setExtendedState(JFrame.MAXIMIZED_BOTH); // Met la fenêtre en plein écran
        maFenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Ferme l'application lorsque la fenêtre est fermée
        maFenetre.setLayout(new BorderLayout()); // Utilise un BorderLayout pour organiser les composants (Centre + Bords)
        maFenetre.setResizable(true);



        /* Initialisation du panneau droit de la fenêtre, il est utilisé pour afficher les informations du joueur et les ressources. */
        this.vueHUD = new VueHUD(modele);
        // Ajout de composants dans le panneau droite



        // on ajoute les éléments en précisant les zones du BorderLayout
        // Place la zone de jeu au milieu (occupe tout l'espace disponible)
        maFenetre.add(this, BorderLayout.CENTER);
        // Place l'interface utilisateur statique sur la droite
        maFenetre.add(vueHUD, BorderLayout.EAST);

        // Initialisation des vues du monde, elles sont utilisées pour afficher les éléments du monde (carte, joueur, ressources, bâtiments).
        this.vueCarte = new VueCarte(modele);
        //this.vueJoueur = new VueJoueur();

        // Initialisation des contrôleurs de la vue,
        // ils sont utilisés pour recevoir les événements de l'utilisateur et pour les transmettre au contrôleur.
        ControleurSouris controleurSouris = new ControleurSouris(this, modele);
        // Abonne le JPanel principal aux clics de souris
        this.addMouseListener(controleurSouris);
        // Abonne le JPanel principal aux mouvements de souris (pour viser)
        this.addMouseMotionListener(controleurSouris);
        // Abonne le JPanel aux touches du clavier
        this.addKeyListener(new ControleurClavier(this, modele));


        // Initialisation du modèle, il est utilisé pour stocker les données de l'application et pour effectuer des opérations sur ces données.
        this.modele = modele;

        // Lance le moteur de rendu (qui va appeler paintComponent en boucle)
        new Redessine (this, modele);

        // Initialisation des vues du monde, elles sont utilisées pour afficher les éléments du monde (carte, joueur, ressources, bâtiments).
        this.vueRessource = new VueRessource();
        this.vueBatiment = new VueBatiment();
        this.vueMonstre = new VueMonstre();
        this.vueArme = new VueArme(controleurSouris, this, modele);

        this.vueEffetSoin = new VueEffetSoin(modele);

        // Demande à la fenêtre de calculer la taille de tous ses composants
        maFenetre.pack();
        // Affiche enfin la fenêtre à l'écran
        maFenetre.setVisible(true);

        // Indispensable pour que le KeyListener fonctionne : le panneau doit avoir le "focus"
        this.setFocusable(true); // Permet à la vue de recevoir des touches
        this.requestFocusInWindow(); // Demande le focus dès l'ouverture
    }

    /* ---- GETTERS ET SETTERS ---- */

    /* Getter pour l'HUD */
    public VueHUD getVueHUD() {
        return vueHUD;
    }

    public JFrame getMaFenetre() {
        return maFenetre;
    }

    /**
     * Dessine une carte miniature (radar) en haut à droite de l'écran.
     * Transforme les coordonnées du monde réel (3000x3000) en coordonnées locales pour
     * les faire rentrer dans un carré de 300x300 pixels.
     * @param g2d Le contexte graphique sur lequel dessiner.
     */
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

        // Taille en pixels du radar affiché à l'écran
        int tailleMinimap = 300;

        // Déplace l'origine de dessin de la minimap en haut à droite (largeur de l'écran - taille de la map - 10px de marge)
        g2d.translate(getWidth() - tailleMinimap-10, 10); // On se place en haut à droite pour dessiner la mini carte

        // --- DESSIN DU FOND ---
        g2d.setColor(new Color(50,70,50)); // du gris pour le fond de la mini carte
        g2d.fillRect(0, 0, tailleMinimap, tailleMinimap); // Dessine le fond de la mini carte
        g2d.setColor(Color.RED);
        g2d.drawRect(0, 0, tailleMinimap, tailleMinimap); // Dessine la bordure de la mini carte

        // --- DESSIN DES RESSOURCES ---
        for (Ressource r : modele.getUpdateJN().getRessources()) {
            // Utilise la fonction map() pour mettre à l'échelle : 3000 -> 300
            int resX = modele.map (0, LARGEUR_MAP, 0, tailleMinimap-4, r.getPositionX()); // Convertit les coordonnées de la ressource pour les adapter à la mini carte
            int resY = modele.map (0, HAUTEUR_MAP, 0, tailleMinimap-4, r.getPositionY()); // idem
            vueRessource.dessinerRessource(g2d, r, resX, resY, true); // Dessine la ressource sur la mini carte
        }

        // --- DESSIN DU JOUEUR ---
        g2d.setColor(Color.BLACK);
        Joueur joueur = modele.getJoueur();
        int posX = modele.map (0, LARGEUR_MAP, 0, tailleMinimap-5, (int) joueur.getX()); // Convertit les coordonnées du joueur pour les adapter à la mini carte
        int posY = modele.map (0, HAUTEUR_MAP, 0, tailleMinimap-5, (int) joueur.getY()); // idem
        // Dessine un petit point noir pour le joueur
        g2d.fillOval(posX,posY, 5, 5);

        // --- DESSIN DES BÂTIMENTS ---
        for (Batiment b : modele.getGestionnaireBatiments().getBatiments()) {
            int batX = modele.map(0, LARGEUR_MAP, 0, tailleMinimap - 4, (int) b.getX());
            int batY = modele.map(0, HAUTEUR_MAP, 0, tailleMinimap - 4, (int) b.getY());
            VueBatiment.dessinerBatiment(g2d, b, batX, batY, true);
        }

        // --- DESSIN DES MONSTRES ---
        for (Monstre m : modele.getUpdateJN().getMonstres()) {
            int monstreX = modele.map (0, LARGEUR_MAP, 0, tailleMinimap-4, (int) m.getX()); // Convertit les coordonnées du monstre pour les adapter à la mini carte
            int monstreY = modele.map (0, HAUTEUR_MAP, 0, tailleMinimap-4, (int) m.getY()); // idem
            vueMonstre.dessiner(g2d, m, monstreX, monstreY, true);
        }

    }


    /**
     * Cœur du rendu graphique appelé par le Thread Redessine.
     * C'est ici que s'opère la "magie" de la Caméra : plutôt que de déplacer le joueur
     * sur l'écran, on déplace tout le calque de dessin dans le sens inverse du joueur.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Nettoie l'écran (remplit avec la couleur de fond)
        // Convertit l'outil de base Graphics en Graphics2D, beaucoup plus puissant (rotations, translations, alpha)
        Graphics2D g2d = (Graphics2D) g;

        // Lisse les bords des dessins (anti-aliasing) pour un rendu plus net
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // AMÉLIORE LA PRÉCISION DES POSITIONS
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        // --- CALCUL DE LA CAMÉRA ---
        // On veut que le joueur soit au centre du panneau (this)
        // camX/Y représentent le coin haut-gauche de ce que l'on voit dans le monde
        Joueur joueur = modele.getJoueur();
        // Calcule le décalage pour centrer la vue exactement sur la position absolue du joueur
        double camX = joueur.getX() - ((double) getWidth() / 2);
        double camY = joueur.getY() - ((double) getHeight() / 2);

        // --- DÉBUT DE LA ZONE MONDE ---
        // On demande à Graphics de décaler tout ce qu'on va dessiner ensuite
        // C'est ce qui donne l'illusion que le joueur avance dans le décor
        g2d.translate(-camX, -camY);

        // 1. Dessiner le fond (VueCarte doit dessiner de 0,0 à LargeurMap, HauteurMap)
        vueCarte.dessiner(g2d);

        // 2. Dessiner les ressources
        // PLUS BESOIN de calculs compliqués : on utilise leurs vraies coordonnées X, Y
        // La translation de la caméra s'occupe de les afficher au bon endroit sur l'écran
        for (Ressource r : modele.getUpdateJN().getRessources()) {
            vueRessource.dessinerRessource(g2d, r, r.getPositionX(), r.getPositionY(), false);
        }

        // 3. Dessiner le joueur (à sa vraie position X, Y dans le monde)
        // Comme on a fait un translate(-camX, -camY), il apparaîtra pile au centre de l'écran
        //vueJoueur.dessiner(g2d, joueur);

        // 4. Dessiner les bâtiments
        for (Batiment b : modele.getGestionnaireBatiments().getBatiments()) {
            // Passe le relais à la sous-vue spécialisée avec les coordonnées absolues
            vueBatiment.dessinerBatiment(g2d, b, (int) b.getX(), (int) b.getY(), false);
        }

        // Calcule la nouvelle position des particules
        vueEffetSoin.miseAJour();
        // Dessine le grand cercle vert et les "+" par-dessus les bâtiments
        vueEffetSoin.dessiner(g2d);

        // Dessine l'arme du joueur par-dessus le reste
        vueArme.dessiner(g2d);

        // 5. Dessiner les monstres (si on en a)
        for (Monstre m : modele.getUpdateJN().getMonstres()) {
            vueMonstre.dessiner(g2d, m, (int) m.getX(), (int) m.getY(), false);
        }

        // --- FIN DE LA ZONE MONDE ---
        // Annule l'effet de caméra pour revenir aux coordonnées de l'écran fixes (0,0 en haut à gauche de la fenêtre)
        // Indispensable avant de dessiner des éléments d'interface qui ne doivent pas bouger avec le joueur (comme la minimap)
        g2d.translate(camX, camY); // On remet à zéro pour l'interface si besoin

        if (modele.getPartieTerminee()) {
            // --- ÉCRAN DE GAME OVER ---
            // Fond noir transparent
            g2d.setColor(new Color(0, 0, 0, 160));
            g2d.fillRect(0, 0, getWidth(), getHeight());

            // Texte rouge centré
            g2d.setColor(Color.RED);
            g2d.setFont(new Font("Arial", Font.BOLD, 100));
            String message = "GAME OVER";
            FontMetrics fm = g2d.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(message)) / 2;
            int y = (getHeight() / 2) + (fm.getAscent() / 4);

            g2d.drawString(message, x, y);

            // possible pour plus tard : Afficher le score (pièces, jours survécus...)

            g2d.setFont(new Font("Arial", Font.BOLD, 40));
            g2d.setColor(Color.BLACK);

            message = "Nombre de nuits passées : " + modele.getUpdateJN().getNbNuit();
            x = (getWidth() - g2d.getFontMetrics().stringWidth(message)) / 2;
            y += g2d.getFontMetrics().getHeight() + 20; // Décalage vertical pour le score
            g2d.drawString(message, x, y);


            message = "Nombre de monstres tués : " + modele.getUpdateJN().getMonGestionnaireMonstres().getNbMonstresMorts();
            x = (getWidth() - g2d.getFontMetrics().stringWidth(message)) / 2;
            y += g2d.getFontMetrics().getHeight() + 20; // Décalage vertical pour le score
            g2d.drawString(message, x, y);

            message = "cliquez n'importe ou pour recommencer";
            g2d.setFont(new Font("Arial", Font.BOLD, 15));
            g2d.setColor(Color.GRAY);
            x = (getWidth() - g2d.getFontMetrics().stringWidth(message)) / 2;
            y += g2d.getFontMetrics().getHeight() + 40; // Décalage vertical pour le score
            g2d.drawString(message, x, y);

            return; // ON S'ARRÊTE LÀ
        }


        // Dessine l'interface radar par-dessus le monde
        dessineMinimap(g2d);

    }

    public VueArme getVueArme() {
        return vueArme;
    }
}