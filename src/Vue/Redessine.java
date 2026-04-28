package Vue;

import Vue.HUD.VueHUD;

import static Modele.Constantes.*;

/**
 * Moteur de rendu graphique.
 * Ce thread force le rafraîchissement régulier de la vue monde et du HUD afin
 * de maintenir une animation fluide indépendamment du reste des traitements.
 */
public class Redessine extends Thread {

    /** ---------- [Propriétés] ---------- **/

    private Vue vue;
    private VueHUD vueHUD;

    /** ---------- [Constructeurs] ---------- **/

    /**
     * Initialise la boucle de redessin et la démarre immédiatement.
     *
     * @param vue - Vue principale contenant la scène et l'accès au HUD
     * @param modele - Modèle transmis par convention d'initialisation
     */
    public Redessine(Vue vue, Modele.Modele modele) {
        this.vue = vue;
        this.vueHUD = vue.getVueHUD();

        // Le thread démarre dès sa création pour brancher immédiatement la boucle de rendu.
        this.start();
    }

    /** ---------- [Méthodes Publiques - Exécution] ---------- **/

    /**
     * Exécute la boucle de rafraîchissement graphique.
     * Le monde et le HUD sont redessinés au même rythme pour éviter un décalage
     * visuel entre la scène principale et les informations d'interface.
     */
    @Override
    public void run() {
        while (true) {
            vue.repaint();
            vueHUD.repaint();

            try {
                // Le délai cadence l'affichage et évite de monopoliser inutilement le CPU.
                Thread.sleep(REDESSINE_DELAY);
            } catch (InterruptedException e) {
                // Une interruption de cette boucle est considérée ici comme une rupture critique du rendu.
                throw new RuntimeException(e);
            }
        }
    }
}