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

    // Indique si c'est le jour (true) ou la nuit (false)
    private boolean jour = true;

    // Compteur de frames pour le cycle actuel
    private int framesInCurrentCycle = 0;

    // Constructeur qui démarre le thread du cycle jour/nuit
    public CycleJourNuit() {
        this.start();
    }

    // Getter pour savoir si c'est le jour ou la nuit
    public boolean isDay() {
        return jour;
    }

    @Override
    public void run() {
        while (true) {
            update(); // Gère le timer

            // Logique spécifique au jour ou à la nuit
            if (jour) {
                updateDayLogic();
            } else {
                updateNightLogic(); // Monstres, attaques
            }

            if (framesInCurrentCycle % FPS == 0) { // Affiche le temps restant toutes les secondes
                System.out.println("Jour " + jour + " - Temps restant: " + getTempsRestant() + "s" + " - Frames dans le cycle: " + framesInCurrentCycle + "/" + TICKS_PAR_CYCLE);
            }

            try {
                Thread.sleep(1000 / FPS); // Environ 16ms
            } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }

    /* Met à jour le timer du cycle jour/nuit, bascule entre le jour et la nuit lorsque le temps est écoulé.*/
    private void update() {
        framesInCurrentCycle++;
        if (framesInCurrentCycle >= TICKS_PAR_CYCLE) {
            jour = !jour; // On bascule
            framesInCurrentCycle = 0;
        }
    }

    // Getter pour le temps restant dans la phase actuelle (en secondes)
    public int getTempsRestant() {
        return DUREE_CYCLE - (framesInCurrentCycle / FPS);
    }

    // Tout ce qu'il se passe le jour
    public void updateDayLogic() {

    }

    // Tout ce qu'il se passe la nuit
    public void updateNightLogic() {

    }
}
