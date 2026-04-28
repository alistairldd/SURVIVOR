package Modele;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Registre global des constantes du jeu.
 * Regroupe les paramètres d'équilibrage, les dimensions physiques,
 * les coûts économiques et gère le préchargement des assets graphiques.
 */
public final class Constantes {

    private Constantes() {
        // Empêche l'instanciation
    }

    /** ---------- [Paramètres du Moteur et Cycle Temporel] ---------- **/

    public final static int FPS = 60;
    public final static int DUREE_CYCLE_JOUR = 90; // Secondes réelles
    public final static int DUREE_CYCLE_NUIT = 120; // Secondes réelles

    public final static int TICKS_PAR_CYCLE_JOUR = DUREE_CYCLE_JOUR * FPS;
    public final static int TICKS_PAR_CYCLE_NUIT = DUREE_CYCLE_NUIT * FPS;

    public static final int BAT_DELAY = 1000;
    public static final int TOWER_DELAY = 200;
    public static final int MINE_DELAY = 5000;
    public static final int REDESSINE_DELAY = 50;
    public static final int HEALING_DELAY = 500;
    public static int REPARATION_DELAY = 50;


    /** ---------- [Dimensions Globales et Map] ---------- **/

    public final static int LARGEUR = 1920;
    public final static int HAUTEUR = 1080;
    public final static int LARGEUR_HUD = 300;

    public static final int LARGEUR_MAP = 3000;
    public static final int HAUTEUR_MAP = 3000;


    /** ---------- [Équilibrage : Joueur & Mécaniques] ---------- **/

    public static final int VITESSE = 10;
    public static final int HP_JOUEUR = 100;
    public static final int J_TAILLE = 20;

    public static final int REPARATION_RANGE = 50;
    public static final int FEU_RANGE = 50;
    public static final int TEMPETE_RANGE = 250;
    public static final int TEMPETE_KNOCKBACK_FORCE = 200;

    // Ressources générées
    public static final int[] TYPE_RESSOURCE = {0, 1, 2, 3}; // 0: bois, 1: pierre, 2: fer, 3: or
    public static final int NB_RESSOURCES = 20;
    public static final int R_TAILLE = 10;
    public static final int RAYON_RAMASSAGE = 70;
    public static final int VITESSE_RAMASSAGE = 10;


    /** ---------- [Équilibrage : Bâtiments & Défenses] ---------- **/

    public static final int HP_DEFAUT = 100;

    // Quartier Général (HQ)
    public static final int HP_HQ = 300;

    // Tour
    public static final int HP_TOWER = 100;
    public static final int TOWER_BASE_DAMAGE = 20;
    public static final int TOWER_BASE_RANGE = 100;

    // Mine
    public static final int HP_MINE = 100;
    public static final int MINE_BASE_RANGE = 100;
    public static final int PROBA_PIERRE = 70;
    public static final int PROBA_FER = 25;
    public static final int PROBA_OR = 5;

    // Tente
    public static final int HP_TENTE = 100;
    public static int SOIN_BAT = 1;
    public static final int HEALING_POWER = 5;
    public static final int HEALING_RANGE = 100;

    // Abatis
    public static final int HP_ABATIS = 500;

    // Mortier
    public static final int HP_MORTIER = 200;
    public static final int MORTIER_MIN_RANGE = 150;
    public static final int MORTIER_MAX_RANGE = 300;
    public static final int MORTIER_CORE_DAMAGE = 100;
    public static final int MORTIER_OUTER_DAMAGE = 45;
    public static final int EXPLOSION_CORE_RADIUS = 60;
    public static final int EXPLOSION_OUTER_RADIUS = 120;
    public static final int MORTIER_DELAY = 2500;
    public static final int TEMPS_DE_VOL = 750;


    /** ---------- [Équilibrage : Économie & Couts (Format : Bois, Pierre, Fer, Or)] ---------- **/

    public static final int prixArmureLegere = 10;
    public static final int prixArmuresLourde = 20;

