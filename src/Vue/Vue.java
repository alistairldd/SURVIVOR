package Vue;

import static Modele.Constantes.*;
import Modele.*;
import Controleur.ControleurClavier;
import Controleur.ControleurSouris;
import Modele.Batiments.Batiment;
import Modele.Monstres.Monstre;
import Vue.Batiments.VueBatiment;
import Vue.Batiments.VueEffetSoin;
import Vue.Batiments.VueEffetTente;
import Vue.HUD.VueHUD;
import Vue.HUD.VueHUDInstructions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Architecture de rendu hybride (JLayeredPane).
 * - Calque DEFAULT : Moteur de rendu du monde (JPanel) à 60 FPS.
 * - Calque PALETTE : Interface utilisateur statique (Overlay UI).
 */
public class Vue extends JPanel {

    private JFrame maFenetre;
    private JLayeredPane layeredPane;

    // Components UI & Vues
    private final VueHUD vueHUD;
    private final VueCarte vueCarte;
    private final VueArme vueArme;
    private final VueRessource vueRessource;
    private final VueBatiment vueBatiment;
    private final VueMonstre vueMonstre;

    // Business Logic
    private final Modele modele;

    // Systems d'effets
    private VueEffetSoin vueEffetSoin;
    private VueEffetTente vueEffetTente;

    // Overlay Instructions (Composant Swing natif)
    private final VueHUDInstructions vueInstructions;

