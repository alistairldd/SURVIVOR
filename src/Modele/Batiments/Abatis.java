package Modele.Batiments;

import Modele.GestionnaireBatiments;
import static Modele.Constantes.*;

/**
 * Bâtiment défensif passif (Rempart).
 * Agit comme un mur pour bloquer le passage, supporte une rotation asymétrique.
 */
public class Abatis extends Batiment {

    /** ---------- [Propriétés] ---------- **/

    private boolean rotation;

    /** ---------- [Constructeurs] ---------- **/

    /**
     * Initialise un Abatis en tenant compte de son orientation.
     *
     * @param x - Coordonnée X de placement
     * @param y - Coordonnée Y de placement
     * @param gB - Référence au gestionnaire de bâtiments
     * @param rotation - true pour incliner à gauche, false pour incliner à droite
     */
    public Abatis(int x, int y, GestionnaireBatiments gB, boolean rotation) {
        super(x, y, gB, 0);
        this.hp = HP_ABATIS;
        this.rotation = rotation;
        this.largeurEncombrement = ABATIS_LARGEUR;
        this.hauteurEncombrement = ABATIS_HAUTEUR;
        this.largeurHitbox = ABATIS_LARGEUR;
        this.hauteurHitbox = ABATIS_HAUTEUR;
        this.offsetYHitbox = ABATIS_OFFSET_Y;

        this.angleRotation = rotation ? -ABATIS_ANGLE_RAD : ABATIS_ANGLE_RAD;
    }

    /** ---------- [Accesseurs] ---------- **/

    public boolean isRotation() { return rotation; }

    /** ---------- [Méthodes Héritées] ---------- **/

    @Override
    public int getMaxHp() { return HP_ABATIS; }

    @Override
    public String getNom() { return "Abatis"; }

    /**
     * Maintien en vie du thread pour la gestion des dégâts subis.
     */
    @Override
    public void run() {
        while (!gBatiments.getPartieTerminee()) {
            if (this.hp <= 0 && isFonctionnel()) {
                setFonctionnel(false);
                setAttaquable(false);
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}