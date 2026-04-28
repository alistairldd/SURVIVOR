package Controleur;

import Modele.Modele;
import Vue.Vue;

/**
 * Classe principale du contrôleur orchestrant le lien entre la Vue et le Modèle.
 * Elle centralise l'initialisation et l'attachement des sous-contrôleurs (souris, clavier)
 * aux composants graphiques pour intercepter les actions de l'utilisateur.
 */
public class Controleur {

    /** ---------- [Propriétés] ---------- **/

    private Modele monModele;
    private Vue maVue;

    /** ---------- [Constructeurs] ---------- **/

    /**
     * Initialise le contrôleur principal en établissant la liaison globale.
     *
     * @param modele - L'instance du modèle contenant la logique métier (cœur du jeu)
     * @param vue - L'instance de la vue gérant l'affichage graphique
     */
    public Controleur(Modele modele, Vue vue) {
        monModele = modele;
        maVue = vue;
    }
}