package Modele.Monstres;

import Modele.GestionnaireMonstres;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static Modele.Constantes.IMAGES_SLIMES_MUTANT;

/**
 * Implémentation concrète d'une variante : le Slime Mutant.
 * Statistiques légèrement supérieures au Slime classique.
 */
public class SlimeMutant extends Monstre {

    /** ---------- [Propriétés] ---------- **/

    private final GestionnaireMonstres gestionnaireMonstres;
    private final Image imageSlimeMutant;

    // Index aléatoire pour désynchroniser les animations entre les différentes entités
    int randomNum = ThreadLocalRandom.current().nextInt(0, 51);

    /** ---------- [Constructeurs] ---------- **/

    public SlimeMutant(int x, int y, GestionnaireMonstres gestionnaireMonstres) {
        super("SlimeMutant", 80, 8, 35, 6, 6);

        this.x = x;
        this.y = y;

        // Attribution aléatoire d'une image depuis le sprite sheet
        Random rand = new Random();
        int indexAleatoire = rand.nextInt(IMAGES_SLIMES_MUTANT.size());
        this.imageSlimeMutant = IMAGES_SLIMES_MUTANT.get(indexAleatoire);

        this.gestionnaireMonstres = gestionnaireMonstres;
        this.start();
    }

    /** ---------- [Accesseurs] ---------- **/

    @Override
    public Image getImage() {
        return this.imageSlimeMutant;
    }

    /** ---------- [Méthodes Héritées - Boucle de Vie (Thread)] ---------- **/

    /**
     * Gère la boucle comportementale avec une animation d'oscillation basée sur le décalage (randomNum).
     */
    @Override
    public void run() {
        double dt = 0.05;

        while(true) {
            try {
                // Délai d'attente avant le début du cycle d'animation
                if (randomNum <= 0){
                    this.ajouterAnimation(Math.PI / 8);
                }
                else {
                    randomNum--;
                }

                this.mettreAJourPosition(gestionnaireMonstres.trouverCible(this), dt);
                Thread.sleep(50);

                // Conditions de décès
                if (this.getHp() <= 0) {
                    gestionnaireMonstres.supprimerMonstre(this);
                    gestionnaireMonstres.incrementerMonstresMorts();
                    break;
                }
            } catch (InterruptedException e) {
                System.out.println("Debug : Le thread du " + this.getNom() + " a bien été tué.");
                break;
            }
        }
    }
}