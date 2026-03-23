package Vue;

import Modele.Joueur;
import Modele.Ressource;
import java.awt.*;
import java.util.ArrayList;
import static Modele.Constantes.*;

public class VueShop {
    public void dessiner(Graphics g, int yDebut, Joueur joueur) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString("--- Shop ---", 40, yDebut);

        g.drawString("Pièces : " + joueur.getPieces(), xOffset, yDebut + 30);

        int y = yDebut + 50;
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));

        // Article 1
        g2d.drawString("1. Épée Aciérée (+5 Dégâts)", 20, y);
        g2d.setFont(new Font("Arial", Font.ITALIC, 11));
        g2d.drawString("   Prix: 10 Fer, 5 Or", 20, y + 15);

        // Article 2
        y += 50;
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        g2d.drawString("2. Armure (+20 PV)", 20, y);
        g2d.setFont(new Font("Arial", Font.ITALIC, 11));
        g2d.drawString("   Prix: 15 Pierre, 5 Fer", 20, y + 15);

        // Article 3
        y += 50;
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        g2d.drawString("3. Armure lourde", 20, y);
        g2d.setFont(new Font("Arial", Font.ITALIC, 11));
        g2d.drawString("   Prix: 10 Bois, 5 Or", 20, y + 15);

        g2d.setFont(new Font("Arial", Font.BOLD, 12));


        // Article 4
        y += 50;
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        g2d.drawString("4. Épée", 20, y);
        g2d.setFont(new Font("Arial", Font.ITALIC, 11));
        g2d.drawString("   Prix: 10 Bois, 5 Or", 20, y + 15);

        g2d.setFont(new Font("Arial", Font.BOLD, 12));


        // Article 5
        y += 50;
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        g2d.drawString("5. Potion de Vie", 20, y);
        g2d.setFont(new Font("Arial", Font.ITALIC, 11));
        g2d.drawString("   Prix: 10 Bois, 5 Or", 20, y + 15);

        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString("[I] Quitter | [1-5] Acheter", 20, y + 80);

    }
}