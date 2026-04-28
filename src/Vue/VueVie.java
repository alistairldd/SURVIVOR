package Vue;

import Modele.Batiments.*;
import Modele.Modele;
import java.awt.*;

import java.io.IOException;

import Modele.*;
import Modele.Monstres.Monstre;
import Modele.Monstres.Ogre;
import Modele.Monstres.Slime;
import Modele.Monstres.SlimeMutant;

import static Modele.Constantes.*;

public class VueVie {

    /** ---------- [Propriétés] ---------- **/

    private Modele modele;

    /** ---------- [Constructeurs] ---------- **/

    /**
     * Initialise la vue du panneau de vie détaillé.
     *
     * @param modele - Modèle donnant accès à la cible actuellement affichée
     */
    public VueVie(Modele modele) {
        this.modele = modele;
    }

    /** ---------- [Méthodes Publiques - Rendu] ---------- **/

    /**
     * Dessine les informations de vie et l'illustration de l'entité actuellement sélectionnée.
     * La méthode retourne la dernière position verticale atteinte afin de permettre
     * l'empilement propre d'autres composants dans le même panneau.
     *
     * @param g - Contexte graphique du panneau
     * @param yDebut - Position Y de départ
     * @param width - Largeur disponible pour la barre de vie
     * @param height - Hauteur de la barre de vie
     * @return coordonnée Y finale après rendu du composant
     * @throws IOException conservé tel quel par compatibilité de signature
     */
    public int dessiner(Graphics g, int yDebut, int width, int height) throws IOException {
        Graphics2D g2d = (Graphics2D) g;
        Localisable localisable = modele.getCibleAffichage();

        int yCourant = yDebut;
        int tailleImage = 0;

        if (localisable != null) {

            String nom = localisable.getNom();
            int vie = localisable.getHp();
            int vieMax = localisable.getMaxHp();
            Color color;
            Image img = null;

            color = Color.GRAY;

            // La couleur et l'illustration sont déterminées à partir du type réel de la cible affichée.
            switch (localisable) {
                case Joueur joueur -> {
                    color = Color.GREEN;
                    if (joueur.getArmurePrincipale() != null || joueur.getArmureSecondaire() != null) {
                        img = IMAGE_JOUEUR_ARMURE;
                    } else {
                        img = IMAGE_JOUEUR;
                    }
                }

                case Tower ignored -> {
                    color = Color.BLUE;
                    img = IMAGE_TOUR;
                }

                case Mine ignored -> {
                    color = Color.YELLOW;
                    img = IMAGE_MINE;
                }

                case HQ ignored -> {
                    color = Color.MAGENTA;
                    img = IMAGE_HQ;
                }

                case TenteDeSoin ignored -> {
                    color = Color.PINK;
                    img = IMAGE_TENTE;
                }

                case Abatis ignored -> {
                    color = Color.ORANGE;
                    img = IMAGE_ABATIS_1;
                }

                case Mortier ignored -> {
                    color = Color.CYAN;
                    img = IMAGE_MORTIER;
                }

                case Monstre m -> {
                    color = Color.RED;
                    img = m.getImage();
                }

                default -> {
                }
            }

            int xOffset = 20;

            // Le fond noir crée un conteneur visuel simple pour rendre la jauge lisible quel que soit le thème du panneau.
            g2d.setColor(Color.BLACK);
            g2d.fillRect(xOffset - 5, yCourant - 5, width + 10, height + 10);

            int filledWidth = (int) ((double) vie / vieMax * width);
            g2d.setColor(color);
            g2d.fillRect(xOffset, yCourant, filledWidth, height);

            yCourant += height + 25;

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            g2d.drawString(nom + " : " + vie + " / " + vieMax + " PV", xOffset, yCourant);

            yCourant += 20;

            tailleImage = 150;
            int hauteurProp;

            if (img != null) {
                // Certains sprites n'ont pas les mêmes proportions source, d'où cet ajustement spécifique.
                if (nom.equals("Slime")) {
                    hauteurProp = (int) (tailleImage * ((double) HAUTEUR_SLIME_SOURCE / LARGEUR_SLIME_SOURCE));
                } else {
                    hauteurProp = (int) (tailleImage * ((double) HAUTEUR_JOUEUR_SOURCE / LARGEUR_JOUEUR_SOURCE));
                }

                int offsetCentrageY = (tailleImage - hauteurProp) / 2;

                g2d.setColor(new Color(0, 0, 0, 20));
                g2d.fillRect(xOffset, yCourant, tailleImage, tailleImage);

                // L'image est insérée dans un cadre fixe pour stabiliser la mise en page du panneau.
                g2d.drawImage(img, xOffset, yCourant + offsetCentrageY, tailleImage, hauteurProp, null);

                g2d.setColor(Color.BLACK);
                g2d.drawRect(xOffset, yCourant, tailleImage, tailleImage);
            } else {
                // Placeholder discret lorsque l'entité ne possède pas d'illustration dédiée.
                g2d.setColor(new Color(50, 50, 50, 150));
                g2d.fillRect(xOffset, yCourant, tailleImage, tailleImage);

                g2d.setColor(Color.WHITE);
                g2d.drawRect(xOffset, yCourant, tailleImage, tailleImage);

                g2d.setFont(new Font("Arial", Font.ITALIC, 12));
                g2d.drawString("[Image " + nom + "]", xOffset + 10, yCourant + (tailleImage / 2));
            }

            yCourant += tailleImage + 30;
        }

        return yCourant;
    }
}