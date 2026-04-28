package Modele.Batiments;

import Modele.GestionnaireBatiments;
import Modele.Monstres.Monstre;
import static Modele.Constantes.*;

/**
 * Bâtiment défensif automatisé (Tourelle).
 * Scanne l'environnement et inflige des dégâts monocibles réguliers.
 */
public class Tower extends Batiment{

    /** ---------- [Propriétés] ---------- **/

    private int damage;
    private long dernierTempsAttaque = 0;
    private Monstre monstreCible = null;

    /** ---------- [Constructeurs] ---------- **/

    public Tower(int x, int y, GestionnaireBatiments gB) {
        super(x, y, gB, TOWER_BASE_RANGE);
        this.hp = HP_TOWER;
        this.range = TOWER_BASE_RANGE;
        this.damage = TOWER_BASE_DAMAGE;

        this.largeurEncombrement = TOUR_LARGEUR_ENC;
        this.hauteurEncombrement = TOUR_HAUTEUR_ENC;
        this.largeurHitbox = TOUR_LARGEUR_HIT;
        this.hauteurHitbox = TOUR_HAUTEUR_HIT;
        this.offsetYHitbox = TOUR_OFFSET_Y;
    }

    /** ---------- [Accesseurs pour la Vue] ---------- **/

    @Override
    public int getRange() { return range; }

    public Monstre getMonstreCible() { return monstreCible; }

    public long getDernierTempsAttaque() { return dernierTempsAttaque; }

    /** ---------- [Méthodes Publiques - Métier] ---------- **/

    /**
     * Applique les dégâts à la cible et met à jour le chronomètre d'attaque.
     *
     * @param monstre - L'entité ennemie ciblée
     */
    public void attaquer(Monstre monstre) {
        monstre.perdreHp(this.damage);
        this.dernierTempsAttaque = System.currentTimeMillis();
    }

    /** ---------- [Méthodes Héritées] ---------- **/

    @Override
    public int getMaxHp() { return HP_TOWER; }

    @Override
    public String getNom() { return "Tour"; }

    /**
     * Boucle d'acquisition de cible et de tir en rafale.
     */
    @Override
    public void run() {
        while (!gBatiments.getPartieTerminee()) {
            if (this.hp <= 0 && isFonctionnel()) {
                setFonctionnel(false);
                setAttaquable(false);
            }

            if (isFonctionnel()) {
                try {
                    monstreCible = gBatiments.trouverCible(this);
                    if (monstreCible != null) {
                        this.attaquer(monstreCible);
                    }
                    Thread.sleep(TOWER_DELAY);
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