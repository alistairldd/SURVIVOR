package Vue.Batiments;

import Modele.Modele;
import Modele.Batiments.Batiment;
import Modele.Joueur;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Gère le rendu visuel d'une réparation en cours.
 * Combine une zone de soin au sol et un système de particules pour rendre
 * l'action de réparation perceptible sans interface supplémentaire.
 */
public class VueEffetSoin {

    /** ---------- [Propriétés] ---------- **/

    private Modele modele;
    private ArrayList particules;

    /** ---------- [Constructeurs] ---------- **/

    /**
     * Initialise le gestionnaire visuel des effets de soin liés au modèle courant.
     *
     * @param modele - Modèle de jeu fournissant le joueur et la cible en réparation
     */
    public VueEffetSoin(Modele modele) {
        this.modele = modele;
        this.particules = new ArrayList<>();
    }

    /** ---------- [Méthodes Publiques - Cycle de vie] ---------- **/

    /**
     * Met à jour les particules existantes et en génère de nouvelles tant qu'une
     * réparation est active.
     * Le rayon de génération tient compte de la taille réelle du bâtiment pour que
     * l'effet visuel épouse correctement son encombrement.
     */
    public void miseAJour() {
        Joueur joueur = modele.getJoueur();
        Batiment cible = modele.getJoueur().getBatimentEnReparation();

        if (cible != null) {
            int dimensionMax = Math.max(cible.getLargeurHitbox(), cible.getHauteurHitbox());
            int rayonDynamique = cible.getHealingRange() + (dimensionMax / 2);

            // Plusieurs particules par tick évitent un rendu trop pauvre pendant l'animation.
            for (int i = 0; i < 2; i++) {
                particules.add(new VueParticuleSoin(cible.getX(), cible.getY(), rayonDynamique));
            }
        }

        // Les particules sont mises à jour puis supprimées dès qu'elles sortent de leur cycle de vie.
        Iterator it = particules.iterator();
        while (it.hasNext()) {
            VueParticuleSoin p = (VueParticuleSoin) it.next();
            p.miseAJour();
            if (p.estMorte()) {
                it.remove();
            }
        }
    }

    /** ---------- [Méthodes Publiques - Rendu] ---------- **/

    /**
     * Dessine l'aura de soin et les particules associées à la réparation courante.
     *
     * @param g2d - Contexte graphique principal
     */
    public void dessiner(Graphics2D g2d) {
        Joueur joueur = modele.getJoueur();
        Batiment cible = modele.getJoueur().getBatimentEnReparation();

        if (cible != null) {
            int dimensionMax = Math.max(cible.getLargeurHitbox(), cible.getHauteurHitbox());
            int rayonDynamique = cible.getHealingRange() + (dimensionMax / 2);

            int diametre = rayonDynamique * 2;
            int cx = (int) cible.getX();
            int cy = (int) cible.getY();

            // L'aura matérialise la zone effective du soin autour du bâtiment réparé.
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.15f));
            g2d.setColor(Color.GREEN);
            g2d.fillOval(cx - rayonDynamique, cy - rayonDynamique, diametre, diametre);

            // La bordure rend la lecture de la portée plus nette que le simple remplissage.
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(cx - rayonDynamique, cy - rayonDynamique, diametre, diametre);
            g2d.setStroke(new BasicStroke(1));
        }

        g2d.setColor(Color.GREEN);
        g2d.setFont(new Font("Arial", Font.BOLD, 18));

        for (Object particule : particules) {
            VueParticuleSoin p = (VueParticuleSoin) particule;

            // Chaque particule pilote sa propre opacité pour produire un fondu progressif.
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, p.getOpacite()));
            g2d.drawString("+", (int) p.getX(), (int) p.getY());
        }

        // Réinitialisation explicite pour éviter d'impacter les rendus suivants.
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }
}