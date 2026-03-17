package Modele;

import Controleur.ControleurSouris;

import java.util.ArrayList;

import static Modele.Map.HAUTEUR_MAP;
import static Modele.Map.LARGEUR_MAP;
import static Vue.VueJoueur.TAILLE;
import static java.lang.Math.abs;

public class Joueur {

    //Stats du joueur
    private static int hp;
    private static int attack;
    private static ArrayList<Ressource> inventaire;
    private Arme armeEquipee;
    private Modele modele;
    private ControleurSouris controleurSouris;

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

    public static int getHp() {return hp;}

    public void setHp(int hp) {Joueur.hp = hp;}

    public static int getAttack() {return attack;}

    public void setAttack(int attack) {Joueur.attack = attack;}

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




    public void attaquer(double angleAttaque) {
        /*
            Cette méthode permet au joueur d'attaquer les monstres qui sont à proximité.
            Elle prend en paramètre les coordonnées de la souris, elle calcule l'angle entre le joueur et la souris,
            puis elle parcourt la liste des monstres du modèle et applique les dégâts à ceux qui sont dans le cône d'attaque de l'arme équipée.
         */

        // Récupérer les caractéristiques de l'arme équipée
        double portee = armeEquipee.getPortee();
        double angle = armeEquipee.getAngle();

        // Parcourir la liste des monstres du modèle et appliquer les dégâts à ceux qui sont dans le cône d'attaque de l'arme équipée
        for (Monstre m : modele.getMonstres()) {

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
                    m.setHp(m.getHp() - attack);
                    System.out.println("Monstre touché ! HP restant : " + m.getHp());
                }
            }
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


