package Vue.HUD;

import Modele.Modele;
import Vue.VueJourNuit;

import javax.swing.*;
import java.awt.*;
import static Modele.Constantes.*;

/**
 * Panneau latéral de l'interface utilisateur (Heads-Up Display).
 * Fonctionne désormais comme un routeur dynamique (CardLayout) qui bascule
 * entre plusieurs pages indépendantes et scrollables selon l'état du Modèle.
 */
public class VueHUD extends JPanel {

    // Référence au modèle pour lire l'état du jeu
    private Modele modele;

    // Le gestionnaire de mise en page en "paquet de cartes"
    private JPanel panelCartes;

    private CardLayout cardLayout;

    // Les 3 pages indépendantes (qui hériteront de JPanel)
    private VueHUDPageEtat pageEtat;
    private VueHUDPageAction pageAction;
    private VueHUDPageBoutique pageBoutique;
    private VueJourNuit vueJourNuit;

    // Mémorise la dernière page affichée pour ne demander le changement que si nécessaire
    private int dernierePageAffichee = -1;

    /**
     * Configure le panneau latéral et instancie ses composants textuels et graphiques.
     * @param modele Le modèle global.
     */
    public VueHUD(Modele modele) {
        this.modele = modele;
        this.setPreferredSize(new Dimension(LARGEUR_HUD, getHeight()));

        this.setLayout(new BorderLayout());

        panelCartes = new JPanel();
        panelCartes.setOpaque(false); // rend le fond transrebnt pour voir la couleur de fond du HUD
        cardLayout = new CardLayout();
        panelCartes.setLayout(cardLayout);


        // Instanciation des 3 pages (Conteneurs indépendants)
        pageEtat = new VueHUDPageEtat(modele);
        pageAction = new VueHUDPageAction(modele);
        pageBoutique = new VueHUDPageBoutique(modele);
        vueJourNuit = new VueJourNuit(modele);

        // Création des ascenseurs (JScrollPane) pour chaque page
        JScrollPane scrollEtat = creerScroll(pageEtat);
        JScrollPane scrollAction = creerScroll(pageAction);
        JScrollPane scrollBoutique = creerScroll(pageBoutique);

        // Ajout du panel de cartes au centre du HUD
        panelCartes.add(scrollEtat, "PAGE_1");
        panelCartes.add(scrollAction, "PAGE_2");
        panelCartes.add(scrollBoutique, "PAGE_3");
        this.add(panelCartes, BorderLayout.CENTER);

        // panneau vide en bas pour laisser de la place au timer
        JPanel espaceJourNuit = new JPanel();
        espaceJourNuit.setOpaque(false);
        espaceJourNuit.setPreferredSize(new Dimension(LARGEUR_HUD, 350));
        this.add(espaceJourNuit, BorderLayout.SOUTH);
    }

    /**
     * Méthode utilitaire pour configurer proprement les barres de défilement en Overlay.
     */
    private JScrollPane creerScroll(JPanel page) {
        JScrollPane scroll = new JScrollPane(page);

        // Configuration de base
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        // 1. Applique notre design personnalisé (transparence, pas de boutons)
        scroll.getVerticalScrollBar().setUI(new OverlayScrollBarUI());
        scroll.getVerticalScrollBar().setOpaque(false);

        // 2. Modifie le Layout pour superposer la barre au-dessus du contenu
        scroll.setLayout(new ScrollPaneLayout() {
            @Override
            public void layoutContainer(Container parent) {
                JScrollPane scrollPane = (JScrollPane) parent;
                Rectangle availR = scrollPane.getBounds();
                availR.x = availR.y = 0;

                Insets insets = parent.getInsets();
                availR.width -= insets.left + insets.right;
                availR.height -= insets.top + insets.bottom;

                // Le contenu prend 100% de la largeur
                if (viewport != null) {
                    viewport.setBounds(availR);
                }
                // La barre se dessine par-dessus, tout à droite
                if (vsb != null) {
                    Rectangle vsbR = new Rectangle();
                    vsbR.width = 8; // Largeur très fine pour la barre
                    vsbR.height = availR.height;
                    vsbR.x = availR.x + availR.width - vsbR.width - 2; // -2 pour une petite marge à droite
                    vsbR.y = availR.y;
                    vsb.setBounds(vsbR);
                }
            }
        });

        // 3. Assure que la barre passe bien au premier plan par rapport au contenu
        scroll.setComponentZOrder(scroll.getVerticalScrollBar(), 0);
        scroll.setComponentZOrder(scroll.getViewport(), 1);

        return scroll;
    }

    /**
     * Méthode appelée à chaque rafraîchissement (déclenché par Redessine).
     * S'occupe de changer la couleur de fond et d'afficher la bonne carte.
     */
    @Override
    protected void paintComponent(Graphics g) {

        // --- 1. GESTION DE L'AMBIANCE VISUELLE (Couleur de fond) ---
        if (modele.getHudPageActuelle() == 3) {
            this.setBackground(new Color(255, 215, 0)); // Fond doré pour le shop
        } else if (modele.getLeCycleJourNuit().isDay()) {
            this.setBackground(new Color(112, 216, 255)); // Bleu ciel clair pour le jour
        } else {
            this.setBackground(new Color(0, 13, 89)); // Bleu très sombre pour la nuit
        }

        // Nettoie le panneau avec la couleur définie ci-dessus
        super.paintComponent(g);

        // --- 2. GESTION DU ROUTAGE ---
        // Vérifie si l'utilisateur a pressé une touche pour changer de page
        int pageActuelle = modele.getHudPageActuelle();
        if (pageActuelle != dernierePageAffichee) {
            // Demande au CardLayout d'afficher la carte correspondante
            cardLayout.show(panelCartes, "PAGE_" + pageActuelle);
            dernierePageAffichee = pageActuelle;
        }
        // --- 3. AFFICHAGE DU TIMER ---
        // On calcule la position Y pour que l'image apparaisse en bas du HUD
        // TAILLE_LS (250) + environ 100 pixels pour le texte et les marges
        int yPositionEnBas = getHeight() - 350;

        // On appelle la méthode dessiner avec cette nouvelle coordonnée
        vueJourNuit.dessiner(g, yPositionEnBas);
    }

    public VueHUDPageBoutique getPageBoutique() {
        return pageBoutique;
    }

    public VueHUDPageAction getPageAction() {
        return pageAction;
    }

    public VueHUDEquipement getHUDEquipement() {
        return pageEtat.getVueHUDEquipement(); // ou pageAction selon où elle est instanciée
    }

    public Component getPageEtat() {
        return pageEtat;
    }
}