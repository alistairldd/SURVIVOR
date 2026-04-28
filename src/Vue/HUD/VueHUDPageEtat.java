package Vue.HUD;

import Modele.Modele;
import Vue.VueJourNuit;
import Vue.VueVie;

import javax.swing.JPanel;
import java.awt.*;
import java.io.IOException;

import static Modele.Constantes.*;

/**
 * Première page du HUD affichant l'état du joueur (Vie, Stats).
 * Allégée de l'affichage des instructions qui sont désormais gérées
 * en Overlay par la Vue principale.
 */
public class VueHUDPageEtat extends JPanel {

    private Modele modele;
    private VueVie vueVie;
    private VueHUDInventaire vueHUDInventaire;
    private VueHUDEquipement vueHUDEquipement;
    private VueJourNuit vueJourNuit;

    public VueHUDPageEtat(Modele modele) {
        this.modele = modele;
        // Transparence pour laisser transparaître la couleur de fond du HUD (Jour/Nuit)
        this.setOpaque(false);

        // Instanciation de nos sous-vues restantes
        this.vueVie = new VueVie(modele);
        this.vueJourNuit = new VueJourNuit(modele);
        this.vueHUDInventaire = new VueHUDInventaire();
        this.vueHUDEquipement = new VueHUDEquipement();

        // Taille de départ
        this.setPreferredSize(new Dimension(LARGEUR_HUD, 600));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;


        // Point de départ (POI initial)
        int y = 20;



        // On dessine en cascade en récupérant le nouveau point bas à chaque fois
        try {
            y = vueVie.dessiner(g, y, (int) (getWidth() * 0.9), 20);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        y = vueHUDEquipement.dessiner(g, y, modele.getJoueur());



        y+=30;

        // On utilise exactement les mêmes appels que dans VueHUDPageAction
        y = vueHUDInventaire.dessiner(g, y, modele, modele.getJoueur());
        // (L'appel à vueHUDInstructions a été supprimé ici)

        //y += 1000; // test scroll conservé

        // --- Redimensionnement dynamique (Trailing Stop) ---
        // Si le contenu dépasse la taille du panneau, on agrandit le panneau pour activer le scroll
        if (y > getPreferredSize().height) {
            this.setPreferredSize(new Dimension(getWidth(), y + 20));
            this.revalidate(); // Prévient le JScrollPane du changement de taille
        }
    }

    public VueHUDEquipement getVueHUDEquipement() {
        return vueHUDEquipement;
    }
}