package Modele;
import static Modele.Constantes.*;

/**
 * Représente le Quartier Général (HeadQuarters) du joueur.
 * C'est un bâtiment spécifique qui hérite des propriétés de base d'un Batiment.
 */
public class HQ extends Batiment {

    /**
     * Constructeur par défaut.
     * Place automatiquement le HQ exactement au centre géographique de la carte.
     */
    public HQ(GestionnaireBatiments gB) {
        // Appelle le constructeur parent (Batiment) en lui passant le centre de la Map calculé dynamiquement
        super(LARGEUR_MAP/2, HAUTEUR_MAP/2, gB, 0);
        this.hp = HP_HQ;
        this.rayonHitbox = RAYON_HITBOX_QG;
    }

    // Récupère la position horizontale sur la carte
    public double getX(){ return x; }

    // Récupère la position verticale sur la carte
    public double getY(){ return y; }

    @Override
    public int getMaxHp() {
        return HP_HQ; // PV maximum du HQ
    }

    @Override
    public String getNom() {
        return "HQ";
    }

    @Override
    public void run() {
        while (!gBatiments.getPartieTerminee()) {
            // Si le QG tombe à 0 PV, il disjoncte (même si techniquement c'est souvent un Game Over,
            // cette logique le protège de crashs si tu changes les règles plus tard)
            if (this.hp <= 0 && isFonctionnel()) {
                setFonctionnel(false);
            }

            // Si le QG est allumé
            if (isFonctionnel()) {
                try {
                    Thread.sleep(BAT_DELAY);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            } else {
                // Le QG est détruit : le Thread se repose
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