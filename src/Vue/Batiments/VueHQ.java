package Vue.Batiments;

import Modele.Batiments.HQ;
import java.awt.*;
import static Modele.Constantes.*;

public class VueHQ {

    /**
     * PASSE 2 : Dessine le sprite du HQ.
     */
    public static void dessiner(Graphics2D g2d, HQ hq, int x, int y, boolean minimap) {

        if (minimap) {
            g2d.setColor(Color.WHITE);
            int tailleM = TAILLE_BATIMENT_MINIMAP + 2;
            g2d.fillRect(x - (tailleM / 2), y - (tailleM / 2), tailleM, tailleM);
        } else {
            Image spriteAAfficher;

            if (!hq.isFonctionnel() || hq.getHp() <= (hq.getMaxHp() * 0.10)) {
                spriteAAfficher = IMAGE_HQ_ENDOMMAGE;
            } else {
                spriteAAfficher = IMAGE_HQ;
            }

            // Ancrage au sol (y - TAILLE_HQ)
            if (spriteAAfficher != null) {
                g2d.drawImage(spriteAAfficher, x - (TAILLE_HQ / 2), y - TAILLE_HQ * 2/3, null);
            } else {
                g2d.setColor(Color.WHITE);
                g2d.fillRect(x - (TAILLE_HQ / 2), y - TAILLE_HQ, TAILLE_HQ, TAILLE_HQ);
            }
        }
    }
}