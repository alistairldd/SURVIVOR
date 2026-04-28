package Vue.HUD;

import Modele.Items.Item;
import Modele.Joueur;
import Modele.Modele;
import Modele.Ressource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import static Modele.Constantes.*;

/**
 * Sous-panneau du HUD listant les quantités de matériaux possédés par le joueur.
 * Affiche dynamiquement les compteurs de ressources et l'or accumulé.
 */
public class VueHUDInventaire {

    /** ---------- [Méthodes Publiques - Moteur de Rendu] ---------- **/

    /**
     * Dessine le tableau récapitulatif de l'inventaire des ressources matérielles et de l'or.
     *
     * @param g - Contexte graphique 2D
     * @param yDebut - Hauteur Y initiale pour le rendu dans le HUD
     * @param modele - Le modèle pour adapter la colorimétrie au cycle jour/nuit
     * @param joueur - Le joueur contenant l'état actuel des stocks
     * @return La coordonnée Y finale après avoir dessiné le composant
     */
    public int dessiner(Graphics g, int yDebut, Modele modele, Joueur joueur) {
        Graphics2D g2d = (Graphics2D) g;
        int yCourant = yDebut;

        boolean isDay = modele.getLeCycleJourNuit().isDay();
        Color couleurTexte = isDay ? Color.BLACK : Color.WHITE;
        g2d.setColor(couleurTexte);

        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("RESSOURCES", xOffset, yCourant);

        int[] compteurs = new int[4];
        ArrayList<Ressource> inventaire = joueur.getRessources();

        // Comptage des ressources par type
        for (Ressource r : inventaire) {
            int type = r.getType();
            if (type >= 0 && type < compteurs.length) {
                compteurs[type]++;
            }
        }

        String[] noms = {"Bois", "Pierre", "Fer", "Or"};
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));

        yCourant += 25;

        // Rendu ligne par ligne (Icône + Texte + Quantité)
        for (int i = 0; i < noms.length; i++) {
            try {
                // Lecture disque directe (à optimiser potentiellement hors contraintes)
                Image img = ImageIO.read(new File("src/images/ressources/" + noms[i] + ".png"));
                g2d.drawImage(img, xOffset, yCourant - 14, 16, 16, null);
            } catch (Exception e) {
                System.out.println("Erreur de chargement de l'image pour " + noms[i] + ": " + e.getMessage());
            }
            g2d.drawString(noms[i] + " : " + compteurs[i], xOffset + 20, yCourant);
            yCourant += 20;
        }

        yCourant += 15;
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("Pièces : " + joueur.getPieces(), xOffset, yCourant);

        return yCourant + 30;
    }
}