package Vue;

import Modele.Batiments.HQ;
import Modele.Batiments.Mine;
import Modele.Batiments.TenteDeSoin;
import Modele.Batiments.Tower;
import Modele.Modele;
import java.awt.*;

import java.io.IOException;

import Modele.*;
import Modele.Monstres.Monstre;
import Modele.Monstres.Ogre;
import Modele.Monstres.Slime;
import Modele.Monstres.SlimeMutant;

import static Modele.Constantes.*;

public class    VueVie {

    private Modele modele;

    public VueVie(Modele modele) {
        this.modele = modele;
    }

    /**
     * Dessine la barre de vie et l'avatar de l'entité.
     * @return La coordonnée Y finale après avoir dessiné ce composant (pour empiler la suite).
     */
    public int dessiner(Graphics g, int yDebut, int width, int height) throws IOException {
        Graphics2D g2d = (Graphics2D) g;
        Localisable localisable = modele.getCibleAffichage();

        // Curseur vertical qui mémorisera notre progression
        int yCourant = yDebut;

        int tailleImage = 0;
        if (localisable != null) {

            String nom = localisable.getNom();
            int vie = localisable.getHp();
            int vieMax = localisable.getMaxHp();
            Color color;
            Image img = null;

            color = Color.GRAY; // Couleur par défaut si le type n'est pas reconnu
            switch (localisable) {
                case Joueur ignored -> {
                    color = Color.GREEN;
                    img = IMAGE_JOUEUR;
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
                case Monstre m -> {
                    color = Color.RED;
                    img = m.getImage();
                }
                default -> {
                }
            }

            // 1. Dessin de la barre de vie
            g2d.setColor(Color.BLACK);
            g2d.fillRect(xOffset - 5, yCourant - 5, width + 10, height + 10);

            int filledWidth = (int) ((double) vie / vieMax * width);
            g2d.setColor(color);
            g2d.fillRect(xOffset, yCourant, filledWidth, height);

            yCourant += height + 25; // On descend sous la barre

            // Affiche le nom et les points de vie
            g2d.setColor(Color.WHITE); // Toujours blanc sur les pages du CardLayout (fond sombre global prévu)
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            g2d.drawString(nom + " : " + vie + " / " + vieMax + " PV", xOffset, yCourant);

            yCourant += 20; // On descend pour l'image
            // Dessin de l'image de l'entité
            tailleImage = 150;
            int hauteurProp;
            if (img != null) {
                if (nom.equals("Slime")) {
                    hauteurProp = (int) (tailleImage * ((double) HAUTEUR_SLIME_SOURCE / LARGEUR_SLIME_SOURCE));
                } else {
                    hauteurProp = (int) (tailleImage * ((double) HAUTEUR_JOUEUR_SOURCE / LARGEUR_JOUEUR_SOURCE));
                }
                int offsetCentrageY = (tailleImage - hauteurProp) / 2;

                g2d.setColor(Color.DARK_GRAY);
                g2d.fillRect(xOffset, yCourant, tailleImage, tailleImage);
                // Dessine l'image redimensionnée pour remplir le cadre de 100x100
                g2d.drawImage(img, xOffset, yCourant + offsetCentrageY, tailleImage, hauteurProp, null);

                g2d.setColor(Color.WHITE);
                g2d.drawRect(xOffset, yCourant, tailleImage, tailleImage);
                } else {

                g2d.setColor(new Color(50, 50, 50, 150)); // Fond gris transparent pour la boîte d'image
                g2d.fillRect(xOffset, yCourant, tailleImage, tailleImage);
                g2d.setColor(Color.WHITE);
                g2d.drawRect(xOffset, yCourant, tailleImage, tailleImage);

                g2d.setFont(new Font("Arial", Font.ITALIC, 12));
                g2d.drawString("[Image " + nom + "]", xOffset + 10, yCourant + (tailleImage / 2));
            }

            yCourant += tailleImage + 30; // On ajoute la taille de l'image plus une marge
        }

        // On retourne la position Y exacte où l'on s'est arrêté
        return yCourant;
    }
}