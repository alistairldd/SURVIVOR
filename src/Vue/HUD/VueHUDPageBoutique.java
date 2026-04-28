package Vue.HUD;

import Modele.Modele;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Dimension;
import static Modele.Constantes.*;

/**
 * Conteneur UI gérant la page "Boutique" du HUD.
 * Gère le défilement adaptatif (Trailing Stop) et délègue le rendu à la classe VueHUDShop.
 */
public class VueHUDPageBoutique extends JPanel {

    /** ---------- [Propriétés] ---------- **/

    private Modele modele;
    private VueHUDShop vueHUDShop;

    /** ---------- [Constructeurs] ---------- **/

    /**
     * Initialise la page Boutique en mode transparent.
     *
     * @param modele - Le modèle central de l'application
     */
    public VueHUDPageBoutique(Modele modele) {
        this.modele = modele;
        this.setOpaque(false);
        this.vueHUDShop = new VueHUDShop();

        this.setPreferredSize(new Dimension(LARGEUR_HUD, 600));
    }

    /** ---------- [Accesseurs / Getters] ---------- **/

    public VueHUDShop getVueHUDShop() {
        return vueHUDShop;
    }

    /** ---------- [Méthodes Protégées - Rendu] ---------- **/

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int y = 40;
        y = vueHUDShop.dessiner(g, y, modele);

        // Adaptation dynamique de la taille du conteneur pour le défilement vertical
        if (y > getPreferredSize().height) {
            this.setPreferredSize(new Dimension(getWidth(), y + 20));
            this.revalidate();
        }
    }
}