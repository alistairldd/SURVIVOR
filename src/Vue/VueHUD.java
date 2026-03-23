package Vue;

import Modele.Modele;
import static Modele.Constantes.*;
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
/**
 * Panneau latéral de l'interface utilisateur (Heads-Up Display).
 * Il regroupe l'inventaire, les instructions de jeu, et l'indicateur visuel du cycle Jour/Nuit.
 * Il est indépendant du système de Caméra de la Vue principale.
 */
public class VueHUD extends JPanel {

    // Référence au modèle pour lire l'état du jeu (cycle, inventaire du joueur)
    private Modele modele;

    /* Vue du jour et de la nuit, elle est utilisée pour afficher l'état du jour et de la nuit. */
    // Sous-panneaux gérant des sections spécifiques de l'interface
    private VueJourNuit vueJourNuit;
    private VueInventaire vueInventaire;
    private VueBatHud vueBatHud;
    private VueInstructions vueInstructions;
    private VueVie vueVie;
    private VueShop vueShop = new VueShop();


    /**
     * Configure le panneau latéral et instancie ses composants textuels et graphiques.
     * @param modele Le modèle global.
     */
    public VueHUD(Modele modele) {

        /* Initialisation du panneau droit de la fenêtre, il est utilisé pour afficher les informations du joueur et les ressources. */
        this.setPreferredSize(new Dimension(LARGEUR_HUD, getHeight())); // Définit la taille préférée du panneau (fixe en largeur, flexible en hauteur)
        this.setBackground(new Color(0, 255, 255)); // Définit la couleur de fond du panneau (cyan par défaut)
        this.setLayout(new BorderLayout());

        // Sauvegarde le modèle et prépare les sous-vues spécialisées
        this.modele = modele;
        vueJourNuit = new VueJourNuit(modele.getLeCycleJourNuit());
        this.vueInventaire = new VueInventaire();
        this.vueBatHud = new VueBatHud();
        this.vueInstructions = new VueInstructions();
        this.vueVie = new VueVie(modele);
        this.vueShop = new VueShop();


    }

    /* ---- GETTERS ET SETTERS ---- */

    // Dessiner les éléments du HUD
    /**
     * Méthode appelée à chaque rafraîchissement (déclenché par Redessine).
     * Modifie l'ambiance globale du panneau et appelle les sous-vues pour dessiner les textes et images.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Nettoie le panneau avec la couleur de fond actuelle
        if (modele.isShopOuvert()) {
            this.setBackground(new Color(255, 215, 0)); // Fond doré pour le shop
            vueShop.dessiner(g, 50, modele.getJoueur());
        }else {
         // Dessin de l'inventaire en haut du HUD (Y=40)
        vueInventaire.dessiner(g, (int) (getHeight()*0.3), modele, modele.getJoueur());

        // Dessin des batîments un peu plus bas (Y=200)
        vueBatHud.dessiner(g, (int) (getHeight()*0.42), modele, modele.getJoueur());

        // Demande à la vue des instructions de s'afficher un peu plus bas (Y=200)
        vueInstructions.dessiner(g, (int) (getHeight()*0.6), modele.getJoueur());

        vueVie.dessiner(g, (int) (getHeight()*0.01), (int) (getWidth()*0.9), 20);
        // Affiche l'image lune/soleil tout en bas du panneau
        vueJourNuit.dessiner(g, getHeight());

        // --- GESTION DE L'AMBIANCE VISUELLE ---


            // Change dynamiquement la couleur de fond du panneau entier en fonction du cycle temporel du Modèle
            if (modele.getLeCycleJourNuit().isDay()) {
                // Bleu ciel clair pour le jour
                this.setBackground(new Color(112, 216, 255));

            } else {
                // Bleu très sombre pour la nuit
                this.setBackground(new Color(0, 13, 89));
            }
        }

    }
}