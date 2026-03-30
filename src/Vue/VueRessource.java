package Vue;

import Modele.Ressource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Utilitaires de dessin pour les ressources tombées au sol.
 * Associe visuellement un ID de matériau (Modèle) à une couleur (Vue).
 */
public class VueRessource {
    private BufferedImage imageFer;
    private BufferedImage imageOr;
    private BufferedImage imagePierre;

    public VueRessource() {
        // Importe les images des ressources
        try {
            this.imageFer = ImageIO.read(new File("src/images/lingot_fer.png"));
            this.imageOr = ImageIO.read(new File("src/images/lingot_or.png"));
            this.imagePierre = ImageIO.read(new File("src/images/pierre.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur : Impossible de charger l'image.");
        }

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
    public void dessinerRessource(Graphics g, Ressource r, int x, int y, boolean minimap) {
        // Dictionnaire visuel : associe un type (0, 1, 2, 3) à un code couleur précis (RGB)
        // 0 (Bois) -> Marron
        // 1 (Pierre) -> Gris clair
        // 2 (Fer) -> Gris argent/métallique
        // 3 (Or) -> Jaune/Doré

        // Diamètre de l'objet sur la carte
        int taille = 50;

        if (minimap){
            // Si on dessine sur la minimap, on réduit la taille des ressources à de minuscules points de 4 pixels
            taille = 16;
        }

        // Récupère l'identifiant du matériau
        int type = r.getType();
        // On dessine aux coordonnées x, y fournies par la Vue principale (qui a déjà appliqué la translation de Caméra)
        switch (type) {
            case 0: // Bois
                g.setColor(new Color(139, 69, 19)); // Marron
                g.fillOval(x, y, taille, taille);
                break;
            case 1: // Pierre
                g.drawImage(imagePierre, x-taille/2, y-taille/2, taille, taille, null);
                break;
            case 2: // Fer
                g.drawImage(imageFer, x-taille/2, y-taille/2, taille, taille, null);
                break;
            case 3: // Or
                g.drawImage(imageOr, x-taille/2, y-taille/2, taille, taille, null);
                break;
            default:
                g.setColor(Color.BLACK); // Couleur de secours
                g.fillOval(x-taille/2, y-taille/2, taille, taille);
        }



    }
}