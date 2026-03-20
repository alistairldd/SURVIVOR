package Modele;

import Vue.VueArme;

public class AnimationArme extends Thread{

    private VueArme vueArme;
    private int duree; // Durée de l'animation en millisecondes
    private int pas = 15; // Pas de temps en ms pour l'animation

    public AnimationArme(VueArme vueArme, int duree) {
        this.duree = duree;
        this.vueArme = vueArme;
    }

    private double calculerOffset(double ratio) {
        /*
            * Cette fonction utilise une fonction sinus pour créer une animation fluide de l'arme.
            * Le ratio est un nombre entre 0 et 1 qui représente la progression de l'animation.
            * Lorsque le ratio est de 0, l'offset est de -π/4 (l'arme est en position de départ).
            * Lorsque le ratio est de 1, l'offset est de π/4 (l'arme est en position d'attaque).
         */
        return (-Math.PI/4) + (ratio * Math.PI/2);
    }
    @Override
    public void run(){
        long debut = System.currentTimeMillis();
        long maintenant = debut;

        while (maintenant - debut < duree) {
            double ratio = (double)(maintenant - debut) / duree; // Progression de 0 à 1
            vueArme.setAngleOffsetAnimation(calculerOffset(ratio)); // Met à jour l'angle de l'arme en fonction de la progression de l'animation
            try {
                Thread.sleep(pas); // Pause pour créer l'effet d'animation
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            maintenant = System.currentTimeMillis(); // Met à jour le temps actuel
        }
        vueArme.setAngleOffsetAnimation(0); // Réinitialise l'angle de l'arme à la fin de l'animation
        vueArme.setEnAnimation(false); // Indique que l'animation est terminée
    }
}
