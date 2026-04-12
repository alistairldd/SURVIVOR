package Vue.HUD;

import Modele.Modele;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Dimension;
import static Modele.Constantes.*;

public class VueHUDPageAction extends JPanel {
    private Modele modele;
    private VueHUDInventaire vueHUDInventaire;
    private VueHUDBat vueHUDBat;

    public VueHUDPageAction(Modele modele) {
        this.modele = modele;
        this.setOpaque(false);

        this.vueHUDInventaire = new VueHUDInventaire();
        this.vueHUDBat = new VueHUDBat();

        this.setPreferredSize(new Dimension(LARGEUR_HUD, 600));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int y = 40;
        y = vueHUDInventaire.dessiner(g, y, modele, modele.getJoueur());
        y = vueHUDBat.dessiner(g, y, modele, modele.getJoueur());

        // Trailing Stop pour le scroll
        if (y > getPreferredSize().height) {
            this.setPreferredSize(new Dimension(getWidth(), y + 20));
            this.revalidate();
        }
    }
}