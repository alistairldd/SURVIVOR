package Vue.HUD;

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

        boolean isDay = modele.getLeCycleJourNuit().isDay();
        Color couleurTexte = isDay ? Color.BLACK : Color.WHITE;

        g2d.setColor(couleurTexte);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));

        // --- TITRE ---
        g2d.drawString("CONSTRUCTIONS", xOffset, yCourant);

        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        if (isDay) {
            g2d.setColor(new Color(0, 150, 0));
            g2d.drawString("- PRÊT", xOffset + 150, yCourant);
        } else {
            g2d.setColor(Color.RED);
            g2d.drawString("- NUIT (OFF)", xOffset + 150, yCourant);
        }

        // --- LIGNE : TOUR ---
        yCourant += 30;
        g2d.setColor(couleurTexte);
        g2d.setFont(new Font("Arial", Font.BOLD, 13));
        g2d.drawString("• Tour de défense", xOffset + 5, yCourant);

        yCourant += 15;
        g2d.setFont(new Font("Arial", Font.ITALIC, 11));
        g2d.drawString("(Cout: 4 Bois, 4 Pierre, 2 Fer, 1 Or)", xOffset + 15, yCourant);

        // --- LIGNE : TENTE ---
        yCourant += 28;
        boolean tenteExiste = modele.getGestionnaireBatiments().aDejaUneTente();

        g2d.setFont(new Font("Arial", Font.BOLD, 13));
        g2d.setColor(tenteExiste ? Color.GRAY : couleurTexte); // Grisé si déjà construite
        g2d.drawString("• Tente de soin" + (tenteExiste ? " (Unique)" : ""), xOffset + 5, yCourant);

        yCourant += 15;
        g2d.setFont(new Font("Arial", Font.ITALIC, 11));
        if (tenteExiste) {
            g2d.setColor(new Color(178, 34, 34)); // Rouge pour signifier l'indisponibilité
            g2d.drawString("Déjà construite sur le terrain", xOffset + 15, yCourant);
        } else {
            g2d.drawString("(Cout: 7 Bois, 2 Pierre, 4 Fer, 5 Or)", xOffset + 15, yCourant);
        }

        return yCourant + 30;
    }
}