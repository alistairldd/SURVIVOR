package Vue.Batiments;

import java.awt.*;

import Modele.Batiments.Batiment;
import Modele.Batiments.HQ;
import Modele.Batiments.Mine;
import Modele.Batiments.Tower;

import static Modele.Constantes.*;

/**
 * Gère l'aiguillage du rendu visuel des structures fixes.
 * Cette classe délègue le dessin spécifique aux composants dédiés (VueHQ, VueTower, etc.)
 * tout en conservant la logique de sélection basée sur le type de bâtiment.
 */
public class VueBatiment {

    public VueBatiment() {
    }

    /**
     * Point d'entrée principal pour le dessin d'un bâtiment.
     * @param g Le contexte graphique.
     * @param b L'instance du bâtiment à restituer.
     * @param x Coordonnée X de l'entité.
     * @param y Coordonnée Y de l'entité.
     * @param minimap Indique si le rendu doit être optimisé pour le radar.
     */
    public static void dessinerBatiment(Graphics g, Batiment b, int x, int y, boolean minimap) {

        Graphics2D g2d = (Graphics2D) g.create();

        // Aiguillage vers les classes de rendu spécialisées
        if (b instanceof HQ) {
            VueHQ.dessiner(g2d, (HQ) b, x, y, minimap);
        }
        else if (b instanceof Tower) {
            VueTower.dessiner(g2d, (Tower) b, x, y, minimap);
        }
        else if (b instanceof Mine) {
            VueMine.dessiner(g2d, (Mine) b, x, y, minimap);
        }
        else if (b instanceof Modele.Batiments.TenteDeSoin) {
            VueTente.dessiner(g2d, (Modele.Batiments.TenteDeSoin) b, x, y, minimap);
        }
        else {
            // Rendu par défaut pour les structures non répertoriées
            g2d.setColor(Color.GRAY);
            int taille = minimap ? TAILLE_BATIMENT_MINIMAP : TAILLE_BATIMENT;
            g2d.fillRect(x - (taille / 2), y - (taille / 2), taille, taille);
        }

        g2d.dispose();
    }
}