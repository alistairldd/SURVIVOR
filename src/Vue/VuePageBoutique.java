package Vue;

import Modele.Modele;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Dimension;
import static Modele.Constantes.*;

public class VuePageBoutique extends JPanel {
    private Modele modele;
    private VueShop vueShop;

    public VuePageBoutique(Modele modele) {
        this.modele = modele;
        this.setOpaque(false);
        this.vueShop = new VueShop();

        this.setPreferredSize(new Dimension(LARGEUR_HUD, 600));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int y = 40;
        y = vueShop.dessiner(g, y, modele.getJoueur());

        // Trailing Stop pour le scroll
        if (y > getPreferredSize().height) {
            this.setPreferredSize(new Dimension(getWidth(), y + 20));
            this.revalidate();
        }
    }
}