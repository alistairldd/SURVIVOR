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


        // On dessine la map
        // Positionné à (0,0) et de taille globale fixée par le modèle (3000x3000)
        g2d.drawImage(imageMap, 0, 0, LARGEUR_MAP, HAUTEUR_MAP, null);

        // On crée une couche pour assombrir l'image
        g2d.setColor(new Color(0, 0, 0,150));
        g2d.fillRect(0, 0, LARGEUR_MAP, HAUTEUR_MAP);

        // (Optionnel) Ajout d'une bordure rouge pour bien voir les limites de la map
        // Aide le joueur à comprendre pourquoi il ne peut pas aller plus loin (collision dans Joueur.deplaceX/Y)
        g2d.setColor(Color.RED);
        g2d.drawRect(0, 0, LARGEUR_MAP, HAUTEUR_MAP);
        // On dessiner des arbres sur les bordures de la carte pour renforcer l'immersion et masquer les limites
        // (Note : Ces arbres sont purement décoratifs et n'ont pas de collision, ils sont dessinés par-dessus la bordure rouge pour la masquer visuellement)
        int deb = -900; // Décalage pour commencer à dessiner les arbres avant le bord de la carte
        int fin = LARGEUR_MAP + 900; // Décalage pour continuer à dessiner les arbres après le bord de la carte
        int haut = -600;
        int bas = HAUTEUR_MAP - ( ARBRE1.getHeight() / 2 ) ;
            // Tire un nombre aléatoire entre 1 et 4 pour choisir aléatoirement entre 4 types d'arbres différents (ARBRE1, ARBRE2, ARBRE3, ARBRE4)


            for (int h = haut; h < 0; h += ARBRE1.getHeight()) {
                for (int i = deb; i < fin; i += ARBRE1.getWidth()) {
                    int i2 = i + ARBRE1.getWidth() / 2; // Décalage pour centrer les arbres sur les bords
                    g2d.drawImage(ARBRE1, i, h, ARBRE1.getWidth(), ARBRE1.getHeight(), null); // Arbres en haut
                    g2d.drawImage(ARBRE2, i2, h, ARBRE2.getWidth(), ARBRE2.getHeight(), null); // Arbres en haut

                    g2d.drawImage(ARBRE1, i, bas, ARBRE1.getWidth(), ARBRE1.getHeight(), null); // Arbres en bas
                    g2d.drawImage(ARBRE2, i2, bas, ARBRE2.getWidth(), ARBRE2.getHeight(), null); // Arbres en bas
                }
            }

        for (int i = 0; i < HAUTEUR_MAP; i += 100) {
            g2d.drawImage(ARBRE1, 10 - 50, 10 + i, 50, 50, null); // Arbres à gauche
            g2d.drawImage(ARBRE1, 10 + LARGEUR_MAP, 10 + i, 50, 50, null); // Arbres à droite
        }

    }

}