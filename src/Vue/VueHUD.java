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
    private VuePageEtat pageEtat;
    private VuePageAction pageAction;
    private VuePageBoutique pageBoutique;

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
        pageEtat = new VuePageEtat(modele);
        pageAction = new VuePageAction(modele);
        pageBoutique = new VuePageBoutique(modele);

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
     * Méthode utilitaire pour configurer proprement les barres de défilement.
     */
    private JScrollPane creerScroll(JPanel page) {
        JScrollPane scroll = new JScrollPane(page);
        // Cache la barre horizontale, affiche la verticale seulement si l'écran est trop petit
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Rend le fond transparent pour laisser transparaître la couleur du HUD
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null); // Retire la bordure native inesthétique

        // Accélère la vitesse de défilement de la molette
        scroll.getVerticalScrollBar().setUnitIncrement(16);
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