package Vue;

import Modele.*;
import java.awt.*;

public class VueCarte {

    private final Modele modele;

    public VueCarte(Modele modele) {
        this.modele = modele;
    }

    protected void dessiner(Graphics g, int xCentre, int yCentre) {
        // La carte fait 2000x2000.
        // Si le centre du monde est (0,0), le coin haut-gauche de la carte est à (-1000, -1000).
        int coinHautGaucheMapX = -Map.LARGEUR_MAP / 2;
        int coinHautGaucheMapY = -Map.HAUTEUR_MAP / 2;

        // On applique la même logique de caméra que pour les ressources :
        // Centre Ecran + Position Objet - Position Joueur
        int x = xCentre + coinHautGaucheMapX - Joueur.getPositionX();
        int y = yCentre + coinHautGaucheMapY - Joueur.getPositionY();

        g.setColor(new Color(0, 101, 0));

        // On dessine le rectangle vert qui représente le monde entier
        g.fillRect(x, y, Map.LARGEUR_MAP, Map.HAUTEUR_MAP);

        // (Optionnel) Ajout d'une bordure rouge pour bien voir les limites de la map
        g.setColor(Color.RED);
        g.drawRect(x, y, Map.LARGEUR_MAP, Map.HAUTEUR_MAP);
    }
}