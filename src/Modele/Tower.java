package Modele;

import java.util.ArrayList;
import static Modele.Constantes.*;

/**
 * Bâtiment défensif automatisé (Tourelle).
 * Hérite des propriétés d'un Batiment classique (HP, position) mais intègre
 * sa propre logique de combat (portée, dégâts, cadence) et un système de ciblage
 * pour interagir avec le ThreadBatiments de manière indépendante.
 */
public class Tower extends Batiment {

    // Portée effective de cette instance précise
    private int range;
    // Dégâts effectifs de cette instance précise
    private int damage;
    // Mémorise l'heure du dernier tir pour vérifier le cooldown (Le chronomètre interne de LA tour)
    private long dernierTempsAttaque = 0;

    // Mémorisation de la cible pour la vue
    // Stocke temporairement l'ennemi visé pour que la VueBatiment sache où dessiner le laser/projectile
    private Monstre monstreCible = null;

    /**
     * Construit une tour défensive à des coordonnées précises.
     * @param x Coordonnée X de placement.
     * @param y Coordonnée Y de placement.
     */
    public Tower(int x, int y) {
        // Initialise la structure via le constructeur parent (Batiment)
        super(x, y);
        // Applique les statistiques de combat par défaut
        this.range = TOWER_BASE_RANGE;
        this.damage = TOWER_BASE_DAMAGE;
    }

    // Récupère la portée de la tour (utilisé par la vue pour dessiner le cercle de portée)
    public int getRange() { return range; }

    // Getters pour la vue (pour dessiner le laser)
    // Retourne l'ennemi actuellement visé
    public Monstre getMonstreCible() { return monstreCible; }
    // Retourne le timestamp du dernier tir (permet à la vue de savoir combien de temps afficher le laser)
    public long getDernierTempsAttaque() { return dernierTempsAttaque; }

    /**
     * Logique de tir autonome de la tour.
     * Appelée en boucle par le ThreadBatiments, elle scanne les monstres proches,
     * vérifie son cooldown, et tire sur le premier ennemi à portée.
     * @param monstres La liste complète des monstres vivants sur la carte.
     */
    public void attaquerSiPossible(ArrayList<Monstre> monstres) {
        // Récupère l'heure exacte à l'instant T
        long tempsActuel = System.currentTimeMillis();

        // Le bâtiment vérifie s'il s'est écoulé assez de temps depuis son dernier tir (Cooldown)
        if (tempsActuel - dernierTempsAttaque >= CADENCE_TOWER) {

            // On réinitialise la cible au début du scan pour ne pas garder en mémoire un vieux monstre mort
            monstreCible = null;

            // Parcourt tous les ennemis présents sur la carte
            for (Monstre m : monstres) {
                // Calcule la distance directe (hypoténuse) entre le centre de la tour et le monstre
                double distance = Math.hypot(m.getX() - this.x, m.getY() - this.y);

                // Si le monstre entre dans le périmètre de défense de la tour
                if (distance <= this.range) {
                    // On retire des PV au monstre ciblé
                    m.perdreHp(this.damage);
                    // Affiche l'action dans la console pour le debug
                    System.out.println("Pew! Tour (" + x + "," + y + ") tire sur monstre " + m.getId());

                    // On remet le chrono à zéro pour CETTE tour (elle ne pourra plus tirer avant 1 seconde)
                    this.dernierTempsAttaque = tempsActuel;

                    // On mémorise le monstre attaqué pour que l'effet visuel (laser) s'affiche à l'écran
                    this.monstreCible = m;

                    // On tire sur un seul monstre à la fois, donc on arrête la boucle de scan dès qu'on a trouvé une cible
                    break;
                }
            }
        }
    }

    @Override
    public int getMaxHp() {
        return 100;
    }

    @Override
    public String getNom() {
        return "Tour";
    }
}