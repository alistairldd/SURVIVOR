package Modele.Batiments;
import Modele.GestionnaireBatiments;

import static Modele.Constantes.*;

/**
 * Représente le Quartier Général (HQ) du joueur.
 * Bâtiment central dont la destruction provoque un Game Over.
 */
public class HQ extends Batiment {

    /** ---------- [Constructeurs] ---------- **/

    /**
     * Initialise le HQ au centre exact de la carte avec ses dimensions spécifiques.
     */
    public HQ(GestionnaireBatiments gB) {
        super(LARGEUR_MAP/2, HAUTEUR_MAP/2, gB, 0);
        this.hp = HP_HQ;
        this.largeurEncombrement = HQ_LARGEUR_ENC;
        this.hauteurEncombrement = HQ_HAUTEUR_ENC;
        this.largeurHitbox = HQ_LARGEUR_HIT;
        this.hauteurHitbox = HQ_HAUTEUR_HIT;
        this.offsetYHitbox = HQ_OFFSET_Y;
    }

    /** ---------- [Méthodes Héritées] ---------- **/

    @Override
    public double getX(){ return x; }

    @Override
    public double getY(){ return y; }

    @Override
    public int getMaxHp() { return HP_HQ; }

    @Override
    public String getNom() { return "HQ"; }

    /**
     * Gère l'état d'alimentation et de destruction du HQ.
     */
    @Override
    public void run() {
        while (!gBatiments.getPartieTerminee()) {
            if (this.hp <= 0 && isFonctionnel()) {
                setFonctionnel(false);
            }

            if (isFonctionnel()) {
                try {
                    Thread.sleep(BAT_DELAY);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            } else {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }
}