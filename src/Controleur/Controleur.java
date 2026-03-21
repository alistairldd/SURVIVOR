package Controleur;

import Modele.Modele;
import Vue.Vue;

import javax.swing.*;

/**
 * Classe principale du contrôleur orchestrant le lien entre la Vue et le Modèle.
 * Elle centralise l'initialisation et l'attachement des sous-contrôleurs (souris, clavier)
 * aux composants graphiques pour intercepter les actions de l'utilisateur.
 */
public class Controleur {

    private Modele monModele;
    private Vue maVue;

    /**
     * Constructeur du contrôleur principal.
     * Initialise le sous-contrôleur de souris et l'attache aux écouteurs de la vue principale.
     * * @param modele L'instance du modèle contenant la logique métier (cœur du jeu).
     * @param vue L'instance de la vue gérant l'affichage graphique.
     */
    public Controleur(Modele modele, Vue vue){
        monModele = modele;
        maVue = vue;

        ControleurSouris controleurSouris = new ControleurSouris(vue, modele);
        this.maVue.addMouseListener(controleurSouris);
        this.maVue.addMouseMotionListener(controleurSouris);

    }
}