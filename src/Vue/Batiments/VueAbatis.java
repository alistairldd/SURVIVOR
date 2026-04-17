package Vue.Batiments;

import Modele.Batiments.Abatis;
import java.awt.*;
import static Modele.Constantes.*;

/**
 * Gère le rendu visuel de l'Abatis.
 * Alterne entre les états Bullish (Sain) et Bearish (Endommagé).
 */
public class VueAbatis {

    public static void dessinerSprite(Graphics2D g2d, Abatis a, int x, int y, boolean minimap) {
        if (minimap) {
            g2d.setColor(new Color(139, 69, 19)); // Marron pour la minimap
            g2d.fillRect(x - 3, y - 3, 6, 6);
            return;
        }

        Image imgAffichee;
        boolean estEndommage = (!a.isFonctionnel());

        // Sélection de l'image selon la rotation (Miroir) et l'état de santé
        if (!a.isRotation()) {
            // Image 1 (Inclinaison standard)
            imgAffichee = estEndommage ? IMAGE_ABATIS_1_ENDOMMAGE : IMAGE_ABATIS_1;
        } else {
            // Image 2 (Image miroir)
            imgAffichee = estEndommage ? IMAGE_ABATIS_2_ENDOMMAGE : IMAGE_ABATIS_2;
        }

        if (imgAffichee != null) {
            // On centre l'image sur les coordonnées (x, y)
            int offsetX = x - (TAILLE_ABATIS / 2);
            int offsetY = y - (TAILLE_ABATIS / 2);
            g2d.drawImage(imgAffichee, offsetX, offsetY, null);
        }
    }
}