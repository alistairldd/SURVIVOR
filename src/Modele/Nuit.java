package Modele;

public class Nuit extends Thread {

    public final static int DUREE_NUIT = 1500;
    private int temps_restant = DUREE_NUIT/100;
    private boolean nuit = false;
    private Jour jour;

    public Nuit(Jour leJour) {
        jour = leJour;
        this.start();
    }

    public boolean get_Nuit() {
        return nuit;
    }

    public void lever() {
        nuit = true;
        temps_restant= DUREE_NUIT/100;
    }

    public void coucher() {
        nuit = false;
    }

    public int getTemps_restant() {
        return temps_restant;
    }

    @Override
    public void run() {
        while (true) {
            if (temps_restant > 0 && nuit) {
                try {
                    Thread.sleep(1000);
                    temps_restant -= 1;
                    System.out.println("Nuit: " + nuit + " Temps restant: " + temps_restant);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } else {
                // Ne pas appeler jour.lever() en boucle : vérifier l'état avant d'appeler
                nuit = false;
                if (jour != null) {
                    try {
                        if (!jour.get_Jour()) {
                            jour.lever();
                        }
                    } catch (Exception e) {
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
