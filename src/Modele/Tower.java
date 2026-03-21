package Modele;

import java.util.ArrayList;

public class Tower extends Batiment {
    public final int BASE_DAMAGE = 20;
    public final int BASE_RANGE = 100;

    // VITESSE D'ATTAQUE DE LA TOUR (1000 = 1 seconde)
    private int cadenceTir = 1000;

    private int range;
    private int damage;
    private long dernierTempsAttaque = 0; // Le chronomètre interne de LA tour

    // Mémorisation de la cible pour la vue
    private Monstre monstreCible = null;

    public Tower(int x, int y) {
        super(x, y);
        this.range = BASE_RANGE;
        this.damage = BASE_DAMAGE;
    }

    public int getRange() { return range; }

    // Getters pour la vue (pour dessiner le laser)
    public Monstre getMonstreCible() { return monstreCible; }
    public long getDernierTempsAttaque() { return dernierTempsAttaque; }

    public void attaquerSiPossible(ArrayList<Monstre> monstres) {
        long tempsActuel = System.currentTimeMillis();

        // Le bâtiment vérifie s'il s'est écoulé assez de temps depuis son dernier tir
        if (tempsActuel - dernierTempsAttaque >= cadenceTir) {

            monstreCible = null; // On réinitialise la cible

            for (Monstre m : monstres) {
                double distance = Math.hypot(m.getX() - this.x, m.getY() - this.y);

                if (distance <= this.range) {
                    m.perdreHp(this.damage);
                    System.out.println("Pew! Tour (" + x + "," + y + ") tire sur monstre " + m.getId());

                    // On remet le chrono à zéro pour CETTE tour
                    this.dernierTempsAttaque = tempsActuel;

                    // On mémorise le monstre attaqué pour l'effet visuel
                    this.monstreCible = m;

                    break; // On tire sur un seul monstre à la fois
                }
            }
        }
    }
}