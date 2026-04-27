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
import Vue.HUD.VueHUDEquipement;
import Vue.HUD.VueHUDInstructions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Architecture de rendu hybride (JLayeredPane).
 * Gère le cycle de rendu 2.5D : Passe Sol, Y-Sorting, Passe Volume et Fantôme RTS.
 */
public class Vue extends JPanel {

    private JFrame maFenetre;
    private JLayeredPane layeredPane;

    // Composants UI & Moteurs de rendu
    private final VueHUD vueHUD;
    private final VueCarte vueCarte;
    private final VueArme vueArme;
    private final VueRessource vueRessource;
    private final VueBatiment vueBatiment;
    private final VueMonstre vueMonstre;

    // Logique métier
    private final Modele modele;

    // Systèmes d'effets visuels
    private VueEffetSoin vueEffetSoin;
    private VueEffetTente vueEffetTente;

    // NOUVEAU : Système de feedback visuel (textes flottants d'erreur)
    private final VueTexteFlottant vueTexteFlottant;

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

        // 5. Initialisation du système de textes flottants
        this.vueTexteFlottant = new VueTexteFlottant();

        // 6. Initialisation du composant Overlay
        this.vueInstructions = new VueHUDInstructions(modele);

        // --- ASSEMBLAGE DES COUCHES ---
        layeredPane.add(this, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(vueInstructions, JLayeredPane.PALETTE_LAYER);

        maFenetre.add(layeredPane, BorderLayout.CENTER);

        // 7. Injection des Contrôleurs
        ControleurSouris controleurSouris = new ControleurSouris(this, modele);
        this.addMouseListener(controleurSouris);
        this.addMouseMotionListener(controleurSouris);
        this.addKeyListener(new ControleurClavier(this, modele));

        // Liaison du shop pour la détection de clic
        this.vueHUD.getPageBoutique().addMouseListener(controleurSouris);
        this.vueHUD.getPageEtat().addMouseListener(controleurSouris);


        this.vueArme = new VueArme(controleurSouris, this, modele);

        // Gestion du redimensionnement dynamique
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

        // --- PASSE 1 : LES AURAS ET EFFETS AU SOL (Layering) ---
        for (Batiment b : modele.getGestionnaireBatiments().getBatiments()) {
            VueBatiment.dessinerAura(g2d, b, (int) b.getX(), (int) b.getY());
        }

        vueEffetSoin.miseAJour();
        vueEffetSoin.dessiner(g2d);

        vueEffetTente.miseAJour();
        vueEffetTente.dessiner(g2d);

        // --- TRI PAR PROFONDEUR (Y-Sorting) ---
        ArrayList<Localisable> entites = new ArrayList<>();
        entites.addAll(modele.getGestionnaireBatiments().getBatiments());
        entites.addAll(modele.getUpdateJN().getMonstres());
        entites.add(joueur);

        entites.sort(Comparator.comparingDouble(Localisable::getY));

        // --- PASSE 2 : LES VOLUMES (Sprites) ---
        for (Localisable entite : entites) {
            if (entite instanceof Batiment) {
                VueBatiment.dessinerSprite(g2d, (Batiment) entite, (int) entite.getX(), (int) entite.getY(), false);
            } else if (entite instanceof Monstre) {
                vueMonstre.dessiner(g2d, (Monstre) entite, (int) entite.getX(), (int) entite.getY(), false);
            } else if (entite instanceof Joueur) {
                vueArme.dessiner(g2d);
            }
        }

        // --- PASSE 3 : FANTÔME DE CONSTRUCTION (RTS) ---
        dessinerFantomeConstruction(g2d);

        // --- PASSE 4 : OVERLAYS (PV / Barres de vie) ---
        if (modele.isAffichagePV()) {
            VueBarreDeVie.dessiner(g2d, joueur);
            for (Monstre m : modele.getUpdateJN().getMonstres()) {
                VueBarreDeVie.dessiner(g2d, m);
            }
            for (Batiment b : modele.getGestionnaireBatiments().getBatiments()) {
                if (!(b instanceof Mine)) VueBarreDeVie.dessiner(g2d, b);
            }
        }

        // --- PASSE 4.5 : DEBUG HITBOXES (Temporaire pour calibrage 2.5D) ---
        // Affiche la base physique (Hitbox) de chaque bâtiment en rouge
        for (Batiment b : modele.getGestionnaireBatiments().getBatiments()) {
            Graphics2D g2dDebug = (Graphics2D) g2d.create();

            // On calcule le centre de la hitbox (avec le fameux Offset Y)
            double centreHitboxY = b.getY() + b.getOffsetYHitbox();

            g2dDebug.translate(b.getX(), centreHitboxY);
            g2dDebug.rotate(b.getAngleRotation());

            // Rectangle rouge transparent
            g2dDebug.setColor(new Color(255, 0, 0, 120));
            g2dDebug.fillRect((int)(-b.getLargeurHitbox()/2.0), (int)(-b.getHauteurHitbox()/2.0), b.getLargeurHitbox(), b.getHauteurHitbox());

            // Bordure rouge vif
            g2dDebug.setColor(Color.RED);
            g2dDebug.setStroke(new BasicStroke(2));
            g2dDebug.drawRect((int)(-b.getLargeurHitbox()/2.0), (int)(-b.getHauteurHitbox()/2.0), b.getLargeurHitbox(), b.getHauteurHitbox());

            g2dDebug.dispose();
        }

        // --- PASSE 5 : TEXTES FLOTTANTS (Feedback d'erreur) ---
        // Dessinés en dernier dans l'espace monde pour apparaître au-dessus de tout
        vueTexteFlottant.miseAJour();
        vueTexteFlottant.dessiner(g2d);

        // --- RESET TRANSLATION ---
        g2d.translate(camX, camY);

        if (modele.getPartieTerminee()) {
            dessinerGameOver(g2d);
            return;
        }

        dessineMinimap(g2d);
    }

