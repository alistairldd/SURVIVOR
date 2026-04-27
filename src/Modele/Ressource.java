package Modele;

import static Modele.Constantes.*;

/**
 * Représente un matériau ramassable sur la carte (Bois, Pierre, Fer, Or).
 * Gère sa propre logique d'apparition aléatoire et son animation de déplacement
 * (aspiration) vers le joueur.
 */
public class Ressource {

    // Coordonnées en double pour permettre une interpolation fluide lors de l'aspiration
    private double positionX;
    private double positionY;
    // Identifiant définissant la nature de la ressource (bois, pierre...)
    private int type;
    // Indique si la ressource est en train de voler vers le joueur
    private boolean estAspiree;

    /**
     * Constructeur par défaut utilisé pour la génération naturelle sur la carte.
     * Calcule automatiquement des coordonnées aléatoires sécurisées.
     */
    public Ressource() {
        // Marge de sécurité (offset) basée sur la taille du joueur
        int offsetDecale = 10 + R_TAILLE / 2;

        // Formule de génération : Marge + Nombre aléatoire
        this.positionX = offsetDecale + (Math.random() * (LARGEUR_MAP - 2 * offsetDecale));
        this.positionY = offsetDecale + (Math.random() * (HAUTEUR_MAP - 2 * offsetDecale));

        // --- NOUVELLE LOGIQUE DE PROBABILITÉ ---
        double rand = Math.random(); // Génère un nombre entre 0.0 et 0.999...
        int index;

        if (rand < 0.55) {
            index = 0; // 55% de chance
        } else if (rand < 0.80) {
            index = 1; // 25% de chance
        } else if (rand < 0.90) {
            index = 2; // 10% de chance
        } else {
            index = 3; // 10% de chance
        }

        this.type = TYPE_RESSOURCE[index];
        this.estAspiree = false;
    }

    /**
     * Constructeur spécifique utilisé pour créer des ressources directement dans l'inventaire
     * ou larguées par les monstres.
     * @param i Le type de ressource forcé.
     */
    public Ressource(int i) {
        this.type = i;
        this.positionX = 0;
        this.positionY = 0;
        this.estAspiree = false;
    }

    public double getPositionX() { return positionX; }
    public double getPositionY() { return positionY; }
    public int getType() { return type; }

    public boolean isEstAspiree() { return estAspiree; }
    public void setEstAspiree(boolean estAspiree) { this.estAspiree = estAspiree; }

    /**
     * Calcule le vecteur directionnel vers le joueur et déplace la ressource
     * pas à pas pour créer l'effet d'aspiration visuelle.
     */
    public void mettreAJourPosition(Localisable cible) {
        if (!estAspiree) return;

        double diffX = cible.getX() - this.positionX;
        double diffY = cible.getY() - this.positionY;
        double distance = Math.hypot(diffX, diffY);

        if (distance > 0) {
            // Si la distance est très petite, on la "téléporte" sur la cible pour éviter les tremblements
            if (distance < VITESSE_RAMASSAGE) {
                this.positionX = cible.getX();
                this.positionY = cible.getY();
            } else {
                // Déplacement vectoriel classique
                this.positionX += (diffX / distance) * VITESSE_RAMASSAGE;
                this.positionY += (diffY / distance) * VITESSE_RAMASSAGE;
            }
        }
    }
}