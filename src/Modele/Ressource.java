package Modele;

import static Modele.Constantes.*;

/**
 * Représente un matériau ramassable sur la carte (Bois, Pierre, Fer, Or).
 * Gère sa propre logique d'apparition aléatoire tout en s'assurant de ne pas
 * spawn hors des limites ou trop près des bords de l'écran.
 */
public class Ressource {

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
        int offsetDecale = 10 + R_TAILLE / 2;

        // Formule de génération : Marge + Nombre aléatoire compris entre 0 et (Taille totale - 2 fois la marge)
        this.positionX = offsetDecale + (int) (Math.random() * (LARGEUR_MAP - 2 * (double) offsetDecale)); // On ajoute un offset pour éviter que les ressources soient générées trop près des bords de la carte
        this.positionY = offsetDecale + (int) (Math.random() * (HAUTEUR_MAP - 2 * (double) offsetDecale)); // Idem

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



    // Récupère la position horizontale
    public int getPositionX() { return positionX; }
    // Récupère la position verticale
    public int getPositionY() { return positionY; }
    // Récupère l'identifiant du matériau
    public int getType() { return type; }
}