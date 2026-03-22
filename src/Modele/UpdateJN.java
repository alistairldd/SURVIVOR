package Modele;

import java.util.ArrayList;

// Classe pour la mise à jour du jeu en fonction du temps (jour et la nuit)
/**
 * Gestionnaire de la logique liée au cycle temporel.
 * Exécute les actions majeures de transition (apparition/disparition des ressources et des monstres)
 * dictées par le Thread CycleJourNuit.
 */
public class UpdateJN {

    // Indique si c'est le jour (true) ou la nuit (false)
    // État principal du jeu qui conditionne les règles (ex: construction autorisée uniquement la nuit)
    private boolean jour = true;

    // Référence au modèle pour accéder aux données globales (carte, ressources, etc.)
    private Modele modele;

    // Instance unique responsable de la création et du nettoyage des ennemis
    private GestionnaireMonstres monGestionnaireMonstres = new GestionnaireMonstres();


    public UpdateJN(Modele modele){
        this.modele = modele;
    }


    // Getter pour savoir si c'est le jour ou la nuit
    public boolean isDay() {
        return jour;
    }


    /**
     * Logique de transition lors du lever du soleil.
     * Nettoie les dangers restants et renouvelle les matériaux sur la carte.
     */
    // Méthode pour changer au jour (appelé une seule fois)
    public void changeJour(){
        // Logique pour changer au jour
        // Met à jour l'état global
        jour = true;
        // Supprimer les monstres de la nuit précédente (fin de la vague)
        monGestionnaireMonstres.clearMonstres();
        // On génère de nouvelles ressources fraîches pour la phase d'exploration
        Ressource.genereRessources(Ressource.NB_RESSOURCES);

    }

    /**
     * Logique de transition lors de la tombée de la nuit.
     * Fait disparaître les ressources pour forcer le joueur à se recentrer sur la défense,
     * et lance la nouvelle vague d'ennemis.
     */
    // Méthode pour changer à la nuit (appelé une seule fois)
    public void changeNuit() {
        // Logique pour changer à la nuit, comme faire apparaître les monstres
        // Met à jour l'état global
        jour = false;
        // Créer les monstres
        // Lance une vague massive de 100 ennemis répartis sur les bords de la carte
        monGestionnaireMonstres.genererMonstre(5); // Génère les monstres pour la nuit

        // Vider les ressources chaque nuit pour forcer les joueurs à se déplacer et à en chercher de nouvelles
        // (Excellente mécanique de game design pour éviter la sur-accumulation passive)
        Ressource.viderRessources();
    }

    // Méthode à boucler le jour
    // Prévue pour accueillir des actions continues pendant la journée (croissance de plantes, etc.)
    public void updateJour() {

    }

    // Méthode à boucler la nuit
    public void updateNuit() {
        // Maintien de la propreté de la carte en temps réel :
        // Supprime de la mémoire les monstres morts pour alléger le processeur et le rendu visuel
        monGestionnaireMonstres.supprimerMonstresMorts();
        monGestionnaireMonstres.chercheCible(modele.getJoueur(), modele.getMap().getBatiments());

    }

    // Getter pour le gestionnaire de monstres
    public GestionnaireMonstres getGestionnaireMonstres() {
        return monGestionnaireMonstres;
    }

    // Getter pour exposer directement la liste des monstres présents pendant la nuit au reste du Modèle
    public ArrayList<Monstre> getMonstres() {
        return monGestionnaireMonstres.getMonstres();
    }

}