package Vue;

import Modele.*;
import java.awt.*;

/**
 * Responsable du rendu de la toile de fond (le sol du monde).
 * Dessine les limites de l'arène de jeu.
 */
public class VueCarte {

    private final Modele modele;
    private final Joueur joueur;

    public VueCarte(Modele modele) {
        this.modele = modele;
        this.joueur = modele.getJoueur();
    }

    /**
     * Dessine le grand fond vert représentant l'herbe/le terrain,
     * ainsi qu'une ligne rouge matérialisant les limites physiques du monde.
     * @param g Le contexte graphique.
     */
    protected void dessiner(Graphics g) {
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
        g.setColor(new Color(0, 101, 0));

        // On dessine le rectangle vert qui représente le monde entier
        // Positionné à un offset fixe de (10, 10) et de taille globale fixée par le modèle (3000x3000)
        g.fillRect(10, 10, Map.LARGEUR_MAP, Map.HAUTEUR_MAP);

        // (Optionnel) Ajout d'une bordure rouge pour bien voir les limites de la map
        // Aide le joueur à comprendre pourquoi il ne peut pas aller plus loin (collision dans Joueur.deplaceX/Y)
        g.setColor(Color.RED);
        g.drawRect(10, 10, Map.LARGEUR_MAP, Map.HAUTEUR_MAP);
    }

}