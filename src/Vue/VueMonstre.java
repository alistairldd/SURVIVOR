package Vue;

import Modele.Monstres.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import static Modele.Constantes.*;

/**
 * Responsable du dessin d'un ennemi (Monstre) sur la carte.
 * Affiche son avatar physique (un carré rouge) et un indicateur visuel
 * de sa zone de menace (portée d'attaque).
 */
public class VueMonstre {


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
                case Ogre ogre -> {
                    // Calcul la taille des ogres proportionnellement à la taille définie (en hauteur) pour les ogres (TAILLE_OGRE) en fonction de la taille source des images

                    int hpOGM = largeurProportionnelleOgre(IMAGE_OGRE_GM);
                    int hpODM = largeurProportionnelleOgre(IMAGE_OGRE_DM);
                    int hpOG = largeurProportionnelleOgre(IMAGE_OGRE_G);
                    int hpOD = largeurProportionnelleOgre(IMAGE_OGRE_D);

                    if (ogre.regardeGauche() && ogre.isMarche()){

                        if (ogre.getAnimationMarche()) { // Regarde à gauche et en marche
                            g2d.drawImage(IMAGE_OGRE_GM, posX - hpOGM / 2, posY - hpOGM / 2, hpOGM, hpOGM, null);
                        } else
                            g2d.drawImage(IMAGE_OGRE_G, posX - hpOG / 2, posY - hpOG / 2, hpOG, hpOG, null);
                    } else if (ogre.regardeGauche() && !ogre.isMarche()){ // Attaque vers la gauche
                        if (ogre.getAnimationAttaque()) {
                            g2d.drawImage(IMAGE_OGRE_ATTAQUE_GH, posX - hpOGM / 2, posY - hpOGM / 2, hpOGM, hpOGM, null);
                        } else {
                            g2d.drawImage(IMAGE_OGRE_ATTAQUE_G, posX - hpOGM / 2, posY - hpOGM / 2, hpOGM, hpOGM, null);
                        }
                    }
                    else if (!ogre.regardeGauche() && ogre.isMarche()){
                        if (ogre.getAnimationMarche()) { // Regarde vers la droite et marche
                            g2d.drawImage(IMAGE_OGRE_DM, posX - hpODM / 2, posY - hpODM / 2, hpODM, hpODM, null);
                        } else
                            g2d.drawImage(IMAGE_OGRE_D, posX - hpOD / 2, posY - hpOD / 2, hpOD, hpOD, null);
                    }
                    else if (!ogre.regardeGauche() && !ogre.isMarche()){ // Attaque vers la droite
                        if (ogre.getAnimationAttaque()) {
                            g2d.drawImage(IMAGE_OGRE_ATTAQUE_DH, posX - hpOGM / 2, posY - hpOGM / 2, hpOGM, hpOGM, null);
                        } else {
                            g2d.drawImage(IMAGE_OGRE_ATTAQUE_D, posX - hpOGM / 2, posY - hpOGM / 2, hpOGM, hpOGM, null);
                        }
                    }

                }
                case Gobelin gobelin -> {
                    // Calcul la taille des ogres proportionnellement à la taille définie (en hauteur) pour les ogres (TAILLE_OGRE) en fonction de la taille source des images

                    int hpOGM = largeurProportionnelleGobelin(IMAGE_GOB_GM);
                    int hpODM = largeurProportionnelleGobelin(IMAGE_GOB_DM);
                    int hpOG = largeurProportionnelleGobelin(IMAGE_GOB_G);
                    int hpOD = largeurProportionnelleGobelin(IMAGE_GOB_D);

                    if (gobelin.regardeGauche() && gobelin.isMarche()){

                        if (gobelin.getAnimationMarche()) { // Regarde à gauche et en marche
                            g2d.drawImage(IMAGE_GOB_GM, posX - hpOGM / 2, posY - hpOGM / 2, hpOGM, hpOGM, null);
                        } else
                            g2d.drawImage(IMAGE_GOB_G, posX - hpOG / 2, posY - hpOG / 2, hpOG, hpOG, null);
                    } else if (gobelin.regardeGauche() && !gobelin.isMarche()){ // Attaque vers la gauche
                        if (gobelin.getAnimationAttaque()) {
                            g2d.drawImage(IMAGE_GOB_ATTAQUE_GH, posX - hpOGM / 2, posY - hpOGM / 2, hpOGM, hpOGM, null);
                        } else {
                            g2d.drawImage(IMAGE_GOB_ATTAQUE_G, posX - hpOGM / 2, posY - hpOGM / 2, hpOGM, hpOGM, null);
                        }
                    }
                    else if (!gobelin.regardeGauche() && gobelin.isMarche()){
                        if (gobelin.getAnimationMarche()) { // Regarde vers la droite et marche
                            g2d.drawImage(IMAGE_GOB_DM, posX - hpODM / 2, posY - hpODM / 2, hpODM, hpODM, null);
                        } else
                            g2d.drawImage(IMAGE_GOB_D, posX - hpOD / 2, posY - hpOD / 2, hpOD, hpOD, null);
                    }
                    else if (!gobelin.regardeGauche() && !gobelin.isMarche()){ // Attaque vers la droitee
                        if (gobelin.getAnimationAttaque()) {
                            g2d.drawImage(IMAGE_GOB_ATTAQUE_DH, posX - hpOGM / 2, posY - hpOGM / 2, hpOGM, hpOGM, null);
                        } else {
                            g2d.drawImage(IMAGE_GOB_ATTAQUE_D, posX - hpOGM / 2, posY - hpOGM / 2, hpOGM, hpOGM, null);
                        }
                    }

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

    private int largeurProportionnelleOgre(BufferedImage image) {
        return (int) (TAILLE_OGRE * ((double) image.getWidth() / image.getHeight()));
    }

    private int largeurProportionnelleGobelin(BufferedImage image) {
        return (int) (TAILLE_GOBELIN * ((double) image.getWidth() / image.getHeight()));
    }

}