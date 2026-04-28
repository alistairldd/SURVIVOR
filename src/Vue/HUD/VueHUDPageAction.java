package Vue.HUD;

import Modele.Modele;
import javax.swing.*;
import java.awt.*;

import static Modele.Constantes.*;

/**
 * Page d'action du HUD.
 * Orchestre les points d'entrée de construction tout en gardant lisible l'état opérationnel du joueur.
 */
public class VueHUDPageAction extends JPanel {

    /** ---------- [Propriétés] ---------- **/

    private static final int LARGEUR_BOUTON_ACTION = 50;
    private static final int HAUTEUR_BOUTON_ACTION = 25;

    private final Modele modele;
    private final VueHUDInventaire vueHUDInventaire;
    private final VueHUDBat vueHUDBat;

    private JButton btnTour;
    private JButton btnTente;
    private JButton btnAbatis;
    private JButton btnMortier;
    private JButton btnRotate;
    private JButton btnCancel;

    /** ---------- [Constructeurs] ---------- **/

    /**
     * Initialise la page Action, déploie les sous-vues et prépare la couche de contrôle utilisateur.
     *
     * @param modele - Le modèle métier du jeu
     */
    public VueHUDPageAction(Modele modele) {
        this.modele = modele;
        this.setOpaque(false);
        this.setLayout(null);

        this.vueHUDInventaire = new VueHUDInventaire();
        this.vueHUDBat = new VueHUDBat();

        initButtons();

        this.setPreferredSize(new Dimension(LARGEUR_HUD, 800));
    }

    /** ---------- [Méthodes Privées - Initialisation UI] ---------- **/

    /**
     * Instancie les commandes d'action et relie chaque bouton à son intention de gameplay.
     * Centralise ici la couche de contrôle pour éviter de disperser la logique de construction.
     */
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

        btnMortier = createStyledButton("+", new Color(34, 139, 34));
        btnMortier.addActionListener(e -> modele.setModeConstruction(Modele.TypeConstruction.MORTIER));
        this.add(btnMortier);

        btnRotate = createStyledButton("↔", new Color(0, 102, 204));
        btnRotate.addActionListener(e -> {
            modele.toggleRotationAbatis();
            repaint();
        });
        this.add(btnRotate);

        btnCancel = createStyledButton("X", new Color(178, 34, 34));
        btnCancel.addActionListener(e -> modele.annulerConstruction());
        this.add(btnCancel);
    }

    /**
     * Fabrique un bouton homogène visuellement pour toute la grammaire d'action du HUD.
     * Force un rendu stable des couleurs et du symbole quel que soit le bouton concerné.
     *
     * @param text - Symbole affiché
     * @param color - Couleur dominante du bouton
     * @return Le bouton configuré
     */
    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(true);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        btn.setMargin(new Insets(0, 0, 0, 0));
        btn.setHorizontalAlignment(SwingConstants.CENTER);
        btn.setVerticalAlignment(SwingConstants.CENTER);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setVisible(false);
        return btn;
    }

    /** ---------- [Méthodes Protégées - Cycle de Rendu] ---------- **/

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int y = 20;

        y = vueHUDInventaire.dessiner(g, y, modele, modele.getJoueur());

        y += 20;

        int yPlacementButtons = y;
        y = vueHUDBat.dessiner(g, yPlacementButtons, modele, modele.getJoueur());

        updateButtonsState();

        int yTour = vueHUDBat.getYTour();
        int yTente = vueHUDBat.getYTente();
        int yAbatis = vueHUDBat.getyAbatis();

        // --- ANCRAGE DES CONTRÔLES AU RYTHME VISUEL DES ENTRÉES BÂTIMENTS ---
        btnTour.setBounds(LARGEUR_HUD - 80, yPlacementButtons + 30, LARGEUR_BOUTON_ACTION, HAUTEUR_BOUTON_ACTION);
        btnTente.setBounds(LARGEUR_HUD - 80, yTour + 30, LARGEUR_BOUTON_ACTION, HAUTEUR_BOUTON_ACTION);
        btnAbatis.setBounds(LARGEUR_HUD - 80, yTente + 30, LARGEUR_BOUTON_ACTION, HAUTEUR_BOUTON_ACTION);
        btnMortier.setBounds(LARGEUR_HUD - 80, yAbatis + 30, LARGEUR_BOUTON_ACTION, HAUTEUR_BOUTON_ACTION);

        btnCancel.setBounds(LARGEUR_HUD - 80, yPlacementButtons - 10, LARGEUR_BOUTON_ACTION, HAUTEUR_BOUTON_ACTION);

        // --- SUBSTITUTION CONTEXTUELLE DE L'ACTION ABATIS PAR LA ROTATION ---
        btnRotate.setBounds(LARGEUR_HUD - 80, yTente + 30, LARGEUR_BOUTON_ACTION, HAUTEUR_BOUTON_ACTION);

        if (y > getPreferredSize().height) {
            this.setPreferredSize(new Dimension(getWidth(), y + 40));
            this.revalidate();
        }
    }

    /** ---------- [Méthodes Privées - Logique Conditionnelle UI] ---------- **/

    /**
     * Pilote l'exposition des commandes selon le contexte courant de jeu.
     * Empêche l'interface de proposer une action invalide, impossible ou contradictoire.
     */
    private void updateButtonsState() {
        boolean isDay = modele.getLeCycleJourNuit().isDay();
        boolean buildActive = modele.getModeConstruction() != Modele.TypeConstruction.AUCUN;
        var joueur = modele.getJoueur();

        btnTour.setVisible(isDay && !buildActive && joueur.aAssezDeRessources(COUT_TOUR));
        btnTente.setVisible(isDay && !buildActive
                && joueur.aAssezDeRessources(COUT_TENTE)
                && !modele.getGestionnaireBatiments().aDejaUneTente());
        btnAbatis.setVisible(isDay && !buildActive && joueur.aAssezDeRessources(COUT_ABATIS));
        btnMortier.setVisible(isDay && !buildActive && joueur.aAssezDeRessources(COUT_MORTIER));

        btnRotate.setVisible(modele.getModeConstruction() == Modele.TypeConstruction.ABATIS);
        btnCancel.setVisible(buildActive);
    }
}