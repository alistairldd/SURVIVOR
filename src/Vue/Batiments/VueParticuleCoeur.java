package Vue.Batiments;

/**
 * Représente une particule visuelle en forme de cœur générée par une Tente de Soin.
 * Cette classe porte uniquement l'état minimal nécessaire à l'animation d'un élément
 * éphémère dans la scène.
 */
public class VueParticuleCoeur {

    /** ---------- [Propriétés] ---------- **/

    private double x;
    private double y;
    private int dureeVie;
    private int dureeVieMax;

    /** ---------- [Constructeurs] ---------- **/

    /**
     * Initialise une particule à une position aléatoire dans la zone d'effet de la tente.
     * La durée de vie varie légèrement afin d'éviter un rendu trop uniforme entre les particules.
     *
     * @param centreX - Coordonnée X du centre de génération
     * @param centreY - Coordonnée Y du centre de génération
     * @param rayonMax - Rayon maximal de dispersion autour du centre
     */
    public VueParticuleCoeur(double centreX, double centreY, int rayonMax) {
        this.x = centreX + (Math.random() * rayonMax * 2) - rayonMax;
        this.y = centreY + (Math.random() * rayonMax * 2) - rayonMax;

        this.dureeVieMax = 20 + (int) (Math.random() * 20);
        this.dureeVie = this.dureeVieMax;
    }

    /** ---------- [Méthodes Publiques - Cycle de vie] ---------- **/

    /**
     * Fait évoluer la particule d'un tick d'animation.
     * Le déplacement vertical simple suffit ici à évoquer une élévation légère et apaisée.
     */
    public void miseAJour() {
        this.y -= 1.5;
        this.dureeVie--;
    }

    /**
     * Indique si la particule a terminé son cycle de vie et peut être supprimée.
     *
     * @return true si la particule n'a plus de durée de vie
     */
    public boolean estMorte() {
        return this.dureeVie <= 0;
    }

    /** ---------- [Getters] ---------- **/

    public double getX() { return x; }

    public double getY() { return y; }

    /**
     * Calcule l'opacité courante de la particule.
     * Le fondu est linéaire sur toute la durée de vie pour rester lisible et peu coûteux.
     *
     * @return opacité normalisée entre 0 et 1
     */
    public float getOpacite() {
        return Math.max(0.0f, (float) dureeVie / dureeVieMax);
    }
}