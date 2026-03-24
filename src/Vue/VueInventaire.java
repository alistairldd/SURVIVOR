package Vue;

import Modele.Joueur;
import Modele.Modele;
import Modele.Ressource;
import java.awt.*;
import java.util.ArrayList;
import static Modele.Constantes.*;

/**
 * Sous-panneau du HUD listant les quantités de matériaux possédés par le joueur.
 * Lit directement les données du Modèle et les met en forme.
 */
public class VueInventaire {

    /**
     * Dessine le tableau récapitulatif de l'inventaire.
     * @param g Contexte graphique.
     * @param yDebut Hauteur de départ dans le HUD.
     * @param modele Le modèle pour accéder au cycle jour/nuit et adapter la couleur du texte en conséquence.
     * @param joueur Le joueur dont on lit l'inventaire.
     * @return La coordonnée Y finale après avoir dessiné ce composant (pour empiler la suite).
     */
    public int dessiner(Graphics g, int yDebut, Modele modele, Joueur joueur) {
        Graphics2D g2d = (Graphics2D) g;

        // Curseur vertical qui mémorisera notre progression
        int yCourant = yDebut;

        // --- MODIFICATION : Couleur adaptative ---
        // Vérifie l'état jour/nuit pour garantir la lisibilité
        boolean isDay = modele.getLeCycleJourNuit().isDay();
        Color couleurTexte = isDay ? Color.BLACK : Color.WHITE;
        g2d.setColor(couleurTexte);
        // -----------------------------------------

        // Affiche le titre en grand
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.drawString("INVENTAIRE", xOffset, yCourant);

        // Récupère la liste brute des objets possédés
        ArrayList<Ressource> inventaire = joueur.getInventaire();

        // Comptage des ressources (0: bois, 1: pierre, 2: fer, 3: or)
        // Crée un tableau de 4 cases pour stocker les totaux de chaque matériau
        int[] compteurs = new int[4];

        // Parcours du sac à dos entier
        for (Ressource r : inventaire) {
            // Extrait l'identifiant (0, 1, 2 ou 3)
            int type = r.getType();
            // Sécurité : Vérifie que l'ID rentre bien dans notre tableau pour éviter un crash (IndexOutOfBounds)
            if (type >= 0 && type < compteurs.length) {
                // Incrémente la case correspondante
                compteurs[type]++;
            }
        }

        // Tableau des labels pour l'affichage humain
        String[] noms = {"Bois", "Pierre", "Fer", "Or"};
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));

        yCourant += 25; // On descend sous le titre principal

        // Boucle pour dessiner les 4 lignes de ressources
        for (int i = 0; i < noms.length; i++) {
            // Concatène le nom et la quantité finale, et décale le texte vers le bas de 20px à chaque itération
            g2d.drawString(noms[i] + " : " + compteurs[i], xOffset + 10, yCourant);
            yCourant += 20; // Descend pour la ligne suivante
        }

        // Affichage de l'or/pièces
        g.drawString("Pièces : " + joueur.getPieces(), xOffset, yCourant);

        yCourant += 30; // Marge de sécurité de 30px avant le prochain composant visuel

        return yCourant;
    }
}