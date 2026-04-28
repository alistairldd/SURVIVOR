package Modele.Batiments;

import Modele.GestionnaireBatiments;
import Modele.Monstres.Monstre;
import static Modele.Constantes.*;

/**
 * Bâtiment défensif lourd à dégâts de zone (AoE).
 * Intègre une mécanique de tir asynchrone (temps de vol du boulet) et un angle mort de ciblage.
 */
public class Mortier extends Batiment {

    /** ---------- [Propriétés] ---------- **/

    private long dernierTempsAttaque = 0;
    private boolean enTrainDeTirer = false;
    private double cibleX = 0;
    private double cibleY = 0;
    private long debutTir = 0;

    /** ---------- [Constructeurs] ---------- **/

    public Mortier(int x, int y, GestionnaireBatiments gB) {
        super(x, y, gB, MORTIER_MAX_RANGE);
        this.minRange = MORTIER_MIN_RANGE;
        this.hp = HP_MORTIER;

        this.largeurEncombrement = MORTIER_LARGEUR_ENC;
        this.hauteurEncombrement = MORTIER_HAUTEUR_ENC;
        this.largeurHitbox = MORTIER_LARGEUR_HIT;
        this.hauteurHitbox = MORTIER_HAUTEUR_HIT;
        this.offsetYHitbox = MORTIER_OFFSET_Y;
    }

    /** ---------- [Accesseurs pour la Vue] ---------- **/

    public boolean isEnTrainDeTirer() { return enTrainDeTirer; }
    public double getCibleX() { return cibleX; }
    public double getCibleY() { return cibleY; }
    public long getDebutTir() { return debutTir; }

    /** ---------- [Méthodes Publiques - Métier] ---------- **/

    /**
     * Séquence de tir asynchrone générant un délai avant impact.
     *
     * @param cibleInitiale - Le monstre ciblé au départ de l'obus
     */
    public void attaquer(Monstre cibleInitiale) {
        this.enTrainDeTirer = true;
        this.dernierTempsAttaque = System.currentTimeMillis();
        this.debutTir = System.currentTimeMillis();

        this.cibleX = cibleInitiale.getX();
        this.cibleY = cibleInitiale.getY();

        try {
            Thread.sleep(TEMPS_DE_VOL);
            exploser(this.cibleX, this.cibleY);

            // Délai visuel pour le rendu de l'onde de choc
            Thread.sleep(300);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        this.enTrainDeTirer = false;

        // Gestion du délai de rechargement restant
        try {
            long tempsRestant = MORTIER_DELAY - TEMPS_DE_VOL - 300;
            if (tempsRestant > 0) Thread.sleep(tempsRestant);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /** ---------- [Méthodes Privées] ---------- **/

    /**
     * Notifie le gestionnaire central d'appliquer les dégâts de zone.
     */
    private void exploser(double impactX, double impactY) {
        gBatiments.declencherExplosion(
                impactX, impactY,
                EXPLOSION_CORE_RADIUS, EXPLOSION_OUTER_RADIUS,
                MORTIER_CORE_DAMAGE, MORTIER_OUTER_DAMAGE
        );
    }

    /** ---------- [Méthodes Héritées] ---------- **/

    @Override
    public int getMaxHp() { return HP_MORTIER; }

    @Override
    public String getNom() { return "Mortier"; }

    /**
     * Boucle d'acquisition de cible et déclenchement des attaques.
     */
    @Override
    public void run() {
        while (!gBatiments.getPartieTerminee()) {
            if (this.hp <= 0 && isFonctionnel()) {
                setFonctionnel(false);
                setAttaquable(false);
                this.enTrainDeTirer = false;
            }

            if (isFonctionnel()) {
                try {
                    Monstre cible = gBatiments.trouverCibleMortier(this);
                    if (cible != null) {
                        this.attaquer(cible);
                    } else {
                        Thread.sleep(200);
                    }
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