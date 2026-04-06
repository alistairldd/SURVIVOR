package Modele;

import java.util.ArrayList;
import static Modele.Constantes.*;

/**
 * Bâtiment défensif automatisé (Tourelle).
 * Hérite des propriétés d'un Batiment classique (HP, position) mais intègre
 * sa propre logique de combat (portée, dégâts, cadence) et un système de ciblage
 * pour interagir avec le ThreadBatiments de manière indépendante.
 */
public class TenteDeSoin extends Batiment{

    private int heal;
    // Mémorise l'heure du dernier tir pour vérifier le cooldown (Le chronomètre interne de LA tour)
    private long dernierTempsSoin = 0;

    // Mémorisation de la cible pour la vue
    // Stocke temporairement l'ennemi visé pour que la VueBatiment sache où dessiner le laser/projectile
    private Joueur joueur = null;

    /**
     * Construit une tour défensive à des coordonnées précises.
     * @param x Coordonnée X de placement.
     * @param y Coordonnée Y de placement.
     */
    public TenteDeSoin(int x, int y, GestionnaireBatiments gB) {
        // Initialise la structure via le constructeur parent (Batiment)
        super(x, y, gB, TOWER_BASE_RANGE);
        this.hp = HP_TENTE;
        // Applique les statistiques de combat par défaut
        this.range = HEALING_RANGE;
        this.heal = HEALING_POWER;
        this.rayonHitbox = RAYON_HITBOX_TOUR;
    }

    // Récupère la portée de la tour (utilisé par la vue pour dessiner le cercle de portée)
    public int getRange() { return range; }

    // Getters pour la vue (pour dessiner le laser)
    // Retourne l'ennemi actuellement visé
    public Joueur joueurCible() { return joueur; }
    // Retourne le timestamp du dernier tir (permet à la vue de savoir combien de temps afficher le laser)
    public long getDernierTempsSoin() { return dernierTempsSoin; }

    /**
     * Logique de tir autonome de la tour.
     * Appelée en boucle par le ThreadBatiments, elle scanne les monstres proches,
     * vérifie son cooldown, et tire sur le premier ennemi à portée.
     * @param joueur le jouer dans la portée.
     */
    public void soigner(Joueur joueur) {
        joueur.soigner(this.heal);
        this.dernierTempsSoin = System.currentTimeMillis();
    }

    @Override
    public void run() {
        while (!gBatiments.getPartieTerminee()) {
            // Si les PV tombent à 0 ou moins, la tente disjoncte et arrête de soigner
            if (this.hp <= 0 && isFonctionnel()) {
                setFonctionnel(false);
            }

            // Si la tente est allumée (soit neuve, soit réparée à 100%)
            if (isFonctionnel()) {
                try {
                    joueur = gBatiments.trouverJoueur(this);
                    if (joueur != null) {
                        this.soigner(joueur);
                    }
                    Thread.sleep(HEALING_DELAY);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break; // On quitte la boucle proprement si le jeu s'arrête
                }
            } else {
                // Le bâtiment est en panne : le Thread se repose (500ms)
                // en attendant d'être réparé.
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