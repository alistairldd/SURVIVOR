package Vue;

import Modele.Modele;
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
    private CardLayout cardLayout;

    // Les 3 pages indépendantes (qui hériteront de JPanel)
    private VueHUDPageEtat pageEtat;
    private VueHUDPageAction pageAction;
    private VueHUDPageBoutique pageBoutique;

    // Mémorise la dernière page affichée pour ne demander le changement que si nécessaire
    private int dernierePageAffichee = -1;

    /**
     * Configure le panneau latéral et instancie ses composants textuels et graphiques.
     * @param modele Le modèle global.
     */
    public VueHUD(Modele modele) {
        this.modele = modele;
        this.setPreferredSize(new Dimension(LARGEUR_HUD, getHeight()));

        // Initialisation du CardLayout comme gestionnaire principal
        cardLayout = new CardLayout();
        this.setLayout(cardLayout);

        // Instanciation des 3 pages (Conteneurs indépendants)
        pageEtat = new VueHUDPageEtat(modele);
        pageAction = new VueHUDPageAction(modele);
        pageBoutique = new VueHUDPageBoutique(modele);

        // Création des ascenseurs (JScrollPane) pour chaque page
        JScrollPane scrollEtat = creerScroll(pageEtat);
        JScrollPane scrollAction = creerScroll(pageAction);
        JScrollPane scrollBoutique = creerScroll(pageBoutique);

        // Ajout des ascenseurs au layout avec un identifiant "String" unique
        this.add(scrollEtat, "PAGE_1");
        this.add(scrollAction, "PAGE_2");
        this.add(scrollBoutique, "PAGE_3");
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
            cardLayout.show(this, "PAGE_" + pageActuelle);
            dernierePageAffichee = pageActuelle;
        }
    }
}