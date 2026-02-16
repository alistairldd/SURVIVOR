package Vue;

import Modele.Joueur;
import Modele.Modele;
import Modele.Ressource;

import javax.swing.*;
import java.awt.*;

public class Vue extends JPanel {

    private final VueCarte vueCarte;
    private final VueJoueur vueJoueur;
    private final VueRessource vueRessource;

    public Vue(Modele modele) {
        this.vueCarte = new VueCarte(modele);
        this.vueJoueur = new VueJoueur();
        this.vueRessource = new VueRessource();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int mi_largeur = getWidth() / 2;
        int mi_hauteur = getHeight() / 2;

        // 1. Dessiner le fond
        vueCarte.dessiner(g, mi_largeur, mi_hauteur);

        // 2. Dessiner les ressources
        // On récupère la liste des ressources de la map
        for (Ressource r : Modele.getMap().getRessources()) {

            // Calcul de la position relative :
            // Centre écran + Position Ressource - Position Joueur
            int x = mi_largeur + r.getPositionX() - Joueur.getPositionX();
            int y = mi_hauteur + r.getPositionY() - Joueur.getPositionY();

            // On demande à VueRessource de dessiner CETTE ressource à CES coordonnées
            vueRessource.dessinerRessource(g, r, x, y);
        }

        // 3. Dessiner le joueur (toujours au centre)
        vueJoueur.dessiner(g, mi_largeur, mi_hauteur);
    }
}