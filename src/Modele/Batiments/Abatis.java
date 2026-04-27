package Modele.Batiments;

import Modele.GestionnaireBatiments;
import static Modele.Constantes.*;

/**
 * Bâtiment défensif passif (Rempart).
 * Ne possède pas de boucle d'action active (Thread mort dès le lancement)
 * et utilise une logique de collision polygonale (OBB) orientée à 15°.
 */
public class Abatis extends Batiment {

    private boolean rotation;

    /**
     * Construit un Abatis.
     * @param x Coordonnée X de placement.
     * @param y Coordonnée Y de placement.
     * @param gB Référence au gestionnaire.
     * @param rotation true pour l'image 2 (miroir), false pour l'image 1.
     */
    public Abatis(int x, int y, GestionnaireBatiments gB, boolean rotation) {
        super(x, y, gB, 0); // Portée 0 : bâtiment purement passif
        this.hp = HP_ABATIS;
        this.rotation = rotation;
        this.largeurEncombrement = ABATIS_LARGEUR;
        this.hauteurEncombrement = ABATIS_HAUTEUR;
        this.largeurHitbox = ABATIS_LARGEUR;
        this.hauteurHitbox = ABATIS_HAUTEUR;
        this.offsetYHitbox = ABATIS_OFFSET_Y;

        // CORRECTION : L'angle de rotation envoyé au moteur SAT dépend maintenant du sens de l'Abatis
        this.angleRotation = rotation ? -ABATIS_ANGLE_RAD : ABATIS_ANGLE_RAD;
    }

    @Override
    public void run() {
        while (!gBatiments.getPartieTerminee()) {
            // Si les PV tombent à 0 ou moins, le bâtiment ne fait plus rien
            if (this.hp <= 0 && isFonctionnel()) {
                setFonctionnel(false);
                setAttaquable(false);
            }

            try {
                Thread.sleep(500); // Pause de 500 millisecondes
            } catch (InterruptedException e) {
                // Si le jeu se ferme ou que le thread est tué de l'extérieur
                Thread.currentThread().interrupt();
                break; // On quitte proprement la boucle
            }
        }
    }

    public boolean isRotation() {
        return rotation;
    }

    @Override
    public int getMaxHp() {
        return HP_ABATIS;
    }

    @Override
    public String getNom() {
        return "Abatis";
    }
}