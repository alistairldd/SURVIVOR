package Vue.Batiments;

import Modele.Batiments.HQ;
import java.awt.*;

import static Modele.Constantes.*;

public class VueHQ {

    /** ---------- [Méthodes Publiques - Rendu] ---------- **/

    /**
     * Dessine la représentation visuelle du HQ dans la scène principale ou sur la minimap.
     * Le sprite affiché reflète l'état critique du bâtiment afin de rendre sa lisibilité
     * immédiate pour le joueur.
     *
     * @param g2d - Contexte graphique de dessin
     * @param hq - Quartier général à afficher
     * @param x - Position X écran du point d'ancrage
     * @param y - Position Y écran du point d'ancrage
     * @param minimap - Indique si le rendu cible la minimap
     */
    public static void dessiner(Graphics2D g2d, HQ hq, int x, int y, boolean minimap) {

        if (minimap) {
            // Marqueur volontairement simple et légèrement accentué pour rester repérable.
            g2d.setColor(Color.WHITE);
            int tailleM = TAILLE_BATIMENT_MINIMAP + 2;
            g2d.fillRect(x - (tailleM / 2), y - (tailleM / 2), tailleM, tailleM);
        } else {
            Image spriteAAfficher;

            // Le HQ bascule visuellement en état endommagé dès qu'il devient non fonctionnel
            // ou lorsqu'il entre dans une zone de survie critique.
            if (!hq.isFonctionnel() || hq.getHp() <= (hq.getMaxHp() * 0.10)) {
                spriteAAfficher = IMAGE_HQ_ENDOMMAGE;
            } else {
                spriteAAfficher = IMAGE_HQ;
            }

            if (spriteAAfficher != null) {
                // L'image est ancrée sur le sol plutôt que centrée afin de respecter la perspective 2.5D.
                g2d.drawImage(spriteAAfficher, x - (TAILLE_HQ / 2), y - TAILLE_HQ * 2 / 3, null);
            } else {
                // Rendu de secours pour conserver une présence visuelle même sans ressource chargée.
                g2d.setColor(Color.WHITE);
                g2d.fillRect(x - (TAILLE_HQ / 2), y - TAILLE_HQ, TAILLE_HQ, TAILLE_HQ);
            }
        }
    }
}