package Vue;

import Modele.Monstres.Monstre;
import Modele.Monstres.Slime;
import Modele.Monstres.SlimeMutant;

import java.awt.*;

import static Modele.Constantes.*;

/**
 * Responsable du dessin d'un ennemi (Monstre) sur la carte.
 * Affiche son avatar physique (un carré rouge) et un indicateur visuel
 * de sa zone de menace (portée d'attaque).
 */
public class VueMonstre {


    // Constructeur de la classe VueMonstre
    public VueMonstre() {

    }

    /**
     * Dessine le monstre et son cercle d'aggro.
     * @param g Contexte graphique (Caméra déjà appliquée).
     * @param monstre Les données de l'ennemi à afficher (position, portée).
     * @param posX Position X (monde ou minimap).
     * @param posY Position Y (monde ou minimap).
     * @param minimap Indique si on doit utiliser le format réduit.
     */
    public void dessiner(Graphics g, Monstre monstre, int posX, int posY, boolean minimap) {


        // Crée un calque indépendant pour manipuler la transparence sans casser les autres dessins
        Graphics2D g2d = (Graphics2D) g.create();
        // Lisse les bords (utile surtout pour le cercle de portée)
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (minimap) {
            // Sur la minimap, on garde souvent un point de couleur (plus lisible qu'un petit sprite)
            g2d.setColor(Color.RED);
            g2d.fillRect(posX - TAILLE_MINIMAP_MONSTRE / 2, posY - TAILLE_MINIMAP_MONSTRE / 2, TAILLE_MINIMAP_MONSTRE, TAILLE_MINIMAP_MONSTRE);
        } else {
            switch (monstre) {
                case Slime slime -> {


                    // VUE PRINCIPALE : On dessine le Slime
                    Image imgSlime = slime.getImage(); // On utilise le getter que nous avons créé
                    // On centre l'image par rapport à posX et posY
                    int hauteurProp = (int) (TAILLE_MONSTRE * ((double) HAUTEUR_SLIME_SOURCE / LARGEUR_SLIME_SOURCE));

                    // Les variables pour l'animation
                    double anim = slime.getAnimation();
                    int decalageY = (int) (-10 * Math.sin(anim)); // Décalage vertical pour l'affichage de l'image
                    int etirement = (int) (10 * Math.sin(anim)); // Étirement vertical pour tirer l'image vers le bas

                    g2d.drawImage(imgSlime,
                            posX - TAILLE_MONSTRE / 2,
                            posY - hauteurProp / 2 + decalageY,
                            TAILLE_MONSTRE,
                            hauteurProp + etirement,
                            null);
                }
                case SlimeMutant slimeMutant -> {

                    // VUE PRINCIPALE : On dessine le Slime
                    Image imgSlime = slimeMutant.getImage(); // On utilise le getter que nous avons créé

                    // On centre l'image par rapport à posX et posY
                    int hauteurProp = (int) (TAILLE_MONSTRE * ((double) HAUTEUR_SLIME_MUTANT_SOURCE / LARGEUR_SLIME_MUTANT_SOURCE));

                    // Les variables pour l'animation
                    double anim = slimeMutant.getAnimation();
                    int decalageY = (int) (-10 * Math.sin(anim)); // Décalage vertical pour l'affichage de l'image
                    int etirement = (int) (10 * Math.sin(anim)); // Étirement vertical pour tirer l'image vers le bas

                    g2d.drawImage(imgSlime,
                            posX - TAILLE_MONSTRE / 2,
                            posY - hauteurProp / 2 + decalageY,
                            TAILLE_MONSTRE,
                            hauteurProp + etirement,
                            null);

                }
                default -> {
                    // Sécurité : si l'image n'est pas chargée, on met le carré rouge par défaut
                    g2d.setColor(Color.RED);
                    g2d.fillRect(posX - TAILLE_MONSTRE / 2, posY - TAILLE_MONSTRE / 2, TAILLE_MONSTRE, TAILLE_MONSTRE);
                }
            }

        }

        // Dessin du cercle de portée du monstre
        // On ne surcharge pas la minimap avec les cercles de portée, on ne les dessine que sur la vue principale
        if (!minimap) {
            // Prépare une couleur rouge avec une base de transparence manuelle (bien que l'AlphaComposite gère déjà l'opacité)
            g2d.setColor(new Color(255, 0, 0, 50)); // Rouge transparent
            // Lit la portée d'attaque de ce monstre spécifique
            int portee = (int) monstre.getPortee();

            // Applique une opacité très faible (20%) pour le fond de la zone
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));

            // on dessine le cercle directement autour de ce point (x, y).
            // - portee permet de décaler l'origine du cercle en haut à gauche pour que le centre du cercle tombe pile sur le monstre
            g2d.fillOval(posX - portee, posY - portee, portee * 2, portee * 2);

            // Remonte l'opacité à 60% pour bien marquer la bordure extérieure de la zone
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
            // Trait d'une épaisseur de 2 pixels
            g2d.setStroke(new BasicStroke(2));
            // Dessine juste le contour
            g2d.drawOval(posX - portee, posY - portee, portee * 2, portee * 2);

            // Restaure l'opacité maximale (100%) par propreté avant de détruire le contexte
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }

        // (Note: g2d.dispose() serait recommandé ici en fin de méthode pour libérer la mémoire du calque créé).
    }
}