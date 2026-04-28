package Modele.Batiments;

import Modele.GestionnaireBatiments;
import Modele.Localisable;
import Modele.Ressource;

import java.util.ArrayList;
import java.util.Random;

import static Modele.Constantes.*;

/**
 * Bâtiment de production automatisé (Mine).
 * S'auto-place loin du HQ et génère des ressources minérales périodiquement.
 */
public class Mine extends Batiment implements Localisable {

    /** ---------- [Propriétés] ---------- **/

    private int range;
    private ArrayList<Ressource> ressources;
    private Random randomNumbers = new Random();

    /** ---------- [Constructeurs] ---------- **/

    /**
     * Initialise une mine en calculant dynamiquement une position de spawn sécurisée.
     */
    public Mine(GestionnaireBatiments gB) {
        super(0, 0, gB, TOWER_BASE_RANGE);

        int marge = 300;
        double distanceSecuriteHQ = 500.0;
        boolean positionValide = false;

        HQ hq = gB.getHQ();

        // Calcul du placement aléatoire sécurisé
        while (!positionValide) {
            this.x = marge + randomNumbers.nextInt(LARGEUR_MAP - 2 * marge);
            this.y = marge + randomNumbers.nextInt(HAUTEUR_MAP - 2 * marge);

            if (hq != null) {
                double distance = Math.hypot(this.x - hq.getX(), this.y - hq.getY());
                if (distance >= distanceSecuriteHQ) {
                    positionValide = true;
                }
            } else {
                positionValide = true;
            }
        }

        this.hp = HP_MINE;
        this.range = MINE_BASE_RANGE;
        this.largeurEncombrement = MINE_LARGEUR_ENC;
        this.hauteurEncombrement = MINE_HAUTEUR_ENC;
        this.largeurHitbox = MINE_LARGEUR_HIT;
        this.hauteurHitbox = MINE_HAUTEUR_HIT;
        this.offsetYHitbox = MINE_OFFSET_Y;
        this.ressources = new ArrayList<>();
        this.attaquable = false;
        this.setFonctionnel(false);
    }

    /** ---------- [Accesseurs] ---------- **/

    public int getRange() { return range; }

    public ArrayList<Ressource> getRessources() { return ressources; }
    public void setRessources(ArrayList<Ressource> ressources) { this.ressources = ressources; }

    /** ---------- [Méthodes Publiques - Métier] ---------- **/

    /**
     * Ajoute une nouvelle ressource au stockage interne selon des probabilités définies.
     */
    public void genererRessources() {
        int tirage = (int) (Math.random() * 100);
        int typeChoisi;

        if (tirage < PROBA_PIERRE) {
            typeChoisi = 1;
        }
        else if (tirage < PROBA_PIERRE + PROBA_FER) {
            typeChoisi = 2;
        }
        else {
            typeChoisi = 3;
        }

        this.ressources.add(new Ressource(typeChoisi));
    }

    /** ---------- [Méthodes Héritées] ---------- **/

    @Override
    public int getMaxHp() { return HP_MINE; }

    @Override
    public String getNom() { return "Mine"; }

    /**
     * Cycle de production autonome de la mine.
     */
    @Override
    public void run() {
        while (!gBatiments.getPartieTerminee()) {
            if (this.hp <= 0 && isFonctionnel()) {
                setFonctionnel(false);
            }

            if (isFonctionnel()) {
                try {
                    genererRessources();
                    Thread.sleep(MINE_DELAY);
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