package Vue.HUD;

import Modele.Modele;
import javax.swing.*;
import java.awt.*;
import static Modele.Constantes.*;

/**
 * Page d'action du HUD.
 * Gère désormais les boutons Swing pour le mode construction.
 */
public class VueHUDPageAction extends JPanel {
    private Modele modele;
    private VueHUDInventaire vueHUDInventaire;
    private VueHUDBat vueHUDBat;

    // Boutons de construction
    private JButton btnTour;
    private JButton btnTente;
    private JButton btnAbatis;
    private JButton btnRotate;
    private JButton btnCancel;

    public VueHUDPageAction(Modele modele) {
        this.modele = modele;
        this.setOpaque(false);
        this.setLayout(null); // Positionnement absolu pour coller au dessin Graphics2D

        this.vueHUDInventaire = new VueHUDInventaire();
        this.vueHUDBat = new VueHUDBat();

        initButtons();

        this.setPreferredSize(new Dimension(LARGEUR_HUD, 600));
    }

    private void initButtons() {
        // --- BOUTON TOUR (+) en Vert ---
        btnTour = createStyledButton("+", new Color(34, 139, 34));
        btnTour.addActionListener(e -> modele.setModeConstruction(Modele.TypeConstruction.TOUR));
        this.add(btnTour);

        // --- BOUTON TENTE (+) en Vert ---
        btnTente = createStyledButton("+", new Color(34, 139, 34));
        btnTente.addActionListener(e -> modele.setModeConstruction(Modele.TypeConstruction.TENTE));
        this.add(btnTente);

        // --- BOUTON ABATIS (+) en Vert Bullish ---
        btnAbatis = createStyledButton("+", new Color(34, 139, 34));
        btnAbatis.addActionListener(e -> modele.setModeConstruction(Modele.TypeConstruction.ABATIS));
        this.add(btnAbatis);

        // --- BOUTON ROTATION (↔) en Bleu (Couleur utilitaire) ---
        btnRotate = createStyledButton("↔", new Color(0, 102, 204));
        btnRotate.addActionListener(e -> modele.toggleRotationAbatis());
        this.add(btnRotate);

        // --- BOUTON ANNULER (X) en Rouge ---
        btnCancel = createStyledButton("x", new Color(178, 34, 34));
        btnCancel.addActionListener(e -> modele.annulerConstruction());
        this.add(btnCancel);
    }

    /**
     * Crée un bouton stylisé avec une couleur de fond forcée.
     * Les trois lignes correctives permettent de contourner le style natif du système (Look and Feel).
     */
    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFocusable(false);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);

        // --- CORRECTIF POUR L'AFFICHAGE DES COULEURS ---
        btn.setContentAreaFilled(false); // Désactive le remplissage natif (qui cache souvent la couleur)
        btn.setOpaque(true);             // Force le bouton à peindre son propre fond avec la couleur 'bg'
        btn.setBorderPainted(false);     // Supprime la bordure système pour un rendu plus net et coloré

        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMargin(new Insets(0, 0, 0, 0)); // Marge Haut, Gauche, Bas, Droite à zéro
        return btn;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Mise à jour de l'affichage des boutons selon l'état du jeu
        updateButtonsState();

        int y = 40;
        y = vueHUDInventaire.dessiner(g, y, modele, modele.getJoueur());

        // On récupère les positions Y pour caler nos boutons sur le texte
        int yPlacementButtons = y + 22;
        y = vueHUDBat.dessiner(g, y, modele, modele.getJoueur());

        int yTour = vueHUDBat.getYTour();
        int yTente = vueHUDBat.getYTente();
        //int yAbatis = vueHUDBat.getyAbatis(); on en a besoin que si on rajoute d'autre batiments

        // Positionnement des boutons d'achat (+)
        btnTour.setBounds(LARGEUR_HUD - 80, yPlacementButtons+30, 50, 20);
        btnTente.setBounds(LARGEUR_HUD - 80, yTour+30, 50, 20);
        btnAbatis.setBounds(LARGEUR_HUD - 80, yTente+30, 50, 20);

        // Si on construit, on place le Cancel et le Rotate en haut du bloc
        btnCancel.setBounds(LARGEUR_HUD - 80, yPlacementButtons-10, 50, 20);
        btnRotate.setBounds(LARGEUR_HUD - 150, yPlacementButtons-10, 60, 20);

        // Trailing Stop pour le scroll
        if (y > getPreferredSize().height) {
            this.setPreferredSize(new Dimension(getWidth(), y + 20));
            this.revalidate();
        }
    }

    private void updateButtonsState() {
        boolean isDay = modele.getLeCycleJourNuit().isDay();
        boolean buildActive = modele.getModeConstruction() != Modele.TypeConstruction.AUCUN;

        // 1. Boutons d'achats visibles si Jour ET pas de construction en cours ET ressources OK
        btnTour.setVisible(isDay && !buildActive && modele.getJoueur().aAssezDeRessources(COUT_TOUR));

        btnTente.setVisible(isDay && !buildActive &&
                modele.getJoueur().aAssezDeRessources(COUT_TENTE) &&
                !modele.getGestionnaireBatiments().aDejaUneTente());

        btnAbatis.setVisible(isDay && !buildActive && modele.getJoueur().aAssezDeRessources(COUT_ABATIS));

        // 2. Bouton Annuler (Bearish rouge) visible uniquement si on construit
        btnCancel.setVisible(buildActive);

        // 3. Bouton Rotation visible UNIQUEMENT si on tient un Abatis
        btnRotate.setVisible(buildActive && modele.getModeConstruction() == Modele.TypeConstruction.ABATIS);
    }
}