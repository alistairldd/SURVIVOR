package Vue.Batiments;

import Modele.Batiments.Mine;
import java.awt.*;
import static Modele.Constantes.*;

public class VueMine {

    /**
     * PASSE 2 : Dessine le sprite de la Mine et son texte.
     */
    public static void dessiner(Graphics2D g2d, Mine mine, int x, int y, boolean minimap) {

        if (minimap) {
            g2d.setColor(new Color(150, 75, 0));
            g2d.fillRect(x - (TAILLE_BATIMENT_MINIMAP / 2), y - (TAILLE_BATIMENT_MINIMAP / 2), TAILLE_BATIMENT_MINIMAP, TAILLE_BATIMENT_MINIMAP);
        } else {
            Image spriteAAfficher;

            if (!mine.isFonctionnel() || mine.getHp() <= (mine.getMaxHp() / 2)) {
                spriteAAfficher = IMAGE_MINE_ENDOMMAGE;
            } else {
                spriteAAfficher = IMAGE_MINE;
            }

            // Ancrage au sol (y - TAILLE_MINE)
            if (spriteAAfficher != null) {
                g2d.drawImage(spriteAAfficher, x - (TAILLE_MINE / 2), y - TAILLE_MINE, null);
            } else {
                g2d.setColor(new Color(150, 75, 0));
                g2d.fillRect(x - (TAILLE_MINE / 2), y - TAILLE_MINE, TAILLE_MINE, TAILLE_MINE);
            }

            // Affichage du texte des minerais (On le remonte un peu plus haut pour qu'il soit au-dessus du toit)
            int stock = mine.getRessources().size();
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            g2d.drawString(stock + " Minerais", x - 30, y - TAILLE_MINE - 10);
        }
    }
}