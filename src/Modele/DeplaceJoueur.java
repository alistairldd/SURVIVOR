package Modele;

public class DeplaceJoueur extends Thread {
    private final double destX;
    private final double destY;
    private final int VITESSE = 10; // Plus facile à régler ici

    public DeplaceJoueur(double destX, double destY) {
        this.destX = destX;
        this.destY = destY;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            double posX = Joueur.getPositionX();
            double posY = Joueur.getPositionY();

            if (posX == destX && posY == destY) break;

            double dx = destX - posX;
            double dy = destY - posY;
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance > VITESSE){
                // Normalisation du vecteur de déplacement
                double moveX = (dx / distance) * VITESSE;
                double moveY = (dy / distance) * VITESSE;

                Joueur.deplaceX(posX + moveX);
                Joueur.deplaceY(posY + moveY);
            }

            else {
                Joueur.deplaceX(destX);
                Joueur.deplaceY(destY);
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