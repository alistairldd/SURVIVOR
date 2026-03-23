package Vue;

import Modele.Modele;

import java.awt.*;
import Modele.Localisable;

import static Modele.Constantes.xOffset;

public class VueVie {

    // Référence au modèle pour accéder aux données du jeu si nécessaire (ex: pour récupérer la vie du joueur)
    private Modele modele;

    public VueVie(Modele modele) {
        // Constructeur par défaut, les paramètres de la barre de vie seront mis à jour plus tard via setParam()
        this.modele = modele;
    }


    public void dessiner(Graphics g, int yDebut, int width, int height) {
        /*
            * Dessine une barre de vie à la position (x, y) avec les dimensions spécifiées (width, height).
            * La barre de vie est remplie proportionnellement à la vie actuelle par rapport à la vie maximale.
            * La couleur de la barre de vie est déterminée par le paramètre "color" passé au constructeur.
         */

        Localisable localisable = modele.getCibleAffichage(); // Par défaut, on suppose que c'est pour le joueur

        if (localisable != null) { // Ne dessine pas la barre de vie tant qu'on a pas hover qqchose

            String nom = localisable.getNom();
            int vie = localisable.getHp();
            int vieMax = localisable.getMaxHp();
            Color color;

            if (nom.equals("Joueur")) {
                color = Color.GREEN;
            } else if (nom.equals("Tour")) {
                color = Color.BLUE;
            } else {
                color = Color.RED;
            }


            // Dessine une bordure noire pour la barre de vie
            g.setColor(Color.BLACK);
            g.fillRect(xOffset -5, yDebut -5, width + 10, height + 10);

            // Calcule la largeur de la partie remplie de la barre de vie en fonction du pourcentage de vie restante
            int filledWidth = (int) ((double) vie / vieMax * width);

            // Dessine la partie remplie de la barre de vie avec la couleur spécifiée
            g.setColor(color);
            g.fillRect(xOffset, yDebut, filledWidth, height);
        }
    }

}
