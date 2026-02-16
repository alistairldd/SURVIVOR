package Modele;

public class Joueur {

    // Position
    private static int positionX;
    private static int positionY;

    // Constructeur de la classe Joueur, il initialise les données du joueur.
    public Joueur() { // on initialise la position en 0,0 dans le modèle
        positionX = 0;
        positionY = 0;
    }

    public static int getPositionX() {return positionX;}

    public static int getPositionY() {return positionY;}

    public void setPositionX(int positionX) {Joueur.positionX = positionX;}

    public void setPositionY(int positionY) {Joueur.positionY = positionY;}


    // Méthode pour déplacer le joueur en x,
    // elle prend en paramètre le déplacement en x,
    // elle met à jour la position du joueur en x.
    public void deplaceX(int x) {

        if (x >= -Modele.getTailleCarte() && x <= Modele.getTailleCarte()) {
            this.setPositionX(x);
        }
        else if (x <= -Modele.getTailleCarte()) {
            this.setPositionX(-Modele.getTailleCarte());
        }
        else {
            this.setPositionX(Modele.getTailleCarte());
        }
    }

    // Méthode pour déplacer le joueur en y,
    // elle prend en paramètre le déplacement en y,
    // elle met à jour la position du joueur en y.
    public void deplaceY(int y) {
        if (y >= -Modele.getTailleCarte() && y <= Modele.getTailleCarte()) {
            this.setPositionY(y);
        }
        else if (y <= -Modele.getTailleCarte()) {
            this.setPositionY(-Modele.getTailleCarte());
        }
        else {
            this.setPositionY(Modele.getTailleCarte());
        }
    }

    // Méthode pour déplacer le joueur, elle prend
    // en paramètre les déplacements en x et en y,
    // elle met à jour la position du joueur et affiche la nouvelle position du joueur.
    public void deplaceJoueur(int x, int y){
        deplaceX(x);
        deplaceY(y);
        System.out.println("Position du joueur : (" + Joueur.getPositionX() + ", " + Joueur.getPositionY() + ")");
    }

}
