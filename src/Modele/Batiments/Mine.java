package Modele.Batiments;

import Modele.GestionnaireBatiments;
import Modele.Localisable;
import Modele.Ressource;

import java.util.ArrayList;

import static Modele.Constantes.*;

/**
 * Bâtiment défensif automatisé (Tourelle).
 * Hérite des propriétés d'un Batiment classique (HP, position) mais intègre
 * sa propre logique de combat (portée, dégâts, cadence) et un système de ciblage
 * pour interagir avec le ThreadBatiments de manière indépendante.
 */
public class Mine extends Batiment implements Localisable {

    // Portée effective de cette instance précise
    private int range;
    private ArrayList<Ressource> ressources;

    /**
     * Construit une mine à des coordonnées précises.
     */
    public Mine(GestionnaireBatiments gB) {
        // Initialise la structure via le constructeur parent (Batiment)
        super(300, HAUTEUR_MAP - 300, gB, TOWER_BASE_RANGE);
        this.hp = HP_MINE;
        // Applique les statistiques de combat par défaut
        this.range = MINE_BASE_RANGE;
        this.largeurEncombrement = MINE_LARGEUR_ENC;
        this.hauteurEncombrement = MINE_HAUTEUR_ENC;
        this.largeurHitbox = MINE_LARGEUR_HIT;
        this.hauteurHitbox = MINE_HAUTEUR_HIT;
        this.offsetYHitbox = MINE_OFFSET_Y;
        this.ressources = new ArrayList<>();
        this.attaquable = false; // La mine n'est pas attaquable, elle ne peut pas être détruite par les monstres
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
        // Tirage d'un nombre aléatoire entre 0 et 99 (pour simuler 100%)
        int tirage = (int) (Math.random() * 100);
        int typeChoisi;

        // Définition des paliers de probabilité
        if (tirage < PROBA_PIERRE) {
            typeChoisi = 1; // Pierre
        }
        else if (tirage < PROBA_PIERRE + PROBA_FER) {
            typeChoisi = 2; // Fer
        }
        else {
            typeChoisi = 3; // Or
        }

        // Ajout de la nouvelle ressource à la liste de stockage de la mine
        this.ressources.add(new Ressource(typeChoisi));
    }

    @Override
    public void run() {
        while (!gBatiments.getPartieTerminee()) {
            // Si les PV tombent à 0 ou moins, la mine disjoncte et arrête de produire
            if (this.hp <= 0 && isFonctionnel()) {
                setFonctionnel(false);
            }

            // Si la mine est allumée (soit neuve, soit réparée à 100%)
            if (isFonctionnel()) {
                try {
                    genererRessources();
                    Thread.sleep(MINE_DELAY);
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
        return HP_MINE;
    }

    @Override
    public String getNom() {
        return "Mine";
    }

}