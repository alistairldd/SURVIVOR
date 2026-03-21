package Vue;

import Modele.Joueur;
import Modele.Ressource;

import java.awt.*;
import java.util.ArrayList;

public class VueInstructions {
    private final int xOffset = 20;

    public void dessiner(Graphics g, int yDebut, Joueur joueur) {
        int yCourant = yDebut; // Utilisation d'une variable locale pour gérer la hauteur dynamiquement
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);

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
            yCourant += 25; // On descend pour chaque ligne d'instruction
            g2d.drawString(instructions[i], xOffset + 5, yCourant);
        }

        // --- ESPACEMENT ENTRE LES SECTIONS ---
        yCourant += 45; // Saut de ligne pour séparer les Commandes des Constructions

        // --- SECTION : CONSTRUCTIONS ---
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("CONSTRUCTIONS DISPONIBLES", xOffset, yCourant);

        // Récupération des ressources depuis le Modèle (Inventaire du joueur)
        ArrayList<Ressource> inv = joueur.getInventaire();
        int bois = 0, pierre = 0, fer = 0, or = 0;

        for (Ressource r : inv) {
            switch (r.getType()) {
                case 0 -> bois++;   // Type 0 : Bois
                case 1 -> pierre++; // Type 1 : Pierre
                case 2 -> fer++;    // Type 2 : Fer
                case 3 -> or++;     // Type 3 : Or
            }
        }

        // Calcul du nombre de tours possibles selon les coûts : 4 Bois, 4 Pierres, 2 Fer, 1 Or
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