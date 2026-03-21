package Modele;

public class DeplaceJoueur extends Thread {
    private final double destX;
    private final double destY;
    private final int VITESSE = 10; // Plus facile à régler ici
    private Joueur joueur;

    public DeplaceJoueur(double destX, double destY, Joueur joueur ) {
        this.destX = destX;
        this.destY = destY;
        this.joueur = joueur;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            double posX = joueur.getPositionX();
            double posY = joueur.getPositionY();

            if (posX == destX && posY == destY) break;

            double dx = destX - posX;
            double dy = destY - posY;
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance > VITESSE){
                // Normalisation du vecteur de déplacement
                double moveX = (dx / distance) * VITESSE;
                double moveY = (dy / distance) * VITESSE;

                joueur.deplaceX(posX + moveX);
                joueur.deplaceY(posY + moveY);
            }

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