package Modele.Batiments;

import Modele.GestionnaireBatiments;
import Modele.Joueur;

import static Modele.Constantes.*;

/**
 * Bâtiment défensif automatisé (Tente de soin).
 * Hérite des propriétés d'un Batiment classique (HP, position) mais intègre
 * sa propre logique de soin et un système de ciblage.
 */
public class TenteDeSoin extends Batiment{

    private int heal;
    // Mémorise l'heure du dernier tir pour vérifier le cooldown (Le chronomètre interne de LA tour)
    private long dernierTempsSoin = 0;

    // Mémorisation de la cible pour la vue
    private Joueur joueur = null;

    /**
     * Construit une tente de soin à des coordonnées précises.
     * @param x Coordonnée X de placement.
     * @param y Coordonnée Y de placement.
     */
    public TenteDeSoin(int x, int y, GestionnaireBatiments gB) {
        // Initialise la structure via le constructeur parent (Batiment)
        super(x, y, gB, TOWER_BASE_RANGE);
        this.hp = HP_TENTE;
        // Applique les statistiques de soin par défaut
        this.range = HEALING_RANGE;
        this.heal = HEALING_POWER;
        this.rayonHitbox = RAYON_HITBOX_TOUR;
    }

    public int getRange() { return range; }

    public Joueur joueurCible() { return joueur; }

    public long getDernierTempsSoin() { return dernierTempsSoin; }

    public void soigner(Joueur joueur) {
        joueur.soigner(this.heal);
        this.dernierTempsSoin = System.currentTimeMillis();
    }

    @Override
    public void run() {
        while (!gBatiments.getPartieTerminee()) {
            if (this.hp <= 0 && isFonctionnel()) {
                setFonctionnel(false);
            }

            if (isFonctionnel()) {
                try {
                    joueur = gBatiments.trouverJoueur(this);

                    // CORRECTION ICI : Le bâtiment détecte le joueur, mais ne le soigne
                    // que si ses PV actuels sont inférieurs à ses PV max.
                    if (joueur != null && joueur.getHp() < joueur.getHpMax()) {
                        this.soigner(joueur);
                    }

                    Thread.sleep(HEALING_DELAY);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            } else {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
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
        return "Tente de soin";
    }
}