package Modele;

public final class Constantes {
    private Constantes(){}

    /*** ---Bâtiments--- ***/
    // Constante : Points de vie maximum d'un bâtiment neuf
    public static final int BASE_HP = 100;

    /*--- Tower ---*/
    // Constante : Points de dégâts fixes infligés à chaque tir
    public static final int TOWER_BASE_DAMAGE = 20;
    // Constante : Rayon d'action maximum (en pixels) de la tourelle
    public static final int TOWER_BASE_RANGE = 100;
    // VITESSE D'ATTAQUE DE LA TOUR (1000 = 1 seconde)
    // Temps de recharge nécessaire entre deux tirs successifs
    public static final int CADENCE_TOWER = 1000;

    /*** ---Cycle Jour/Nuit--- ***/
    // Constantes pour le cycle
    // Nombre de rafraîchissements virtuels par seconde (détermine la vitesse d'écoulement du temps)
    public final static int FPS = 60;
    // Durée fixe d'une phase jour en secondes réelles
    public final static int DUREE_CYCLE_JOUR = 5;
    // Durée fixe d'une phase nuit en secondes réelles
    public final static int DUREE_CYCLE_NUIT = 120;
    // Nombre total de tours de boucle nécessaires pour terminer une phase entière
    public final static int TICKS_PAR_CYCLE_JOUR = DUREE_CYCLE_JOUR * FPS;
    public final static int TICKS_PAR_CYCLE_NUIT = DUREE_CYCLE_NUIT * FPS;

    /*** ---Joueur--- ***/
    // Vitesse de déplacement en pixels par itération de la boucle
    public static final int VITESSE = 10;
    public static final int ATTAQUE_BASE = 10;
    public static final int HP_JOUEUR = 100;

    /*** ---Map--- ***/
    // Constante : Largeur totale de l'aire de jeu en pixels
    public static final int LARGEUR_MAP = 3000;
    // Constante : Hauteur totale de l'aire de jeu en pixels
    public static final int HAUTEUR_MAP = 3000;

    /*** ---Ressources--- ***/
    // Tableau des identifiants de types de ressources (0 : bois, 1 : pierre, 2 : fer, 3: or)
    public static final int[] TYPE_RESSOURCE = {0, 1, 2, 3}; // 0 : bois, 1 : pierre, 2 : fer, 3: or
    // Nombre total de ressources à générer simultanément sur la carte au lever du jour
    public static final int NB_RESSOURCES = 20;

    /*** --- Threads --- ***/
    // Intervalle de temps en millisecondes entre deux vérifications globales
    // 50ms = 20 vérifications par seconde (suffisant pour réagir vite sans saturer le processeur)
    public static final int BAT_DELAY = 50; // Le thread tourne à 20 FPS pour vérifier les attaques

    // Délai en millisecondes entre chaque image (50ms = 20 images par seconde / FPS)
    public static final int REDESSINE_DELAY = 50;


    /*** --- Vue --- ***/
    // Taille de la fenêtre principale de l'application, elle est utilisée pour définir la taille de la fenêtre.
    public final static int LARGEUR = 1920;
    public final static int HAUTEUR = 1080;
    // Largeur fixe allouée au panneau latéral sur l'écran
    public final static int LARGEUR_HUD = 300;
    // Marge horizontale fixe pour aligner joliment tout le texte à gauche
    public static final int xOffset = 20;
    // Diamètre visuel du sprite du joueur en pixels
    public static final int J_TAILLE = 20;
    public static final int R_TAILLE = 10;

    // Taille physique de l'ennemi sur l'écran principal
    public static final int TAILLE_MONSTRE = 30;
    // Taille drastiquement réduite pour que le monstre soit juste un point rouge sur la minimap
    public static final int TAILLE_MINIMAP_MONSTRE = 10;

}
