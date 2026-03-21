package Vue;

import Modele.Joueur;
import Modele.Ressource;
import java.awt.*;
import java.util.ArrayList;

public class VueInventaire {
    private final int xOffset = 20;

    public void dessiner(Graphics g, int yDebut, Joueur joueur) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 18));

        g2d.drawString("INVENTAIRE", xOffset, yDebut);

        ArrayList<Ressource> inventaire = joueur.getInventaire();

        // Comptage des ressources (0: bois, 1: pierre, 2: fer, 3: or)
        int[] compteurs = new int[4];
        for (Ressource r : inventaire) {
            int type = r.getType();
            if (type >= 0 && type < compteurs.length) {
                compteurs[type]++;
            }
        }

        String[] noms = {"Bois", "Pierre", "Fer", "Or"};
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));

        for (int i = 0; i < noms.length; i++) {
            g2d.drawString(noms[i] + " : " + compteurs[i], xOffset + 10, yDebut + 25 + (i * 20));
        }
    }
}