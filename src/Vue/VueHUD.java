package Vue;

import Modele.Modele;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/*
 * La classe de la vue du HUD, elle est utilisée pour afficher les informations du joueur et les ressources.
 * Elle est utilisée dans la classe Vue pour afficher les informations du joueur et les ressources.
 *
 */
public class VueHUD extends JPanel {

    public final static int LARGEUR = 300;

    private Modele modele;

    /* Vue du jour et de la nuit, elle est utilisée pour afficher l'état du jour et de la nuit. */
    private VueJourNuit vueJourNuit;
    private VueInventaire vueInventaire;


    public VueHUD(Modele modele) {

        /* Initialisation du panneau droit de la fenêtre, il est utilisé pour afficher les informations du joueur et les ressources. */
        this.setPreferredSize(new Dimension(LARGEUR, getHeight())); // Définit la taille préférée du panneau
        this.setBackground(new Color(0, 255, 255)); // Définit la couleur de fond du panneau
        this.setLayout(new BorderLayout());

        this.modele = modele;
        vueJourNuit = new VueJourNuit(modele.getLeCycleJourNuit());
        this.vueInventaire = new VueInventaire();
    }

    /* ---- GETTERS ET SETTERS ---- */

    // Dessiner les éléments du HUD
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Dessin de l'inventaire en haut du HUD
        vueInventaire.dessiner(g, 40, modele.getJoueur());
        vueJourNuit.dessiner(g, getHeight());
        if (modele.getLeCycleJourNuit().isDay()){
            this.setBackground(new Color(0, 255, 255));

        } else {
            this.setBackground(new Color(0, 0, 153));
        }

    }
}
