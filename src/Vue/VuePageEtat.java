package Vue;

import Modele.Modele;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Dimension;
import static Modele.Constantes.*;

public class VuePageEtat extends JPanel {
    private Modele modele;
    private VueVie vueVie;
    private VueInstructions vueInstructions;
    private VueJourNuit vueJourNuit;

    public VuePageEtat(Modele modele) {
        this.modele = modele;
        // Transparence pour laisser transparaître la couleur de fond du HUD (Jour/Nuit)
        this.setOpaque(false);

        // Instanciation de nos sous-vues
        this.vueVie = new VueVie(modele);
        this.vueInstructions = new VueInstructions();
        this.vueJourNuit = new VueJourNuit(modele);

        // Taille de départ
        this.setPreferredSize(new Dimension(LARGEUR_HUD, 600));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Point de départ (POI initial)
        int y = 20;

        // On dessine en cascade en récupérant le nouveau point bas à chaque fois
        y = vueVie.dessiner(g, y, (int) (getWidth() * 0.9), 20);
        y = vueInstructions.dessiner(g, y, modele.getJoueur());
        y = vueJourNuit.dessiner(g, y);

        y += 1000; //test scroll
        // --- Redimensionnement dynamique (Trailing Stop) ---
        // Si le contenu dépasse la taille du panneau, on agrandit le panneau pour activer le scroll
        if (y > getPreferredSize().height) {
            this.setPreferredSize(new Dimension(getWidth(), y + 20));
            this.revalidate(); // Prévient le JScrollPane du changement de taille
        }
    }
}