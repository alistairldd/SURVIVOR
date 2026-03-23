package Vue;

import java.awt.*;

public class VueVie {

    // Valeur actuelle de la vie du joueur
    private int vie;
    // Valeur maximale de la vie du joueur
    private int vieMax;
    // Couleur utilisée pour dessiner la barre de vie en fonction du type (monstre, tour, joueur)
    private Color color;


    // Constructeur de la classe VueVie
    public VueVie(int vie, int vieMax, Color color) {
        this.vie = vie;
        this.vieMax = vieMax;
        this.color = color;
    }

    public void dessinerVie(Graphics g, int x, int y, int width, int height) {
        // Dessine une bordure noire pour la barre de vie
        g.setColor(Color.BLACK);
        g.drawRect(x-5, y-5, width+5, height+5);

        // Calcule la largeur de la partie remplie de la barre de vie en fonction du pourcentage de vie restante
        int filledWidth = (int) ((double) vie / vieMax * width);

        // Dessine la partie remplie de la barre de vie avec la couleur spécifiée
        g.setColor(color);
        g.fillRect(x, y, filledWidth, height);
    }

}
