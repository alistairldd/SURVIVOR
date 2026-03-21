package Vue;

import Modele.*;
import java.awt.*;

public class VueCarte {

    private final Modele modele;
    private final Joueur joueur;

    public VueCarte(Modele modele) {
        this.modele = modele;
        this.joueur = modele.getJoueur();
    }

    protected void dessiner(Graphics g) {
        // La carte fait 2000x2000.
        // Si le centre du monde est (0,0), le coin haut-gauche de la carte est à (-1000, -1000).
        int coinHautGaucheMapX = 0;
        int coinHautGaucheMapY = 0;

        // On applique la même logique de caméra que pour les ressources :
        // Centre Ecran + Position Objet - Position Joueur
        double x = coinHautGaucheMapX - joueur.getPositionX();
        double y = coinHautGaucheMapY - joueur.getPositionY();

        g.setColor(new Color(0, 101, 0));

        // On dessine le rectangle vert qui représente le monde entier
        g.fillRect(10, 10, Map.LARGEUR_MAP, Map.HAUTEUR_MAP);

        // (Optionnel) Ajout d'une bordure rouge pour bien voir les limites de la map
        g.setColor(Color.RED);
        g.drawRect(10, 10, Map.LARGEUR_MAP, Map.HAUTEUR_MAP);
    }

}