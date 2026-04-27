package Vue.Batiments;

import java.awt.*;

import Modele.Batiments.*;

import static Modele.Constantes.*;

/**
 * Aiguilleur de rendu pour le système de profondeur 2.5D.
 * Sépare le dessin des auras (sol) du dessin des structures (volumes).
 */
public class VueBatiment {

    public VueBatiment() {}

    /**
     * PASSE 1 : Dessine uniquement les cercles de portée et effets au sol.
     * Cette méthode est appelée AVANT le tri par profondeur.
     */
    public static void dessinerAura(Graphics2D g2d, Batiment b, int x, int y) {
        if (b instanceof Tower) {
            VueTower.dessinerAura(g2d, (Tower) b, x, y);
        }
        else if (b instanceof TenteDeSoin) {
            VueTente.dessinerAura(g2d, (TenteDeSoin) b, x, y);
        }
        else if (b instanceof Mortier) {
            VueMortier.dessinerAura(g2d, (Mortier) b, x, y);
        }
        // Le HQ et la Mine n'ont pas d'auras au sol à dessiner dans cette passe.
    }

    /**
     * PASSE 2 : Dessine le sprite du bâtiment et ses overlays (laser, texte).
     * Cette méthode est appelée APRÈS le tri par profondeur (Y-Sorting).
     */
    public static void dessinerSprite(Graphics2D g2d, Batiment b, int x, int y, boolean minimap) {
        if (b instanceof HQ) {
            VueHQ.dessiner(g2d, (HQ) b, x, y, minimap);
        }
        else if (b instanceof Tower) {
            VueTower.dessinerSprite(g2d, (Tower) b, x, y, minimap);
        }
        else if (b instanceof Mine) {
            VueMine.dessiner(g2d, (Mine) b, x, y, minimap);
        }
        else if (b instanceof TenteDeSoin) {
            VueTente.dessinerSprite(g2d, (TenteDeSoin) b, x, y, minimap);
        }
        else if (b instanceof Modele.Batiments.Abatis) {
            VueAbatis.dessinerSprite(g2d, (Modele.Batiments.Abatis) b, x, y, minimap);
        }
        else if (b instanceof Mortier) { // NOUVEAU
            VueMortier.dessinerSprite(g2d, (Mortier) b, x, y, minimap);
        }
        else {
            // Rendu de secours
            g2d.setColor(Color.GRAY);
            int taille = minimap ? TAILLE_BATIMENT_MINIMAP : TAILLE_TOUR;
            g2d.fillRect(x - (taille / 2), y - (taille / 2), taille, taille);
        }
    }

    /**
     * Compatibilité pour la Minimap (qui ne nécessite pas de tri complexe).
     */
    public static void dessinerBatiment(Graphics g, Batiment b, int x, int y, boolean minimap) {
        Graphics2D g2d = (Graphics2D) g.create();
        dessinerSprite(g2d, b, x, y, minimap);
        g2d.dispose();
    }
}