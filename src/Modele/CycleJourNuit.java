package Modele;
import static Modele.Constantes.*;
/**
 * Ce thread gère le cycle temporel du jeu de manière autonome.
 * Il alterne entre les phases de jour (exploration/récolte) et de nuit (survie/combats).
 * Il se base sur un compteur de "frames" (tours de boucle) pour déterminer le moment exact du basculement,
 * et délègue les conséquences de ce basculement à l'objet UpdateJN.
 */
public class CycleJourNuit extends Thread {

    // Compteur interne pour suivre l'avancement exact de la phase de jour actuelle
    private int framesInCurrentCycleJour = 0;
    // Compteur interne pour suivre l'avancement exact de la phase de nuit actuelle
    private int framesInCurrentCycleNuit = 0;

    // Objet responsable d'appliquer les changements d'état (spawn de monstres, nettoyage, etc.)
    private UpdateJN updateJN;

    /**
     * Constructeur qui démarre automatiquement le thread du cycle jour/nuit
     * dès sa création en mémoire.
     */
    public CycleJourNuit(UpdateJN updateJN) {
        // Lance l'exécution de la méthode run() en parallèle
        this.updateJN = updateJN;
        this.start();
    }

    /**
     * Boucle principale du thread temporel.
     * S'exécute en continu, incrémente les compteurs de frames et déclenche
     * les transitions Jour -> Nuit et Nuit -> Jour quand le temps est écoulé.
     */
    @Override
    public void run() {
        // Initialise la partie en forçant l'état de "Jour" au tout début
        updateJN.changeJour();

        // Boucle infinie pour maintenir le temps qui passe tout au long du jeu
        while (true) {
            // Vérifie l'état actuel (vrai = jour, faux = nuit)

            if (updateJN.getModele().getPartieTerminee()) {
                // Si la partie est finie, on arrête le thread du cycle jour/nuit !
                Thread.currentThread().interrupt();
                break; // Sort de la boucle infinie, le thread s'arrête proprement.
            }

            if (updateJN.isDay()) {
                // Incrémente le compteur de temps pour le jour
                framesInCurrentCycleJour++;

                // Vérifie si la durée totale du jour a été atteinte
                if (framesInCurrentCycleJour >= TICKS_PAR_CYCLE_JOUR) {
                    // Remet le compteur du jour à zéro pour le prochain cycle
                    framesInCurrentCycleJour = 0;
                    // Déclenche la tombée de la nuit et ses événements (spawn de monstres)
                    updateJN.changeNuit();
                } else {
                    // Exécute la logique continue spécifique au jour
                    updateJN.updateJour();

                    // (Ligne commentée d'origine) Affiche le temps restant toutes les secondes
                    if (framesInCurrentCycleJour % FPS == 0) {
                        //System.out.println("Jour " + jour + " - Temps restant: " + getTempsRestantJour() + "s" + " - Frames dans le cycle: " + framesInCurrentCycleJour + "/" + TICKS_PAR_CYCLE);
                    }
                }
            } else {
                // Incrémente le compteur de temps pour la nuit
                framesInCurrentCycleNuit++;

                // Vérifie si la durée totale de la nuit a été atteinte
                if (framesInCurrentCycleNuit >= TICKS_PAR_CYCLE_NUIT) {

                    if (updateJN.getMonstresRestants() > 0) {
                        // Si des monstres sont encore en vie à la fin de la nuit, on considère que le joueur a perdu
                        updateJN.getModele().declencherGameOver();
                    }
                } else {
                    // Exécute la logique continue spécifique à la nuit (ex: supprimer les monstres morts)
                    updateJN.updateNuit();

                    // (Ligne commentée d'origine) Affiche le temps restant toutes les secondes
                    if (framesInCurrentCycleNuit % FPS == 0) {
                        //System.out.println("Jour " + jour + " - Temps restant: " + getTempsRestantNuit() + "s" + " - Frames dans le cycle: " + framesInCurrentCycleNuit + "/" + TICKS_PAR_CYCLE);
                    }
                }
            }

            try {
                // Met le thread en pause pendant ~16ms pour simuler un rythme de 60 FPS
                Thread.sleep(1000 / FPS);
            } catch (InterruptedException e) {
                // Capture et affiche l'erreur si le thread est interrompu brutalement
                e.printStackTrace();
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    // Calcule et retourne le temps restant de la phase globale actuelle (en secondes)
    public int getTempsRestant() {
        if (updateJN.isDay()) {
            return getTempsRestantJour();
        } else {
            return getTempsRestantNuit();
        }
    }

    // Calcule le temps restant (en secondes) spécifiquement pour la phase de jour
    public int getTempsRestantJour() {
        // Soustrait le nombre de secondes écoulées à la durée totale du cycle
        return DUREE_CYCLE_JOUR - (framesInCurrentCycleJour / FPS);
    }

    // Calcule le temps restant (en secondes) spécifiquement pour la phase de nuit
    public int getTempsRestantNuit() {
        // Soustrait le nombre de secondes écoulées à la durée totale du cycle
        return DUREE_CYCLE_NUIT - (framesInCurrentCycleNuit / FPS);
    }

    // Raccourci pour vérifier si c'est actuellement le jour
    public boolean isDay() {
        return updateJN.isDay();
    }

    public void resetFramesNuit() {
        framesInCurrentCycleNuit = 0;
    }

}