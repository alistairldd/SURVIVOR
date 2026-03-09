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
        int posJoueurX = (int) Modele.Joueur.getPositionX();
        int posJoueurY = (int) Modele.Joueur.getPositionY();

        int centerX = vue.getWidth() / 2;
        int centerY = vue.getHeight() / 2;

        int mouseX = controleurSouris.getMX();
        int mouseY = controleurSouris.getMY();


        double angle = Math.atan2(mouseY - centerY, mouseX - centerX);
        int rayon = 20; // distance entre le joueur et l'arme
        int armeX = (int) (posJoueurX + rayon * Math.cos(angle));
        int armeY = (int) (posJoueurY + rayon * Math.sin(angle));

        g2d.setColor(Color.GRAY);
        g2d.fillRect(armeX - TAILLE/2, armeY - TAILLE/2, TAILLE, TAILLE);
    }

}
