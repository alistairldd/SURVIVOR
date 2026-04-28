package Vue.HUD;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

/**
 * Interface utilisateur personnalisée pour les barres de défilement.
 * Implémente un design "Overlay" épuré : masque la piste et les boutons directionnels,
 * et affiche uniquement un curseur semi-transparent aux bords arrondis.
 */
public class OverlayScrollBarUI extends BasicScrollBarUI {

    /** ---------- [Méthodes Protégées - Rendu Graphique] ---------- **/

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        // La piste de fond est volontairement laissée vide pour un effet de transparence totale.
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
            return;
        }
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 10, 10);

        g2d.dispose();
    }

    /** ---------- [Méthodes Protégées - Configuration des Boutons] ---------- **/

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createZeroButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createZeroButton();
    }

    /** ---------- [Méthodes Privées - Utilitaires] ---------- **/

    /**
     * Instancie un bouton fonctionnel mais visuellement et spatialement nul.
     * Utilisé pour masquer les flèches natives de la scrollbar.
     *
     * @return Un JButton de dimension 0x0
     */
    private JButton createZeroButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0, 0));
        button.setMinimumSize(new Dimension(0, 0));
        button.setMaximumSize(new Dimension(0, 0));
        return button;
    }
}