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


    // Méthode pour changer au jour
    public void changeJour(){
        // Logique pour changer au jour
        jour = true;
        // Supprimer les monstres
        monGestionnaireMonstres.clearMonstres();
    }

    // Méthode pour changer à la nuit
    public void changeNuit() {
        // Logique pour changer à la nuit, comme faire apparaître les monstres
        jour = false;
        // Créer les monstres
        monGestionnaireMonstres.genererMonstre(100); // Génère 5 monstres pour la nuit
    }

    // Méthode à boucler le jour
    public void updateJour() {

    }

    // Méthode à boucler la nuit
    public void updateNuit() {

    }

    // Getter pour les monstres présents pendant la nuit
    public ArrayList<Monstre> getMonstres() {
        return monGestionnaireMonstres.getMonstres();
    }

}
