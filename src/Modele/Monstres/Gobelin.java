package Modele.Monstres;

import Modele.GestionnaireMonstres;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static Modele.Constantes.IMAGES_SLIMES_MUTANT;

/**
 * Implémentation concrète d'un ennemi : le gobelin.
 * Hérite de la classe abstraite Monstre et définit ses statistiques de combat.
 */

public class Gobelin extends Monstre {

    // Référence au gestionnaire de monstres
    private final GestionnaireMonstres gestionnaireMonstres;

    // Variable pour gérer l'animation du gobelin

    /**
     * Crée un Ogre à des coordonnées précises (généralement fournies par le GestionnaireMonstres).
     *
     * @param x Coordonnée d'apparition horizontale.
     * @param y Coordonnée d'apparition verticale.
     */
    public Gobelin(int x, int y, GestionnaireMonstres gestionnaireMonstres) {
        // Appelle le constructeur parent (Monstre) en lui injectant les statistiques de cette espèce :
        // Nom: "Gobelin"
        // PV: 65
        // Attaque: 10 points de dégâts
        // Portée: 40 pixels
        // Vitesse: 8 pixels par déplacement
        // Drop: 5 pièces en récompense
        super("Gobelin", 65, 10, 40, 8, 5);;

        // Initialise la position de départ avec les coordonnées fournies
        this.x = x;
        this.y = y;

        this.gestionnaireMonstres = gestionnaireMonstres;
        this.start(); // Démarre le thread du monstre pour qu'il commence à agir immédiatement après sa création
    }

    @Override
    public void run() {
        // On définit le pas de temps (50ms exprimé en secondes)
        double dt = 0.05;
        // Boucle de comportement du monstre
        while (true) {

            try {
                if (marche) {
                    animation += 1; // Incrémente l'animation pour faire bouger le monstre
                    if (animation % 10 == 0) {
                        animationMarche = !animationMarche;
                    }
                } else {
                    animationAtt += 1; // Incrémente l'animation d'attaque pour faire bouger le monstre
                    if (animationAtt % 10 == 0) {
                        animationAttaque = !animationAttaque;
                    }

                }
                this.mettreAJourPosition(gestionnaireMonstres.trouverCible(this), dt);
                Thread.sleep(50); // Petite pause pour ne pas surcharger le processeur
                if (this.getHp() <= 0) {
                    // Demande la suppression visuelle et logique de la carte
                    gestionnaireMonstres.supprimerMonstre(this);
                    gestionnaireMonstres.incrementerMonstresMorts();
                    // Interrompt la boucle infinie pour terminer le thread proprement
                    break;
                }
            } catch (InterruptedException e) {
                // Capture de l'interruption
                System.out.println("Debug : Le thread du " + this.getNom() + " a bien été tué.");

                // Sécurité : arrête le thread en cas d'interruption externe pour éviter un thread zombie
                break;
            }
        }
    }
}

