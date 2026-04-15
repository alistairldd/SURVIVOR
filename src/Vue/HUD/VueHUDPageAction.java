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
        // --- BOUTON TOUR (+) en Vert ---
        btnTour = createStyledButton("+", new Color(34, 139, 34));
        btnTour.addActionListener(e -> modele.setModeConstruction(Modele.TypeConstruction.TOUR));
        this.add(btnTour);

        // --- BOUTON TENTE (+) en Vert ---
        btnTente = createStyledButton("+", new Color(34, 139, 34));
        btnTente.addActionListener(e -> modele.setModeConstruction(Modele.TypeConstruction.TENTE));
        this.add(btnTente);

        // --- BOUTON ANNULER (X) en Rouge ---
        btnCancel = createStyledButton("X", new Color(178, 34, 34));
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

        btnTour.setBounds(LARGEUR_HUD - 80, yPlacementButtons, 50, 20);
        btnTente.setBounds(LARGEUR_HUD - 80, yPlacementButtons + 43, 50, 20);

        // Le bouton cancel ("X") est reculé encore un peu plus à gauche
        btnCancel.setBounds(LARGEUR_HUD - 80, yPlacementButtons, 50, 20);

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

        // 2. Bouton Tente visible si Jour ET ressources suffisantes ET qu'aucune tente n'existe
        btnTente.setVisible(isDay && !buildActive &&
                modele.getJoueur().aAssezDeRessources(COUT_TENTE) &&
                !modele.getGestionnaireBatiments().aDejaUneTente());

        // 3. Bouton Annuler visible uniquement si un mode de construction est actif
        btnCancel.setVisible(buildActive);
    }
}