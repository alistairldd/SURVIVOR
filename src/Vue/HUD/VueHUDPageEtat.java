package Vue.HUD;

import Modele.Modele;
import Vue.VueJourNuit;
import Vue.VueVie;

import javax.swing.JPanel;
import java.awt.*;
import java.io.IOException;

import static Modele.Constantes.*;

/**
 * Première page du HUD affichant l'état consolidé du joueur (Santé, Équipement, Stocks).
 * Centralise les rendus vitaux et orchestre l'extension verticale du panneau.
 */
public class VueHUDPageEtat extends JPanel {

    /** ---------- [Propriétés] ---------- **/

    private Modele modele;
    private VueVie vueVie;
    private VueHUDInventaire vueHUDInventaire;
    private VueHUDEquipement vueHUDEquipement;
    private VueJourNuit vueJourNuit;

    /** ---------- [Constructeurs] ---------- **/

    /**
     * Initialise la page d'État et déploie les sous-vues dédiées.
     *
     * @param modele - Instance du modèle métier
     */
    public VueHUDPageEtat(Modele modele) {
        this.modele = modele;
        this.setOpaque(false);

        this.vueVie = new VueVie(modele);
        this.vueJourNuit = new VueJourNuit(modele);
        this.vueHUDInventaire = new VueHUDInventaire();
        this.vueHUDEquipement = new VueHUDEquipement();

        this.setPreferredSize(new Dimension(LARGEUR_HUD, 600));
    }

    /** ---------- [Accesseurs] ---------- **/

    public VueHUDEquipement getVueHUDEquipement() {
        return vueHUDEquipement;
    }

    /** ---------- [Méthodes Protégées - Cycle de Rendu] ---------- **/

    /**
     * Coordonne le rendu visuel en cascade, chaque sous-composant transmettant
     * son encombrement final (Y) pour le composant suivant.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int y = 20;

        try {
            y = vueVie.dessiner(g, y, (int) (getWidth() * 0.9), 20);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        y = vueHUDEquipement.dessiner(g, y, modele.getJoueur());

        y += 30;

        y = vueHUDInventaire.dessiner(g, y, modele, modele.getJoueur());

        // Ajustement automatique du conteneur en cas de dépassement pour activer le scroll
        if (y > getPreferredSize().height) {
            this.setPreferredSize(new Dimension(getWidth(), y + 20));
            this.revalidate();
        }
    }
}