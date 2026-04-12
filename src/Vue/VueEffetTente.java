package Vue;

import Modele.Modele;
import Modele.Batiments.Batiment;
import Modele.Batiments.TenteDeSoin;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Gère l'apparition et l'animation des coeurs lorsque les tentes de soin sont actives.
 */
public class VueEffetTente {

    private Modele modele;
    private ArrayList<VueParticuleCoeur> particules;

    public VueEffetTente(Modele modele) {
        this.modele = modele;
        this.particules = new ArrayList<>();
    }

    public void miseAJour() {
        // On cherche toutes les tentes sur la carte
        for (Batiment b : modele.getGestionnaireBatiments().getBatiments()) {
            if (b instanceof TenteDeSoin) {
                TenteDeSoin tente = (TenteDeSoin) b;

                // Si la tente a soigné un joueur il y a moins de 600 millisecondes (Le soin ayant un delay de 500)
                if (System.currentTimeMillis() - tente.getDernierTempsSoin() < 600) {

                    // On ajoute aléatoirement une particule pour ne pas saturer l'écran
                    if (Math.random() > 0.4) {
                        particules.add(new VueParticuleCoeur(tente.getX(), tente.getY(), tente.getHealingRange()));
                    }
                }
            }
        }

        // Fait vieillir les particules existantes
        Iterator<VueParticuleCoeur> it = particules.iterator();
        while (it.hasNext()) {
            VueParticuleCoeur p = it.next();
            p.miseAJour();
            if (p.estMorte()) {
                it.remove();
            }
        }
    }

    public void dessiner(Graphics2D g2d) {
        g2d.setColor(Color.RED);
        g2d.setFont(new Font("Arial", Font.BOLD, 20)); // Police assez grosse pour bien voir le coeur

        for (VueParticuleCoeur p : particules) {
            // Applique la transparence dégressive
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, p.getOpacite()));
            g2d.drawString("❤", (int) p.getX(), (int) p.getY()); // Dessine le caractère Unicode du coeur
        }

        // Remet l'opacité à 100% pour la suite du jeu
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }
}