package Vue;

import Modele.Modele;

import java.util.Objects;

/**
 * Thread autonome gérant l'effet visuel de l'attaque d'une arme.
 * Il modifie progressivement l'angle d'affichage dans la vue (VueArme)
 * pour simuler un mouvement de frappe fluide, indépendamment de la boucle principale du jeu.
 * (Note architecturale : Ce thread crée un lien direct exceptionnel du Modèle vers la Vue pour les besoins d'animation).
 */
public class AnimationArme extends Thread {

    // Référence à l'objet graphique de l'arme que l'on va faire pivoter
    private VueArme vueArme;
    // Durée totale prévue pour l'animation complète de l'attaque en millisecondes
    private int duree;
    // Intervalle de rafraîchissement (en ms) entre chaque image de l'animation pour un rendu fluide
    private int pas = 15;
    private Modele modele;

    /**
     * @param vueArme L'instance graphique de l'arme qui subira la rotation.
     * @param duree   La durée totale de l'animation (généralement calquée sur le cooldown/cadence de l'arme).
     * @param modele
     */
    public AnimationArme(VueArme vueArme, int duree, Modele modele) {
        // Initialisation des paramètres de l'animation
        this.duree = duree;
        this.vueArme = vueArme;
        this.modele = modele;
    }

    /**
     * Calcule le décalage angulaire à appliquer à l'arme à un instant T.
     *
     * @param ratio La progression temporelle de l'animation, de 0.0 (début) à 1.0 (fin).
     * @return L'angle de décalage (offset) en radians.
     */
    private double calculerOffset(double ratio) {
        /*
         * Cette fonction utilise une fonction sinus pour créer une animation fluide de l'arme.
         * Le ratio est un nombre entre 0 et 1 qui représente la progression de l'animation.
         * Lorsque le ratio est de 0, l'offset est de -π/4 (l'arme est en position de départ).
         * Lorsque le ratio est de 1, l'offset est de π/4 (l'arme est en position d'attaque).
         */
        // Calcule l'angle exact en fonction de l'avancement
        double angle = modele.getJoueur().getArmeEquipee().getAngle();
        if (Objects.equals(modele.getJoueur().getArmeEquipee().getNom(), "Lance")) {
            return 0; // le lance n'a pas d'animation de rotation, il part droit devant et revient droit devant
        }
        if (Objects.equals(modele.getJoueur().getArmeEquipee().getNom(), "Hache")) {
            return ratio * angle; // on commence pas à -angle/2 pour la hache, elle part de 0 et fait un tour complet
        } else {
            return (-angle / 2) + (ratio * angle);
        }

    }

    private int calculerTranslationLance(double ratio) {
        int amplitude = (int) (modele.getJoueur().getArmeEquipee().getPortee() * 0.2);
        // Courbe aller-retour fluide : 0 -> max -> 0
        return (int) (Math.sin(Math.PI * ratio) * amplitude);
    }

    /**
     * Exécute la boucle d'animation dans un flux séparé.
     * Met à jour l'angle d'offset visuel à intervalles réguliers (définis par "pas")
     * jusqu'à l'écoulement de la durée prévue, puis réinitialise l'état de l'arme.
     */
    @Override
    public void run() {
        long debut = System.currentTimeMillis();
        long maintenant = debut;

        boolean estLance = Objects.equals(modele.getJoueur().getArmeEquipee().getNom(), "Lance");

        while (maintenant - debut < duree) {
            double ratio = (double) (maintenant - debut) / duree;

            if (estLance) {
                vueArme.setAngleOffsetAnimation(0);
                vueArme.setTranslationOffsetAnimation(calculerTranslationLance(ratio));
            } else {
                vueArme.setTranslationOffsetAnimation(0);
                vueArme.setAngleOffsetAnimation(calculerOffset(ratio));
            }

            try {
                Thread.sleep(pas);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            maintenant = System.currentTimeMillis();
        }

        // reset propre dans tous les cas
        vueArme.setAngleOffsetAnimation(0);
        vueArme.setTranslationOffsetAnimation(0);
        vueArme.setEnAnimation(false);
    }
}
