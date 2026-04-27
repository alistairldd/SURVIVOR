package Modele.Items;

import java.awt.*;

import static Modele.Constantes.*;

public abstract class Sort extends Item implements Runnable {
    protected double positionX;
    protected double positionY;
    protected double directionX;
    protected double directionY;
    protected boolean actif;
    protected double distanceParcourue = 0;
    protected Thread threadSort;

    // Constantes communes (peuvent être surchargées par les enfants)
    protected double vitesse;
    protected double porteeMax;
    protected int degats;

    public Sort(String nom, int prix, Image image, int effet, double x, double y, double dirX, double dirY) {
        super(nom, prix, image, effet);
        this.positionX = x;
        this.positionY = y;
        this.directionX = dirX;
        this.directionY = dirY;
        this.actif = true;

        // Valeurs par défaut
        this.vitesse = 5.0;
        this.porteeMax = 800.0;
        this.degats = effet;

        lancerSort();
    }

    // Constructeur pour l'inventaire
    public Sort(String nom, int prix, Image image, int effet) {
        super(nom, prix, image, effet);
        this.actif = false;
    }

    @Override
    public void run() {
        try {
            while (actif && distanceParcourue < porteeMax) {
                positionX += directionX * vitesse;
                positionY += directionY * vitesse;
                distanceParcourue += vitesse;
                Thread.sleep(16); // ~60 FPS
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            actif = false;
        }
    }

    public void lancerSort() {
        if (threadSort == null || !threadSort.isAlive()) {
            threadSort = new Thread(this);
            threadSort.start();
        }
    }

    public void desactiver() { this.actif = false; }
    public boolean isActif() { return actif; }
    public double getX() { return positionX; }
    public double getY() { return positionY; }
    public int getDegats() { return degats; }
    public double getDirectionX() { return directionX; }
    public double getDirectionY() { return directionY; }

    public void arreterSort() {
    }
}