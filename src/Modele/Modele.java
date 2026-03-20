package Modele;


import Controleur.ControleurSouris;

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

        this.joueur = new Joueur(this);

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

    public GestionnaireMonstres getGestionnaireMonstres() {
        return leCycleJourNuit.getUpdateJN().getGestionnaireMonstres();
    }

    public int map(int debut, int fin, int valDebut, int valFin, int val){
        return (val - debut) * (valFin - valDebut) / (fin - debut) + valDebut;
    }


    // Attaque du joueur
    public void joueurAttaque(double angleAttaque) {
                /*
            Cette méthode permet au joueur d'attaquer les monstres qui sont à proximité.
            Elle prend en paramètre les coordonnées de la souris, elle calcule l'angle entre le joueur et la souris,
            puis elle parcourt la liste des monstres du modèle et applique les dégâts à ceux qui sont dans le cône d'attaque de l'arme équipée.
         */

        // Récupérer les caractéristiques de l'arme équipée
        double portee = joueur.getArmeEquipee().getPortee();
        double angle = joueur.getArmeEquipee().getAngle();
        // Récupérer la position du joueur
        double positionX = Joueur.getPositionX();
        double positionY = Joueur.getPositionY();
        // Récupérer les monstres
        ArrayList<Monstre> monstres = getMonstres();

        // Parcourir la liste des monstres du modèle et appliquer les dégâts à ceux qui sont dans le cône d'attaque de l'arme équipée
        for (Monstre m : monstres) {

            // Calculer la distance entre le joueur et le monstre en x et en y
            double distance = Math.hypot(m.getX() - positionX, m.getY() - positionY);

            // Vérifier si le monstre est à portée de l'arme
            if (distance <= portee) {
                // Calculer l'angle entre le joueur et le monstre
                double angleMonstre = Math.atan2(m.getY() - positionY, m.getX() - positionX);

                // Vérifier si le monstre est dans l'angle d'attaque
                double diffAngle = angleMonstre - angleAttaque;
                diffAngle = Math.atan2(Math.sin(diffAngle), Math.cos(diffAngle));

                // Si la différence est dans notre cône
                if (Math.abs(diffAngle) <= angle / 2) {
                    // On applique les dégâts
                    m.perdreHp( joueur.getAttack());
                    System.out.println("Monstre touché ! HP restant : " + m.getHp());
                }
            }
        }
    }

}
