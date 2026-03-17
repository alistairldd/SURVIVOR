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

    private boolean enAnimation = false; // Indique si une animation de tir est en cours
    private int progressionAnim = 0; // Progression de l'animation
    private final int DUREE_ANIM = 15; // Durée maximale de l'animation

    // Constructeur de la classe VueArme
    public VueArme(ControleurSouris controleurSouris, Vue vue, Modele modele) {
        this.controleurSouris = controleurSouris;
        this.modele = modele;
        this.vue = vue;
    }

    public void declancherAnimation(){
        /*
            * Cette méthode déclenche l'animation de tir de l'arme. Elle est appelée lorsque le joueur attaque.
            * Si une animation est déjà en cours, elle ne fait rien. Sinon, elle réinitialise la progression de l'animation et indique qu'une animation est en cours.
         */
        if (!enAnimation) {
            enAnimation = true;
            progressionAnim = 0;
        }
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

        if (enAnimation){
            // On calcule un ration entre le début et la fin
            double ratio = (double) progressionAnim / DUREE_ANIM;

            // On veut que l'épée aille de -45° à +45° pendant l'animation, on calcule donc un angle de décalage en fonction du ratio
            double angleDébut = -Math.PI / 4; // -45°
            double angleFin = Math.PI / 4; // +45°
            angleOffset = angleDébut + ratio * (angleFin - angleDébut);

            // On incrémente l'animation
            progressionAnim++;

            if (progressionAnim >= DUREE_ANIM) {
                enAnimation = false; // Fin de l'animation
            }
        }

        // Dessiner l'arme
        g2d.setColor(Color.GRAY);

        // On utilise les transformations pour dessiner l'arme à la bonne position et avec le bon angle
        g2d.translate(posJoueurX, posJoueurY);
        g2d.rotate(angle + angleOffset);

        // On dessine un rectangle centré sur le joueur, à une distance de rayon, avec une taille de TAILLE
        g2d.fillRect(rayon,-TAILLE/2, portee, TAILLE);

        g2d.dispose();
    }

}