    public Vue(Modele modele) {
        this.modele = modele;

        // 1. Configuration de la Frame principale
        maFenetre = new JFrame("Survivor - Trading Engine Edition");
        maFenetre.setPreferredSize(new Dimension(LARGEUR, HAUTEUR));
        maFenetre.setExtendedState(JFrame.MAXIMIZED_BOTH);
        maFenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        maFenetre.setLayout(new BorderLayout());
        maFenetre.setResizable(true);

        // 2. Initialisation du LayeredPane (Gestionnaire de couches)
        layeredPane = new JLayeredPane();

        // 3. Initialisation du HUD latéral
        this.vueHUD = new VueHUD(modele);
        maFenetre.add(vueHUD, BorderLayout.EAST);

        // 4. Initialisation des moteurs de rendu du monde
        this.vueCarte = new VueCarte(modele);
        this.vueRessource = new VueRessource();
        this.vueBatiment = new VueBatiment();
        this.vueMonstre = new VueMonstre();
        this.vueEffetSoin = new VueEffetSoin(modele);
        this.vueEffetTente = new VueEffetTente(modele);

        // 5. Initialisation du composant Overlay
        this.vueInstructions = new VueHUDInstructions(modele);

        // --- ASSEMBLAGE DES COUCHES ---
        // Le moteur de rendu (this) est sur la couche de fond
        layeredPane.add(this, JLayeredPane.DEFAULT_LAYER);

        // L'interface interactive est sur la couche supérieure
        layeredPane.add(vueInstructions, JLayeredPane.PALETTE_LAYER);

        maFenetre.add(layeredPane, BorderLayout.CENTER);

        // 6. Injection des Contrôleurs
        ControleurSouris controleurSouris = new ControleurSouris(this, modele);
        this.addMouseListener(controleurSouris);
        this.addMouseMotionListener(controleurSouris);
        this.addKeyListener(new ControleurClavier(this, modele));

        this.vueArme = new VueArme(controleurSouris, this, modele);

        // 7. Auto-scaling des calques lors du redimensionnement de la Frame
        maFenetre.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());
                vueInstructions.setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());
                revalidate();
            }
        });

        // Lancement du Thread de rafraîchissement
        new Redessine(this, modele);

        maFenetre.pack();
        maFenetre.setVisible(true);

        // Capture du focus pour le KeyListener
        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    /**
     * Méthode de rendu du monde.
     * Les composants Overlay Swing sont gérés par le LayeredPane et ne sont pas dessinés ici.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        Joueur joueur = modele.getJoueur();
        double camX = joueur.getX() - ((double) getWidth() / 2);
        double camY = joueur.getY() - ((double) getHeight() / 2);

        // --- TRANSLATION CAMÉRA (COORDONNÉES MONDE) ---
        g2d.translate(-camX, -camY);

        vueCarte.dessiner(g2d);

        for (Ressource r : modele.getUpdateJN().getRessources()) {
            vueRessource.dessinerRessource(g2d, r, r.getPositionX(), r.getPositionY(), false);
        }

        for (Batiment b : modele.getGestionnaireBatiments().getBatiments()) {
            VueBatiment.dessinerBatiment(g2d, b, (int) b.getX(), (int) b.getY(), false);
        }

        vueEffetSoin.miseAJour();
        vueEffetSoin.dessiner(g2d);

        vueEffetTente.miseAJour();
        vueEffetTente.dessiner(g2d);

        vueArme.dessiner(g2d);

        for (Monstre m : modele.getUpdateJN().getMonstres()) {
            vueMonstre.dessiner(g2d, m, (int) m.getX(), (int) m.getY(), false);
        }

        // --- RESET TRANSLATION (COORDONNÉES ÉCRAN) ---
        g2d.translate(camX, camY);

        if (modele.getPartieTerminee()) {
            dessinerGameOver(g2d);
            return;
        }

        dessineMinimap(g2d);
    }

    private void dessinerGameOver(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, 160));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.setColor(Color.RED);
        g2d.setFont(new Font("Arial", Font.BOLD, 100));
        String message = "GAME OVER";
        FontMetrics fm = g2d.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(message)) / 2;
        int y = (getHeight() / 2) + (fm.getAscent() / 4);
        g2d.drawString(message, x, y);

        g2d.setFont(new Font("Arial", Font.BOLD, 40));
        g2d.setColor(Color.BLACK);
        message = "Nombre de nuits passées : " + modele.getUpdateJN().getNbNuit();
        x = (getWidth() - g2d.getFontMetrics().stringWidth(message)) / 2;
        y += g2d.getFontMetrics().getHeight() + 20;
        g2d.drawString(message, x, y);

        message = "Nombre de monstres tués : " + modele.getUpdateJN().getMonGestionnaireMonstres().getNbMonstresMorts();
        x = (getWidth() - g2d.getFontMetrics().stringWidth(message)) / 2;
        y += g2d.getFontMetrics().getHeight() + 20;
        g2d.drawString(message, x, y);

        message = "Appuyez sur ESPACE pour recommencer";
        g2d.setFont(new Font("Arial", Font.BOLD, 15));
        g2d.setColor(Color.GRAY);
        x = (getWidth() - g2d.getFontMetrics().stringWidth(message)) / 2;
        y += g2d.getFontMetrics().getHeight() + 40;
        g2d.drawString(message, x, y);
    }

    protected void dessineMinimap(Graphics2D g2d) {
        int tailleMinimap = 300;
        g2d.translate(getWidth() - tailleMinimap - 10, 10);
        g2d.setColor(new Color(50,70,50));
        g2d.fillRect(0, 0, tailleMinimap, tailleMinimap);
        g2d.setColor(Color.RED);
        g2d.drawRect(0, 0, tailleMinimap, tailleMinimap);

        for (Ressource r : modele.getUpdateJN().getRessources()) {
            double resX = modele.map (0, LARGEUR_MAP, 0, tailleMinimap-4, r.getPositionX());
            double resY = modele.map (0, HAUTEUR_MAP, 0, tailleMinimap-4, r.getPositionY());
            vueRessource.dessinerRessource(g2d, r, resX, resY, true);
        }

        g2d.setColor(Color.BLACK);
        Joueur joueur = modele.getJoueur();
        int posX = (int) modele.map (0, LARGEUR_MAP, 0, tailleMinimap-5, joueur.getX());
        int posY = (int) modele.map (0, HAUTEUR_MAP, 0, tailleMinimap-5, joueur.getY());
        g2d.fillOval(posX,posY, 5, 5);

        for (Batiment b : modele.getGestionnaireBatiments().getBatiments()) {
            int batX = (int) modele.map(0, LARGEUR_MAP, 0, tailleMinimap - 4, b.getX());
            int batY = (int) modele.map(0, HAUTEUR_MAP, 0, tailleMinimap - 4, b.getY());
            VueBatiment.dessinerBatiment(g2d, b, batX, batY, true);
        }

        for (Monstre m : modele.getUpdateJN().getMonstres()) {
            int monstreX = (int) modele.map (0, LARGEUR_MAP, 0, tailleMinimap-4, m.getX());
            int monstreY = (int) modele.map (0, HAUTEUR_MAP, 0, tailleMinimap-4, m.getY());
            vueMonstre.dessiner(g2d, m, monstreX, monstreY, true);
        }
    }

    // --- ACCESSEURS (Getters) ---
    public VueHUD getVueHUD() { return vueHUD; }
    public VueArme getVueArme() { return vueArme; }
    public JFrame getMaFenetre() { return maFenetre; }
}