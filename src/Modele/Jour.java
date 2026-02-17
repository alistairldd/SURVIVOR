package Modele;

/*
 * La classe Jour, elle est utilisée pour gérer le jour dans le jeu. Elle est un thread qui s'exécute en parallèle avec les autres threads du modèle.
 * Elle contient une variable booléenne qui indique si c'est le jour ou la nuit, et une variable qui indique le temps restant avant que le jour ne se termine.
 * Elle contient également des méthodes pour lever et coucher le jour, et pour obtenir l'état du jour.
 *
 */
public class Jour extends Thread{

    /* DUREE_JOUR est la durée d'un jour en millisecondes, elle est utilisée pour déterminer combien de temps le jour doit durer avant de se terminer.*/
    public final static int DUREE_JOUR = 1500;

    /* temps_restant est le temps restant avant que le jour ne se termine */
    private  int temps_restant = DUREE_JOUR/100;

    /* jour est une variable booléenne qui indique si c'est le jour ou la nuit, elle est utilisée pour déterminer si le jour doit continuer à s'exécuter ou non. */
    private boolean jour = true;

    private Nuit nuit;

    public Jour() {
        this.start();
        nuit = new Nuit(this);
    }

    public boolean get_Jour() {
        return jour;
    }

    public void lever() {
        jour = true;
        temps_restant = DUREE_JOUR/100;
    }

    public void coucher() {
        jour = false;
    }

    public int getTemps_restant() {
        return temps_restant;
    }

        @Override
    public void run() {
        while(true) {
            if (temps_restant > 0 && jour) {
                try {
                    Thread.sleep(1000);
                    temps_restant -= 1;
                    System.out.println("Jour: " + jour + " Temps restant: " + temps_restant);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } else {
                // on change le jour
                jour = false;
                // vérifier la nullité et l'état avant d'appeler
                if (nuit != null) {
                    try {
                        if (!nuit.get_Nuit()) {
                            nuit.lever();
                        }
                    } catch (Exception e) {
                        // safeguard: ne pas planter si un état inattendu survient
                        e.printStackTrace();
                    }
                }
                // éviter une boucle occupée quand ce thread n'est pas actif
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
