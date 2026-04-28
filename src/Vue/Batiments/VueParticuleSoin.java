package Vue.Batiments;

/**
 * Représente une petite particule visuelle de soin.
 * Cette classe encapsule l'état minimal d'un élément éphémère affiché pendant
 * une réparation afin de produire un feedback léger et répétable.
 */
public class VueParticuleSoin {

    /** ---------- [Propriétés] ---------- **/

    private double x;
    private double y;
    private int dureeVie;
    private int dureeVieMax;

    /** ---------- [Constructeurs] ---------- **/

    /**
     * Crée une nouvelle particule à une position aléatoire autour du centre fourni.
     * La dispersion spatiale et la durée de vie variable évitent un rendu mécanique
     * lorsque plusieurs particules sont générées successivement.
     *
     * @param centreX - Coordonnée X du centre de génération
     * @param centreY - Coordonnée Y du centre de génération
     * @param rayonMax - Rayon maximal de dispersion autour du centre
     */
    public VueParticuleSoin(double centreX, double centreY, int rayonMax) {
        this.x = centreX + (Math.random() * rayonMax * 2) - rayonMax;
        this.y = centreY + (Math.random() * rayonMax * 2) - rayonMax;

        this.dureeVieMax = 20 + (int) (Math.random() * 20);
        this.dureeVie = this.dureeVieMax;
    }

    /** ---------- [Méthodes Publiques - Cycle de vie] ---------- **/

    /**
     * Fait évoluer la particule d'un tick d'animation.
     * Le déplacement vertical suffit ici à rendre l'effet de soin vivant sans
     * complexifier inutilement l'animation.
     */
    public void miseAJour() {
        this.y -= 1.5;
        this.dureeVie--;
    }

    /**
     * Indique si la particule a terminé son cycle de vie et peut être retirée.
     *
     * @return true si la durée de vie restante est épuisée
     */
    public boolean estMorte() {
        return this.dureeVie <= 0;
    }

    /** ---------- [Getters] ---------- **/

    public double getX() { return x; }

    public double getY() { return y; }

    /**
     * Calcule l'opacité courante de la particule pour produire un fondu progressif.
     *
     * @return opacité normalisée entre 0 et 1
     */
    public float getOpacite() {
        return Math.max(0.0f, (float) dureeVie / dureeVieMax);
    }
}