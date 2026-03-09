package Vue;

import java.awt.*;
import Controleur.ControleurSouris;

public class VueArme {

    public static final int TAILLE = 10;
    private final ControleurSouris controleurSouris;
    private final Vue vue;

    // Constructeur de la classe VueArme
    public VueArme(ControleurSouris controleurSouris, Vue vue) {
        this.controleurSouris = controleurSouris;
        this.vue = vue;
    }

    // methode pour dessiner l'arme sur la carte
    public void dessiner(Graphics g2d) {
        /*
            * Cette méthode dessine l'arme du joueur en fonction de la position de la souris.
            * L'arme est dessinée à une distance fixe du joueur, dans la direction de la souris.
            * L'angle entre le joueur et la souris est calculé à l'aide de la fonction atan2, qui retourne l'angle en radians entre les deux points.
            * Ensuite, les coordonnées de l'arme sont calculées en utilisant les fonctions cos et sin pour déterminer la position de l'arme par rapport au joueur.
            * Enfin, l'arme est dessinée sous forme de rectangle gris à la position calculée.
         */

        // Récupérer la position du joueur
        int posJoueurX = (int) Modele.Joueur.getPositionX();
        int posJoueurY = (int) Modele.Joueur.getPositionY();

        // Calculer l'angle entre le joueur et la souris
        int centerX = vue.getWidth() / 2;
        int centerY = vue.getHeight() / 2;

        // Récupérer les coordonnées de la souris
        int mouseX = controleurSouris.getMX();
        int mouseY = controleurSouris.getMY();

        // Calculer l'angle entre le joueur et la souris
        double angle = Math.atan2(mouseY - centerY, mouseX - centerX);
        int rayon = 20; // distance entre le joueur et l'arme
        int armeX = (int) (posJoueurX + rayon * Math.cos(angle));
        int armeY = (int) (posJoueurY + rayon * Math.sin(angle));

        g2d.setColor(Color.GRAY);
        g2d.fillRect(armeX - TAILLE/2, armeY - TAILLE/2, TAILLE, TAILLE);
    }

}
