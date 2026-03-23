package Vue;

import Modele.Joueur;
import Modele.Ressource;

import java.awt.*;
import java.util.ArrayList;
import static Modele.Constantes.*;

/**
 * Sous-panneau du HUD affichant le tutoriel des commandes et l'état des constructions.
 * Met à jour dynamiquement la lisibilité (couleur du texte) et la faisabilité
 * des actions (ex: nombre de tours fabricables) selon l'état du Modèle.
 */
public class VueInstructions {
    /**
     * Méthode de rendu des instructions.
     * @param g Contexte graphique fourni par la VueHUD parent.
     * @param yDebut Ordonnée Y de départ pour dessiner ce bloc de texte.
     * @param joueur Référence au joueur pour lire son inventaire et son modèle.
     */
    public void dessiner(Graphics g, int yDebut, Joueur joueur) {
        // Curseur vertical qui va descendre au fur et à mesure qu'on ajoute des lignes de texte
        int yCourant = yDebut;
        Graphics2D g2d = (Graphics2D) g;

        // --- MODIFICATION : Déterminer la couleur de base selon le cycle ---
        // Vérifie si l'état actuel est le Jour
        boolean isDay = joueur.getModele().getLeCycleJourNuit().isDay();
        // Change dynamiquement la couleur du texte pour qu'il reste lisible sur le fond changeant du HUD
        Color couleurTexte = isDay ? Color.BLACK : Color.WHITE;

        // Applique la couleur sélectionnée au pinceau
        g2d.setColor(couleurTexte);
        // -------------------------------------------------------------------

        // --- SECTION : COMMANDES ---
        // Définit une police Arial en Gras, taille 16, pour le titre
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        // Dessine le titre principal
        g2d.drawString("COMMANDES", xOffset, yCourant);

        // Repasse en police normale (plus petite) pour le corps du texte
        g2d.setFont(new Font("Arial", Font.PLAIN, 13));
        // Tableau contenant les touches de base
        String[] instructions = {
                "• CLIC DROIT : Se déplacer",
                "• CLIC GAUCHE : Attaquer",
                "• TOUCHE E : Ramasser ressource",
                "• TOUCHE T : Construire tour",
                "• TOUCHE C : Afficher le rayon de l'attaque"
        };

        // Boucle pour afficher chaque instruction ligne par ligne
        for (int i = 0; i < instructions.length; i++) {
            // Descend le curseur vertical de 25 pixels pour la nouvelle ligne
            yCourant += 25;
            // Dessine le texte avec un léger décalage (+5px) vers la droite pour faire un effet de liste
            g2d.drawString(instructions[i], xOffset + 5, yCourant);
        }
    }
}