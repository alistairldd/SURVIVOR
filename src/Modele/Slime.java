package Modele;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static Modele.Constantes.IMAGES_SLIMES;

/**
 * Implémentation concrète d'un ennemi de base : le Slime.
 * Hérite de la classe abstraite Monstre et définit ses statistiques de combat.
 */
public class Slime extends Monstre {

    // Référence au gestionnaire de monstres
    private final GestionnaireMonstres gestionnaireMonstres;

    private final Image imageSlime;

    // Variable pour gérer l'animation du slime

    // on prend un index aléatoire pour commencer l'animation du slime à des frames différentes
    int randomNum = ThreadLocalRandom.current().nextInt(0, 51);

    /**
     * Crée un Slime à des coordonnées précises (généralement fournies par le GestionnaireMonstres).
     * @param x Coordonnée d'apparition horizontale.
     * @param y Coordonnée d'apparition verticale.
     */
    public Slime(int x, int y, GestionnaireMonstres gestionnaireMonstres) {
        // Appelle le constructeur parent (Monstre) en lui injectant les statistiques de cette espèce :
        // Nom: "Slime"
        // PV: 50
        // Attaque: 5 points de dégâts
        // Portée: 50 pixels
        // Vitesse: 1 pixel par déplacement
        super("Slime", 50, 5, 30, 5);

        // Initialise la position de départ avec les coordonnées fournies
        this.x = x;
        this.y = y;
        // Choisit un index aléatoire entre 0 et 5
        Random rand = new Random();
        int indexAleatoire = rand.nextInt(IMAGES_SLIMES.size());
        // Récupère l'image 30x30 correspondante
        this.imageSlime = IMAGES_SLIMES.get(indexAleatoire); // Choix aléatoire d'une image de slime pour la variété visuelle
        this.gestionnaireMonstres = gestionnaireMonstres;
        this.start(); // Démarre le thread du monstre pour qu'il commence à agir immédiatement après sa création
    }

    // Getter Image
    public Image getImage() {
        return this.imageSlime;
    }

    @Override
    public int getMaxHp() {
        return 50; // PV maximum du Slime
    }

    @Override
    public void run() {
    // On définit le pas de temps (50ms exprimé en secondes)
            double dt = 0.05;
        // Boucle de comportement du monstre
        while(true) {

            try {
                if (randomNum <= 0){
                    this.ajouterAnimation( Math.PI/8); // Incrémente l'animation pour faire osciller le slime
                }
                else {
                    randomNum--; // Décrémente le compteur pour atteindre 0 et déclencher l'animation
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
                e.printStackTrace();
                // Sécurité : arrête le thread en cas d'interruption externe pour éviter un thread zombie
                break;
            }
        }
    }
}