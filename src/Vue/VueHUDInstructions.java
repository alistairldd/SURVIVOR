package Vue;

import Modele.Joueur;
import java.awt.*;
import static Modele.Constantes.*;

/**
 * Sous-panneau du HUD affichant le tutoriel des commandes et l'état des constructions.
 * Met à jour dynamiquement la lisibilité (couleur du texte) et la faisabilité
 * des actions (ex: nombre de tours fabricables) selon l'état du Modèle.
 */
public class VueHUDInstructions {
    /**
     * Méthode de rendu des instructions.
     * @return La coordonnée Y finale.
     */
    public int dessiner(Graphics g, int yDebut, Joueur joueur) {
        int yCourant = yDebut;
        Graphics2D g2d = (Graphics2D) g;

        boolean isDay = joueur.getModele().getLeCycleJourNuit().isDay();
        Color couleurTexte = isDay ? Color.BLACK : Color.WHITE;
        g2d.setColor(couleurTexte);

        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("COMMANDES", xOffset, yCourant);

        g2d.setFont(new Font("Arial", Font.PLAIN, 13));

        // Liste complète et actualisée des commandes du jeu
        String[] instructions = {
                "• CLIC DROIT : Se déplacer",
                "• CLIC GAUCHE : Attaquer",
                "• TOUCHE R : Récolter la mine (Jour)",
                "• TOUCHE T : Construire tour",
                "• TOUCHE C : Afficher rayon de l'attaque",
                "• FLÈCHES : Changer de page (HUD)",
                "• PAVÉ NUM. 1 à 5 : Acheter (Shop)"
        };

        for (int i = 0; i < instructions.length; i++) {
            yCourant += 25;
            g2d.drawString(instructions[i], xOffset + 5, yCourant);
        }

        yCourant += 30; // Marge dynamique
        return yCourant;
    }
}