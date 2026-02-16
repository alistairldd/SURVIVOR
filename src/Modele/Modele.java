package Modele;


/*
* La clase générale du modèle, elle contient les classes de données et les méthodes pour manipuler ces données.
* Elle est utilisée pour stocker les données de l'application et pour effectuer des opérations sur ces données.
* On va initialiser les threads du modèle à partir d'ici
*
 */
public class Modele {

    private Joueur joueur;

    private static final int tailleCarte = 1120;


    // Constructeur de la classe Modele, il initialise les données du modèle.
    public Modele() {
        this.joueur = new Joueur();
    }


    // Getter de la carte
    public static int getTailleCarte() {
        return tailleCarte;
    }

    // Getter du joueur
    public Joueur getJoueur() {
        return joueur;
    }

}
