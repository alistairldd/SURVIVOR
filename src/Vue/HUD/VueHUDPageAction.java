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
        // --- BOUTON TOUR (+) ---
        btnTour = createStyledButton("+", new Color(34, 139, 34)); // Vert
        btnTour.addActionListener(e -> modele.setModeConstruction(Modele.TypeConstruction.TOUR));
        this.add(btnTour);

        // --- BOUTON TENTE (+) ---
        btnTente = createStyledButton("+", new Color(34, 139, 34)); // Vert
        btnTente.addActionListener(e -> modele.setModeConstruction(Modele.TypeConstruction.TENTE));
        this.add(btnTente);

        // --- BOUTON ANNULER (X) ---
        btnCancel = createStyledButton("X", new Color(178, 34, 34)); // Rouge
        btnCancel.addActionListener(e -> modele.annulerConstruction());
        this.add(btnCancel);
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFocusable(false);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);
        btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
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

        // Positionnement dynamique des boutons sur la page
        btnTour.setBounds(LARGEUR_HUD - 60, yPlacementButtons, 30, 20);
        btnTente.setBounds(LARGEUR_HUD - 60, yPlacementButtons + 43, 30, 20);

        // Le bouton cancel apparaît à côté du bouton actif ou en bas
        btnCancel.setBounds(LARGEUR_HUD - 100, yPlacementButtons, 30, 20);

        // Trailing Stop pour le scroll
        if (y > getPreferredSize().height) {
            this.setPreferredSize(new Dimension(getWidth(), y + 20));
            this.revalidate();
        }
    }

    private void updateButtonsState() {
        boolean isDay = modele.getLeCycleJourNuit().isDay();
        boolean buildActive = modele.getModeConstruction() != Modele.TypeConstruction.AUCUN;

        // Logique de visibilité demandée :
        // 1. Bouton Tour visible si Jour ET ressources suffisantes
        btnTour.setVisible(isDay && !buildActive && modele.getJoueur().aAssezDeRessources(COUT_TOUR));

        // 2. Bouton Tente visible si Jour ET ressources suffisantes
        btnTente.setVisible(isDay && !buildActive && modele.getJoueur().aAssezDeRessources(COUT_TENTE));

        // 3. Bouton Annuler visible uniquement si un mode de construction est actif
        btnCancel.setVisible(buildActive);
    }
}