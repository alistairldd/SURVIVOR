package Vue;

import Modele.Joueur;
import Modele.Modele;

import java.awt.*;

import static Modele.Constantes.xOffset;

public class VueHUDBat {
    /**
     * Dessine les informations de construction.
     * @return La coordonnée Y finale après dessin.
     */
    public int dessiner(Graphics g, int yDebut, Modele modele, Joueur joueur) {

        int yCourant = yDebut;
        Graphics2D g2d = (Graphics2D) g;

        // Vérifie si l'état actuel est le Jour
        boolean isDay = modele.getLeCycleJourNuit().isDay();
        Color couleurTexte = isDay ? Color.BLACK : Color.WHITE;

        g2d.setColor(couleurTexte);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));

        // --- SECTION : CONSTRUCTIONS ---
        g2d.drawString("CONSTRUCTIONS", xOffset, yCourant);

        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth("CONSTRUCTIONS");
        int statusX = xOffset + textWidth + 10;

        g2d.setFont(new Font("Arial", Font.BOLD, 12));

        if (isDay) {
            g2d.setColor(new Color(0, 150, 0));
            g2d.drawString("- ACTIVÉE", statusX, yCourant - 1);
        } else {
            g2d.setColor(Color.RED);
            g2d.drawString("- DÉSACTIVÉE", statusX, yCourant - 1);
        }

        g2d.setColor(couleurTexte);

        int nbTours = joueur.calculerMaxToursConstructibles();

        yCourant += 25;
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        g2d.drawString("• Tour : " + nbTours + " possible(s)", xOffset + 5, yCourant);

        yCourant += 18;
        g2d.setFont(new Font("Arial", Font.ITALIC, 11));
        g2d.drawString("(Coût : 4 Bois, 4 Pierres, 2 Fer, 1 Or)", xOffset + 15, yCourant);

        yCourant += 30; // Marge pour le composant suivant
        return yCourant;
    }
}