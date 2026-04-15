package Modele.Batiments;

import Modele.GestionnaireBatiments;
import Modele.Monstres.Monstre;
import Modele.Ressource;

import java.util.Map;

import static Modele.Constantes.*;

/**
 * Bâtiment défensif automatisé (Tourelle).
 * Hérite des propriétés d'un Batiment classique (HP, position) mais intègre
 * sa propre logique de combat (portée, dégâts, cadence) et un système de ciblage
 * pour interagir avec le ThreadBatiments de manière indépendante.
 */
public class Tower extends Batiment{

    // Dégâts effectifs de cette instance précise
    private int damage;
    // Mémorise l'heure du dernier tir pour vérifier le cooldown (Le chronomètre interne de LA tour)
    private long dernierTempsAttaque = 0;

    // Mémorisation de la cible pour la vue
    // Stocke temporairement l'ennemi visé pour que la VueBatiment sache où dessiner le laser/projectile
    private Monstre monstreCible = null;

    private Map<Integer, Integer> coutConstruction = COUT_TOUR;
    /**
     * Construit une tour défensive à des coordonnées précises.
     * @param x Coordonnée X de placement.
     * @param y Coordonnée Y de placement.
     */
    public Tower(int x, int y, GestionnaireBatiments gB) {
        // Initialise la structure via le constructeur parent (Batiment)
        super(x, y, gB, TOWER_BASE_RANGE);
        this.hp = HP_TOWER;
        // Applique les statistiques de combat par défaut
        this.range = TOWER_BASE_RANGE;
        this.damage = TOWER_BASE_DAMAGE;
        this.rayonHitbox = RAYON_HITBOX_TOUR;
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
     * @param monstre le monstre dans la porté.
     */
    public void attaquer(Monstre monstre) {
        monstre.perdreHp(this.damage);
        this.dernierTempsAttaque = System.currentTimeMillis();
    }

    @Override
    public void run() {
        while (!gBatiments.getPartieTerminee()) {
            // Si les PV tombent à 0 ou moins, le bâtiment ne fait plus rien
            if (this.hp <= 0 && isFonctionnel()) {
                setFonctionnel(false);
                setAttaquable(false);
            }

            // Si le bâtiment est allumé (soit neuf, soit réparé à 100%)
            if (isFonctionnel()) {
                try {
                    monstreCible = gBatiments.trouverCible(this);
                    if (monstreCible != null) {
                        this.attaquer(monstreCible);
                    }
                    Thread.sleep(TOWER_DELAY);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break; // On quitte la boucle proprement si le jeu s'arrête
                }
            } else {
                // Le bâtiment est détruit : le Thread ne meurt pas mais se repose (500ms)
                // en attendant que le joueur finisse sa réparation.
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
        return HP_TOWER;
    }

    @Override
    public String getNom() {
        return "Tour";
    }

}