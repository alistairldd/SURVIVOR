package Vue.HUD;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class OverlayScrollBarUI extends BasicScrollBarUI {

    // Rend la piste (le fond de l'ascenseur) totalement invisible
    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        // Ne rien faire.
    }


    // Dessine uniquement la poignée (le curseur)
    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
            return;
        }
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Couleur noire avec canal Alpha (transparence à environ 40%)
        // Tu peux remplacer par du blanc (255, 255, 255, 100) si tu préfères
        g2d.setColor(new Color(0, 0, 0, 100));

        // Dessine un rectangle aux coins arrondis
        g2d.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 10, 10);
        g2d.dispose();
    }

    // Masque le bouton fléché du haut
    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createZeroButton();
    }

    // Masque le bouton fléché du bas
    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createZeroButton();
    }

    // Méthode utilitaire pour créer un bouton de taille 0x0
    private JButton createZeroButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0, 0));
        button.setMinimumSize(new Dimension(0, 0));
        button.setMaximumSize(new Dimension(0, 0));
        return button;
    }
}