package Modele;

import java.util.ArrayList;

public class ThreadBatiments extends Thread {

    private final Modele modele;
    private final int DELAY = 50; // Le thread tourne à 20 FPS pour vérifier les attaques

    public ThreadBatiments(Modele modele) {
        this.modele = modele;
    }

    @Override
    public void run() {
        while (true) {
            // On récupère les bâtiments et les monstres
            ArrayList<Batiment> batiments = modele.getMap().getBatiments();
            ArrayList<Monstre> monstres = modele.getMonstres();

            // On demande à chaque bâtiment de faire son action s'il le peut
            for (int i = 0; i < batiments.size(); i++) {
                Batiment b = batiments.get(i);

                // Si c'est une tour, elle gère son attaque à SA propre vitesse
                if (b instanceof Tower) {
                    ((Tower) b).attaquerSiPossible(monstres);
                }
            }

            try {
                Thread.sleep(DELAY); // Petite pause pour ne pas surcharger le processeur
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}