package Vue.HUD;

import Modele.Modele;
import Vue.VueJourNuit;

import javax.swing.*;
import java.awt.*;
import static Modele.Constantes.*;

/**
 * Panneau principal de l'interface utilisateur (Heads-Up Display).
 * Agit comme un conteneur et un routeur dynamique (CardLayout) basculant
 * entre différentes vues indépendantes (État, Action, Boutique) selon l'état du modèle.
 */
public class VueHUD extends JPanel {

    /** ---------- [Propriétés] ---------- **/

    private Modele modele;
    private JPanel panelCartes;
    private CardLayout cardLayout;

    private VueHUDPageEtat pageEtat;
    private VueHUDPageAction pageAction;
    private VueHUDPageBoutique pageBoutique;
    private VueJourNuit vueJourNuit;

    private int dernierePageAffichee = -1;

    /** ---------- [Constructeurs] ---------- **/

    /**
     * Initialise l'architecture du HUD, instancie les sous-vues et configure
     * le système de routage (CardLayout) avec défilement personnalisé.
     *
     * @param modele - Le modèle central de l'application
     */
    public VueHUD(Modele modele) {
        this.modele = modele;
        this.setPreferredSize(new Dimension(LARGEUR_HUD, getHeight()));
        this.setLayout(new BorderLayout());

        panelCartes = new JPanel();
        panelCartes.setOpaque(false);
        cardLayout = new CardLayout();
        panelCartes.setLayout(cardLayout);

        pageEtat = new VueHUDPageEtat(modele);
        pageAction = new VueHUDPageAction(modele);
        pageBoutique = new VueHUDPageBoutique(modele);
        vueJourNuit = new VueJourNuit(modele);

        JScrollPane scrollEtat = creerScroll(pageEtat);
        JScrollPane scrollAction = creerScroll(pageAction);
        JScrollPane scrollBoutique = creerScroll(pageBoutique);

        panelCartes.add(scrollEtat, "PAGE_1");
        panelCartes.add(scrollAction, "PAGE_2");
        panelCartes.add(scrollBoutique, "PAGE_3");
        this.add(panelCartes, BorderLayout.CENTER);

        JPanel espaceJourNuit = new JPanel();
        espaceJourNuit.setOpaque(false);
        espaceJourNuit.setPreferredSize(new Dimension(LARGEUR_HUD, 350));
        this.add(espaceJourNuit, BorderLayout.SOUTH);
    }

    /** ---------- [Accesseurs / Getters] ---------- **/

    public VueHUDPageBoutique getPageBoutique() {
        return pageBoutique;
    }

    public VueHUDPageAction getPageAction() {
        return pageAction;
    }

    public VueHUDEquipement getHUDEquipement() {
        return pageEtat.getVueHUDEquipement();
    }

    public Component getPageEtat() {
        return pageEtat;
    }

    /** ---------- [Méthodes Protégées - Rendu] ---------- **/

    /**
     * Cycle de rendu du composant. Gère l'adaptation de l'ambiance colorimétrique
     * selon le cycle jour/nuit et déclenche la transition visuelle du CardLayout.
     */
    @Override
    protected void paintComponent(Graphics g) {
        if (modele.getHudPageActuelle() == 3) {
            this.setBackground(new Color(255, 215, 0));
        } else if (modele.getLeCycleJourNuit().isDay()) {
            this.setBackground(new Color(112, 216, 255));
        } else {
            this.setBackground(new Color(0, 13, 89));
        }

        super.paintComponent(g);

        int pageActuelle = modele.getHudPageActuelle();
        if (pageActuelle != dernierePageAffichee) {
            cardLayout.show(panelCartes, "PAGE_" + pageActuelle);
            dernierePageAffichee = pageActuelle;
        }

        int yPositionEnBas = getHeight() - 350;
        vueJourNuit.dessiner(g, yPositionEnBas);
    }

    /** ---------- [Méthodes Privées - Configuration] ---------- **/

    /**
     * Encapsule un panneau dans un JScrollPane avec un style "Overlay" personnalisé.
     * La barre de défilement est affinée et superposée au contenu sans altérer sa taille.
     *
     * @param page - Le panneau de contenu à intégrer
     * @return Le JScrollPane configuré
     */
    private JScrollPane creerScroll(JPanel page) {
        JScrollPane scroll = new JScrollPane(page);

        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        scroll.getVerticalScrollBar().setUI(new OverlayScrollBarUI());
        scroll.getVerticalScrollBar().setOpaque(false);

        scroll.setLayout(new ScrollPaneLayout() {
            @Override
            public void layoutContainer(Container parent) {
                JScrollPane scrollPane = (JScrollPane) parent;
                Rectangle availR = scrollPane.getBounds();
                availR.x = availR.y = 0;

                Insets insets = parent.getInsets();
                availR.width -= insets.left + insets.right;
                availR.height -= insets.top + insets.bottom;

                if (viewport != null) {
                    viewport.setBounds(availR);
                }
                if (vsb != null) {
                    Rectangle vsbR = new Rectangle();
                    vsbR.width = 8;
                    vsbR.height = availR.height;
                    vsbR.x = availR.x + availR.width - vsbR.width - 2;
                    vsbR.y = availR.y;
                    vsb.setBounds(vsbR);
                }
            }
        });

        scroll.setComponentZOrder(scroll.getVerticalScrollBar(), 0);
        scroll.setComponentZOrder(scroll.getViewport(), 1);

        return scroll;
    }
}