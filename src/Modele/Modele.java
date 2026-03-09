package Modele;


import java.util.ArrayList;

/*
* La clase générale du modèle, elle contient les classes de données et les méthodes pour manipuler ces données.
* Elle est utilisée pour stocker les données de l'application et pour effectuer des opérations sur ces données.
* On va initialiser les threads du modèle à partir d'ici
*
 */
public class Modele {

    private Joueur joueur;
    private static Map map;
    private Ressource ressource;
    private Batiment batiment;

    private CycleJourNuit leCycleJourNuit;

    // Constructeur de la classe Modele, il initialise les données du modèle.
    public Modele() {

        this.joueur = new Joueur();

        map = new Map();

        // Initialisation du jour et de la nuit
        leCycleJourNuit = new CycleJourNuit();


    }

    /*---- GETTERS ET SETTERS ---- */

    public static Map getMap() {
        return map;
    }


    // Getter du joueur
    public Joueur getJoueur() {
        return joueur;
    }

    // Getter du cycle jour/nuit
    public CycleJourNuit getLeCycleJourNuit() {
            return leCycleJourNuit;
}

    public ArrayList<Monstre> getMonstres() {
        return leCycleJourNuit.getUpdateJN().getMonstres();
    }

    public int map(int debut, int fin, int valDebut, int valFin, int val){
        return (val - debut) * (valFin - valDebut) / (fin - debut) + valDebut;
    }

}
