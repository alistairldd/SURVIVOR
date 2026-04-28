package Modele.Items;

import java.awt.*;

import static Modele.Constantes.*;

/**
 * Classe abstraite définissant la base de tous les projectiles magiques (Sorts).
 * Gère la logique spatiale asynchrone (Thread de déplacement) et la gestion des limites de carte.
 */
public abstract class Sort extends Item implements Runnable {

    /** ---------- [Propriétés - Moteur Physique] ---------- **/

    protected double positionX;
    protected double positionY;
    protected double directionX;
    protected double directionY;
    protected boolean actif;
    protected double distanceParcourue = 0;
    protected Thread threadSort;

    /** ---------- [Propriétés - Caractéristiques du Sort] ---------- **/

    protected double vitesse;
    protected double porteeMax;
    protected int degats;

    /** ---------- [Constructeurs] ---------- **/

    /**
     * Instanciation du sort lors du TIR (actif sur la map).
     */
    public Sort(String nom, int prix, Image image, int effet, double x, double y, double dirX, double dirY) {
        super(nom, prix, image, effet);
        this.positionX = x;
        this.positionY = y;
        this.directionX = dirX;
        this.directionY = dirY;
        this.actif = true;

        this.vitesse = 5.0;
        this.porteeMax = 800.0;
        this.degats = effet;

        lancerSort();
    }

    /**
     * Instanciation du sort pour l'INVENTAIRE / BOUTIQUE (passif).
     */
    public Sort(String nom, int prix, Image image, int effet) {
        super(nom, prix, image, effet);
        this.actif = false;
    }

    /** ---------- [Accesseurs] ---------- **/

    public boolean isActif() { return actif; }
    public double getX() { return positionX; }
    public double getY() { return positionY; }
    public int getDegats() { return degats; }
    public double getDirectionX() { return directionX; }
    public double getDirectionY() { return directionY; }

    /** ---------- [Méthodes Publiques - Contrôle du Thread] ---------- **/

    /**
     * Démarre la boucle de mouvement autonome du projectile.
     */
    public void lancerSort() {
        if (threadSort == null || !threadSort.isAlive()) {
            threadSort = new Thread(this);
            threadSort.start();
        }
    }

    public void desactiver() { this.actif = false; }

    public void arreterSort() {}

    /**
     * Logique de déplacement interpolée.
     * Le projectile avance selon son vecteur de direction jusqu'à heurter les bords de la map.
     */
    @Override
    public void run() {
        try {
            while (actif) {
                positionX += directionX * vitesse;
                positionY += directionY * vitesse;

                if (positionX < 0 || positionX > LARGEUR_MAP ||
                        positionY < 0 || positionY > HAUTEUR_MAP) {
                    actif = false;
                }

                Thread.sleep(16); // Simulation 60 FPS
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            actif = false;
        }
    }
}