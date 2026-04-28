package Vue.Batiments;

import Modele.Modele;
import Modele.Batiments.Batiment;
import Modele.Batiments.TenteDeSoin;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Gère l'apparition et l'animation des cœurs produits par les tentes de soin.
 * Cet effet complète l'aura de la tente par un feedback visuel ponctuel au moment
 * où un soin vient réellement d'être appliqué.
 */
public class VueEffetTente {

    /** ---------- [Propriétés] ---------- **/

    private Modele modele;
    private ArrayList particules;

    /** ---------- [Constructeurs] ---------- **/

    /**
     * Initialise le gestionnaire d'effets visuels des tentes à partir du modèle courant.
     *
     * @param modele - Modèle de jeu contenant les bâtiments actifs
     */
    public VueEffetTente(Modele modele) {
        this.modele = modele;
        this.particules = new ArrayList<>();
    }

    /** ---------- [Méthodes Publiques - Cycle de vie] ---------- **/

    /**
     * Met à jour les particules existantes et en génère de nouvelles autour des tentes
     * ayant soigné récemment.
     * La génération reste probabiliste pour conserver un effet vivant sans saturer
     * visuellement l'écran lorsque plusieurs tentes s'activent en parallèle.
     */
    public void miseAJour() {
        // Parcourt l'ensemble des bâtiments pour repérer uniquement les tentes actives visuellement.
        for (Batiment b : modele.getGestionnaireBatiments().getBatiments()) {
            if (b instanceof TenteDeSoin) {
                TenteDeSoin tente = (TenteDeSoin) b;

                // La fenêtre temporelle courte synchronise l'effet avec l'événement de soin récent.
                if (System.currentTimeMillis() - tente.getDernierTempsSoin() < 600) {

                    // La génération partielle évite un empilement de particules trop dense.
                    if (Math.random() > 0.4) {
                        particules.add(new VueParticuleCoeur(tente.getX(), tente.getY(), tente.getHealingRange()));
                    }
                }
            }
        }

        // Les particules vieillissent indépendamment puis sont supprimées dès qu'elles expirent.
        Iterator it = particules.iterator();
        while (it.hasNext()) {
            VueParticuleCoeur p = (VueParticuleCoeur) it.next();
            p.miseAJour();
            if (p.estMorte()) {
                it.remove();
            }
        }
    }

    /** ---------- [Méthodes Publiques - Rendu] ---------- **/

    /**
     * Dessine les particules cœur actuellement actives.
     *
     * @param g2d - Contexte graphique principal
     */
    public void dessiner(Graphics2D g2d) {
        g2d.setColor(Color.RED);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));

        for (Object particule : particules) {
            VueParticuleCoeur p = (VueParticuleCoeur) particule;

            // Chaque particule porte sa propre opacité afin de produire une disparition progressive.
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, p.getOpacite()));
            g2d.drawString("❤", (int) p.getX(), (int) p.getY());
        }

        // Réinitialisation explicite pour ne pas propager la transparence au reste du rendu.
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }
}