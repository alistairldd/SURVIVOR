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
    private BufferedImage imageBois;

    public VueRessource() {
        // Importe les images des ressources
        try {
            this.imageFer = ImageIO.read(new File("src/images/ressources/lingot_fer.png"));
            this.imageOr = ImageIO.read(new File("src/images/ressources/lingot_or.png"));
            this.imagePierre = ImageIO.read(new File("src/images/ressources/pierre.png"));
             this.imageBois = ImageIO.read(new File("src/images/ressources/bois.png"));

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur : Impossible de charger l'image.");
        }

    }

    /**
     * Dessine l'objet ressource (cercle coloré ou sprite).
     * @param g Contexte graphique principal ou minimap.
     * @param r L'instance de la ressource contenant son ID (Type).
     * @param x Coordonnée X en double (sera castée en int pour le dessin).
     * @param y Coordonnée Y en double (sera castée en int pour le dessin).
     * @param minimap Définit si l'objet doit être dessiné en taille réduite.
     */
    public void dessinerRessource(Graphics g, Ressource r, double x, double y, boolean minimap) {
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

        // Transtypage des coordonnées pour le moteur de rendu graphique
        int drawX = (int) x;
        int drawY = (int) y;

        // On dessine aux coordonnées x, y fournies par la Vue principale (qui a déjà appliqué la translation de Caméra)
        switch (type) {
            case 0: // Bois
                g.drawImage(imageBois, drawX-taille/2, drawY-taille/2, taille, taille, null);
                break;
            case 1: // Pierre
                g.drawImage(imagePierre, drawX-taille/2, drawY-taille/2, taille, taille, null);
                break;
            case 2: // Fer
                g.drawImage(imageFer, drawX-taille/2, drawY-taille/2, taille, taille, null);
                break;
            case 3: // Or
                g.drawImage(imageOr, drawX-taille/2, drawY-taille/2, taille, taille, null);
                break;
        }
    }
}