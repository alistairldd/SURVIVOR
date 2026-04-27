package Vue.HUD;

import Modele.Modele;
import javax.swing.*;
import java.awt.*;
import static Modele.Constantes.*;

/**
 * Page d'action du HUD.
 * Gère désormais l'achat du Mortier en plus des autres bâtiments.
 */
public class VueHUDPageAction extends JPanel {
    private Modele modele;
    private VueHUDInventaire vueHUDInventaire;
    private VueHUDBat vueHUDBat;

    // Boutons de construction
    private JButton btnTour;
    private JButton btnTente;
    private JButton btnAbatis;
    private JButton btnMortier; // NOUVEAU
    private JButton btnRotate;
    private JButton btnCancel;

    public VueHUDPageAction(Modele modele) {
        this.modele = modele;
        this.setOpaque(false);
        this.setLayout(null);

        this.vueHUDInventaire = new VueHUDInventaire();
        this.vueHUDBat = new VueHUDBat();

        initButtons();

        this.setPreferredSize(new Dimension(LARGEUR_HUD, 800)); // Augmenté pour le scroll
    }

    private void initButtons() {
        btnTour = createStyledButton("+", new Color(34, 139, 34));
        btnTour.addActionListener(e -> modele.setModeConstruction(Modele.TypeConstruction.TOUR));
        this.add(btnTour);

        btnTente = createStyledButton("+", new Color(34, 139, 34));
        btnTente.addActionListener(e -> modele.setModeConstruction(Modele.TypeConstruction.TENTE));
        this.add(btnTente);

        btnAbatis = createStyledButton("+", new Color(34, 139, 34));
        btnAbatis.addActionListener(e -> modele.setModeConstruction(Modele.TypeConstruction.ABATIS));
        this.add(btnAbatis);

        // NOUVEAU : Bouton Mortier
        btnMortier = createStyledButton("+", new Color(34, 139, 34));
        btnMortier.addActionListener(e -> modele.setModeConstruction(Modele.TypeConstruction.MORTIER));
        this.add(btnMortier);

        btnRotate = createStyledButton("↔", new Color(0, 102, 204));
        btnRotate.addActionListener(e -> modele.toggleRotationAbatis());
        this.add(btnRotate);

        btnCancel = createStyledButton("x", new Color(178, 34, 34));
        btnCancel.addActionListener(e -> modele.annulerConstruction());
        this.add(btnCancel);
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFocusable(false);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        updateButtonsState();

        int y = 40;
        y = vueHUDInventaire.dessiner(g, y, modele, modele.getJoueur());

        int yPlacementButtons = y + 22;
        y = vueHUDBat.dessiner(g, y, modele, modele.getJoueur());

        int yTour = vueHUDBat.getYTour();
        int yTente = vueHUDBat.getYTente();
        int yAbatis = vueHUDBat.getyAbatis(); // NOUVEAU : On récupère enfin yAbatis

        // Positionnement des boutons d'achat (+)
        btnTour.setBounds(LARGEUR_HUD - 80, yPlacementButtons + 30, 50, 20);
        btnTente.setBounds(LARGEUR_HUD - 80, yTour + 30, 50, 20);
        btnAbatis.setBounds(LARGEUR_HUD - 80, yTente + 30, 50, 20);
        btnMortier.setBounds(LARGEUR_HUD - 80, yAbatis + 30, 50, 20); // NOUVEAU

        btnCancel.setBounds(LARGEUR_HUD - 80, yPlacementButtons - 10, 50, 20);
        btnRotate.setBounds(LARGEUR_HUD - 150, yPlacementButtons - 10, 60, 20);

        if (y > getPreferredSize().height) {
            this.setPreferredSize(new Dimension(getWidth(), y + 40));
            this.revalidate();
        }
    }

    private void updateButtonsState() {
        boolean isDay = modele.getLeCycleJourNuit().isDay();
        boolean buildActive = modele.getModeConstruction() != Modele.TypeConstruction.AUCUN;
        var joueur = modele.getJoueur();

        // Visibilité si Jour + Pas de construction active + Assez de ressources
        btnTour.setVisible(isDay && !buildActive && joueur.aAssezDeRessources(COUT_TOUR));
        btnTente.setVisible(isDay && !buildActive && joueur.aAssezDeRessources(COUT_TENTE) && !modele.getGestionnaireBatiments().aDejaUneTente());
        btnAbatis.setVisible(isDay && !buildActive && joueur.aAssezDeRessources(COUT_ABATIS));
        btnMortier.setVisible(isDay && !buildActive && joueur.aAssezDeRessources(COUT_MORTIER)); // NOUVEAU

        btnCancel.setVisible(buildActive);
        btnRotate.setVisible(buildActive && modele.getModeConstruction() == Modele.TypeConstruction.ABATIS);
    }
}