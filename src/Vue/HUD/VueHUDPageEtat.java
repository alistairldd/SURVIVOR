package Vue.HUD;

import Modele.Modele;
import Vue.VueJourNuit;
import Vue.VueVie;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Dimension;
import java.io.IOException;

import static Modele.Constantes.*;

public class VueHUDPageEtat extends JPanel {
    private Modele modele;
    private VueVie vueVie;
    private VueHUDInstructions vueHUDInstructions;
    private VueJourNuit vueJourNuit;

    public VueHUDPageEtat(Modele modele) {
        this.modele = modele;
        // Transparence pour laisser transparaître la couleur de fond du HUD (Jour/Nuit)
        this.setOpaque(false);

        // Instanciation de nos sous-vues
        this.vueVie = new VueVie(modele);
        this.vueHUDInstructions = new VueHUDInstructions();
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
        try {
            y = vueVie.dessiner(g, y, (int) (getWidth() * 0.9), 20);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        y = vueHUDInstructions.dessiner(g, y, modele.getJoueur());
        // y = vueJourNuit.dessiner(g, y); (timer en doublons)

        y += 1000; //test scroll
        // --- Redimensionnement dynamique (Trailing Stop) ---
        // Si le contenu dépasse la taille du panneau, on agrandit le panneau pour activer le scroll
        if (y > getPreferredSize().height) {
            this.setPreferredSize(new Dimension(getWidth(), y + 20));
            this.revalidate(); // Prévient le JScrollPane du changement de taille
        }
    }
}