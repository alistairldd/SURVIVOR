package Modele;

import java.util.ArrayList;
import java.util.List;

import static Modele.Constantes.*;
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
    private GestionnaireMonstres monGestionnaireMonstres = new GestionnaireMonstres(this);
    private GestionnaireRessources monGestionnaireRessources = new GestionnaireRessources();


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
        monGestionnaireRessources.genereRessources(NB_RESSOURCES);

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
        monGestionnaireMonstres.genererMonstre(100); // Génère les monstres pour la nuit

        // Vider les ressources chaque nuit pour forcer les joueurs à se déplacer et à en chercher de nouvelles
        // (Excellente mécanique de game design pour éviter la sur-accumulation passive)
        monGestionnaireRessources.viderRessources();
    }

    // Méthode à boucler le jour
    // Prévue pour accueillir des actions continues pendant la journée (croissance de plantes, etc.)
    public void updateJour() {

    }

    // Méthode à boucler la nuit
    public void updateNuit() {


    }


    // Getter pour exposer directement la liste des monstres présents pendant la nuit au reste du Modèle
    public List<Monstre> getMonstres() {
        return monGestionnaireMonstres.getMonstres();
    }


    // Getter pour exposer directement la liste des ressources présentes pendant le jour au reste du Modèle
    public ArrayList<Ressource> getRessources() {
        return monGestionnaireRessources.getRessources();
    }

    public Localisable monstreTrouverCible(Localisable m) {
        ArrayList<Batiment> batiments = modele.getGestionnaireBatiments().getBatiments();
        Joueur joueur = modele.getJoueur();
        // On crée une liste de tout ce qui est attaquable par les monstres
        List<Localisable> ciblesPotentielles = new ArrayList<>();
        ciblesPotentielles.add(joueur);
        ciblesPotentielles.addAll(batiments);


        Localisable plusProche = null;
        double distMin = Double.MAX_VALUE;
        for (Localisable cible : ciblesPotentielles) {
            double d = calculerDistance(m, cible);
            if (d < distMin) {
                distMin = d;
                plusProche = cible;
            }
        }
        return plusProche;
    }

    /**
     * Calcule la distance entre deux entités localisables
     */
    public double calculerDistance(Localisable a, Localisable b) {
        // Calcul de la différence sur l'axe X et Y
        double diffX = a.getX() - b.getX();
        double diffY = a.getY() - b.getY();

        // Théorème de Pythagore pour la distance
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

}