package Vue;

import Modele.Ressource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Utilitaires de rendu des ressources présentes au sol.
 * Cette vue traduit les types métiers de ressources en représentations
 * graphiques homogènes dans le monde principal et dans la minimap.
 */
public class VueRessource {

    /** ---------- [Propriétés - Sprites] ---------- **/

    private BufferedImage imageFer;
    private BufferedImage imageOr;
    private BufferedImage imagePierre;
    private BufferedImage imageBois;

    /** ---------- [Constructeurs] ---------- **/

    /**
     * Charge les images associées aux différents types de ressources.
     */
    public VueRessource() {
        try {
            this.imageFer = ImageIO.read(new File("src/images/ressources/Fer.png"));
            this.imageOr = ImageIO.read(new File("src/images/ressources/Or.png"));
            this.imagePierre = ImageIO.read(new File("src/images/ressources/Pierre.png"));
            this.imageBois = ImageIO.read(new File("src/images/ressources/Bois.png"));

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur : Impossible de charger l'image.");
        }
    }

    /** ---------- [Méthodes Publiques - Rendu] ---------- **/

    /**
     * Dessine une ressource à la position demandée.
     * La méthode adapte sa taille selon le contexte d'affichage afin de conserver
     * un bon compromis entre lisibilité dans le monde et densité sur la minimap.
     *
     * @param g - Contexte graphique cible
     * @param r - Ressource à afficher
     * @param x - Coordonnée X de rendu
     * @param y - Coordonnée Y de rendu
     * @param minimap - Indique si le rendu doit être compacté pour la minimap
     */
    public void dessinerRessource(Graphics g, Ressource r, double x, double y, boolean minimap) {
        int taille = 50;

        if (minimap) {
            taille = 16;
        }

        int type = r.getType();
        int drawX = (int) x;
        int drawY = (int) y;

        // Le rendu est centré sur la position logique de la ressource pour rester cohérent avec les autres entités.
        switch (type) {
            case 0:
                g.drawImage(imageBois, drawX - taille / 2, drawY - taille / 2, taille, taille, null);
                break;
            case 1:
                g.drawImage(imagePierre, drawX - taille / 2, drawY - taille / 2, taille, taille, null);
                break;
            case 2:
                g.drawImage(imageFer, drawX - taille / 2, drawY - taille / 2, taille, taille, null);
                break;
            case 3:
                g.drawImage(imageOr, drawX - taille / 2, drawY - taille / 2, taille, taille, null);
                break;
        }
    }
}