package Vue;

import java.awt.*;
import Controleur.ControleurSouris;
import Modele.Arme;
import Modele.Modele;

/**
 * Gère l'affichage dynamique de l'arme du joueur.
 * Calcule l'orientation de l'arme en temps réel pour qu'elle pointe toujours vers
 * le curseur de la souris, et applique les transformations visuelles lors des attaques (animations).
 */
public class VueArme {

    // Épaisseur visuelle de l'arme dessinée à l'écran
    public static final int TAILLE = 10;

    // Référence au contrôleur pour lire la position (X,Y) en temps réel du curseur
    private final ControleurSouris controleurSouris;
    private Modele modele;
    private Vue vue;

    // Angle supplémentaire ajouté artificiellement par le Thread d'animation lors d'un coup
    private double angleOffsetAnimation = 0; // angle de décalage pour l'animation d'attaque
    // Flag (drapeau) indiquant si une animation est en cours
    private boolean enAnimation = false; // indique si l'animation d'attaque est en

    // Constructeur de la classe VueArme
    public VueArme(ControleurSouris controleurSouris, Vue vue, Modele modele) {
        this.controleurSouris = controleurSouris;
        this.modele = modele;
        this.vue = vue;
    }


    // Injecte l'angle calculé par le Thread AnimationArme
    public void setAngleOffsetAnimation(double offset) {
        this.angleOffsetAnimation = offset;
    }

    // Verrouille ou déverrouille l'état d'animation
    public void setEnAnimation(boolean b) {
        this.enAnimation = b;
    }




    /**
     * Dessine l'arme en appliquant les transformations de position et de rotation.
     * @param g Le contexte graphique sur lequel dessiner.
     */
    // methode pour dessiner l'arme sur la carte
    public void dessiner(Graphics g) {
        /*
         * Cette méthode dessine l'arme du joueur en fonction de la position de la souris.
         * L'arme est dessinée à une distance fixe du joueur, dans la direction de la souris.
         * L'angle entre le joueur et la souris est calculé à l'aide de la fonction atan2, qui retourne l'angle en radians entre les deux points.
         * Ensuite, les coordonnées de l'arme sont calculées en utilisant les fonctions cos et sin pour déterminer la position de l'arme par rapport au joueur.
         * Enfin, l'arme est dessinée sous forme de rectangle gris à la position calculée.
         */

        // Crée une copie du contexte graphique pour ne pas affecter le reste des dessins avec nos rotations
        Graphics2D g2d = (Graphics2D) g.create();

        // Récupère les caractéristiques physiques de l'arme pour l'affichage (notamment sa longueur/portée)
        Arme armeEquipee = modele.getJoueur().getArmeEquipee();
        int portee = (int) armeEquipee.getPortee();

        // Récupérer la position du joueur (point d'origine de l'arme)
        int posJoueurX = (int) modele.getJoueur().getPositionX();
        int posJoueurY = (int) modele.getJoueur().getPositionY();

        // Récupérer le centre exact de la fenêtre d'affichage (où se trouve visuellement le joueur)
        int centerX = vue.getWidth() / 2;
        int centerY = vue.getHeight() / 2;

        // Récupérer les coordonnées brutes actuelles de la souris sur l'écran
        int mouseX = controleurSouris.getMX();
        int mouseY = controleurSouris.getMY();

        // Calculer l'angle directionnel entre le centre de l'écran et la souris
        double angle = Math.atan2(mouseY - centerY, mouseX - centerX);
        double angleOffset = 0; // aucun décalage par défaut

        // Distance radiale pour ne pas dessiner l'arme DANS le joueur, mais juste à côté (dans sa main)
        int rayon = 20; // distance entre le joueur et l'arme

        // Dessiner l'arme (couleur générique grise)
        g2d.setColor(Color.GRAY);

        // --- MAGIE DES TRANSFORMATIONS GRAPHIQUES ---
        // 1. Déplace le point d'origine du dessin (0,0) sur les coordonnées absolues du joueur
        g2d.translate(posJoueurX, posJoueurY);
        // 2. Pivote l'ensemble du calque autour de ce nouveau point (0,0) selon l'angle de la souris + l'animation d'attaque
        g2d.rotate(angle + angleOffsetAnimation);

        // 3. On dessine un simple rectangle horizontal.
        // Puisque le calque a été tourné, ce rectangle pointera naturellement vers la souris.
        // On le décale de "rayon" sur l'axe X pour l'éloigner du corps, et on centre son épaisseur (Y = -TAILLE/2)
        g2d.fillRect(rayon,-TAILLE/2, portee, TAILLE);

        // Libère la mémoire et annule les translations/rotations pour les prochains dessins
        g2d.dispose();
    }

}