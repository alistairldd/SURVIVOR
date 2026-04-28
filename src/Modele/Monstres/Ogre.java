package Modele.Monstres;

import Modele.GestionnaireMonstres;

import java.awt.*;

import static Modele.Constantes.IMAGE_OGRE;

/**
 * Implémentation concrète d'un ennemi lourd : l'Ogre.
 * Lent mais possède un grand nombre de PV et une force de frappe élevée.
 */
public class Ogre extends Monstre {

    /** ---------- [Propriétés] ---------- **/

    private final GestionnaireMonstres gestionnaireMonstres;

    /** ---------- [Constructeurs] ---------- **/

    public Ogre(int x, int y, GestionnaireMonstres gestionnaireMonstres) {
        super("Ogre", 300, 15, 50, 4, 10);

        this.x = x;
        this.y = y;
        this.gestionnaireMonstres = gestionnaireMonstres;
        this.start();
    }

    /** ---------- [Accesseurs] ---------- **/

    @Override
    public Image getImage() {
        return IMAGE_OGRE;
    }

    /** ---------- [Méthodes Héritées - Boucle de Vie (Thread)] ---------- **/

    /**
     * Gère la boucle comportementale autonome de l'Ogre (Déplacement, Animation, Mort).
     */
    @Override
    public void run() {
        double dt = 0.05;

        while (true) {
            try {
                // Gestion de la bascule d'animation (Marche / Attaque)
                if (marche) {
                    animation += 1;
                    if (animation % 10 == 0) {
                        animationMarche = !animationMarche;
                    }
                } else {
                    animationAtt += 1;
                    if (animationAtt % 10 == 0) {
                        animationAttaque = !animationAttaque;
                    }
                }

                this.mettreAJourPosition(gestionnaireMonstres.trouverCible(this), dt);
                Thread.sleep(50);

                // Conditions de décès
                if (this.getHp() <= 0) {
                    gestionnaireMonstres.supprimerMonstre(this);
                    gestionnaireMonstres.incrementerMonstresMorts();
                    break;
                }
            } catch (InterruptedException e) {
                System.out.println("Debug : Le thread du " + this.getNom() + " a bien été tué.");
                break;
            }
        }
    }
}