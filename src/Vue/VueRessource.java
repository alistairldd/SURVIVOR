package Vue;

import Modele.Ressource;
import java.awt.*;

/**
 * Utilitaires de dessin pour les ressources tombées au sol.
 * Associe visuellement un ID de matériau (Modèle) à une couleur (Vue).
 */
public class VueRessource {

    public VueRessource() {
        // Le constructeur est vide maintenant car on ne stocke plus de position unique ici
    }

    // MODIFICATION : On ajoute les paramètres 'Ressource r', 'x', et 'y'
    /**
     * Dessine l'objet ressource (cercle coloré).
     * @param g Contexte graphique principal ou minimap.
     * @param r L'instance de la ressource contenant son ID (Type).
     * @param x Coordonnée absolue ou mise à l'échelle.
     * @param y Coordonnée absolue ou mise à l'échelle.
     * @param minimap Définit si l'objet doit être miniaturisé.
     */
    public static void dessinerRessource(Graphics g, Ressource r, int x, int y, boolean minimap) {
        // Dictionnaire visuel : associe un type (0, 1, 2, 3) à un code couleur précis (RGB)
        // 0 (Bois) -> Marron
        // 1 (Pierre) -> Gris clair
        // 2 (Fer) -> Gris argent/métallique
        // 3 (Or) -> Jaune/Doré
        Color[] col = {new Color(109,71,49), new Color(123,123,125),  new Color(182, 182, 182), new Color(218, 165, 32)}; // marron, gris, jaune

        // Diamètre de l'objet sur la carte
        int taille = 20;

        if (minimap){
            // Si on dessine sur la minimap, on réduit la taille des ressources à de minuscules points de 4 pixels
            taille = 4;
        }

        // Récupère l'identifiant du matériau
        int type = r.getType();

        // Sécurité pour la couleur : empêche un crash "ArrayIndexOutOfBounds" si une nouvelle ressource non prévue est ajoutée au Modèle
        if (type >= 0 && type < col.length) {
            // Applique la couleur correspondante issue du tableau
            g.setColor(col[type]);
        } else {
            // Couleur de secours en cas d'erreur
            g.setColor(Color.BLACK);
        }

        // On dessine aux coordonnées x, y fournies par la Vue principale (qui a déjà appliqué la translation de Caméra)
        // La forme est un simple cercle (Oval de largeur égale à hauteur)
        g.fillOval(x, y, taille, taille);
    }
}