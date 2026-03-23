package Vue;

import Modele.Joueur;
import Modele.Modele;

import java.awt.*;

import static Modele.Constantes.xOffset;

public class VueBatHud {
    public void dessiner(Graphics g, int yDebut, Modele modele, Joueur joueur) {

        int yCourant = yDebut;
        Graphics2D g2d = (Graphics2D) g;

        // --- MODIFICATION : Déterminer la couleur de base selon le cycle ---
        // Vérifie si l'état actuel est le Jour
        boolean isDay = modele.getLeCycleJourNuit().isDay();
        // Change dynamiquement la couleur du texte pour qu'il reste lisible sur le fond changeant du HUD
        Color couleurTexte = isDay ? Color.BLACK : Color.WHITE;

        // Applique la couleur sélectionnée au pinceau
        g2d.setColor(couleurTexte);

        // Définit une police Arial en Gras, taille 16, pour le titre
        g2d.setFont(new Font("Arial", Font.BOLD, 16));

        // --- SECTION : CONSTRUCTIONS ---
        g2d.setColor(couleurTexte);
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

        // On remet la couleur dynamique pour les ressources (au lieu du noir)
        g2d.setColor(couleurTexte);

        int nbTours = joueur.calculerMaxToursConstructibles();

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