    public static final int[] PRIX_EPEE_ACIEREE = {0, 0, 10, 5};
    public static final int[] PRIX_ARMURE = {0, 15, 5, 0};
    public static final int[] PRIX_ARMURE_LOURDE = {0, 20, 10, 0};
    public static final int[] PRIX_EPEE_AMELIOREE = {10, 0, 0, 5};
    public static final int[] PRIX_POTION = {5, 5, 0, 0};

    // Maps de couts pour les constructions
    public static final Map<Integer, Integer> COUT_TOUR = Map.of(0, 4, 1, 4, 2, 2, 3, 1);
    public static final Map<Integer, Integer> COUT_TENTE = Map.of(0, 7, 1, 2, 2, 4, 3, 5);
    public static final Map<Integer, Integer> COUT_ABATIS = Map.of(0, 20);
    public static final Map<Integer, Integer> COUT_MORTIER = Map.of(0, 5, 1, 4, 2, 10, 3, 3);


    /** ---------- [Moteur Physique : Dimensions & Hitboxes (RTS)] ---------- **/

    // HQ
    public static final int HQ_LARGEUR_ENC = 500;
    public static final int HQ_HAUTEUR_ENC = 450;
    public static final int HQ_LARGEUR_HIT = 270;
    public static final int HQ_HAUTEUR_HIT = 180;
    public static final int HQ_OFFSET_Y = 20;

    // Tour
    public static final int TOUR_LARGEUR_ENC = 80;
    public static final int TOUR_HAUTEUR_ENC = 80;
    public static final int TOUR_LARGEUR_HIT = 60;
    public static final int TOUR_HAUTEUR_HIT = 40;
    public static final int TOUR_OFFSET_Y = 0;

    // Tente de Soin
    public static final int TENTE_LARGEUR_ENC = 100;
    public static final int TENTE_HAUTEUR_ENC = 80;
    public static final int TENTE_LARGEUR_HIT = 90;
    public static final int TENTE_HAUTEUR_HIT = 40;
    public static final int TENTE_OFFSET_Y = 10;

    // Mine
    public static final int MINE_LARGEUR_ENC = 120;
    public static final int MINE_HAUTEUR_ENC = 100;
    public static final int MINE_LARGEUR_HIT = 100;
    public static final int MINE_HAUTEUR_HIT = 100;
    public static final int MINE_OFFSET_Y = -60;

    // Abatis
    public static final int TAILLE_ABATIS = 150;
    public static final int ABATIS_LARGEUR = 150;
    public static final int ABATIS_HAUTEUR = 30;
    public static final int ABATIS_OFFSET_Y = 0;
    public static final double ABATIS_ANGLE_RAD = Math.toRadians(29);

    // Mortier
    public static final int MORTIER_LARGEUR_ENC = 120;
    public static final int MORTIER_HAUTEUR_ENC = 100;
    public static final int MORTIER_LARGEUR_HIT = 100;
    public static final int MORTIER_HAUTEUR_HIT = 60;
    public static final int MORTIER_OFFSET_Y = 20;

    // UI Rendu Tailles
    public static final int TAILLE_HQ = 400;
    public static final int TAILLE_TOUR = 200;
    public static final int TAILLE_TENTE = 150;
    public static final int TAILLE_MINE = 150;
    public static final int TAILLE_MORTIER = 150;
    public static final int TAILLE_BATIMENT_MINIMAP = 6;


    /** ---------- [Dimensions Entités & UI] ---------- **/

    public static final int xOffset = 20; // Marge alignement texte HUD
    public static final int TAILLE_IMG = 80; // Taille inventaire HUD
    public static final int TAILLE_ICONE = 50;

    public static final int TAILLE_MONSTRE = 40;
    public static final int TAILLE_OGRE = 120;
    public static final int TAILLE_GOBELIN = 70;
    public static final int TAILLE_MINIMAP_MONSTRE = 10;

    public final static int LARGEUR_JOUEUR_SOURCE = 200;
    public final static int HAUTEUR_JOUEUR_SOURCE = 200;
    public final static int LARGEUR_TOP_JOUEUR_SOURCE = 50;
    public final static int HAUTEUR_TOP_JOUEUR_SOURCE = 50;