    /**
     * Affiche un texte rouge flottant à une position dans l'espace monde.
     * À appeler depuis les contrôleurs lorsqu'une action échoue.
     *
     * @param texte   Message à afficher (ex. "Impossible !", "Rechargement…")
     * @param mondeX  Coordonnée X dans l'espace monde
     * @param mondeY  Coordonnée Y dans l'espace monde
     */
    public void afficherTexteErreur(String texte, double mondeX, double mondeY) {
        vueTexteFlottant.ajouter(texte, mondeX, mondeY);
    }

    /**
     * Dessine l'hologramme du bâtiment sous la souris avec indicateur visuel de collision.
     */
    /**
     * Dessine l'hologramme du bâtiment sous la souris avec indicateur visuel de collision.
     */
    /**
     * Dessine l'hologramme du bâtiment sous la souris avec indicateur visuel de collision rectangulaire.
     */
    private void dessinerFantomeConstruction(Graphics2D g2d) {
        Modele.TypeConstruction mode = modele.getModeConstruction();
        if (mode == Modele.TypeConstruction.AUCUN) return;

        double sourisX = modele.getSourisMondeX();
        double sourisY = modele.getSourisMondeY();

        Image imgFantome = null;
        int taille = 0;
        int range = 0;
        int drawY = 0;

        // --- NOUVELLES VARIABLES POUR L'ENCOMBREMENT RECTANGULAIRE ---
        double wEnc = 0;
        double hEnc = 0;
        double angle = 0;

        // 1. Définition des propriétés selon l'objet tenu
        if (mode == Modele.TypeConstruction.TOUR) {
            imgFantome = IMAGE_TOUR;
            taille = TAILLE_TOUR;
            wEnc = TOUR_LARGEUR_ENC;
            hEnc = TOUR_HAUTEUR_ENC;
            angle = 0; // Bâtiment droit
            range = TOWER_BASE_RANGE;
            drawY = (int) sourisY - (taille * 4 / 5);
        }
        else if (mode == Modele.TypeConstruction.TENTE) {
            imgFantome = IMAGE_TENTE;
            taille = TAILLE_TENTE;
            wEnc = TENTE_LARGEUR_ENC;
            hEnc = TENTE_HAUTEUR_ENC;
            angle = 0; // Bâtiment droit
            range = HEALING_RANGE;
            drawY = (int) sourisY - (taille / 2);
        }
        else if (mode == Modele.TypeConstruction.ABATIS) {
            imgFantome = modele.isRotationAbatis() ? IMAGE_ABATIS_2 : IMAGE_ABATIS_1;
            taille = TAILLE_ABATIS;
            wEnc = ABATIS_LARGEUR;
            hEnc = ABATIS_HAUTEUR;
            // Angle positif ou négatif selon la rotation (OBB)
            angle = modele.isRotationAbatis() ? -ABATIS_ANGLE_RAD : ABATIS_ANGLE_RAD;
            range = 0; // Pas d'aura de portée pour un mur
            drawY = (int) sourisY - (taille / 2);
        }

        if (imgFantome != null) {
            int drawX = (int) sourisX - (taille / 2);
            Joueur joueur = modele.getJoueur();

            // 2. Vérification d'intégrité (Ressources)
            boolean aLesFonds = false;
            if (mode == Modele.TypeConstruction.TOUR) aLesFonds = joueur.aAssezDeRessources(COUT_TOUR);
            else if (mode == Modele.TypeConstruction.TENTE) aLesFonds = joueur.aAssezDeRessources(COUT_TENTE);
            else if (mode == Modele.TypeConstruction.ABATIS) aLesFonds = joueur.aAssezDeRessources(COUT_ABATIS);

            // NOUVEAU : On utilise la nouvelle signature du moteur de collision (Phase 2)
            boolean constructible = modele.peutConstruireIci(sourisX, sourisY, wEnc, hEnc, angle) && aLesFonds;

            // 3. Rendu de l'Aura de portée dynamique (Seulement si range > 0)
            if (range > 0) {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
                g2d.setColor(constructible ? Color.GREEN : Color.RED);
                g2d.fillOval((int) sourisX - range, (int) sourisY - range, range * 2, range * 2);

                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawOval((int) sourisX - range, (int) sourisY - range, range * 2, range * 2);
                g2d.setStroke(new BasicStroke(1));
            }

            // 4. Rendu du Sprite Fantôme (Translucide)
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
            g2d.drawImage(imgFantome, drawX, drawY, null);

            // 5. NOUVEAU : Overlay d'encombrement (Zone de construction exacte)
            // On affiche le rectangle exact (vert si OK, rouge si bloqué/sans argent)
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
            g2d.setColor(constructible ? Color.GREEN : Color.RED);

            // On crée un calque temporaire pour la rotation
            Graphics2D g2dRotate = (Graphics2D) g2d.create();
            g2dRotate.translate(sourisX, sourisY); // On place l'origine au centre
            g2dRotate.rotate(angle); // On pivote le calque

            // On dessine le rectangle centré sur sa nouvelle origine
            g2dRotate.fillRect((int)(-wEnc / 2), (int)(-hEnc / 2), (int)wEnc, (int)hEnc);

            // Petite bordure pour que le joueur visualise parfaitement les limites
            g2dRotate.setColor(constructible ? new Color(0, 200, 0) : new Color(200, 0, 0));
            g2dRotate.setStroke(new BasicStroke(2));
            g2dRotate.drawRect((int)(-wEnc / 2), (int)(-hEnc / 2), (int)wEnc, (int)hEnc);

            g2dRotate.dispose(); // On détruit le calque temporaire

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }
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

    public VueHUD getVueHUD() { return vueHUD; }
    public VueArme getVueArme() { return vueArme; }
    public JFrame getMaFenetre() { return maFenetre; }

    public Object identifierElementClique(int x, int y, Object source) {
        if (source == vueHUD.getPageBoutique()) {
            return vueHUD.getPageBoutique().getVueHUDShop().getObjetAuClic(x, y);
        }
        return null;
    }


    public VueHUDEquipement getVueHUDEquipement() {
        return vueHUD.getHUDEquipement();
    }
}