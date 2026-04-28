package Modele;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import static Modele.Constantes.*;

/**
 * Classe dédiée à la gestion centralisée des ressources.
 */
public class GestionnaireRessources {

    /** ---------- [Propriétés] ---------- **/

    private List<Ressource> ressources;

    /** ---------- [Constructeurs] ---------- **/

    public GestionnaireRessources() {
        this.ressources = new CopyOnWriteArrayList<>();
    }

    /** ---------- [Accesseurs / Getters] ---------- **/

    /**
     * Getter pour accéder à la liste des ressources présentes sur la carte.
     * @return La liste actuelle des ressources.
     */
    public List<Ressource> getRessources() {
        return ressources;
    }

    /** ---------- [Méthodes Publiques - Gestion de Liste] ---------- **/

    /**
     * Supprime toutes les ressources présentes sur la carte.
     * Utilisé lors des changements de cycle (ex: passage à la nuit) pour forcer l'exploration.
     */
    public void viderRessources() {
        // Écrase l'ancienne liste avec une nouvelle liste vide
        ressources.clear();
    }

    public void clearRessources() {
        // Vide l'ArrayList
        ressources.clear();
    }

    /** ---------- [Méthodes Publiques - Moteur Métier] ---------- **/

    /**
     * Nettoie la carte et fait apparaître un nouveau lot de ressources fraîches.
     * @param nbRessources Le nombre exact d'objets à disséminer.
     */
    public void genereRessources(int nbRessources) {
        // Boucle de création
        for (int i = 0; i < nbRessources; i++) {
            // Instancie une nouvelle ressource (qui calcule sa propre position) et l'ajoute à la liste de la carte
            this.ressources.add(new Ressource());
        }
    }

    /**
     * Met à jour la position des ressources (aspiration) et gère le ramassage.
     * @param joueur Le joueur qui attire les ressources.
     */
    public void actualiserAspiration(Joueur joueur) {
        // Parcours inversé pour pouvoir supprimer des éléments de la liste en toute sécurité
        for (int i = ressources.size() - 1; i >= 0; i--) {
            Ressource r = ressources.get(i);

            // Calcul de la distance entre le joueur et la ressource
            double diffX = joueur.getX() - r.getPositionX();
            double diffY = joueur.getY() - r.getPositionY();
            double distance = Math.hypot(diffX, diffY);

            // Si le joueur est assez proche, la ressource commence à être aspirée
            if (distance <= RAYON_RAMASSAGE) {
                r.setEstAspiree(true);
            }

            // Si la ressource est en train de voler vers le joueur
            if (r.isEstAspiree()) {
                r.mettreAJourPosition(joueur);

                // Recalcul de la distance après le déplacement vectoriel
                diffX = joueur.getX() - r.getPositionX();
                diffY = joueur.getY() - r.getPositionY();
                distance = Math.hypot(diffX, diffY);

                // Si la ressource touche le centre du joueur (collision validée)
                if (distance <= 10) {
                    joueur.ajouterARessources(r); // Ajout au sac à dos
                    ressources.remove(i);         // Disparition de la carte
                }
            }
        }
    }
}