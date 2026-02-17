package Modele;

public class DeplaceJoueur extends Thread {
    private final int destX;
    private final int destY;
    private final int VITESSE = 10; // Plus facile à régler ici

    public DeplaceJoueur(int destX, int destY) {
        this.destX = destX;
        this.destY = destY;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            int posX = Joueur.getPositionX();
            int posY = Joueur.getPositionY();

            // Vérification si on est arrivé (pour sortir de la boucle)
            if (posX == destX && posY == destY) break;

            // Déplacement X
            if (posX < destX) {
                // Math.min évite de dépasser la cible
                Joueur.deplaceX(Math.min(posX + VITESSE, destX));
            } else if (posX > destX) {
                // Math.max évite de dépasser la cible vers le bas
                Joueur.deplaceX(Math.max(posX - VITESSE, destX));
            }

            // Déplacement Y (exécuté après ou en même temps selon ton choix)
            else if (posY < destY) {
                Joueur.deplaceY(Math.min(posY + VITESSE, destY));
            } else if (posY > destY) {
                Joueur.deplaceY(Math.max(posY - VITESSE, destY));
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