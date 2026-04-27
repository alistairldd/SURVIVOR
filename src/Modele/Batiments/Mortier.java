package Modele.Batiments;

import Modele.GestionnaireBatiments;
import Modele.Monstres.Monstre;
import static Modele.Constantes.*;

/**
 * Bâtiment défensif lourd à dégâts de zone (AoE).
 * Possède un angle mort (minRange) et un tir asynchrone (Temps de vol).
 */
public class Mortier extends Batiment {

    private long dernierTempsAttaque = 0;
    private boolean enTrainDeTirer = false;

    // Mémorisation pour l'interface graphique (La Vue doit savoir où dessiner l'obus)
    private double cibleX = 0;
    private double cibleY = 0;
    private long debutTir = 0;

    public Mortier(int x, int y, GestionnaireBatiments gB) {
        super(x, y, gB, MORTIER_MAX_RANGE);
        this.minRange = MORTIER_MIN_RANGE; // On active l'angle mort !
        this.hp = HP_MORTIER;

        // Physique 2.5D
        this.largeurEncombrement = MORTIER_LARGEUR_ENC;
        this.hauteurEncombrement = MORTIER_HAUTEUR_ENC;
        this.largeurHitbox = MORTIER_LARGEUR_HIT;
        this.hauteurHitbox = MORTIER_HAUTEUR_HIT;
        this.offsetYHitbox = MORTIER_OFFSET_Y;
    }

    // --- GETTERS POUR LA VUE ---
    public boolean isEnTrainDeTirer() { return enTrainDeTirer; }
    public double getCibleX() { return cibleX; }
    public double getCibleY() { return cibleY; }
    public long getDebutTir() { return debutTir; }

    /**
     * Séquence de tir asynchrone.
     */
    public void attaquer(Monstre cibleInitiale) {
        this.enTrainDeTirer = true;
        this.dernierTempsAttaque = System.currentTimeMillis();
        this.debutTir = System.currentTimeMillis();

        // 1. Verrouillage des coordonnées à l'instant T (Le monstre peut bouger, l'obus ira ici !)
        this.cibleX = cibleInitiale.getX();
        this.cibleY = cibleInitiale.getY();

        // 2. Le canon tire : on attend le temps de vol de l'obus
        try {
            Thread.sleep(TEMPS_DE_VOL);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        // 3. L'obus touche le sol -> BOOM !
        exploser(this.cibleX, this.cibleY);
        this.enTrainDeTirer = false;

        // 4. On attend le reste du temps de rechargement (Cooldown) avant de pouvoir retirer
        try {
            long tempsRestant = MORTIER_DELAY - TEMPS_DE_VOL;
            if (tempsRestant > 0) {
                Thread.sleep(tempsRestant);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Déclenche les dégâts de zone (AoE).
     */
    private void exploser(double impactX, double impactY) {
        // Le Mortier délègue le balayage des monstres au moteur central (Créé en Phase 3)
        gBatiments.declencherExplosion(impactX, impactY, EXPLOSION_CORE_RADIUS, EXPLOSION_OUTER_RADIUS, MORTIER_CORE_DAMAGE, MORTIER_OUTER_DAMAGE);
    }

    @Override
    public void run() {
        while (!gBatiments.getPartieTerminee()) {
            // Check de destruction
            if (this.hp <= 0 && isFonctionnel()) {
                setFonctionnel(false);
                setAttaquable(false);
                this.enTrainDeTirer = false;
            }

            if (isFonctionnel()) {
                try {
                    // On cherche une cible valide dans la bande [minRange - maxRange] (Créé en Phase 3)
                    Monstre cible = gBatiments.trouverCibleMortier(this);
                    if (cible != null) {
                        this.attaquer(cible);
                    } else {
                        // Pas de cible en vue, on check le radar fréquemment
                        Thread.sleep(200);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            } else {
                // Détruit, en attente de réparation
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    @Override
    public int getMaxHp() { return HP_MORTIER; }
    @Override
    public String getNom() { return "Mortier"; }
}