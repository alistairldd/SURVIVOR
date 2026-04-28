package Modele.Monstres;

import Modele.GestionnaireMonstres;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static Modele.Constantes.IMAGES_SLIMES_MUTANT;
import static Modele.Constantes.IMAGE_GOB_D;

/**
 * Implémentation concrète d'un ennemi rapide : le Gobelin.
 * Frappe vite et se déplace rapidement, mais reste fragile.
 */
public class Gobelin extends Monstre {

    /** ---------- [Propriétés] ---------- **/

    private final GestionnaireMonstres gestionnaireMonstres;

    /** ---------- [Constructeurs] ---------- **/

    public Gobelin(int x, int y, GestionnaireMonstres gestionnaireMonstres) {
        super("Gobelin", 65, 10, 40, 8, 5);

        this.x = x;
        this.y = y;
        this.gestionnaireMonstres = gestionnaireMonstres;
        this.start();
    }

    /** ---------- [Accesseurs] ---------- **/

    @Override
    public Image getImage() {
        return IMAGE_GOB_D;
    }

    /** ---------- [Méthodes Héritées - Boucle de Vie (Thread)] ---------- **/

    /**
     * Gère la boucle comportementale autonome du Gobelin.
     */
    @Override
    public void run() {
        double dt = 0.05;

        while (true) {
            try {
                // Gestion de la bascule d'animation
                if (marche) {
                    animation += 1;
                    if (animation % 10 == 0) {
                        animationMarche = !animationMarche;
                    }
                } else {
                    animationAtt += 1;
                    if (animationAtt % 10 == 0) {
                        animationAttaque = !animationAttaque;
                    }
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