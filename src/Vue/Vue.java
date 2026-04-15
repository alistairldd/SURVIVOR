package Vue;

import static Modele.Constantes.*;
import Modele.*;
import Controleur.ControleurClavier;
import Controleur.ControleurSouris;
import Modele.Batiments.Batiment;
import Modele.Batiments.Mine;
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
import java.util.ArrayList;
import java.util.Comparator;

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
        layeredPane.add(this, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(vueInstructions, JLayeredPane.PALETTE_LAYER);

        maFenetre.add(layeredPane, BorderLayout.CENTER);

        // 6. Injection des Contrôleurs
        ControleurSouris controleurSouris = new ControleurSouris(this, modele);
        this.addMouseListener(controleurSouris);
        this.addMouseMotionListener(controleurSouris);
        this.addKeyListener(new ControleurClavier(this, modele));
        this.vueHUD.getPageBoutique().addMouseListener(controleurSouris);

        this.vueArme = new VueArme(controleurSouris, this, modele);

        maFenetre.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());
                vueInstructions.setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());
                revalidate();
            }
        });

        new Redessine(this, modele);

        maFenetre.pack();
        maFenetre.setVisible(true);

        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        Joueur joueur = modele.getJoueur();
        double camX = joueur.getX() - ((double) getWidth() / 2);
        double camY = joueur.getY() - ((double) getHeight() / 2);

        // --- TRANSLATION CAMÉRA ---
        g2d.translate(-camX, -camY);

        // --- RENDU FOND (SOL) ---
        vueCarte.dessiner(g2d);

        for (Ressource r : modele.getUpdateJN().getRessources()) {
            vueRessource.dessinerRessource(g2d, r, r.getPositionX(), r.getPositionY(), false);
        }

        // --- PASSE 1 : LES AURAS ET EFFETS AU SOL ---
        // On dessine toutes les portées en premier pour qu'elles soient SOUS les bâtiments
        for (Batiment b : modele.getGestionnaireBatiments().getBatiments()) {
            VueBatiment.dessinerAura(g2d, b, (int) b.getX(), (int) b.getY());
        }

        vueEffetSoin.miseAJour();
        vueEffetSoin.dessiner(g2d);

        vueEffetTente.miseAJour();
        vueEffetTente.dessiner(g2d);

        // --- TRI PAR PROFONDEUR (Y-SORTING) ---
        // On crée une liste d'entités "physiques" (Bâtiments, Monstres, Joueur)
        ArrayList<Localisable> entites = new ArrayList<>();
        entites.addAll(modele.getGestionnaireBatiments().getBatiments());
        entites.addAll(modele.getUpdateJN().getMonstres());
        entites.add(joueur);

        // Tri : les éléments les plus hauts (Y petit) sont dessinés en premier,
        // les éléments les plus bas (Y grand) par-dessus.
        entites.sort(Comparator.comparingDouble(Localisable::getY));

        // --- PASSE 2 : LES VOLUMES (SPRITES) ---
        for (Localisable entite : entites) {
            if (entite instanceof Batiment) {
                VueBatiment.dessinerSprite(g2d, (Batiment) entite, (int) entite.getX(), (int) entite.getY(), false);
            } else if (entite instanceof Monstre) {
                vueMonstre.dessiner(g2d, (Monstre) entite, (int) entite.getX(), (int) entite.getY(), false);
            } else if (entite instanceof Joueur) {
                // Le joueur et son arme sont dessinés ici pour s'insérer dans le tri
                vueArme.dessiner(g2d);
            }
        }

        // --- #DEV : RENDU DES PV (Toujours au-dessus de tout le reste) ---
        if (modele.isAffichagePV()) {
            VueBarreDeVie.dessiner(g2d, joueur);
            for (Monstre m : modele.getUpdateJN().getMonstres()) {
                VueBarreDeVie.dessiner(g2d, m);
            }
            for (Batiment b : modele.getGestionnaireBatiments().getBatiments()) {
                if (!(b instanceof Mine)) VueBarreDeVie.dessiner(g2d, b);
            }
        }

        // --- RESET TRANSLATION ---
        g2d.translate(camX, camY);

        if (modele.getPartieTerminee()) {
            dessinerGameOver(g2d);
            return;
        }

        dessineMinimap(g2d);
    }

    // (Le reste de la classe reste identique : dessinerGameOver, dessineMinimap, etc.)
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

    public VueHUD getVueHUD() { return vueHUD; }
    public VueArme getVueArme() { return vueArme; }
    public JFrame getMaFenetre() { return maFenetre; }

    public Object identifierElementClique(int x, int y, Object source) {
        if (source == vueHUD.getPageBoutique()) {
            return vueHUD.getPageBoutique().getVueHUDShop().getObjetAuClic(x, y);
        }
        return null;
    }
}