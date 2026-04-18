package Vue.HUD;

import Modele.Modele;
import javax.swing.*;
import java.awt.*;

/**
 * Overlay d'interface gérant le bouton d'aide et le popup des commandes.
 * Le popup s'adapte désormais dynamiquement à la taille du texte contenu.
 */
public class VueHUDInstructions extends JPanel {

    private Modele modele;
    private JButton btnAide;

    public static final int BTN_SIZE = 50;
    public static final int MARGIN = 20;

    public VueHUDInstructions(Modele modele) {
        this.modele = modele;
        this.setOpaque(false);
        this.setLayout(null);
        this.setFocusable(false);
        initButton();
    }

    private void initButton() {
        btnAide = new JButton();
        btnAide.setFocusable(false);
        btnAide.setBorderPainted(false);
        btnAide.setContentAreaFilled(false);
        btnAide.setFocusPainted(false);
        btnAide.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnAide.addActionListener(e -> {
            modele.toggleInstructions();
            repaint();
        });

        this.add(btnAide);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        btnAide.setBounds(MARGIN, height - BTN_SIZE - MARGIN, BTN_SIZE, BTN_SIZE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int h = getHeight();
        int btnY = h - BTN_SIZE - MARGIN;

        // --- DESSIN DU BOUTON ---
        g2d.setColor(new Color(50, 50, 50, 200));
        g2d.fillRoundRect(MARGIN, btnY, BTN_SIZE, BTN_SIZE, 10, 10);
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(MARGIN, btnY, BTN_SIZE, BTN_SIZE, 10, 10);

        g2d.setFont(new Font("Monospaced", Font.BOLD, 25));
        g2d.drawString("?", MARGIN + 18, btnY + 35);

        // --- DESSIN DU POPUP DYNAMIQUE ---
        if (modele.isInstructionsOuvert()) {
            drawInstructionPopup(g2d, h);
        }
    }

    private void drawInstructionPopup(Graphics2D g2d, int screenHeight) {
        // 1. DÉFINITION DU CONTENU
        String title = "COMMANDES SYSTÈME";
        String[] instructions = {
                "• CLIC DROIT   : Déplacement",
                "• CLIC GAUCHE  : Attaque",
                "• R            : Extraction Mine",
                "• T            : Construction d'une tour",
                "• P            : Afficher PVs",
                "• C            : Afficher la portée",
                "• ESPACE       : Changer d'arme",
                "• FLÈCHES      : Switch HUD Pages",
                "• PAVÉ NUM     : Transaction Shop"
        };

        // 2. CALCULS DE DIMENSIONS DYNAMIQUES
        Font titleFont = new Font("Segoe UI", Font.BOLD, 16);
        Font textFont = new Font("Segoe UI", Font.PLAIN, 13);
        int lineSpacing = 22;
        int paddingSide = 25;
        int paddingTop = 50; // Espace pour le titre
        int paddingBottom = 20;

        // Mesurer la largeur nécessaire (basée sur la ligne la plus longue)
        g2d.setFont(textFont);
        FontMetrics fm = g2d.getFontMetrics();
        int maxWidth = g2d.getFontMetrics(titleFont).stringWidth(title);
        for (String s : instructions) {
            maxWidth = Math.max(maxWidth, fm.stringWidth(s));
        }

        int width = maxWidth + (paddingSide * 2);
        int height = paddingTop + (instructions.length * lineSpacing) + paddingBottom;

        // Calcul de la position (s'élève vers le haut selon la taille)
        int x = MARGIN;
        int y = screenHeight - BTN_SIZE - MARGIN - height - 10;

        // 3. RENDU DU CADRE
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.90f));
        g2d.setColor(new Color(25, 25, 25));
        g2d.fillRoundRect(x, y, width, height, 15, 15);

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        g2d.setColor(new Color(150, 150, 150));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(x, y, width, height, 15, 15);

        // 4. RENDU DU TEXTE
        g2d.setColor(Color.WHITE);
        g2d.setFont(titleFont);
        g2d.drawString(title, x + paddingSide, y + 35);

        g2d.setFont(textFont);
        for (int i = 0; i < instructions.length; i++) {
            g2d.drawString(instructions[i], x + paddingSide, y + paddingTop + (i * lineSpacing) + 15);
        }
    }

    @Override
    public boolean contains(int x, int y) {
        return false; // Transparent aux clics : les events passent directement à la couche en dessous
    }
}