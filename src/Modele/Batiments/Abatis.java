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
    private final double angleActuel;

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
        this.rayonHitbox = TAILLE_ABATIS / 3; // Valeur par défaut pour compatibilité basique

        // L'inclinaison change de sens selon l'état du miroir
        this.angleActuel = rotation ? -ANGLE_ABATIS : ANGLE_ABATIS;
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

    /**
     * Détermine si un point (ex: coordonnées d'un monstre) entre en collision
     * avec la hitbox rectangulaire inclinée de cet Abatis.
     * * @param pointX Position X de l'entité à tester.
     * @param pointY Position Y de l'entité à tester.
     * @param marge Marge de tolérance (ex: rayon de la hitbox du monstre).
     * @return true si collision détectée.
     */
    public boolean contientPointIncline(double pointX, double pointY, double marge) {
        // 1. Translation vers l'origine (centre de l'abatis)
        double dx = pointX - this.x;
        double dy = pointY - this.y;

        // 2. Rotation inverse pour aligner le rectangle avec les axes X/Y mathématiques
        double cos = Math.cos(-angleActuel);
        double sin = Math.sin(-angleActuel);

        double localX = dx * cos - dy * sin;
        double localY = dx * sin + dy * cos;

        // 3. Test de collision classique (AABB) sur le repère local corrigé
        double demiLargeur = (LARGEUR_HITBOX_ABATIS / 2.0) + marge;
        double demiHauteur = (HAUTEUR_HITBOX_ABATIS / 2.0) + marge;

        return (Math.abs(localX) <= demiLargeur) && (Math.abs(localY) <= demiHauteur);
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