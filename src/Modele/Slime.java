package Modele;

/**
 * Implémentation concrète d'un ennemi de base : le Slime.
 * Hérite de la classe abstraite Monstre et définit ses statistiques de combat.
 */
public class Slime extends Monstre {

    /**
     * Crée un Slime à des coordonnées précises (généralement fournies par le GestionnaireMonstres).
     * @param x Coordonnée d'apparition horizontale.
     * @param y Coordonnée d'apparition verticale.
     */
    public Slime(int x, int y) {
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
    }

    @Override
    public int getMaxHp() {
        return 50; // PV maximum du Slime
    }
}