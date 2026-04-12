package Vue.HUD;

import Modele.Modele;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Overlay d'interface gérant le bouton d'aide et le popup des commandes.
 * Implémentation hybride : JButton pour l'interaction et dessin personnalisé pour le popup.
 */
public class VueHUDInstructions extends JPanel {

    private Modele modele;
    private JButton btnAide;
    private BufferedImage imgManette;
    private boolean imageChargee = false;

    // Dimensions et marges constantes
    public static final int BTN_SIZE = 50;
    public static final int MARGIN = 20;

    public VueHUDInstructions(Modele modele) {
        this.modele = modele;

        // Configuration du conteneur d'overlay
        this.setOpaque(false); // Indispensable pour voir le jeu en dessous
        this.setLayout(null);  // Positionnement absolu des composants UI
        this.setFocusable(false);

        // 1. Initialisation du bouton physique
        initButton();

        // 2. Chargement de la ressource graphique
        try {
            imgManette = ImageIO.read(new File("src/images/Manette.png"));
            imageChargee = (imgManette != null);
        } catch (Exception e) {
            System.err.println("[DEV-LOG] Asset /images/Manette.png non trouvé. Fallback sur rendu textuel.");
            imageChargee = false;
        }
    }

    /**
     * Initialise le JButton Swing avec protection du focus clavier.
     */
    private void initButton() {
        btnAide = new JButton();

        // Empêche le bouton de voler le focus au KeyListener du jeu
        btnAide.setFocusable(false);

        // Style du bouton
        btnAide.setBorderPainted(false);
        btnAide.setContentAreaFilled(false);
        btnAide.setFocusPainted(false);
        btnAide.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Action Listener : Bascule l'état du popup dans le modèle
        btnAide.addActionListener(e -> {
            modele.toggleInstructions();
            // Force le rafraîchissement visuel pour afficher/masquer le popup
            repaint();
        });

        this.add(btnAide);
    }

    /**
     * Gère le positionnement dynamique du bouton lors du redimensionnement.
     */
    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        // On replace le bouton en bas à gauche à chaque changement de taille de fenêtre
        btnAide.setBounds(MARGIN, height - BTN_SIZE - MARGIN, BTN_SIZE, BTN_SIZE);
    }

    /**
     * Rendu graphique du bouton (si image absente) et du popup.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int h = getHeight();

        // --- DESSIN DU BOUTON (Fond et icône) ---
        int btnY = h - BTN_SIZE - MARGIN;
        g2d.setColor(new Color(50, 50, 50, 200));
        g2d.fillRoundRect(MARGIN, btnY, BTN_SIZE, BTN_SIZE, 10, 10);
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(MARGIN, btnY, BTN_SIZE, BTN_SIZE, 10, 10);

        if (imageChargee) {
            g2d.drawImage(imgManette, MARGIN + 5, btnY + 5, BTN_SIZE - 10, BTN_SIZE - 10, null);
        } else {
            g2d.setFont(new Font("Monospaced", Font.BOLD, 25));
            g2d.drawString("?", MARGIN + 18, btnY + 35);
        }

        // --- DESSIN DU POPUP (Si actif dans le modèle) ---
        if (modele.isInstructionsOuvert()) {
            drawInstructionPopup(g2d, h);
        }
    }

    /**
     * Procédure de rendu du menu d'aide translucide.
     */
    private void drawInstructionPopup(Graphics2D g2d, int screenHeight) {
        int width = 320;
        int height = 240;
        int x = MARGIN;
        int y = screenHeight - BTN_SIZE - MARGIN - height - 10;

        // Background translucide (Layer Alpha 85%)
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85f));
        g2d.setColor(new Color(30, 30, 30));
        g2d.fillRoundRect(x, y, width, height, 15, 15);

        // Border rendering
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        g2d.setColor(new Color(180, 180, 180));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(x, y, width, height, 15, 15);

        // Header
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 16));
        g2d.drawString("COMMANDES SYSTÈME", x + 15, y + 35);

        // Body text mapping
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        String[] instructions = {
                "• CLIC DROIT   : Déplacement vecteur",
                "• CLIC GAUCHE  : Execution attaque",
                "• TOUCHE R     : Extraction Mine",
                "• TOUCHE T     : Build Tower (Resources req.)",
                "• TOUCHE C     : Debug Range UI",
                "• FLÈCHES      : Switch HUD Pages",
                "• PAVÉ NUM     : Transaction Shop"
        };

        for (int i = 0; i < instructions.length; i++) {
            g2d.drawString(instructions[i], x + 20, y + 70 + (i * 22));
        }
    }
}