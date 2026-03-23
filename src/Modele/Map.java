package Modele;

import Vue.VueCarte;
import java.util.ArrayList;
import static Modele.Constantes.*;

/**
 * Représente l'environnement spatial du jeu.
 * C'est le conteneur principal qui stocke les dimensions physiques du monde
 * ainsi que toutes les entités inanimées ou structurelles (Ressources au sol, Bâtiments construits).
 */
public class Map {
    // Dimensions effectives de l'instance actuelle
    private int largeur;
    private int hauteur;
    // Liste de toutes les ressources (bois, pierre, etc.) actuellement présentes sur le sol
    private ArrayList<Ressource> ressources;
    // Liste de tous les bâtiments (HQ, Tours) construits et actifs sur la carte
    private ArrayList<Batiment> batiments;

    /**
     * Constructeur de la carte.
     * Initialise les dimensions, prépare les listes d'entités et place les bâtiments de départ
     * (le Quartier Général et quelques tours de test).
     */
    public Map() {
        // Applique les dimensions par défaut
        this.largeur = LARGEUR_MAP;
        this.hauteur = HAUTEUR_MAP;

        // Initialise les listes pour éviter les NullPointerException
        this.ressources = new ArrayList<>();
        this.batiments = new ArrayList<>();

        // Ajoute le bâtiment central (HQ) à la liste globale
        this.batiments.add(new HQ());

        //Petit test
        // On stocke la tour dans une variable avant de l'ajouter
        Tower maTourBlessee = new Tower(largeur/2 + 100, hauteur/2+100);
        // On modifie ses PV pour tester visuellement le changement d'état (ex: barre de vie rouge)
        maTourBlessee.setHp(10); // On lui met 10 HP
        // Ajoute la tour blessée
        this.batiments.add(maTourBlessee); // On l'ajoute à la liste

        // Ajoute deux autres tours par défaut pour tester le système de défense
        this.batiments.add(new Tower(largeur/2 - 100, hauteur/2 - 100));
    }

    /**
     * Supprime toutes les ressources présentes sur la carte.
     * Utilisé lors des changements de cycle (ex: passage à la nuit) pour forcer l'exploration.
     */
    public void viderRessources() {
        // Écrase l'ancienne liste avec une nouvelle liste vide
        this.ressources = new ArrayList<>();
    }

    // supprime une ressource de la map (appelé quand le joueur la ramasse)
    public void deleteResources(Ressource ressource){ this.ressources.remove(ressource);}

    // Retourne la liste complète des ressources
    public ArrayList<Ressource> getRessources() {
        return ressources;
    }

    // Retourne la liste complète des bâtiments (utilisé par ThreadBatiments pour les faire attaquer)
    public ArrayList<Batiment> getBatiments() { return batiments; }

}