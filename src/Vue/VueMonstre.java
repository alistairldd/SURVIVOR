package Vue;

import Modele.GestionnaireMonstres;
import Modele.Monstre;

import java.awt.*;

public class VueMonstre {
    public static final int TAILLE = 30;
    public static final int TAILLE_MINIMAP = 10;

    // Constructeur de la classe VueMonstre
    public VueMonstre() {

    }

    public void dessiner(Graphics g, Monstre monstre, int posX, int posY, boolean minimap) {


        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //Centrage du monstre
        int taille = minimap ? TAILLE_MINIMAP : TAILLE;
        g2d.setColor(Color.RED);
        // On décale la position du monstre pour le centrer par rapport à sa taille
        g2d.fillRect((int) posX - taille / 2, (int) posY - taille / 2, taille, taille);

        // Dessin du cercle de portée du monstre
        if (!minimap) {
            g2d.setColor(new Color(255, 0, 0, 50)); // Rouge transparent
            int portee = (int) monstre.getPortee();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));

            // on dessine le cercle directement autour de ce point (x, y).
            g2d.fillOval(posX - portee, posY - portee, portee * 2, portee * 2);

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(posX - portee, posY - portee, portee * 2, portee * 2);

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }
    }
}
