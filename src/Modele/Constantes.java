package Modele;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public final class Constantes {
    private Constantes() {
    }

    /*** --- DIMENSIONS DE RENDU --- ***/
    public static final int TAILLE_TOUR = 200;
    public static final int TAILLE_TENTE = 150;
    public static final int TAILLE_MINE = 150;
    public static final int TAILLE_HQ = 400;
    public static final int TAILLE_BATIMENT_MINIMAP = 6;

    /*** ---Bâtiments--- ***/
    // Paramètres d'équilibrage du soin
    public static int SOIN_BAT = 1; // Nombre de PV restaurés par itération
    // Constante : Points de vie maximum d'un bâtiment neuf
    public static final int HP_TOWER = 100;
    public static final int HP_HQ = 150;
    public static final int HP_MINE = 100;
    public static final int HP_DEFAUT = 100;
    public static final int REPARATION_RANGE = 50;

    /*--- Tower ---*/
    // Constante : Points de dégâts fixes infligés à chaque tir
    public static final int TOWER_BASE_DAMAGE = 20;

    // Constante : Rayon d'action maximum (en pixels) de la tourelle
    public static final int TOWER_BASE_RANGE = 100;
    public static final int MINE_BASE_RANGE = 100;
    public static final int RAYON_HITBOX_TOUR =  50; // Rayon d'encombrement d'une tour
    public static final int RAYON_HITBOX_MINE = TAILLE_MINE / 2 + 10; // Rayon d'encombrment de la mine
    public static final int RAYON_HITBOX_QG = TAILLE_HQ / 2 + 10;   // Le QG est plus gros, il prend plus de place
    public static final int RAYON_HITBOX_TENTE = TAILLE_TENTE / 2 + 5; // La tente de soin a une hitbox intermédiaire
    /*--- Mine ---*/
    public static final int PROBA_PIERRE = 70; // 50% de chance d'obtenir de la pierre
    public static final int PROBA_FER = 25;   // 30% de chance d'obtenir du fer
    public static final int PROBA_OR = 5;    //

    /*--- Tente de soin ---*/
    public static final int HEALING_POWER = 5; // Nombre de PV restaurés par tir de la tente de soin
    public static final int HP_TENTE = 100;
    public static final int HEALING_RANGE = 100;


    /*--- Couts ---*/
    // Coût : 4 Bois (0), 4 Pierre (1), 2 Fer (2), 1 Or (3)
    public static final Map<Integer, Integer> COUT_TOUR = Map.of(
            0, 4,
            1, 4,
            2, 2,
            3, 1
    );

    // Coût : 7 Bois (0), 2 Pierre (1), 4 Fer (2), 5 Or (3)
    public static final Map<Integer, Integer> COUT_TENTE = Map.of(
            0, 7,
            1, 2,
            2, 4,
            3, 5
    );

    /*** ---Cycle Jour/Nuit--- ***/
    // Constantes pour le cycle
    // Nombre de rafraîchissements virtuels par seconde (détermine la vitesse d'écoulement du temps)
    public final static int FPS = 60;
    // Durée fixe d'une phase jour en secondes réelles
    public final static int DUREE_CYCLE_JOUR = 60;
    // Durée fixe d'une phase nuit en secondes réelles
    public final static int DUREE_CYCLE_NUIT = 120;
    // Nombre total de tours de boucle nécessaires pour terminer une phase entière
    public final static int TICKS_PAR_CYCLE_JOUR = DUREE_CYCLE_JOUR * FPS;
    public final static int TICKS_PAR_CYCLE_NUIT = DUREE_CYCLE_NUIT * FPS;

    /*** ---Joueur--- ***/
    // Vitesse de déplacement en pixels par itération de la boucle
    public static final int VITESSE = 10;
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

    // Rayon d'attraction pour le ramassage automatique des ressources
    public static final int RAYON_RAMASSAGE = 70;
    // Vitesse de déplacement des ressources lors de leur aspiration vers le joueur
    public static final int VITESSE_RAMASSAGE = 10;

    /*** ---Prix --- ***/
    // Prix des armes, armures, objets
    public static final int prixArmureLegere = 10;
    public static final int prixArmuresLourde = 20;

    /*** ---Drop Monstres--- ***/
    public static final int DEFAULT_DROP = 10; // Nombre de pièces par défaut donné par un monstre
    public static final int SLIME_DROP = 5;

    /*** --- Threads --- ***/
    public static final int BAT_DELAY = 1000;
    public static final int TOWER_DELAY = 200; // Le thread tourne à 20 FPS pour vérifier les attaques

    public static final int MINE_DELAY = 5000; // La mine génère une ressource toutes les 5 secondes
    // Délai en millisecondes entre chaque image (50ms = 20 images par seconde / FPS)
    public static final int REDESSINE_DELAY = 50;

    public static final int HEALING_DELAY = 500;

    public static int REPARATION_DELAY = 50; // Pause de 50 millisecondes (0.05s) entre chaque soin


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
    public static final int TAILLE_MONSTRE = 40;
    // Taile ogre
    public static final int TAILLE_OGRE = 120;
    // Taille drastiquement réduite pour que le monstre soit juste un point rouge sur la minimap
    public static final int TAILLE_MINIMAP_MONSTRE = 10;

    // --- PRIX DE LA BOUTIQUE (Format : {Bois, Pierre, Fer, Or}) ---
    public static final int[] PRIX_EPEE_ACIEREE = {0, 0, 10, 5};
    public static final int[] PRIX_ARMURE = {0, 15, 5, 0};
    public static final int[] PRIX_ARMURE_LOURDE = {0, 20, 10, 0};
    public static final int[] PRIX_EPEE_AMELIOREE = {10, 0, 0, 5};
    public static final int[] PRIX_POTION = {5, 5, 0, 0};


    // --- HUD ---
    public static final int TAILLE_IMG = 80;
    public static final int TAILLE_ICONE = 50;


    /*** --- Images --- ***/

    public static Image IMAGE_HQ = null;
    public static Image IMAGE_HQ_ENDOMMAGE = null;
    public static Image IMAGE_TOUR = null;
    public static Image IMAGE_TOUR_ENDOMMAGE = null;
    public static Image IMAGE_MINE = null;
    public static Image IMAGE_MINE_ENDOMMAGE = null;
    public static Image IMAGE_TENTE = null;
    public static Image IMAGE_TENTE_ENDOMMAGE = null;

    static {
        try {
            IMAGE_HQ = chargerEtRedimensionner("src/images/Batiments/HQ.png", TAILLE_HQ);
            IMAGE_HQ_ENDOMMAGE = chargerEtRedimensionner("src/images/Batiments/HQ_endommage.png", TAILLE_HQ);

            IMAGE_TOUR = chargerEtRedimensionner("src/images/Batiments/tour.png", TAILLE_TOUR);
            IMAGE_TOUR_ENDOMMAGE = chargerEtRedimensionner("src/images/Batiments/tour_endommage.png", TAILLE_TOUR);

            IMAGE_MINE = chargerEtRedimensionner("src/images/Batiments/mine.png", TAILLE_MINE);
            IMAGE_MINE_ENDOMMAGE = chargerEtRedimensionner("src/images/Batiments/mine_endommage.png", TAILLE_MINE);

            IMAGE_TENTE = chargerEtRedimensionner("src/images/Batiments/tente.png", TAILLE_TENTE);
            IMAGE_TENTE_ENDOMMAGE = chargerEtRedimensionner("src/images/Batiments/tente_endommage.png", TAILLE_TENTE);
        } catch (Exception e) {
            System.err.println("ERREUR : Impossible de charger les images des bâtiments.");
            e.printStackTrace();
        }
    }

    // Liste d'images pour l'affichage
    // Liste pour les slimes
    public final static List<Image> IMAGES_SLIMES = new ArrayList<>();
    public final static int LARGEUR_SLIME_SOURCE = 724;
    public final static int HAUTEUR_SLIME_SOURCE = 492;

    static {
        try {
            // Charger la planche de slimes
            BufferedImage planche = ImageIO.read(new File("src/images/monstres/slime.png"));


            int[] lesX = {111, 85, 88, 1051, 1031, 1030}; // X pour chaque slime
            int[] lesY = {70, 651, 1263, 69, 651, 1266};   // Y pour chaque slime

            for (int i = 0; i < 5; i++) {


                // Découpe la sous-image
                BufferedImage spriteSource = planche.getSubimage(lesX[i], lesY[i], LARGEUR_SLIME_SOURCE, HAUTEUR_SLIME_SOURCE);

                // Redimensionne immédiatement en 30x30
                // SCALE_SMOOTH donne le meilleur résultat visuel
                Image spriteRedimensionne = spriteSource.getScaledInstance(100, 100, Image.SCALE_SMOOTH);

                // Ajoute à la liste
                IMAGES_SLIMES.add(spriteRedimensionne);
                ;
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement des images !");
        }
    }

    // Liste pour les slimes mutants

    public final static List<Image> IMAGES_SLIMES_MUTANT = new ArrayList<>();
    public final static int LARGEUR_SLIME_MUTANT_SOURCE = 700;
    public final static int HAUTEUR_SLIME_MUTANT_SOURCE = 520;

    static {
        try {
            // Charger la planche de slimes mutants
            BufferedImage planche = ImageIO.read(new File("src/images/monstres/slime_mutant.png"));


            int[] lesX = {150, 1038, 164, 1039, 166, 1022}; // X pour chaque slime
            int[] lesY = {70, 68, 658, 656, 1276, 1270};   // Y pour chaque slime

            for (int i = 0; i < 5; i++) {


                // Découpe la sous-image
                BufferedImage spriteSource = planche.getSubimage(lesX[i], lesY[i], LARGEUR_SLIME_MUTANT_SOURCE, HAUTEUR_SLIME_MUTANT_SOURCE);

                // Redimensionne immédiatement en 100x100
                // SCALE_SMOOTH donne le meilleur résultat visuel
                Image spriteRedimensionne = spriteSource.getScaledInstance(100, 100, Image.SCALE_SMOOTH);

                // Ajoute à la liste
                IMAGES_SLIMES_MUTANT.add(spriteRedimensionne);
                ;
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement des images !");
        }
    }

    // Chargement des images des équipements et consommables

    public static Image IMAGE_EPEE = null;
    public static Image IMAGE_EPEEBOIS = null;
    public static Image IMAGE_BATON = null;
    public static Image IMAGE_HACHE = null;
    public static Image IMAGE_EPEE_LOURDE = null;
    public static Image IMAGE_LANCE = null;

    public static Image IMAGE_ARMURE_LEGERE = null;
    public static Image IMAGE_ARMURE_LOURDE = null;

    public static Image IMAGE_POTION_DE_VIE = null;
    public static Image IMAGE_POTION_VITESSE = null;
    public static Image IMAGE_POTION_DEGATS = null;

    static {
        try {
            // Charger l'image de l'épée
            IMAGE_EPEE = ImageIO.read(new File("src/images/equipement/Epee.png"));

            // Charger l'image de l'épée en bois
            IMAGE_EPEEBOIS = ImageIO.read(new File("src/images/equipement/EpeeBois.png"));

            // Charger image baton
            IMAGE_BATON = ImageIO.read(new File("src/images/equipement/Baton.png"));

            // Charger image hache
            IMAGE_HACHE = ImageIO.read(new File("src/images/equipement/Hache.png"));

            // Charger image épée lourde
            IMAGE_EPEE_LOURDE = ImageIO.read(new File("src/images/equipement/EpeeLourde.png"));

            // Charger image lance
            IMAGE_LANCE = ImageIO.read(new File("src/images/equipement/Lance.png"));

            // Charger l'image de l'armure légère
            IMAGE_ARMURE_LEGERE = ImageIO.read(new File("src/images/equipement/ArmureLegere.png"));

            //Charger l'image de l'armure lourde
            IMAGE_ARMURE_LOURDE = ImageIO.read(new File("src/images/equipement/ArmureLourde.png"));
            // Charger l'image de la potion de vie
            IMAGE_POTION_DE_VIE = ImageIO.read(new File("src/images/items/PotionDeVie.png"));
            // Charger l'image de la potion de vitesse
            IMAGE_POTION_VITESSE = ImageIO.read(new File("src/images/items/PotionVitesse.png"));
            // Charger l'image de la potion de dégâts
            IMAGE_POTION_DEGATS = ImageIO.read(new File("src/images/items/PotionDegats.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final static int LARGEUR_JOUEUR_SOURCE = 200;
    public final static int HAUTEUR_JOUEUR_SOURCE = 200;

    public final static int LARGEUR_TOP_JOUEUR_SOURCE = 50;
    public final static int HAUTEUR_TOP_JOUEUR_SOURCE = 50;

    public static Image IMAGE_JOUEUR = null;
    public static Image IMAGE_TOP_JOUEUR = null;
    static {
        try {
            // Charger l'image du joueur
            IMAGE_JOUEUR = ImageIO.read(new File("src/images/perso.png"));

            // Charger l'image du joueur vue de dessus
            IMAGE_TOP_JOUEUR = ImageIO.read(new File("src/images/perso_topview.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Image chargerEtRedimensionner(String chemin, int taille) throws IOException {
        File fichier = new File(chemin);
        if (!fichier.exists()) {
            System.out.println("Attention : Fichier manquant -> " + chemin);
            return null;
        }
        BufferedImage img = ImageIO.read(fichier);
        return img.getScaledInstance(taille, taille, Image.SCALE_SMOOTH);
    }
}