package Modele;

import static Modele.Constantes.*;

/**
 * Thread dédié au déplacement fluide du joueur vers une destination donnée (clic droit).
 * Interpole la position asynchrone pas à pas sans bloquer la boucle principale du jeu.
 */
public class DeplaceJoueur extends Thread {

    /** ---------- [Propriétés] ---------- **/

    private final double destX;
    private final double destY;
    private Joueur joueur;

    /** ---------- [Constructeurs] ---------- **/

    /**
     * Prépare le vecteur de déplacement.
     *
     * @param destX - La coordonnée X visée sur la carte monde
     * @param destY - La coordonnée Y visée sur la carte monde
     * @param joueur - L'instance du joueur à manipuler
     */
    public DeplaceJoueur(double destX, double destY, Joueur joueur) {
        this.destX = destX;
        this.destY = destY;
        this.joueur = joueur;
    }

    /** ---------- [Méthodes Héritées - Cycle de Vie (Thread)] ---------- **/

    /**
     * Boucle de mouvement asynchrone.
     * Calcule le vecteur directionnel, applique la vitesse (avec modificateurs d'armure),
     * et s'arrête une fois la destination atteinte.
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {

            double posX = joueur.getX();
            double posY = joueur.getY();

            if (posX == destX && posY == destY) break;

            double dx = destX - posX;
            double dy = destY - posY;
            double distance = Math.sqrt(dx * dx + dy * dy);

            // Modificateur de vitesse en fonction de l'équipement lourd/léger
            double facteurArmure = 0;
            if (joueur.getArmurePrincipale() != null) {
                double vit = joueur.getArmurePrincipale().getVitesse() * 0.5;
                if (vit < 0) {
                    facteurArmure = -vit;
                } else if (vit > 0) {
                    facteurArmure = vit;
                }
            }

            double vitesseEffective = VITESSE - facteurArmure + (double) joueur.getVitesse();

            // Rapprochement progressif
            if (distance > vitesseEffective) {
                double moveX = (dx / distance) * vitesseEffective;
                double moveY = (dy / distance) * vitesseEffective;

                joueur.deplaceX(posX + moveX);
                joueur.deplaceY(posY + moveY);
            }
            // Fix final (évite les tremblements sur le point exact)
            else {
                joueur.deplaceX(destX);
                joueur.deplaceY(destY);
            }

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}