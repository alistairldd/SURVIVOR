package Vue.HUD;

import Modele.Modele;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Dimension;
import static Modele.Constantes.*;

public class VueHUDPageBoutique extends JPanel {
    private Modele modele;
    private VueHUDShop vueHUDShop;

    public VueHUDPageBoutique(Modele modele) {
        this.modele = modele;
        this.setOpaque(false);
        this.vueHUDShop = new VueHUDShop();

        this.setPreferredSize(new Dimension(LARGEUR_HUD, 600));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int y = 40;
        y = vueHUDShop.dessiner(g, y, modele);

        // Trailing Stop pour le scroll
        if (y > getPreferredSize().height) {
            this.setPreferredSize(new Dimension(getWidth(), y + 20));
            this.revalidate();
        }
    }
    public VueHUDShop getVueHUDShop() {
        return vueHUDShop;
    }
}