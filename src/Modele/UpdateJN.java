package Modele;

import java.util.ArrayList;

// Classe pour la mise à jour du jeu en fonction du temps (jour et la nuit)
public class UpdateJN {

    // Indique si c'est le jour (true) ou la nuit (false)
    private boolean jour = true;

    private GestionnaireMonstres monGestionnaireMonstres = new GestionnaireMonstres();

    // Getter pour savoir si c'est le jour ou la nuit
    public boolean isDay() {
        return jour;
    }
    

    // Méthode pour changer au jour (appelé une seule fois)
    public void changeJour(){
        // Logique pour changer au jour
        jour = true;
        // Supprimer les monstres
        monGestionnaireMonstres.clearMonstres();
        Ressource.genereRessources(Ressource.NB_RESSOURCES); // On génére de nouvelles ressources

    }

    // Méthode pour changer à la nuit (appelé une seule fois)
    public void changeNuit() {
        // Logique pour changer à la nuit, comme faire apparaître les monstres
        jour = false;
        // Créer les monstres
        monGestionnaireMonstres.genererMonstre(100); // Génère 5 monstres pour la nuit
        Ressource.viderRessources(); // Vider les ressources chaque nuit pour forcer les joueurs à se déplacer et à en chercher de nouvelles
    }

    // Méthode à boucler le jour
    public void updateJour() {

    }

    // Méthode à boucler la nuit
    public void updateNuit() {
        // Supprime les monstres morts.
        monGestionnaireMonstres.supprimerMonstresMorts();

    }

    // Getter pour le gestionnaire de monstres
    public GestionnaireMonstres getGestionnaireMonstres() {
        return monGestionnaireMonstres;
    }

    // Getter pour les monstres présents pendant la nuit
    public ArrayList<Monstre> getMonstres() {
        return monGestionnaireMonstres.getMonstres();
    }

}
