package Modele;
import static Modele.Constantes.*;
/**
 * Thread dédié au déplacement fluide du joueur vers une destination donnée (clic droit).
 * En s'exécutant de manière asynchrone, il évite de bloquer la boucle principale du jeu
 * tout en interpolant la position du joueur pas à pas jusqu'à la cible.
 */
public class DeplaceJoueur extends Thread {
    // Coordonnée X de la destination finale visée par le clic
    private final double destX;
    // Coordonnée Y de la destination finale visée par le clic
    private final double destY;
    // Référence au joueur qui doit être déplacé
    private Joueur joueur;

    /**
     * @param destX La position X du point visé dans le monde.
     * @param destY La position Y du point visé dans le monde.
     * @param joueur L'instance du joueur à déplacer.
     */
    public DeplaceJoueur(double destX, double destY, Joueur joueur ) {
        // Initialisation des coordonnées cibles et du joueur
        this.destX = destX;
        this.destY = destY;
        this.joueur = joueur;
    }

    /**
     * Exécute le déplacement progressif.
     * Calcule le vecteur directionnel à chaque itération, déplace le joueur d'un "pas" (VITESSE),
     * et s'arrête lorsque la destination est atteinte ou si un nouveau clic interrompt ce thread.
     */
    @Override
    public void run() {
        // Continue de tourner tant que le thread n'est pas interrompu (ex: par un nouveau clic de déplacement)
        while (!Thread.currentThread().isInterrupted()) {
            // Récupère la position actuelle du joueur à l'instant T
            double posX = joueur.getX();
            double posY = joueur.getY();

            // Condition de sortie : si le joueur est pile sur la destination, on arrête le thread
            if (posX == destX && posY == destY) break;

            // Calcul de la différence de position (vecteur directeur brut)
            double dx = destX - posX;
            double dy = destY - posY;
            // Théorème de Pythagore pour calculer la distance absolue restante jusqu'à la cible
            double distance = Math.sqrt(dx * dx + dy * dy);

            // Si la distance restante est plus grande que notre vitesse de déplacement par pas
            if (distance > VITESSE){
                // Normalisation du vecteur (dx/distance) multiplié par la vitesse pour obtenir le déplacement exact sur X et Y
                double moveX = (dx / distance) * VITESSE;
                double moveY = (dy / distance) * VITESSE;

                // Applique les nouvelles coordonnées calculées au joueur
                joueur.deplaceX(posX + moveX);
                joueur.deplaceY(posY + moveY);
            }
            // Si le joueur est presque arrivé (distance plus petite que la vitesse)
            else {
                // On le téléporte directement sur la coordonnée finale pour éviter qu'il ne "tremble" autour du point
                joueur.deplaceX(destX);
                joueur.deplaceY(destY);
            }

            try {
                // Met le thread en pause pendant 50ms (crée l'effet de mouvement fluide)
                // La durée de sommeil est ajustée en fonction de la vitesse du joueur (plus il est rapide, moins il dort pour réagir plus vite)
                long sleepBase = 50 / (1 + joueur.getVitesse());
                double facteurArmure = 1.0 + (-joueur.getArmurePrincipale().getVitesse()) * 0.3; // Plus l'armure est lourde, plus le facteur augmente (et donc le temps de sommeil)
                Thread.sleep(Math.max(1, (long)(sleepBase * facteurArmure)));
            } catch (InterruptedException e) {
                // Si le thread est interrompu pendant son sommeil, on relance l'interruption pour sortir proprement de la boucle
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}