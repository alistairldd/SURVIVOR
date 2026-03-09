package Modele;

// Classe pour la mise à jour du jeu en fonction du temps (jour et la nuit)
public class UpdateJN {

    // Indique si c'est le jour (true) ou la nuit (false)
    private boolean jour = true;

    // Getter pour savoir si c'est le jour ou la nuit
    public boolean isDay() {
        return jour;
    }
    

    // Méthode pour changer au jour
    public void changeJour(){
        // Logique pour changer au jour
        System.out.println("Il fait jour !");
        jour = true;
    }

    // Méthode pour changer à la nuit
    public void changeNuit() {
        // Logique pour changer à la nuit, comme faire apparaître les monstres
        System.out.println("Il fait nuit !");
        jour = false;
    }

    // Méthode à boucler le jour
    public void updateJour() {

    }

    // Méthode à boucler la nuit
    public void updateNuit() {

    }


}
