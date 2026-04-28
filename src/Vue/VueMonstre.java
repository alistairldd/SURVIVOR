package Vue;

import Modele.Monstres.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import static Modele.Constantes.*;

/**
 * Responsable du rendu visuel des monstres.
 * Cette vue sélectionne le sprite approprié selon le type, l'orientation
 * et l'état d'animation, puis dessine la zone de menace dans la vue principale.
 */
public class VueMonstre {

    /** ---------- [Méthodes Publiques - Rendu] ---------- **/

    /**
     * Dessine un monstre soit dans la vue principale, soit dans la minimap.
     * Le rendu principal affiche le sprite animé et sa portée d'attaque,
     * tandis que la minimap privilégie un repère compact et lisible.
     *
     * @param g - Contexte graphique courant
     * @param monstre - Entité à afficher
     * @param posX - Position X de rendu dans le repère cible
     * @param posY - Position Y de rendu dans le repère cible
     * @param minimap - Indique si le rendu concerne la minimap
     */
    public void dessiner(Graphics g, Monstre monstre, int posX, int posY, boolean minimap) {

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (minimap) {
            // Sur la minimap, un repère simple reste plus lisible qu'un sprite réduit.
            g2d.setColor(Color.RED);
            g2d.fillRect(posX - TAILLE_MINIMAP_MONSTRE / 2, posY - TAILLE_MINIMAP_MONSTRE / 2, TAILLE_MINIMAP_MONSTRE, TAILLE_MINIMAP_MONSTRE);
        } else {
            switch (monstre) {
                case Slime slime -> {

                    Image imgSlime = slime.getImage();
                    int hauteurProp = (int) (TAILLE_MONSTRE * ((double) HAUTEUR_SLIME_SOURCE / LARGEUR_SLIME_SOURCE));

                    // Le slime repose sur une déformation simple pour simuler un mouvement organique.
                    double anim = slime.getAnimation();
                    int decalageY = (int) (-10 * Math.sin(anim));
                    int etirement = (int) (10 * Math.sin(anim));

                    g2d.drawImage(
                            imgSlime,
                            posX - TAILLE_MONSTRE / 2,
                            posY - hauteurProp / 2 + decalageY,
                            TAILLE_MONSTRE,
                            hauteurProp + etirement,
                            null
                    );
                }

                case SlimeMutant slimeMutant -> {

                    Image imgSlime = slimeMutant.getImage();
                    int hauteurProp = (int) (TAILLE_MONSTRE * ((double) HAUTEUR_SLIME_MUTANT_SOURCE / LARGEUR_SLIME_MUTANT_SOURCE));

                    // Le mutant réutilise la même logique de squash/stretch avec ses propres proportions source.
                    double anim = slimeMutant.getAnimation();
                    int decalageY = (int) (-10 * Math.sin(anim));
                    int etirement = (int) (10 * Math.sin(anim));

                    g2d.drawImage(
                            imgSlime,
                            posX - TAILLE_MONSTRE / 2,
                            posY - hauteurProp / 2 + decalageY,
                            TAILLE_MONSTRE,
                            hauteurProp + etirement,
                            null
                    );
                }

                case Ogre ogre -> {

                    int hpOGM = largeurProportionnelleOgre(IMAGE_OGRE_GM);
                    int hpODM = largeurProportionnelleOgre(IMAGE_OGRE_DM);
                    int hpOG = largeurProportionnelleOgre(IMAGE_OGRE_G);
                    int hpOD = largeurProportionnelleOgre(IMAGE_OGRE_D);

                    if (ogre.regardeGauche() && ogre.isMarche()) {

                        if (ogre.getAnimationMarche()) {
                            g2d.drawImage(IMAGE_OGRE_GM, posX - hpOGM / 2, posY - hpOGM / 2, hpOGM, hpOGM, null);
                        } else {
                            g2d.drawImage(IMAGE_OGRE_G, posX - hpOG / 2, posY - hpOG / 2, hpOG, hpOG, null);
                        }

                    } else if (ogre.regardeGauche() && !ogre.isMarche()) {

                        if (ogre.getAnimationAttaque()) {
                            g2d.drawImage(IMAGE_OGRE_ATTAQUE_GH, posX - hpOGM / 2, posY - hpOGM / 2, hpOGM, hpOGM, null);
                        } else {
                            g2d.drawImage(IMAGE_OGRE_ATTAQUE_G, posX - hpOGM / 2, posY - hpOGM / 2, hpOGM, hpOGM, null);
                        }

                    } else if (!ogre.regardeGauche() && ogre.isMarche()) {

                        if (ogre.getAnimationMarche()) {
                            g2d.drawImage(IMAGE_OGRE_DM, posX - hpODM / 2, posY - hpODM / 2, hpODM, hpODM, null);
                        } else {
                            g2d.drawImage(IMAGE_OGRE_D, posX - hpOD / 2, posY - hpOD / 2, hpOD, hpOD, null);
                        }

                    } else if (!ogre.regardeGauche() && !ogre.isMarche()) {

                        if (ogre.getAnimationAttaque()) {
                            g2d.drawImage(IMAGE_OGRE_ATTAQUE_DH, posX - hpOGM / 2, posY - hpOGM / 2, hpOGM, hpOGM, null);
                        } else {
                            g2d.drawImage(IMAGE_OGRE_ATTAQUE_D, posX - hpOGM / 2, posY - hpOGM / 2, hpOGM, hpOGM, null);
                        }
                    }
                }

                case Gobelin gobelin -> {

                    int hpOGM = largeurProportionnelleGobelin(IMAGE_GOB_GM);
                    int hpODM = largeurProportionnelleGobelin(IMAGE_GOB_DM);
                    int hpOG = largeurProportionnelleGobelin(IMAGE_GOB_G);
                    int hpOD = largeurProportionnelleGobelin(IMAGE_GOB_D);

                    if (gobelin.regardeGauche() && gobelin.isMarche()) {

                        if (gobelin.getAnimationMarche()) {
                            g2d.drawImage(IMAGE_GOB_GM, posX - hpOGM / 2, posY - hpOGM / 2, hpOGM, hpOGM, null);
                        } else {
                            g2d.drawImage(IMAGE_GOB_G, posX - hpOG / 2, posY - hpOG / 2, hpOG, hpOG, null);
                        }

                    } else if (gobelin.regardeGauche() && !gobelin.isMarche()) {

                        if (gobelin.getAnimationAttaque()) {
                            g2d.drawImage(IMAGE_GOB_ATTAQUE_GH, posX - hpOGM / 2, posY - hpOGM / 2, hpOGM, hpOGM, null);
                        } else {
                            g2d.drawImage(IMAGE_GOB_ATTAQUE_G, posX - hpOGM / 2, posY - hpOGM / 2, hpOGM, hpOGM, null);
                        }

                    } else if (!gobelin.regardeGauche() && gobelin.isMarche()) {

                        if (gobelin.getAnimationMarche()) {
                            g2d.drawImage(IMAGE_GOB_DM, posX - hpODM / 2, posY - hpODM / 2, hpODM, hpODM, null);
                        } else {
                            g2d.drawImage(IMAGE_GOB_D, posX - hpOD / 2, posY - hpOD / 2, hpOD, hpOD, null);
                        }

                    } else if (!gobelin.regardeGauche() && !gobelin.isMarche()) {

                        if (gobelin.getAnimationAttaque()) {
                            g2d.drawImage(IMAGE_GOB_ATTAQUE_DH, posX - hpOGM / 2, posY - hpOGM / 2, hpOGM, hpOGM, null);
                        } else {
                            g2d.drawImage(IMAGE_GOB_ATTAQUE_D, posX - hpOGM / 2, posY - hpOGM / 2, hpOGM, hpOGM, null);
                        }
                    }
                }

                default -> {
                    // Rendu de secours si aucun sprite spécialisé n'est disponible.
                    g2d.setColor(Color.RED);
                    g2d.fillRect(posX - TAILLE_MONSTRE / 2, posY - TAILLE_MONSTRE / 2, TAILLE_MONSTRE, TAILLE_MONSTRE);
                }
            }

            // La portée n'est affichée que dans la vue principale pour éviter de surcharger la minimap.
            if (!minimap) {
                g2d.setColor(new Color(255, 0, 0, 50));
                int portee = (int) monstre.getPortee();

                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
                g2d.fillOval(posX - portee, posY - portee, portee * 2, portee * 2);

                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawOval(posX - portee, posY - portee, portee * 2, portee * 2);

                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            }
        }

        g2d.dispose();
    }

    /** ---------- [Méthodes Privées - Utilitaires de proportions] ---------- **/

    /**
     * Calcule la largeur proportionnelle d'un sprite d'ogre à partir de sa hauteur cible.
     *
     * @param image - Image source à convertir
     * @return largeur correspondante conservant le ratio d'origine
     */
    private int largeurProportionnelleOgre(BufferedImage image) {
        return (int) (TAILLE_OGRE * ((double) image.getWidth() / image.getHeight()));
    }

    /**
     * Calcule la largeur proportionnelle d'un sprite de gobelin à partir de sa hauteur cible.
     *
     * @param image - Image source à convertir
     * @return largeur correspondante conservant le ratio d'origine
     */
    private int largeurProportionnelleGobelin(BufferedImage image) {
        return (int) (TAILLE_GOBELIN * ((double) image.getWidth() / image.getHeight()));
    }
}