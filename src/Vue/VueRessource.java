package Vue;

import Modele.Ressource;
import java.awt.*;

public class VueRessource {

    public VueRessource() {
        // Le constructeur est vide maintenant car on ne stocke plus de position unique ici
    }

    // MODIFICATION : On ajoute les paramètres 'Ressource r', 'x', et 'y'
    public static void dessinerRessource(Graphics g, Ressource r, int x, int y, boolean minimap) {
        Color[] col = {new Color(109,71,49), new Color(123,123,125), new Color(218, 165, 32), new Color(200,0,0)}; // marron, gris, jaune
        int taille = 20;

        if (minimap){
            // Si on dessine sur la minimap, on réduit la taille des ressources
            taille = 4;
        }

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