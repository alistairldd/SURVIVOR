package Vue;

/**
 * Moteur de rendu graphique (Game Loop d'affichage).
 * C'est un Thread autonome dont l'unique but est de forcer l'interface
 * à se redessiner en continu, garantissant une animation fluide (ex: déplacements, tirs)
 * indépendamment de la vitesse de calcul du Modèle.
 */
public class Redessine extends Thread {
    /* thread qui redessine l'affichage à intervalles réguliers
     * il appelle la méthode repaint() de l'affichage pour déclencher le redessinage
     * le délai entre chaque redessinage est défini par la constante DELAY
     * le thread s'exécute en continu tant que l'application est ouverte*/

    // Référence à la zone de dessin principale (le monde)
    private Vue vue;
    // Référence au panneau d'interface utilisateur (panneau latéral)
    private VueHUD vueHUD;
    // Délai en millisecondes entre chaque image (50ms = 20 images par seconde / FPS)
    public final static int DELAY = 50;

    /*constructeur*/
    /**
     * Initialise et démarre immédiatement la boucle de rendu.
     * @param vue L'interface principale contenant la carte.
     * @param modele Le modèle (non utilisé directement ici, mais passé par convention ou pour évolution future).
     */
    public Redessine(Vue vue, Modele.Modele modele) {
        // Enregistre les références des panneaux à rafraîchir
        this.vue = vue;
        this.vueHUD = vue.getVueHUD();
        // Lance automatiquement l'exécution de la méthode run()
        this.start();
    }

    /*redéfinition de run*/
    /**
     * Boucle infinie d'affichage.
     */
    @Override
    public void run() {
        // Tourne tant que le jeu n'est pas fermé
        while(true){
            // Demande au système Java Swing de nettoyer et rappeler paintComponent() sur le monde
            vue.repaint();
            // Demande la même chose pour l'interface utilisateur latérale
            vueHUD.repaint();

            try {
                // Met le thread en pause pendant 50ms pour cadencer l'affichage
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                // Relance une exception critique si le moteur de rendu plante
                throw new RuntimeException(e);
            }
        }
    }
}