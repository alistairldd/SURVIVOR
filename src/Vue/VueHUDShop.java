package Vue;

import Modele.Joueur;
import java.awt.*;
import static Modele.Constantes.*;

public class VueHUDShop {
    public int dessiner(Graphics g, int yDebut, Joueur joueur) {
        Graphics2D g2d = (Graphics2D) g;
        int yCourant = yDebut;

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString("--- Shop ---", 40, yCourant);

        yCourant += 30;
        g.drawString("Pièces : " + joueur.getPieces(), xOffset, yCourant);

        yCourant += 50;
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));

        // Article 1
        g2d.drawString("1. Épée Aciérée (+5 Dégâts)", 20, yCourant);
        g2d.setFont(new Font("Arial", Font.ITALIC, 11));
        g2d.drawString("   Prix: " + formatPrix(PRIX_EPEE_ACIEREE), 20, yCourant + 15);

        // Article 2
        yCourant += 50;
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        g2d.drawString("2. Armure (+20 PV)", 20, yCourant);
        g2d.setFont(new Font("Arial", Font.ITALIC, 11));
        g2d.drawString("   Prix: " + formatPrix(PRIX_ARMURE), 20, yCourant + 15);

        // Article 3
        yCourant += 50;
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        g2d.drawString("3. Armure lourde", 20, yCourant);
        g2d.setFont(new Font("Arial", Font.ITALIC, 11));
        g2d.drawString("   Prix: " + formatPrix(PRIX_ARMURE_LOURDE), 20, yCourant + 15);

        // Article 4
        yCourant += 50;
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        g2d.drawString("4. Épée améliorée", 20, yCourant);
        g2d.setFont(new Font("Arial", Font.ITALIC, 11));
        g2d.drawString("   Prix: " + formatPrix(PRIX_EPEE_AMELIOREE), 20, yCourant + 15);

        // Article 5
        yCourant += 50;
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        g2d.drawString("5. Potion de Vie", 20, yCourant);
        g2d.setFont(new Font("Arial", Font.ITALIC, 11));
        g2d.drawString("   Prix: " + formatPrix(PRIX_POTION), 20, yCourant + 15);

        yCourant += 80;
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString("[1, 2, 3] Naviguer | [Pavé Num 1-5] Acheter", 20, yCourant);

        yCourant += 40; // Marge finale pour le JScrollPane
        return yCourant;
    }

    private String formatPrix(int[] prix) {
        StringBuilder sb = new StringBuilder();
        if (prix[0] > 0) sb.append(prix[0]).append(" Bois, ");
        if (prix[1] > 0) sb.append(prix[1]).append(" Pierre, ");
        if (prix[2] > 0) sb.append(prix[2]).append(" Fer, ");
        if (prix[3] > 0) sb.append(prix[3]).append(" Or, ");

        if (sb.length() > 0) {
            sb.setLength(sb.length() - 2);
        }
        return sb.toString();
    }
}