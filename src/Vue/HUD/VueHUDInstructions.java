package Vue.HUD;

import Modele.Modele;
import javax.swing.*;
import java.awt.*;

/**
 * Panneau d'Overlay affichant un bouton d'aide et un panneau d'instructions pop-up.
 * Ce composant flottant superpose le jeu et calcule ses dimensions de manière
 * dynamique en fonction du contenu textuel.
 */
public class VueHUDInstructions extends JPanel {

    /** ---------- [Constantes] ---------- **/

    public static final int BTN_SIZE = 50;
    public static final int MARGIN = 20;

    /** ---------- [Propriétés] ---------- **/

    private Modele modele;
    private JButton btnAide;

    /** ---------- [Constructeurs] ---------- **/

    /**
     * Initialise le conteneur en mode transparent et instancie le bouton d'aide.
     *
     * @param modele - Référence au modèle pour gérer l'état d'affichage des instructions
     */
    public VueHUDInstructions(Modele modele) {
        this.modele = modele;
        this.setOpaque(false);
        this.setLayout(null);
        this.setFocusable(false);
        initButton();
    }

    /** ---------- [Méthodes Privées - Configuration] ---------- **/

    /**
     * Crée et configure le bouton interactif ouvrant/fermant le panneau d'aide.
     */
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

    /** ---------- [Méthodes Publiques - Layout] ---------- **/

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        btnAide.setBounds(MARGIN, height - BTN_SIZE - MARGIN, BTN_SIZE, BTN_SIZE);
    }

    /** ---------- [Méthodes Protégées - Rendu] ---------- **/

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int h = getHeight();
        int btnY = h - BTN_SIZE - MARGIN;

        g2d.setColor(new Color(50, 50, 50, 200));
        g2d.fillRoundRect(MARGIN, btnY, BTN_SIZE, BTN_SIZE, 10, 10);
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(MARGIN, btnY, BTN_SIZE, BTN_SIZE, 10, 10);

        g2d.setFont(new Font("Monospaced", Font.BOLD, 25));
        g2d.drawString("?", MARGIN + 18, btnY + 35);

        if (modele.isInstructionsOuvert()) {
            drawInstructionPopup(g2d, h);
        }
    }

    /** ---------- [Méthodes Privées - Sous-Rendu] ---------- **/

    /**
     * Dessine dynamiquement le panneau d'instructions. Calcule automatiquement
     * l'encombrement nécessaire basé sur la police et le contenu.
     *
     * @param g2d - Contexte graphique 2D
     * @param screenHeight - Hauteur totale de l'écran pour l'ancrage bas
     */
    private void drawInstructionPopup(Graphics2D g2d, int screenHeight) {
        String title = "COMMANDES SYSTÈME";
        String[] instructions = {
                "• CLIC DROIT   : Déplacement",
                "• CLIC GAUCHE  : Attaque",
                "• R            : Extraction Mine",
                "• P            : Afficher PVs",
                "• C            : Afficher la portée",
                "• ESPACE       : Changer d'arme",
                "• FLÈCHES      : Switch HUD Pages"
        };

        Font titleFont = new Font("Segoe UI", Font.BOLD, 16);
        Font textFont = new Font("Segoe UI", Font.PLAIN, 13);
        int lineSpacing = 22;
        int paddingSide = 25;
        int paddingTop = 50;
        int paddingBottom = 20;

        g2d.setFont(textFont);
        FontMetrics fm = g2d.getFontMetrics();
        int maxWidth = g2d.getFontMetrics(titleFont).stringWidth(title);
        for (String s : instructions) {
            maxWidth = Math.max(maxWidth, fm.stringWidth(s));
        }

        int width = maxWidth + (paddingSide * 2);
        int height = paddingTop + (instructions.length * lineSpacing) + paddingBottom;

        int x = MARGIN;
        int y = screenHeight - BTN_SIZE - MARGIN - height - 10;

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.90f));
        g2d.setColor(new Color(25, 25, 25));
        g2d.fillRoundRect(x, y, width, height, 15, 15);

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        g2d.setColor(new Color(150, 150, 150));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(x, y, width, height, 15, 15);

        g2d.setColor(Color.WHITE);
        g2d.setFont(titleFont);
        g2d.drawString(title, x + paddingSide, y + 35);

        g2d.setFont(textFont);
        for (int i = 0; i < instructions.length; i++) {
            g2d.drawString(instructions[i], x + paddingSide, y + paddingTop + (i * lineSpacing) + 15);
        }
    }
}