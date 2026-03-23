package Modele;

import java.util.ArrayList;

/**
 * Classe dédiée à la gestion centralisée des ressources
 */
public class GestionnaireRessources {

    private ArrayList<Ressource> ressources;

    public GestionnaireRessources() {
        this.ressources = new ArrayList<>();
    }

    /**
     * Getter pour accéder à la liste des ressources présentes sur la carte.
     * @return La liste actuelle des ressources.
     */
    public ArrayList<Ressource> getRessources() {
        return ressources;
    }

    /**
     * Supprime toutes les ressources présentes sur la carte.
     * Utilisé lors des changements de cycle (ex: passage à la nuit) pour forcer l'exploration.
     */
    public void viderRessources() {
        // Écrase l'ancienne liste avec une nouvelle liste vide
        ressources.clear();
    }

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
}
