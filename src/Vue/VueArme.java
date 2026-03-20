package Vue;

import java.awt.*;
import Controleur.ControleurSouris;
import Modele.Arme;
import Modele.Modele;

public class VueArme {

    public static final int TAILLE = 10;
    private final ControleurSouris controleurSouris;
    private Modele modele;
    private Vue vue;

    private double angleOffsetAnimation = 0; // angle de décalage pour l'animation d'attaque
    private boolean enAnimation = false; // indique si l'animation d'attaque est en

    // Constructeur de la classe VueArme
    public VueArme(ControleurSouris controleurSouris, Vue vue, Modele modele) {
        this.controleurSouris = controleurSouris;
        this.modele = modele;
        this.vue = vue;
    }


    public void setAngleOffsetAnimation(double offset) {
        this.angleOffsetAnimation = offset;
    }

    public void setEnAnimation(boolean b) {
        this.enAnimation = b;
    }




    // methode pour dessiner l'arme sur la carte
    public void dessiner(Graphics g) {
        /*
            * Cette méthode dessine l'arme du joueur en fonction de la position de la souris.
            * L'arme est dessinée à une distance fixe du joueur, dans la direction de la souris.
            * L'angle entre le joueur et la souris est calculé à l'aide de la fonction atan2, qui retourne l'angle en radians entre les deux points.
            * Ensuite, les coordonnées de l'arme sont calculées en utilisant les fonctions cos et sin pour déterminer la position de l'arme par rapport au joueur.
            * Enfin, l'arme est dessinée sous forme de rectangle gris à la position calculée.
         */

        Graphics2D g2d = (Graphics2D) g.create();

        Arme armeEquipee = modele.getJoueur().getArmeEquipee();
        int portee = (int) armeEquipee.getPortee();

        // Récupérer la position du joueur
        int posJoueurX = (int) modele.getJoueur().getPositionX();
        int posJoueurY = (int) modele.getJoueur().getPositionY();

        // Calculer l'angle entre le joueur et la souris
        int centerX = vue.getWidth() / 2;
        int centerY = vue.getHeight() / 2;

        // Récupérer les coordonnées de la souris
        int mouseX = controleurSouris.getMX();
        int mouseY = controleurSouris.getMY();

        // Calculer l'angle entre le joueur et la souris
        double angle = Math.atan2(mouseY - centerY, mouseX - centerX);
        double angleOffset = 0; // aucun décalage par défaut

        int rayon = 20; // distance entre le joueur et l'arme

        // Dessiner l'arme
        g2d.setColor(Color.GRAY);

        // On utilise les transformations pour dessiner l'arme à la bonne position et avec le bon angle
        g2d.translate(posJoueurX, posJoueurY);
        g2d.rotate(angle + angleOffsetAnimation);

        // On dessine un rectangle centré sur le joueur, à une distance de rayon, avec une taille de TAILLE
        g2d.fillRect(rayon,-TAILLE/2, portee, TAILLE);

        g2d.dispose();
    }

}
