package Vue.Batiments;

import Modele.Modele;
import Modele.Batiments.Batiment;
import Modele.Joueur;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Gère le rendu visuel (particules et zone d'effet) lorsqu'une réparation est en cours.
 */
public class VueEffetSoin {

    private Modele modele;
    private ArrayList<VueParticuleSoin> particules;

    public VueEffetSoin(Modele modele) {
        this.modele = modele;
        this.particules = new ArrayList<>();
    }

    /**
     * Calcule la nouvelle position des particules et en génère de nouvelles si le soin est actif.
     * À appeler à chaque "tick" graphique de ton jeu.
     */
    public void miseAJour() {
        Joueur joueur = modele.getJoueur();
        Batiment cible = modele.getJoueur().getBatimentEnReparation();

        // Si une réparation est en cours, on fait "poper" de nouvelles particules (ex: 2 par rafraîchissement)
        if (cible != null) {
            for (int i = 0; i < 2; i++) {
                particules.add(new VueParticuleSoin(cible.getX(), cible.getY(), cible.getHealingRange()));
            }
        }

        // On fait vieillir et monter les particules existantes
        Iterator<VueParticuleSoin> it = particules.iterator();
        while (it.hasNext()) {
            VueParticuleSoin p = it.next();
            p.miseAJour();
            // Si la particule a terminé son cycle de vie, on la supprime de la mémoire
            if (p.estMorte()) {
                it.remove();
            }
        }
    }

    /**
     * Dessine le cercle d'aura vert et toutes les petites particules "+".
     * @param g2d Le pinceau graphique principal
     */
    public void dessiner(Graphics2D g2d) {
        Joueur joueur = modele.getJoueur();
        Batiment cible = modele.getJoueur().getBatimentEnReparation();

        // --- 1. Dessin de la zone de soin (Cercle vert au sol) ---
        if (cible != null) {
            int rayon = cible.getHealingRange();
            int diametre = rayon * 2;
            int cx = (int) cible.getX();
            int cy = (int) cible.getY();

            // Fond très transparent (15% d'opacité)
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.15f));
            g2d.setColor(Color.GREEN);
            g2d.fillOval(cx - rayon, cy - rayon, diametre, diametre);

            // Bordure un peu plus marquée (40% d'opacité)
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(cx - rayon, cy - rayon, diametre, diametre);
            g2d.setStroke(new BasicStroke(1)); // Réinitialise l'épaisseur
        }

        // --- 2. Dessin des particules flottantes ("+") ---
        g2d.setColor(Color.GREEN);
        g2d.setFont(new Font("Arial", Font.BOLD, 18));

        for (VueParticuleSoin p : particules) {
            // Applique l'opacité individuelle calculée par la particule (effet de disparition)
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, p.getOpacite()));
            g2d.drawString("+", (int) p.getX(), (int) p.getY());
        }

        // Sécurité : Remet l'opacité globale à 100% pour ne pas rendre le reste du jeu transparent
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }
}