    public final static int LARGEUR_SLIME_SOURCE = 724;
    public final static int HAUTEUR_SLIME_SOURCE = 492;
    public final static int LARGEUR_SLIME_MUTANT_SOURCE = 700;
    public final static int HAUTEUR_SLIME_MUTANT_SOURCE = 520;


    /** ---------- [Assets : Map & Décors] ---------- **/

    public static BufferedImage IMAGE_FOND_MAP = null;
    public static BufferedImage ARBRE1 = null;
    public static BufferedImage ARBRE2 = null;
    public static BufferedImage ARBRE3 = null;
    public static BufferedImage ARBRE4 = null;
    public static List<BufferedImage> LISTE_ARBRES;

    static {
        try {
            IMAGE_FOND_MAP = ImageIO.read(new File("src/images/carte/herbe.png"));
            ARBRE1 = ImageIO.read(new File("src/images/carte/arbre1.png"));
            ARBRE2 = ImageIO.read(new File("src/images/carte/arbre2.png"));
            ARBRE3 = ImageIO.read(new File("src/images/carte/arbre3.png"));
            ARBRE4 = ImageIO.read(new File("src/images/carte/arbre4.png"));
            LISTE_ARBRES = List.of(ARBRE1, ARBRE2, ARBRE3, ARBRE4);
        } catch (Exception e) {
            System.err.println("Erreur chargement map/décors: " + e.getMessage());
        }
    }


    /** ---------- [Assets : Bâtiments] ---------- **/

    public static Image IMAGE_HQ = null;
    public static Image IMAGE_HQ_ENDOMMAGE = null;
    public static Image IMAGE_TOUR = null;
    public static Image IMAGE_TOUR_ENDOMMAGE = null;
    public static Image IMAGE_MINE = null;
    public static Image IMAGE_TENTE = null;
    public static Image IMAGE_TENTE_ENDOMMAGE = null;
    public static Image IMAGE_ABATIS_1 = null;
    public static Image IMAGE_ABATIS_1_ENDOMMAGE = null;
    public static Image IMAGE_ABATIS_2 = null;
    public static Image IMAGE_ABATIS_2_ENDOMMAGE = null;
    public static Image IMAGE_MORTIER = null;
    public static Image IMAGE_MORTIER_ENDOMMAGE = null;
    public static Image IMAGE_MORTIER_PROJECTILE = null;

    static {
        try {
            IMAGE_HQ = chargerEtRedimensionner("src/images/batiments/HQ.png", TAILLE_HQ);
            IMAGE_HQ_ENDOMMAGE = chargerEtRedimensionner("src/images/batiments/HQ_endommage.png", TAILLE_HQ);
            IMAGE_TOUR = chargerEtRedimensionner("src/images/batiments/tour.png", TAILLE_TOUR);
            IMAGE_TOUR_ENDOMMAGE = chargerEtRedimensionner("src/images/batiments/tour_endommage.png", TAILLE_TOUR);
            IMAGE_MINE = chargerEtRedimensionner("src/images/batiments/mine.png", TAILLE_MINE);
            IMAGE_TENTE = chargerEtRedimensionner("src/images/batiments/tente.png", TAILLE_TENTE);
            IMAGE_TENTE_ENDOMMAGE = chargerEtRedimensionner("src/images/batiments/tente_endommage.png", TAILLE_TENTE);
            IMAGE_ABATIS_1 = chargerEtRedimensionner("src/images/batiments/abatis_1.png", TAILLE_ABATIS);
            IMAGE_ABATIS_2 = chargerEtRedimensionner("src/images/batiments/abatis_2.png", TAILLE_ABATIS);
            IMAGE_ABATIS_1_ENDOMMAGE = chargerEtRedimensionner("src/images/batiments/abatis_1_endommage.png", TAILLE_ABATIS);
            IMAGE_ABATIS_2_ENDOMMAGE = chargerEtRedimensionner("src/images/batiments/abatis_2_endommage.png", TAILLE_ABATIS);
            IMAGE_MORTIER = chargerEtRedimensionner("src/images/batiments/mortier.png", TAILLE_MORTIER);
            IMAGE_MORTIER_ENDOMMAGE = chargerEtRedimensionner("src/images/batiments/mortier_endommage.png", TAILLE_MORTIER);
            IMAGE_MORTIER_PROJECTILE = chargerEtRedimensionner("src/images/batiments/mortier_projectile.png", 50);
        } catch (Exception e) {
            System.err.println("ERREUR chargement images bâtiments.");
        }
    }


