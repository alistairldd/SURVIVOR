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
        return 150; // PV maximum du HQ
    }

    @Override
    public String getNom() {
        return "HQ";
    }

    @Override
    public void run() {
        while (this.hp > 0) {
            try {
                Thread.sleep(BAT_DELAY);
            } catch (InterruptedException e) {}
        }
    }
}