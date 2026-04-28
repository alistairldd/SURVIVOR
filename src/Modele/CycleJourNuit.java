package Modele;
import static Modele.Constantes.*;

/**
 * Gestionnaire asynchrone du cycle temporel global.
 * Implémente la boucle de jeu qui gère la progression du temps,
 * déclenchant le passage du jour (exploration) à la nuit (survie) selon un framerate défini.
 */
public class CycleJourNuit extends Thread {

    /** ---------- [Propriétés] ---------- **/

    private int framesInCurrentCycleJour = 0;
    private int framesInCurrentCycleNuit = 0;
    private UpdateJN updateJN;

    /** ---------- [Constructeur] ---------- **/

    /**
     * Initialise et démarre instantanément la thread responsable du temps.
     * * @param updateJN - L'interface pour notifier la logique de spawn/gestion de l'état
     */
    public CycleJourNuit(UpdateJN updateJN) {
        this.updateJN = updateJN;
        this.start();
    }


    /** ---------- [Méthodes Publiques] ---------- **/

    /**
     * @return Le temps restant de la phase actuelle en secondes
     */
    public int getTempsRestant() {
        if (updateJN.isDay()) {
            return getTempsRestantJour();
        } else {
            return getTempsRestantNuit();
        }
    }

    /**
     * @return Le temps restant avant la tombée de la nuit (secondes)
     */
    public int getTempsRestantJour() {
        return DUREE_CYCLE_JOUR - (framesInCurrentCycleJour / FPS);
    }

    /**
     * @return Le temps restant avant le lever du soleil (secondes)
     */
    public int getTempsRestantNuit() {
        return DUREE_CYCLE_NUIT - (framesInCurrentCycleNuit / FPS);
    }

    /**
     * @return true s'il fait actuellement jour, false sinon
     */
    public boolean isDay() {
        return updateJN.isDay();
    }

    public void resetFramesNuit() {
        framesInCurrentCycleNuit = 0;
    }


    /** ---------- [Boucle Principale (Thread)] ---------- **/

    /**
     * Main loop du thread :
     * Maintient le rythme du temps (FPS cible) et signale l'alternance
     * des phases Jour/Nuit au contrôleur UpdateJN.
     */
    @Override
    public void run() {
        updateJN.changeJour();

        while (true) {
            // Arrêt sécurisé du thread en cas de Game Over
            if (updateJN.getModele().getPartieTerminee()) {
                Thread.currentThread().interrupt();
                break;
            }

            // Phase de Jour
            if (updateJN.isDay()) {
                framesInCurrentCycleJour++;

                if (framesInCurrentCycleJour >= TICKS_PAR_CYCLE_JOUR) {
                    framesInCurrentCycleJour = 0;
                    updateJN.changeNuit();
                } else {
                    updateJN.updateJour();
                }
            }
            // Phase de Nuit
            else {
                framesInCurrentCycleNuit++;

                if (framesInCurrentCycleNuit >= TICKS_PAR_CYCLE_NUIT) {
                    // Condition de défaite : des monstres ont survécu à la nuit
                    if (updateJN.getMonstresRestants() > 0) {
                        updateJN.getModele().declencherGameOver();
                    }
                } else {
                    updateJN.updateNuit();
                }
            }

            // Maintien du rythme à ~60 FPS
            try {
                Thread.sleep(1000 / FPS);
            } catch (InterruptedException e) {
                if (!updateJN.getModele().getPartieTerminee()) {
                    System.err.println("ERREUR ANORMALE : Le cycle a été interrompu pendant le jeu !");
                    e.printStackTrace();
                }
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}