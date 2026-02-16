package Vue;

import Modele.Ressource;
import java.awt.*;

public class VueRessource {

    public VueRessource() {
        // Le constructeur est vide maintenant car on ne stocke plus de position unique ici
    }

    // MODIFICATION : On ajoute les paramètres 'Ressource r', 'x', et 'y'
    public void dessinerRessource(Graphics g, Ressource r, int x, int y) {
        Color[] col = {new Color(0, 255, 0), new Color(53, 53, 53), new Color(120, 46, 0)}; // Vert, Gris, Orange
        int taille = 20;

        int type = r.getType();

        // Sécurité pour la couleur
        if (type >= 0 && type < col.length) {
            g.setColor(col[type]);
        } else {
            g.setColor(Color.BLACK);
        }

        // On dessine aux coordonnées x, y fournies par la Vue principale
        g.fillOval(x, y, taille, taille);
    }
}