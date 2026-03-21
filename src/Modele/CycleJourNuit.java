package Modele;

/* Cette classe gère le cycle jour/nuit de manière autonome.
 * Elle alterne entre les phases de jour et de nuit à intervalles réguliers.
 * Pendant le jour, elle peut déclencher des événements liés au jour (ex: croissance des ressources).
 * Pendant la nuit, elle peut déclencher des événements liés à la nuit (ex: apparition de monstres).
 * Le cycle est basé sur un timer interne qui compte les frames pour déterminer quand basculer.
 */
public class CycleJourNuit extends Thread {


    // Constantes pour le cycle
    // FPS (frames per second) pour le timer, on peut ajuster pour accélérer ou ralentir le cycle
    public final static int FPS = 60;
    public final static int DUREE_CYCLE = 15; // 15 secondes par phase (jour ou nuit)
    // Nombre de ticks (frames) par cycle
    public final static int TICKS_PAR_CYCLE = DUREE_CYCLE * FPS;



    // Compteur de frames pour le cycle actuel
    private int framesInCurrentCycleJour = 0;

    private int framesInCurrentCycleNuit = 0;

    private UpdateJN updateJN = new UpdateJN();

    // Constructeur qui démarre le thread du cycle jour/nuit
    public CycleJourNuit() {
        this.start();
    }

    @Override
    public void run() {
        updateJN.changeJour(); // Commence par le jour
        while (true) {
            boolean jour = updateJN.isDay();
            if (updateJN.isDay()) {
                // Gère le timer
                framesInCurrentCycleJour++;
                // Si on a atteint la fin du cycle de jour, on bascule à la nuit
                if (framesInCurrentCycleJour >= TICKS_PAR_CYCLE) {
                    // Réinitialise le compteur de frames pour le jour
                    framesInCurrentCycleJour = 0;
                    updateJN.changeNuit(); // On bascule
                } else {
                    updateJN.updateJour(); // Logique spécifique au jour
                    if (framesInCurrentCycleJour % FPS == 0) { // Affiche le temps restant toutes les secondes
                        //System.out.println("Jour " + jour + " - Temps restant: " + getTempsRestantJour() + "s" + " - Frames dans le cycle: " + framesInCurrentCycleJour + "/" + TICKS_PAR_CYCLE);
                    }
                }
            } else {
                // Gère le timer
                framesInCurrentCycleNuit++;
                // Si on a atteint la fin du cycle de nuit, on bascule au jour
                if (framesInCurrentCycleNuit >= TICKS_PAR_CYCLE) {
                    // Réinitialise le compteur de frames pour la nuit
                    framesInCurrentCycleNuit = 0;
                    updateJN.changeJour(); // On bascule au jour
                } else {
                    updateJN.updateNuit(); // Logique spécifique à la nuit
                    if (framesInCurrentCycleNuit % FPS == 0) { // Affiche le temps restant toutes les secondes
                        //System.out.println("Jour " + jour + " - Temps restant: " + getTempsRestantNuit() + "s" + " - Frames dans le cycle: " + framesInCurrentCycleNuit + "/" + TICKS_PAR_CYCLE);
                    }
                }
            }

            try {
                Thread.sleep(1000 / FPS); // Environ 16ms
            } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }

    public int getTempsRestant() {
        if (updateJN.isDay()) {
            return getTempsRestantJour();
        } else {
            return getTempsRestantNuit();
        }
    }

    // Getter pour le temps restant dans la phase actuelle (en secondes)
    public int getTempsRestantJour() {
        return DUREE_CYCLE - (framesInCurrentCycleJour / FPS);
    }

    // Getter pour le temps restant dans la phase actuelle (en secondes)
    public int getTempsRestantNuit() {
        return DUREE_CYCLE - (framesInCurrentCycleNuit / FPS);
    }

    public boolean isDay() {
        return updateJN.isDay();
    }

    // Getter updateJN
    public UpdateJN getUpdateJN() {
        return updateJN;
    }

}
