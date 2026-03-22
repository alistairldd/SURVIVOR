package Modele;

import static Vue.VueJoueur.TAILLE;

/**
 * Représente un matériau ramassable sur la carte (Bois, Pierre, Fer, Or).
 * Gère sa propre logique d'apparition aléatoire tout en s'assurant de ne pas
 * spawn hors des limites ou trop près des bords de l'écran.
 */
public class Ressource {
    // Tableau des identifiants de types de ressources (0 : bois, 1 : pierre, 2 : fer, 3: or)
    public static final int[] TYPE_RESSOURCE = {0, 1, 2, 3}; // 0 : bois, 1 : pierre, 2 : fer, 3: or
    // Nombre total de ressources à générer simultanément sur la carte au lever du jour
    public static final int NB_RESSOURCES = 20;

    // Coordonnée horizontale sur la carte
    private int positionX;
    // Coordonnée verticale sur la carte
    private int positionY;
    // Identifiant définissant la nature de la ressource (bois, pierre...)
    private int type;

    /**
     * Constructeur par défaut utilisé pour la génération naturelle sur la carte.
     * Calcule automatiquement des coordonnées aléatoires sécurisées.
     */
    public Ressource() {
        /*
        constructeur de base de ressource
        on génère une ressource à une position aléatoire sur la carte, en évitant les bords
        on appelle ce constructeur dans la méthode de generation des ressources
         */

        // Marge de sécurité (offset) basée sur la taille du joueur pour éviter de coller les objets au bord absolu du monde
        int offsetDecale = 10 + TAILLE / 2;

        // Formule de génération : Marge + Nombre aléatoire compris entre 0 et (Taille totale - 2 fois la marge)
        this.positionX = offsetDecale + (int) (Math.random() * (Map.LARGEUR_MAP - 2 * (double) offsetDecale)); // On ajoute un offset pour éviter que les ressources soient générées trop près des bords de la carte
        this.positionY = offsetDecale + (int) (Math.random() * (Map.HAUTEUR_MAP - 2 * (double) offsetDecale)); // Idem

        // Choisit un index aléatoire parmi les 4 types de ressources disponibles
        int index = (int) (Math.random() * TYPE_RESSOURCE.length);
        // Assigne ce type à la ressource nouvellement créée
        this.type = TYPE_RESSOURCE[index];
    }

    /**
     * Constructeur spécifique utilisé pour créer des ressources directement dans l'inventaire
     * (sans position physique sur la carte). Utilisé pour le debug ou le loot.
     * @param i Le type de ressource forcé (0, 1, 2 ou 3).
     */
    public  Ressource(int i) {
        // Force le type
        this.type = i;
        // Position inutile puisqu'elle n'est pas sur la carte, on la fixe à 0
        this.positionX = 0;
        this.positionY = 0;
    }

    /**
     * Nettoie la carte et fait apparaître un nouveau lot de ressources fraîches.
     * @param nbRessources Le nombre exact d'objets à disséminer.
     */
    public static void genereRessources(int nbRessources) {
        /*
        Cette méthode génère un nombre donné de ressources aléatoires sur la carte.
         */
        // Récupère l'instance unique de la carte depuis le modèle
        Map carte = Modele.getMap2();
        // S'assure qu'il ne reste aucune ancienne ressource avant d'en créer de nouvelles
        carte.viderRessources();

        // Boucle de création
        for (int i = 0; i < nbRessources; i++) {
            // Instancie une nouvelle ressource (qui calcule sa propre position) et l'ajoute à la liste de la carte
            carte.getRessources().add(new Ressource());
        }
    }

    /**
     * Raccourci pour vider complètement la carte de ses ressources.
     */
    public static void viderRessources() {
        /*
        Cette méthode vide la liste des ressources de la carte,
        utilisée à chaque changement de jour pour forcer les joueurs à se
        déplacer et à chercher de nouvelles ressources.
         */
        // Demande à la Map de réinitialiser sa liste de ressources
        Modele.getMap2().viderRessources();
    }

    // Récupère la position horizontale
    public int getPositionX() { return positionX; }
    // Récupère la position verticale
    public int getPositionY() { return positionY; }
    // Récupère l'identifiant du matériau
    public int getType() { return type; }
}