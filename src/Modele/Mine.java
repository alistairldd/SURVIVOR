package Modele;

import java.util.ArrayList;

import static Modele.Constantes.*;

/**
 * Bâtiment défensif automatisé (Tourelle).
 * Hérite des propriétés d'un Batiment classique (HP, position) mais intègre
 * sa propre logique de combat (portée, dégâts, cadence) et un système de ciblage
 * pour interagir avec le ThreadBatiments de manière indépendante.
 */
public class Mine extends Batiment{

    // Portée effective de cette instance précise
    private int range;
    private ArrayList<Ressource> ressources;
    /**
     * Construit une mine à des coordonnées précises.
     */
    public Mine(GestionnaireBatiments gB) {
        // Initialise la structure via le constructeur parent (Batiment)
        super(100, HAUTEUR_MAP, gB, TOWER_BASE_RANGE);
        this.hp = HP_MINE;
        // Applique les statistiques de combat par défaut
        this.range = MINE_BASE_RANGE;
        this.rayonHitbox = RAYON_HITBOX_MINE;
        this.ressources = new ArrayList<Ressource>();
    }

    // Récupère la portée de la tour (utilisé par la vue pour dessiner le cercle de portée)
    public int getRange() { return range; }

    // Getters pour la vue (pour dessiner le laser)
    // Retourne l'ennemi actuellement visé
    public ArrayList<Ressource> getRessources() {
        return ressources;
    }

    public void setRessources(ArrayList<Ressource> ressources) {
        this.ressources = ressources;
    }

    public void genererRessources() {
        // Tableau des minerais autorisés (1: Pierre, 2: Fer, 3: Or) - Pas de bois (0)
        int[] minerais = {1, 2, 3};

        // Sélection aléatoire d'un index
        int indexAleatoire = (int) (Math.random() * minerais.length);
        int typeChoisi = minerais[indexAleatoire];

        // Ajout de la nouvelle ressource à la liste de stockage de la mine
        this.ressources.add(new Ressource(typeChoisi));
    }

    @Override
    public void run() {
        while (this.hp > 0) {
            try {
                genererRessources();
                Thread.sleep(BAT_DELAY);
            } catch (InterruptedException e) {}
        }
    }

    @Override
    public int getMaxHp() {
        return HP_MINE;
    }

    @Override
    public String getNom() {
        return "Mine";
    }

}