package Modele;

// Classe pour la mise à jour du jeu en fonction du temps (jour et la nuit)
public class UpdateJN {

    // Indique si c'est le jour (true) ou la nuit (false)
    private boolean jour = true;

    // Getter pour savoir si c'est le jour ou la nuit
    public boolean isDay() {
        return jour;
    }
    

    // Méthode pour changer au jour (appelé une seule fois)
    public void changeJour(){
        // Logique pour changer au jour
        System.out.println("Il fait jour !");
        jour = true;
        Ressource.genereRessources(Ressource.NB_RESSOURCES); // On génére de nouvelles ressources

    }



    // Méthode pour changer à la nuit (appelé une seule fois)
    public void changeNuit() {
        // Logique pour changer à la nuit, comme faire apparaître les monstres
        System.out.println("Il fait nuit !");
        jour = false;
        Ressource.viderRessources(); // Vider les ressources chaque nuit pour forcer les joueurs à se déplacer et à en chercher de nouvelles
    }

    // Méthode à boucler le jour
    public void updateJour() {

    }

    // Méthode à boucler la nuit
    public void updateNuit() {

    }


}
