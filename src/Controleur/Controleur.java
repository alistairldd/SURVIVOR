package Controleur;

import Modele.Modele;
import Vue.Vue;

import javax.swing.*;

/**
 * Classe principale du contrôleur orchestrant le lien entre la Vue et le Modèle.
 * Elle centralise l'initialisation et l'attachement des écouteurs et sous-contrôleurs
 * aux composants graphiques pour intercepter les actions de l'utilisateur.
 */
public class Controleur {

    /** ---------- [Propriétés] ---------- **/

    private Modele monModele;
    private Vue maVue;

    /** ---------- [Constructeurs] ---------- **/

    /**
     * Initialise le contrôleur principal en établissant la liaison entre les données et l'interface.
     *
     * @param modele - L'instance du modèle contenant la logique métier et l'état de l'application
     * @param vue - L'instance de la vue gérant le rendu visuel et l'interface utilisateur
     */
    public Controleur(Modele modele, Vue vue) {
        monModele = modele;
        maVue = vue;
    }
}