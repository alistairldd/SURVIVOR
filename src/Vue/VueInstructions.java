package Vue;

import Modele.Joueur;
import Modele.Ressource;

import java.awt.*;
import java.util.ArrayList;

public class VueInstructions {
    private final int xOffset = 20;

    public void dessiner(Graphics g, int yDebut, Joueur joueur) {
        int yCourant = yDebut;
        Graphics2D g2d = (Graphics2D) g;

        // --- MODIFICATION : Déterminer la couleur de base selon le cycle ---
        boolean isDay = joueur.getModele().getLeCycleJourNuit().isDay();
        Color couleurTexte = isDay ? Color.BLACK : Color.WHITE;

        g2d.setColor(couleurTexte);
        // -------------------------------------------------------------------

        // --- SECTION : COMMANDES ---
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("COMMANDES", xOffset, yCourant);

        g2d.setFont(new Font("Arial", Font.PLAIN, 13));
        String[] instructions = {
                "• CLIC DROIT : Se déplacer",
                "• CLIC GAUCHE : Attaquer",
                "• TOUCHE E : Ramasser ressource",
                "• TOUCHE T : Construire tour"
        };

        for (int i = 0; i < instructions.length; i++) {
            yCourant += 25;
            g2d.drawString(instructions[i], xOffset + 5, yCourant);
        }

        // --- ESPACEMENT ENTRE LES SECTIONS ---
        yCourant += 45;

        // --- SECTION : CONSTRUCTIONS ---
        g2d.setColor(couleurTexte); // Au lieu du noir forcé
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("CONSTRUCTIONS", xOffset, yCourant);


        // ==========================================================
        // --- STATUT ACTIVÉE / DÉSACTIVÉE ---
        // ==========================================================
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth("CONSTRUCTIONS");
        int statusX = xOffset + textWidth + 10;

        g2d.setFont(new Font("Arial", Font.BOLD, 12));

        if (isDay) {
            // C'est le jour : C'était ACTIVÉE dans ton code (je te laisse la logique telle quelle)
            g2d.setColor(new Color(0, 150, 0));
            g2d.drawString("- ACTIVÉE", statusX, yCourant - 1);
        } else {
            // C'est la nuit : DÉSACTIVÉE
            g2d.setColor(Color.RED);
            g2d.drawString("- DÉSACTIVÉE", statusX, yCourant - 1);
        }
        // ==========================================================


        // On remet la couleur dynamique pour les ressources (au lieu du noir)
        g2d.setColor(couleurTexte);

        // Récupération des ressources
        ArrayList<Ressource> inv = joueur.getInventaire();
        int bois = 0, pierre = 0, fer = 0, or = 0;

        for (Ressource r : inv) {
            switch (r.getType()) {
                case 0 -> bois++;
                case 1 -> pierre++;
                case 2 -> fer++;
                case 3 -> or++;
            }
        }

        int nbTours = Math.min(
                Math.min(bois / 4, pierre / 4),
                Math.min(fer / 2, or / 1)
        );

        yCourant += 25;
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        g2d.drawString("• Tour : " + nbTours + " possible(s)", xOffset + 5, yCourant);

        yCourant += 18;
        g2d.setFont(new Font("Arial", Font.ITALIC, 11));
        g2d.drawString("(Coût : 4 Bois, 4 Pierres, 2 Fer, 1 Or)", xOffset + 15, yCourant);
    }
}