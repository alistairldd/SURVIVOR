package Modele;

import java.util.ArrayList;

import static Modele.Map.HAUTEUR_MAP;
import static Modele.Map.LARGEUR_MAP;
import static Vue.VueJoueur.TAILLE;

public class Joueur {

    //Stats du joueur
    private static int hp;
    private static int attack;
    private static ArrayList<Object> inventaire;

    // Position
    private static double positionX;
    private static double positionY;

    // Déplacement
    private static DeplaceJoueur threadActuel = null;

    // Constructeur de la classe Joueur, il initialise les données du joueur.
    public Joueur() { // on initialise la position en 0,0 dans le modèle
        positionX = (double) LARGEUR_MAP /2;
        positionY = (double) HAUTEUR_MAP /2;
        hp = 100;
        attack = 10;
        inventaire = new ArrayList<>();
    }

    public static int getHp() {return hp;}

    public void setHp(int hp) {Joueur.hp = hp;}

    public static int getAttack() {return attack;}

    public void setAttack(int attack) {Joueur.attack = attack;}

    public static ArrayList<Object> getInventaire() {
        return inventaire;
    }

    public static void addToInventaire(Object item) {
        inventaire.add(item);
    }

    // Getter pour la position X du joueur
    public synchronized static double getPositionX() {return positionX;}
    // Getter pour la position Y du joueur
    public synchronized static double getPositionY() {return positionY;}
    // Setter pour la position X du joueur
    public synchronized static void setPositionX(double positionX) {Joueur.positionX = positionX;}
    // Setter pour la position Y du joueur
    public synchronized static void setPositionY(double positionY) {Joueur.positionY = positionY;}


    // Méthode pour déplacer le joueur en x,
    // elle prend en paramètre le déplacement en x,
    // elle met à jour la position du joueur en x.
    public synchronized static void deplaceX(double x) {
        // On vérifie que le déplacement en x est dans les limites de la carte, sinon on le met à la limite.
        if (x >= 10+TAILLE/2 && x <= LARGEUR_MAP) {
            setPositionX(x);
        }
        else if (x <= 10+TAILLE/2) {
            setPositionX(10+TAILLE/2);
        }
        else {
            setPositionX(LARGEUR_MAP);
        }
    }

    // Méthode pour déplacer le joueur en y,
    // elle prend en paramètre le déplacement en y,
    // elle met à jour la position du joueur en y.
    public synchronized static void deplaceY(double y) {
        // On vérifie que le déplacement en y est dans les limites de la carte, sinon on le met à la limite.
        if (y >= 10+TAILLE/2 && y <= Map.HAUTEUR_MAP) {
            setPositionY(y);
        }
        else if (y <= 10+TAILLE/2) {
            setPositionY(10+TAILLE/2);
        }
        else {
            setPositionY(Map.HAUTEUR_MAP);
        }
    }


    public static void setThreadActuel(DeplaceJoueur thread) {
        // Si un thread tourne déjà, on l'arrête
        if (threadActuel != null && threadActuel.isAlive()) {
            threadActuel.interrupt();
        }
        threadActuel = thread;
    }

}


