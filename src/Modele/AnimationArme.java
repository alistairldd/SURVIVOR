package Modele;

import Vue.VueArme;

/**
 * Thread autonome gérant l'effet visuel de l'attaque d'une arme.
 * Il modifie progressivement l'angle d'affichage dans la vue (VueArme)
 * pour simuler un mouvement de frappe fluide, indépendamment de la boucle principale du jeu.
 * (Note architecturale : Ce thread crée un lien direct exceptionnel du Modèle vers la Vue pour les besoins d'animation).
 */
public class AnimationArme extends Thread{

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
        // Calcule l'angle exact en fonction de l'avancement (balayage d'un angle de 90 degrés, soit PI/2)
        double angle = modele.getJoueur().getArmeEquipee().getAngle();
        return (-angle/2) + (ratio * angle);
    }

    /**
     * Exécute la boucle d'animation dans un flux séparé.
     * Met à jour l'angle d'offset visuel à intervalles réguliers (définis par "pas")
     * jusqu'à l'écoulement de la durée prévue, puis réinitialise l'état de l'arme.
     */
    @Override
    public void run(){
        // Enregistre l'heure exacte du début de l'animation
        long debut = System.currentTimeMillis();
        // Initialise le curseur de temps actuel
        long maintenant = debut;

        // Boucle d'animation : tourne tant que le temps écoulé ne dépasse pas la durée prévue
        while (maintenant - debut < duree) {
            // Calcule le pourcentage d'accomplissement de l'animation (entre 0.0 et 1.0)
            double ratio = (double)(maintenant - debut) / duree;

            // Applique l'angle calculé directement sur l'objet graphique
            vueArme.setAngleOffsetAnimation(calculerOffset(ratio));

            try {
                // Met le thread en pause brièvement pour laisser le temps à l'écran de s'afficher
                Thread.sleep(pas);
            } catch (InterruptedException e) {
                // Gère l'interruption inattendue du thread
                e.printStackTrace();
            }
            // Met à jour le chronomètre pour la prochaine itération de la boucle
            maintenant = System.currentTimeMillis();
        }

        // Fin de la boucle : on force le retour de l'arme à sa position droite originelle
        vueArme.setAngleOffsetAnimation(0);
        // Prévient la vue que le mouvement est terminé pour qu'elle puisse reprendre son comportement normal
        vueArme.setEnAnimation(false);
    }
}