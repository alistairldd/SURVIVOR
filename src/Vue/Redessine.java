package Vue;

public class Redessine extends Thread {
    /* thread qui redessine l'affichage à intervalles réguliers
     * il appelle la méthode repaint() de l'affichage pour déclencher le redessinage
     * le délai entre chaque redessinage est défini par la constante DELAY
     * le thread s'exécute en continu tant que l'application est ouverte*/
    private Vue vue;
    private VueHUD vueHUD;
    public final static int DELAY = 50;

    /*constructeur*/
    public Redessine(Vue vue, Modele.Modele modele) {
        this.vue = vue;
        this.vueHUD = vue.getVueHUD();
        this.start();
    }

    /*redéfinition de run*/
    @Override
    public void run() {
        while(true){
            vue.repaint();
            vueHUD.repaint();
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}