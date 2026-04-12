package Vue.Batiments;

/**
 * Représente une particule visuelle en forme de coeur ("❤") générée par la Tente de Soin.
 */
public class VueParticuleCoeur {

    private double x;
    private double y;
    private int dureeVie;
    private int dureeVieMax;

    public VueParticuleCoeur(double centreX, double centreY, int rayonMax) {
        // Apparition aléatoire dans le rayon d'action de la tente
        this.x = centreX + (Math.random() * rayonMax * 2) - rayonMax;
        this.y = centreY + (Math.random() * rayonMax * 2) - rayonMax;

        // Durée de vie aléatoire entre 20 et 40 frames
        this.dureeVieMax = 20 + (int)(Math.random() * 20);
        this.dureeVie = this.dureeVieMax;
    }

    public void miseAJour() {
        this.y -= 1.5; // La particule s'envole vers le haut
        this.dureeVie--;
    }

    public boolean estMorte() {
        return this.dureeVie <= 0;
    }

    public double getX() { return x; }
    public double getY() { return y; }

    public float getOpacite() {
        return Math.max(0.0f, (float) dureeVie / dureeVieMax);
    }
}