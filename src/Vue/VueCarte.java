package Vue;

import Modele.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.TexturePaint;

import static Modele.Constantes.*;

/**
 * Responsable du rendu de la toile de fond du monde.
 * Gère le dessin du sol via une texture répétée puis ajoute des repères visuels
 * de bord de carte pour rendre les limites du terrain compréhensibles.
 */
public class VueCarte {

    /** ---------- [Propriétés] ---------- **/

    private final Joueur joueur;

    /** ---------- [Constructeurs] ---------- **/

    /**
     * Initialise la vue de la carte à partir du modèle courant.
     *
     * @param modele - Modèle de jeu donnant accès au joueur et au contexte du monde
     */
    public VueCarte(Modele modele) {
        this.joueur = modele.getJoueur();
    }

    /** ---------- [Méthodes Protégées - Rendu] ---------- **/

    /**
     * Dessine le sol du monde et ses repères de limite.
     * Le fond utilise un motif répété à proportions conservées afin de couvrir
     * une grande surface sans déformation visuelle notable.
     *
     * @param g - Contexte graphique principal
     */
    protected void dessiner(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        if (IMAGE_FOND_MAP != null) {
            // Le nombre fixe de répétitions verticales stabilise l'échelle visuelle de la texture.
            double nbRepetitionsY = 8.0;

            double hauteurTuile = (double) HAUTEUR_MAP / nbRepetitionsY;
            double ratioImage = (double) IMAGE_FOND_MAP.getWidth() / IMAGE_FOND_MAP.getHeight();
            double largeurTuile = hauteurTuile * ratioImage;

            // L'ancre définit la taille de répétition sans déformer l'image source.
            Rectangle2D ancre = new Rectangle2D.Double(0, 0, largeurTuile, hauteurTuile);

            TexturePaint texturePattern = new TexturePaint(IMAGE_FOND_MAP, ancre);
            g2d.setPaint(texturePattern);
            g2d.fillRect(0, 0, LARGEUR_MAP, HAUTEUR_MAP);
        } else {
            // Couleur de secours pour conserver un terrain exploitable même sans texture chargée.
            g2d.setColor(new Color(34, 139, 34));
            g2d.fillRect(0, 0, LARGEUR_MAP, HAUTEUR_MAP);
        }

        // L'assombrissement homogénéise légèrement le fond pour améliorer la lisibilité des entités.
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, LARGEUR_MAP, HAUTEUR_MAP);

        // La bordure aide à matérialiser explicitement l'espace jouable.
        g2d.setColor(Color.RED);
        g2d.drawRect(0, 0, LARGEUR_MAP, HAUTEUR_MAP);

        // Ces arbres de bordure servent surtout de masque visuel pour éviter une limite de carte trop brute.
        int deb = -900;
        int fin = LARGEUR_MAP + 900;
        int haut = -600;
        int bas = HAUTEUR_MAP - (ARBRE1.getHeight() / 2);

        for (int h = haut; h < 0; h += ARBRE1.getHeight()) {
            for (int i = deb; i < fin; i += ARBRE1.getWidth()) {
                int i2 = i + ARBRE1.getWidth() / 2;

                g2d.drawImage(ARBRE1, i, h, ARBRE1.getWidth(), ARBRE1.getHeight(), null);
                g2d.drawImage(ARBRE2, i2, h, ARBRE2.getWidth(), ARBRE2.getHeight(), null);

                g2d.drawImage(ARBRE1, i, bas, ARBRE1.getWidth(), ARBRE1.getHeight(), null);
                g2d.drawImage(ARBRE2, i2, bas, ARBRE2.getWidth(), ARBRE2.getHeight(), null);
            }
        }

        for (int i = 0; i < HAUTEUR_MAP; i += 100) {
            g2d.drawImage(ARBRE1, 10 - 50, 10 + i, 50, 50, null);
            g2d.drawImage(ARBRE1, 10 + LARGEUR_MAP, 10 + i, 50, 50, null);
        }
    }
}