package Modele;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import static Modele.Constantes.*;

/**
 * Classe dédiée à la gestion centralisée du cycle de vie spatial des ressources terrestres.
 * Gère l'instanciation dispersée et la physique d'aspiration magnétique vers le joueur.
 */
public class GestionnaireRessources {

    /** ---------- [Propriétés] ---------- **/

    private List<Ressource> ressources;

    /** ---------- [Constructeurs] ---------- **/

    public GestionnaireRessources() {
        this.ressources = new CopyOnWriteArrayList<>();
    }

    /** ---------- [Accesseurs / Getters] ---------- **/

    public List<Ressource> getRessources() {
        return ressources;
    }

    /** ---------- [Méthodes Publiques - Gestion de Liste] ---------- **/

    /**
     * Supprime toutes les ressources présentes sur la carte.
     * Utilisé lors des transitions de cycle temporel.
     */
    public void viderRessources() {
        ressources.clear();
    }

    /**
     * Purge la collection interne de l'ensemble de ses éléments.
     */
    public void clearRessources() {
        ressources.clear();
    }

    /** ---------- [Méthodes Publiques - Moteur Métier] ---------- **/

    /**
     * Instancie de nouvelles ressources et les répartit aléatoirement sur la carte.
     *
     * @param nbRessources - La quantité globale de ressources à générer
     */
    public void genereRessources(int nbRessources) {
        for (int i = 0; i < nbRessources; i++) {
            this.ressources.add(new Ressource());
        }
    }

    /**
     * Calcule l'interaction spatiale entre le joueur et les minerais sur la carte.
     * Si le seuil d'attraction est atteint, anime la ressource vers le joueur et
     * valide son ramassage dès la collision physique.
     *
     * @param joueur - L'entité gravitationnelle de référence
     */
    public void actualiserAspiration(Joueur joueur) {
        for (int i = ressources.size() - 1; i >= 0; i--) {
            Ressource r = ressources.get(i);

            double diffX = joueur.getX() - r.getPositionX();
            double diffY = joueur.getY() - r.getPositionY();
            double distance = Math.hypot(diffX, diffY);

            // Activation de l'état d'aspiration
            if (distance <= RAYON_RAMASSAGE) {
                r.setEstAspiree(true);
            }

            // Moteur de mouvement et validation de collision
            if (r.isEstAspiree()) {
                r.mettreAJourPosition(joueur);

                diffX = joueur.getX() - r.getPositionX();
                diffY = joueur.getY() - r.getPositionY();
                distance = Math.hypot(diffX, diffY);

                if (distance <= 10) {
                    joueur.ajouterARessources(r);
                    ressources.remove(i);
                }
            }
        }
    }
}