package Modele.Batiments;

import Modele.GestionnaireBatiments;
import Modele.Joueur;
import static Modele.Constantes.*;

/**
 * Bâtiment de soutien (Tente de soin).
 * Restaure passivement les points de vie du joueur s'il est à proximité.
 */
public class TenteDeSoin extends Batiment{

    /** ---------- [Propriétés] ---------- **/

    private int heal;
    private long dernierTempsSoin = 0;
    private Joueur joueur = null;

    /** ---------- [Constructeurs] ---------- **/

    public TenteDeSoin(int x, int y, GestionnaireBatiments gB) {
        super(x, y, gB, TOWER_BASE_RANGE);
        this.hp = HP_TENTE;
        this.range = HEALING_RANGE;
        this.heal = HEALING_POWER;

        this.largeurEncombrement = TENTE_LARGEUR_ENC;
        this.hauteurEncombrement = TENTE_HAUTEUR_ENC;
        this.largeurHitbox = TENTE_LARGEUR_HIT;
        this.hauteurHitbox = TENTE_HAUTEUR_HIT;
        this.offsetYHitbox = TENTE_OFFSET_Y;
    }

    /** ---------- [Accesseurs] ---------- **/

    @Override
    public int getRange() { return range; }

    public Joueur joueurCible() { return joueur; }

    public long getDernierTempsSoin() { return dernierTempsSoin; }

    /** ---------- [Méthodes Publiques - Métier] ---------- **/

    /**
     * Applique les soins au joueur et actualise le chronomètre.
     */
    public void soigner(Joueur joueur) {
        joueur.soigner(this.heal);
        this.dernierTempsSoin = System.currentTimeMillis();
    }

    /** ---------- [Méthodes Héritées] ---------- **/

    @Override
    public int getMaxHp() { return 100; }

    @Override
    public String getNom() { return "Tente de soin"; }

    /**
     * Boucle de vérification de présence du joueur et d'application des soins.
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
                    joueur = gBatiments.trouverJoueur(this);

                    if (joueur != null && joueur.getHp() < joueur.getHpMax()) {
                        this.soigner(joueur);
                    }

                    Thread.sleep(HEALING_DELAY);
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