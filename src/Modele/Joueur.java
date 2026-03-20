package Modele;

import Controleur.ControleurSouris;

import java.util.ArrayList;

import static Modele.Map.HAUTEUR_MAP;
import static Modele.Map.LARGEUR_MAP;
import static Vue.VueJoueur.TAILLE;
import static java.lang.Math.abs;

public class Joueur {

    //Stats du joueur
    private int hp;
    private int attack;
    private static ArrayList<Ressource> inventaire;
    private Arme armeEquipee;
    private Modele modele;
    private ControleurSouris controleurSouris;

    // Attaque
    private long dernierTempsAttaque = 0;

    // Position
    private static double positionX;
    private static double positionY;

    // Déplacement
    private static DeplaceJoueur threadActuel = null;

    // Constructeur de la classe Joueur, il initialise les données du joueur.
    public Joueur(Modele modele) { // on initialise la position en 0,0 dans le modèle
        positionX = (double) LARGEUR_MAP /2;
        positionY = (double) HAUTEUR_MAP /2;
        hp = 100;
        attack = 10;
        inventaire = new ArrayList<>();
        armeEquipee = new Epee();
        this.modele = modele;
    }

    public int getHp() {return hp;}

    public void setHp(int hp) {this.hp = hp;}

    public int getAttack() {return this.attack;}

    public void setAttack(int attack) {this.attack = attack;}

    public static ArrayList<Ressource> getInventaire() {
        return inventaire;
    }

    public static void addToInventaire(Ressource item) {
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
    // Getter pour l'arme équipée du joueur
    public Arme getArmeEquipee() {return armeEquipee;}

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
        if (y >= 10+TAILLE/2 && y <= HAUTEUR_MAP) {
            setPositionY(y);
        }
        else if (y <= 10+TAILLE/2) {
            setPositionY(10+TAILLE/2);
        }
        else {
            setPositionY(HAUTEUR_MAP);
        }
    }



    // quand le joueur est sur la ressource et qu'il appuie sur e, le joueur ajoute à son inventaire la ressource.
    public void ramasseRessource(ArrayList<Ressource> ressourcesDispo){
        for (int i = ressourcesDispo.size() - 1; i >= 0; i--) {
            Ressource r = ressourcesDispo.get(i);
            if (abs(r.getPositionY() - positionY) <= 30 && abs(r.getPositionX() - positionX)<= 30){// à modifier à terme (zone d'interaction du joueur)
                addToInventaire(r);
                ressourcesDispo.remove(i);
                System.out.println(inventaire);
            }
        }
    }

    public ArrayList<Monstre> proxyMonstre(){
        /*
            Cette méthode retourne une liste de monstres qui sont à proximité du joueur.
            Elle parcourt la liste des monstres du modèle et ajoute à la liste des monstres proches ceux qui sont à une distance
            inférieure ou égale à 30 pixels du joueur en x et en y.
         */
        ArrayList<Monstre> monstresProx = new ArrayList<Monstre>();
        for (Monstre m : modele.getMonstres()) {
            if (abs(m.getY() - positionY) <= 30 && abs(m.getX() - positionX)<= 30){ // à modifier à terme (zone d'interaction du joueur)

                monstresProx.add(m);
            }
        }
        return monstresProx;
    }


    public boolean peutAttaquer(){
        /*
            Cette méthode vérifie si le joueur peut attaquer,
            c'est à dire si le temps écoulé depuis la dernière attaque est supérieur ou égal au cooldown de l'arme équipée.
         */
        long tempsActuel = System.currentTimeMillis();
        long cooldown = armeEquipee.getCadence(); // Convertir le cooldown en millisecondes
        return (tempsActuel - dernierTempsAttaque) >= cooldown;
    }

    public void setDernierTempsAttaque() {
        /*
            Cette méthode met à jour le temps de la dernière attaque du joueur en le définissant à l'heure actuelle.
         */
        this.dernierTempsAttaque = System.currentTimeMillis();
    }

    public static void setThreadActuel(DeplaceJoueur thread) {
        // Si un thread tourne déjà, on l'arrête
        // utilisé pour le déplacement du joueur, pour éviter que plusieurs threads de déplacement soient actifs en même temps,
        // ce qui pourrait causer des problèmes de synchronisation et de performance.
        if (threadActuel != null && threadActuel.isAlive()) {
            threadActuel.interrupt();
        }
        threadActuel = thread;
    }

}


