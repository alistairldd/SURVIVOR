package Vue;

import Modele.Modele;
import java.awt.*;

import java.io.IOException;

import Modele.*;

import javax.imageio.ImageIO;

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

            color = Color.RED;
            switch (localisable) {
                case Joueur ignored -> {
                    color = Color.GREEN;
                    img = IMAGE_JOUEUR;
                }
                case Tower ignored -> color = Color.BLUE;
                case Slime ignored -> {
                    Monstre m = (Monstre) localisable;
                    img = m.getImage(); // Ton getter qui renvoie le slime aléatoire
                }
                case SlimeMutant ignored -> {
                    Monstre m = (Monstre) localisable;
                    img = m.getImage(); // Ton getter qui renvoie le slime mutant
                }
                default -> {
                    color = Color.GRAY;
                    // Optionnel : on peux aussi essayer de charger une image générique pour les autres entités
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
            // 2. Dessin de l'image de l'entité
            tailleImage = 100;
            int hauteurProp;
            if (img != null) {
                if (nom.equals("Slime")) {
                    hauteurProp = (int) (tailleImage * ((double) HAUTEUR_SLIME_SOURCE / LARGEUR_SLIME_SOURCE));
                } else {
                    hauteurProp = (int) (tailleImage * ((double) HAUTEUR_JOUEUR_SOURCE / LARGEUR_JOUEUR_SOURCE));
                }
                int offsetCentrageY = (tailleImage - hauteurProp) / 2;

                // Dessine l'image redimensionnée pour remplir le cadre de 100x100
                g2d.drawImage(img, xOffset, yCourant + offsetCentrageY, tailleImage, hauteurProp, null);

                // Optionnel : un petit contour blanc pour faire "propre"
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