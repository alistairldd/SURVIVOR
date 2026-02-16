package Modele;


/*
* La clase générale du modèle, elle contient les classes de données et les méthodes pour manipuler ces données.
* Elle est utilisée pour stocker les données de l'application et pour effectuer des opérations sur ces données.
* On va initialiser les threads du modèle à partir d'ici
*
 */
public class Modele {

    private Joueur joueur;

    private final int tailleCarte = 100;

    // Constructeur de la classe Modele, il initialise les données du modèle.
    public Modele() {

        this.joueur = new Joueur();
        Jour jour = new Jour();
    }

    public void deplaceX(int x) {
        int nouvellePositionX = Joueur.getPositionX() + x;
        if (nouvellePositionX >= -tailleCarte && nouvellePositionX <= tailleCarte) {
            joueur.setPositionX(nouvellePositionX);
        }
        else if (nouvellePositionX <= -tailleCarte) {
            joueur.setPositionX(-tailleCarte);
        }
        else {
            joueur.setPositionX(tailleCarte);
        }
    }

    public void deplaceY(int y) {
        int nouvellePositionY = Joueur.getPositionY() + y;
        if (nouvellePositionY >= -tailleCarte && nouvellePositionY <= tailleCarte) {
            joueur.setPositionY(nouvellePositionY);
        }
        else if (nouvellePositionY <= -tailleCarte) {
            joueur.setPositionX(-tailleCarte);
        }
        else {
            joueur.setPositionX(tailleCarte);
        }
    }

    public void deplaceJoueur(int x, int y){
        deplaceX(x);
        deplaceY(y);
    }
}
