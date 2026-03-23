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

        // --- ESPACEMENT ENTRE LES SECTIONS ---
        // Ajoute un grand espace avant le bloc suivant
        yCourant += 45;

        // --- SECTION : CONSTRUCTIONS ---
        g2d.setColor(couleurTexte); // Au lieu du noir forcé
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("CONSTRUCTIONS", xOffset, yCourant);


        // ==========================================================
        // --- STATUT ACTIVÉE / DÉSACTIVÉE ---
        // ==========================================================
        // Outil pour calculer la largeur exacte (en pixels) du mot "CONSTRUCTIONS" avec la police actuelle
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth("CONSTRUCTIONS");
        // Calcule la position X où écrire l'état, juste après le titre (+10px d'espace)
        int statusX = xOffset + textWidth + 10;

        g2d.setFont(new Font("Arial", Font.BOLD, 12));

        // Met à jour l'indication visuelle en fonction des règles de gameplay (construction interdite le jour)
        if (isDay) {
            // C'est le jour : C'était ACTIVÉE dans ton code (je te laisse la logique telle quelle)
            // Note : D'après Joueur.java, la construction est en fait bloquée le jour.
            // (La couleur verte/rouge suit ton code actuel).
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

        // Récupération des ressources directement depuis le sac à dos du joueur
        ArrayList<Ressource> inv = joueur.getInventaire();
        // Compteurs individuels
        int bois = 0, pierre = 0, fer = 0, or = 0;

        // Trie et compte l'inventaire complet
        for (Ressource r : inv) {
            switch (r.getType()) {
                case 0 -> bois++;
                case 1 -> pierre++;
                case 2 -> fer++;
                case 3 -> or++;
            }
        }

        // Algorithme pour calculer combien de tours complètes le joueur peut payer.
        // On divise chaque ressource possédée par son coût, et on prend la valeur la plus petite (le goulot d'étranglement).
        int nbTours = Math.min(
                Math.min(bois / 4, pierre / 4),
                Math.min(fer / 2, or / 1) // (or / 1 est formel, correspond au prix de 1 Or)
        );

        // Affichage dynamique du résultat du calcul
        yCourant += 25;
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        g2d.drawString("• Tour : " + nbTours + " possible(s)", xOffset + 5, yCourant);

        // Affichage du rappel des prix en plus petit et en italique
        yCourant += 18;
        g2d.setFont(new Font("Arial", Font.ITALIC, 11));
        g2d.drawString("(Coût : 4 Bois, 4 Pierres, 2 Fer, 1 Or)", xOffset + 15, yCourant);
    }
}