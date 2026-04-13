package Vue;

import Modele.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import static Modele.Constantes.*;
/**
 * Responsable du rendu de la toile de fond (le sol du monde).
 * Dessine les limites de l'arène de jeu.
 */
public class VueCarte {
    BufferedImage imageMap;
    private final Joueur joueur;

    public VueCarte(Modele modele) {
        this.joueur = modele.getJoueur();
        try {
            imageMap = ImageIO.read(new File("src/images/ressources/map.png"));
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'image de la carte : " + e.getMessage());
        }
    }

    /**
     * Dessine le grand fond vert représentant l'herbe/le terrain,
     * ainsi qu'une ligne rouge matérialisant les limites physiques du monde.
     * @param g Le contexte graphique.
     */
    protected void dessiner(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        // Définition de l'origine absolue de la carte dans l'espace de dessin
        // La carte fait 2000x2000. (Note: 3000x3000 selon Map.java)
        // Si le centre du monde est (0,0), le coin haut-gauche de la carte est à (-1000, -1000).
        int coinHautGaucheMapX = 0;
        int coinHautGaucheMapY = 0;

        // Variables calculées originellement pour une logique de caméra locale
        // (Note architecturale : L'effet de caméra est finalement géré de façon globale via g2d.translate dans Vue.java,
        // ces variables "x" et "y" ne sont donc pas utilisées directement ici pour le dessin).
        // On applique la même logique de caméra que pour les ressources :
        // Centre Ecran + Position Objet - Position Joueur
        double x = coinHautGaucheMapX - joueur.getX();
        double y = coinHautGaucheMapY - joueur.getY();

        // Définit une couleur verte sombre pour le sol
        g2d.setColor(new Color(0, 101, 0));

        // On dessine la map
        // Positionné à un offset fixe de (10, 10) et de taille globale fixée par le modèle (3000x3000)
        g2d.drawImage(imageMap, 10, 10, LARGEUR_MAP, HAUTEUR_MAP, null);

        // On crée une couche pour assombrir l'image
        g2d.setColor(new Color(0, 0, 0,150));
        g2d.fillRect(10, 10, LARGEUR_MAP, HAUTEUR_MAP);

        // (Optionnel) Ajout d'une bordure rouge pour bien voir les limites de la map
        // Aide le joueur à comprendre pourquoi il ne peut pas aller plus loin (collision dans Joueur.deplaceX/Y)
        g2d.setColor(Color.RED);
        g2d.drawRect(10, 10, LARGEUR_MAP, HAUTEUR_MAP);
    }

}