    /** ---------- [Assets : Joueur] ---------- **/

    public static Image IMAGE_JOUEUR = null;
    public static Image IMAGE_JOUEUR_ARMURE = null;
    public static Image IMAGE_TOP_JOUEUR = null;

    static {
        try {
            IMAGE_JOUEUR = ImageIO.read(new File("src/images/perso.png"));
            IMAGE_JOUEUR_ARMURE = ImageIO.read(new File("src/images/perso_armure.png"));
            IMAGE_TOP_JOUEUR = ImageIO.read(new File("src/images/perso_topview.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /** ---------- [Assets : Monstres] ---------- **/

    public final static List<Image> IMAGES_SLIMES = new ArrayList<>();
    public final static List<Image> IMAGES_SLIMES_MUTANT = new ArrayList<>();

    // Ogres
    public static BufferedImage IMAGE_OGRE, IMAGE_OGRE_G, IMAGE_OGRE_D, IMAGE_OGRE_GM, IMAGE_OGRE_DM;
    public static BufferedImage IMAGE_OGRE_ATTAQUE_G, IMAGE_OGRE_ATTAQUE_D, IMAGE_OGRE_ATTAQUE_GH, IMAGE_OGRE_ATTAQUE_DH;

    // Gobelins
    public static BufferedImage IMAGE_GOB_G, IMAGE_GOB_D, IMAGE_GOB_GM, IMAGE_GOB_DM;
    public static BufferedImage IMAGE_GOB_ATTAQUE_G, IMAGE_GOB_ATTAQUE_D, IMAGE_GOB_ATTAQUE_GH, IMAGE_GOB_ATTAQUE_DH;

    static {
        // Chargement Slimes
        try {
            BufferedImage plancheSlimes = ImageIO.read(new File("src/images/monstres/slime.png"));
            int[] slimesX = {111, 85, 88, 1051, 1031, 1030};
            int[] slimesY = {70, 651, 1263, 69, 651, 1266};
            for (int i = 0; i < 5; i++) {
                BufferedImage sprite = plancheSlimes.getSubimage(slimesX[i], slimesY[i], LARGEUR_SLIME_SOURCE, HAUTEUR_SLIME_SOURCE);
                IMAGES_SLIMES.add(sprite.getScaledInstance(100, 100, Image.SCALE_SMOOTH));
            }

            BufferedImage plancheMutants = ImageIO.read(new File("src/images/monstres/slime_mutant.png"));
            int[] mutantsX = {150, 1038, 164, 1039, 166, 1022};
            int[] mutantsY = {70, 68, 658, 656, 1276, 1270};
            for (int i = 0; i < 5; i++) {
                BufferedImage sprite = plancheMutants.getSubimage(mutantsX[i], mutantsY[i], LARGEUR_SLIME_MUTANT_SOURCE, HAUTEUR_SLIME_MUTANT_SOURCE);
                IMAGES_SLIMES_MUTANT.add(sprite.getScaledInstance(100, 100, Image.SCALE_SMOOTH));
            }
        } catch (Exception e) { System.out.println("Erreur Slimes !"); }

        // Chargement Ogres
        try {
            IMAGE_OGRE = ImageIO.read(new File("src/images/monstres/ogre.png"));
            IMAGE_OGRE_G = ImageIO.read(new File("src/images/monstres/ogreGauche.png"));
            IMAGE_OGRE_GM = ImageIO.read(new File("src/images/monstres/ogreGaucheM.png"));
            IMAGE_OGRE_D = ImageIO.read(new File("src/images/monstres/ogreDroit.png"));
            IMAGE_OGRE_DM = ImageIO.read(new File("src/images/monstres/ogreDroitM.png"));
            IMAGE_OGRE_ATTAQUE_G = ImageIO.read(new File("src/images/monstres/ogreAttaqueG.png"));
            IMAGE_OGRE_ATTAQUE_D = ImageIO.read(new File("src/images/monstres/ogreAttaqueD.png"));
            IMAGE_OGRE_ATTAQUE_GH = ImageIO.read(new File("src/images/monstres/ogreAttaqueGH.png"));
            IMAGE_OGRE_ATTAQUE_DH = ImageIO.read(new File("src/images/monstres/ogreAttaqueDH.png"));
        } catch (Exception e) { System.err.println("Erreur Ogres !"); }

        // Chargement Gobelins
        try {
            IMAGE_GOB_G = ImageIO.read(new File("src/images/monstres/gobGauche.png"));
            IMAGE_GOB_GM = ImageIO.read(new File("src/images/monstres/gobGaucheM.png"));
            IMAGE_GOB_D = ImageIO.read(new File("src/images/monstres/gobDroit.png"));
            IMAGE_GOB_DM = ImageIO.read(new File("src/images/monstres/gobDroitM.png"));
            IMAGE_GOB_ATTAQUE_G = ImageIO.read(new File("src/images/monstres/gobAttaqueG.png"));
            IMAGE_GOB_ATTAQUE_D = ImageIO.read(new File("src/images/monstres/gobAttaqueD.png"));
            IMAGE_GOB_ATTAQUE_GH = ImageIO.read(new File("src/images/monstres/gobAttaqueGH.png"));
            IMAGE_GOB_ATTAQUE_DH = ImageIO.read(new File("src/images/monstres/gobAttaqueDH.png"));
        } catch (Exception e) { System.err.println("Erreur Gobelins !"); }
    }


    /** ---------- [Assets : Équipements & Items] ---------- **/

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
    public static Image IMAGE_POTION_GRANDE_VIE = null;
    public static Image IMAGE_PIOCHE = null;

    public static Image IMAGE_SORT_FEU = null;
    public static Image IMAGE_SORT_TEMPETE = null;
    public static Image IMAGE_BOULE_FEU = null;
    public static Image IMAGE_TEMPETE = null;
    public static Image IMAGE_NUKE = null;

    static {
        try {
            IMAGE_BATON = ImageIO.read(new File("src/images/equipement/Baton.png"));
            IMAGE_EPEEBOIS = ImageIO.read(new File("src/images/equipement/EpeeBois.png"));
            IMAGE_EPEE = ImageIO.read(new File("src/images/equipement/Epee.png"));
            IMAGE_HACHE = ImageIO.read(new File("src/images/equipement/Hache.png"));
            IMAGE_LANCE = ImageIO.read(new File("src/images/equipement/Lance.png"));
            IMAGE_EPEE_LOURDE = ImageIO.read(new File("src/images/equipement/EpeeLourde.png"));

            IMAGE_ARMURE_LEGERE = ImageIO.read(new File("src/images/equipement/ArmureLegere.png"));
            IMAGE_ARMURE_LOURDE = ImageIO.read(new File("src/images/equipement/ArmureLourde.png"));

            IMAGE_POTION_DE_VIE = ImageIO.read(new File("src/images/items/PotionDeVie.png"));
            IMAGE_POTION_VITESSE = ImageIO.read(new File("src/images/items/PotionVitesse.png"));
            IMAGE_POTION_DEGATS = ImageIO.read(new File("src/images/items/PotionDegats.png"));
            IMAGE_POTION_GRANDE_VIE = ImageIO.read(new File("src/images/items/GrandePotionVie.png"));
            IMAGE_PIOCHE = ImageIO.read(new File("src/images/items/Pioche.png"));

            IMAGE_SORT_FEU = ImageIO.read(new File("src/images/items/SortFeu.png"));
            IMAGE_SORT_TEMPETE= ImageIO.read(new File("src/images/items/SortTempete.png"));
            IMAGE_BOULE_FEU= ImageIO.read(new File("src/images/items/BouleDeFeu.png"));
            IMAGE_TEMPETE= ImageIO.read(new File("src/images/items/Tempete.png"));
            IMAGE_NUKE = ImageIO.read(new File("src/images/items/Nuke.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /** ---------- [Utilitaires de Chargement] ---------- **/

    /**
     * Tente de charger une image depuis le chemin spécifié et de la redimensionner
     * en préservant un lissage visuel.
     */
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