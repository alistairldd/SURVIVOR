package Vue;

/**
 * Représente une petite particule visuelle (un "+") lors d'un soin.
 * Gère sa position et sa disparition progressive.
 */
public class VueParticuleSoin {

    private double x;
    private double y;
    private int dureeVie;
    private int dureeVieMax;

    /**
     * Crée une nouvelle particule à une position aléatoire autour du centre.
     */
    public VueParticuleSoin(double centreX, double centreY, int rayonMax) {
        // Apparition aléatoire dans le rayon du bâtiment
        this.x = centreX + (Math.random() * rayonMax * 2) - rayonMax;
        this.y = centreY + (Math.random() * rayonMax * 2) - rayonMax;

        // Durée de vie aléatoire entre 20 et 40 "frames" (itérations d'affichage)
        this.dureeVieMax = 20 + (int)(Math.random() * 20);
        this.dureeVie = this.dureeVieMax;
    }

    /**
     * Fait monter la particule et réduit son espérance de vie.
     */
    public void miseAJour() {
        this.y -= 1.5; // Vitesse de montée en pixels
        this.dureeVie--;
    }

    /**
     * Vérifie si la particule doit être supprimée de la liste.
     */
    public boolean estMorte() {
        return this.dureeVie <= 0;
    }

    // --- Getters ---
    public double getX() { return x; }
    public double getY() { return y; }

    /**
     * Calcule l'opacité actuelle (de 1.0f à 0.0f) pour l'effet de fondu (Fade-out).
     */
    public float getOpacite() {
        return Math.max(0.0f, (float) dureeVie / dureeVieMax);
    }
}