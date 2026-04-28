package Vue;

import Modele.Modele;

import java.util.Objects;

/**
 * Thread autonome gérant l'animation visuelle d'une attaque.
 * Il pilote directement les offsets d'affichage de VueArme afin de découpler
 * la sensation de mouvement de la boucle principale du jeu.
 */
public class AnimationArme extends Thread {

    /** ---------- [Propriétés] ---------- **/

    private VueArme vueArme;
    private int duree;
    private int pas = 15;
    private Modele modele;

    /** ---------- [Constructeurs] ---------- **/

    /**
     * Initialise l'animation associée à une attaque.
     *
     * @param vueArme - Vue de l'arme à animer
     * @param duree - Durée totale de l'animation, généralement alignée sur la cadence de l'arme
     * @param modele - Modèle permettant de récupérer les caractéristiques de l'arme équipée
     */
    public AnimationArme(VueArme vueArme, int duree, Modele modele) {
        this.duree = duree;
        this.vueArme = vueArme;
        this.modele = modele;
    }

    /** ---------- [Méthodes Privées - Calculs d'animation] ---------- **/

    /**
     * Calcule l'offset angulaire à appliquer à l'arme en fonction de l'avancement.
     * Le comportement dépend du type d'arme afin de conserver une lecture cohérente
     * avec son intention de gameplay.
     *
     * @param ratio - Progression normalisée de l'animation entre 0.0 et 1.0
     * @return décalage angulaire à appliquer
     */
    private double calculerOffset(double ratio) {
        double angle = modele.getJoueur().getArmeEquipee().getAngle();

        // La lance privilégie une animation en translation, sans balayage angulaire.
        if (Objects.equals(modele.getJoueur().getArmeEquipee().getNom(), "Lance")) {
            return 0;
        }

        // La hache part de l'axe initial et déroule son mouvement sur tout l'angle.
        if (Objects.equals(modele.getJoueur().getArmeEquipee().getNom(), "Hache")) {
            return ratio * angle;
        } else {
            // Les autres armes utilisent un balayage centré autour de leur axe d'attaque.
            return (-angle / 2) + (ratio * angle);
        }
    }

    /**
     * Calcule l'avancée visuelle de la lance pendant l'attaque.
     * La courbe sinus produit un aller-retour fluide avec une extension maximale
     * atteinte au milieu de l'animation.
     *
     * @param ratio - Progression normalisée de l'animation entre 0.0 et 1.0
     * @return translation à appliquer en pixels
     */
    private int calculerTranslationLance(double ratio) {
        int amplitude = (int) (modele.getJoueur().getArmeEquipee().getPortee() * 0.2);
        return (int) (Math.sin(Math.PI * ratio) * amplitude);
    }

    /** ---------- [Méthodes Publiques - Exécution] ---------- **/

    /**
     * Exécute l'animation de l'arme dans un thread dédié.
     * Les offsets sont recalculés à intervalles réguliers jusqu'à la fin prévue,
     * puis l'état visuel est systématiquement réinitialisé.
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

        // Réinitialisation systématique pour éviter de laisser l'arme dans un état visuel intermédiaire.
        vueArme.setAngleOffsetAnimation(0);
        vueArme.setTranslationOffsetAnimation(0);
        vueArme.setEnAnimation(false);
    }
}