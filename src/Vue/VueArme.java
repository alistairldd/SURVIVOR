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
    // Flag indiquant si une animation est en cours
    private boolean enAnimation = false; // indique si l'animation d'attaque est en
    // Flag pour afficher ou non la portée de l'arme (cône d'attaque)
    private boolean affPortee = false; // affiche la portée de l'arme (c

    // Constructeur de la classe VueArme
    public VueArme(ControleurSouris controleurSouris, Vue vue, Modele modele) {
        this.controleurSouris = controleurSouris;
        this.modele = modele;
        this.vue = vue;
    }

    public boolean getAffPortee() {
        return affPortee;
    }

    public void setAffPortee(boolean b) {
        affPortee = b;
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
        double ouvertureCone = armeEquipee.getAngle();

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


        // Déplace le point d'origine du dessin (0,0) sur les coordonnées absolues du joueur
        g2d.translate(posJoueurX, posJoueurY);
        // Pivote l'ensemble du calque autour de ce nouveau point (0,0) selon l'angle de la souris
        g2d.rotate(angle);

        if (affPortee) {
            // Dessiner un cône semi-transparent pour représenter la zone d'attaque de l'arme
            g2d.setColor(new Color(0, 150, 255, 60));

            // La méthode fillArc prend des degrés. On convertit l'ouverture (ex: PI/3 -> 60°)
            int arcAngle = (int) Math.toDegrees(ouvertureCone);

            // Pour que la souris soit pile au milieu du cône, on commence à dessiner
            // à la moitié de l'angle en négatif (ex: de -30° à +30°)
            int startAngle = -arcAngle / 2;

            // fillArc dessine dans un rectangle englobant. Pour un cercle de rayon "portee" centré sur (0,0),
            // le coin supérieur gauche est à (-portee, -portee) et sa taille est (portee*2, portee*2)
            g2d.fillArc(-portee, -portee, portee * 2, portee * 2, startAngle, arcAngle);

            // Optionnel : un petit trait de contour bleu foncé pour faire plus propre
            g2d.setColor(new Color(0, 100, 255, 150));
            g2d.drawArc(-portee, -portee, portee * 2, portee * 2, startAngle, arcAngle);
            // On dessine aussi deux lignes pour relier le joueur au bord de l'arc
            g2d.drawLine(0, 0, (int) (portee * Math.cos(Math.toRadians(startAngle))), (int) (portee * Math.sin(Math.toRadians(startAngle))));
            g2d.drawLine(0, 0, (int) (portee * Math.cos(Math.toRadians(startAngle + arcAngle))), (int) (portee * Math.sin(Math.toRadians(startAngle + arcAngle))));
        }
        g2d.rotate( angleOffsetAnimation); // Applique la rotation d'animation
        // Dessiner l'arme (couleur générique grise)
        g2d.setColor(Color.GRAY);
        // 3. On dessine un simple rectangle horizontal.
        // Puisque le calque a été tourné, ce rectangle pointera naturellement vers la souris.
        // On le décale de "rayon" sur l'axe X pour l'éloigner du corps, et on centre son épaisseur (Y = -TAILLE/2)
        g2d.fillRect(rayon,-TAILLE/2, portee-rayon, TAILLE);

        // Libère la mémoire et annule les translations/rotations pour les prochains dessins
        g2d.dispose();
    }

}