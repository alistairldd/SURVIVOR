package Modele;

import static Modele.Constantes.BAT_DELAY;

/**
 * Implémentation concrète d'un ennemi de base : le Slime.
 * Hérite de la classe abstraite Monstre et définit ses statistiques de combat.
 */
public class Slime extends Monstre {

    // Référence au gestionnaire de monstres
    private final GestionnaireMonstres gestionnaireMonstres;

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
        super("Slime", 50, 5, 30, 1);

        // Initialise la position de départ avec les coordonnées fournies
        this.x = x;
        this.y = y;
        this.gestionnaireMonstres = gestionnaireMonstres;
        this.start(); // Démarre le thread du monstre pour qu'il commence à agir immédiatement après sa création
    }

    @Override
    public int getMaxHp() {
        return 50; // PV maximum du Slime
    }

    @Override
    public void run() {
        // Boucle de comportement du monstre
        while(true) {
            try {
                this.mettreAJourPosition(gestionnaireMonstres.trouverCible(this));
                Thread.sleep(5); // Petite pause pour ne pas surcharger le processeur

                if (this.getHp() <= 0) {
                    // Demande la suppression visuelle et logique de la carte
                    gestionnaireMonstres.supprimerMonstre(this);
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