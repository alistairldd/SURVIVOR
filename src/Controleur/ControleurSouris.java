package Controleur;

import Modele.Modele;
import Vue.Vue;
import Modele.Joueur;

import Modele.Armes.Arme;
import Modele.Armure.Armure;
import Modele.Items.Item;
import Modele.Items.SortFeu;

import Vue.AnimationArme;
import Modele.DeplaceJoueur;
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import static Modele.Constantes.*;
import static Vue.HUD.VueHUDEquipement.*;

/**
 * Contrôleur dédié à la gestion des événements de la souris.
 * Gère le mode RTS (Construction) ou les actions classiques (Attaque/Déplacement).
 */
public class ControleurSouris implements MouseListener, MouseMotionListener {

    private Modele modele;
    private Vue vue;

    private int mouseX = 0;
    private int mouseY = 0;

    public ControleurSouris(Vue vue, Modele modele) {
        this.modele = modele;
        this.vue = vue;
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {

        if (e.getSource() == vue.getVueHUD().getPageEtat()) {  // ou getPageAction()
            String action = vue.getVueHUDEquipement().getActionAuClic(e.getX(), e.getY());
            if (ACTION_SWITCH_ARME.equals(action))   { modele.getJoueur().switchArmes();   return; }
            if (ACTION_SWITCH_ARMURE.equals(action)) { modele.getJoueur().switchArmures(); return; }
            if (ACTION_UTILISER_CONSOMMABLE.equals(action)) {
                Item itemClique = vue.getVueHUDEquipement().getItemAuClic(e.getX(), e.getY());
                if (itemClique instanceof SortFeu) {
                    /*// Calculer la direction vers la souris
                    if (modele.getSortEnAttente() != null) {
                    int centerX = vue.getWidth() / 2;
                    int centerY = vue.getHeight() / 2;
                    double angle = Math.atan2(mouseY - centerY, mouseX - centerX);
                    double directionX = Math.cos(angle);
                    double directionY = Math.sin(angle);

                    modele.getJoueur().utiliserSort(itemClique, directionX, directionY);
                    modele.setSortEnAttente();
                    }*/
                    modele.preparerSort(itemClique);

                } else {
                    modele.getJoueur().utiliserConsommable(itemClique);
                }
            }
        }
        // 1. Priorité UI : Vérifier si on clique dans la boutique
        Object cible = vue.identifierElementClique(e.getX(), e.getY(), e.getSource());

        if (cible != null) {
            if (cible instanceof Arme) {
                modele.getGestionnaireShop().acheterArme((Arme) cible);
            } else if (cible instanceof Armure) {
                modele.getGestionnaireShop().acheterArmure((Armure)cible);
            } else if (cible instanceof Item) {
                modele.getGestionnaireShop().acheterItem((Item) cible);
            }
            return;
        }
        // si l'élément cliqué n'est pas dans l'UI, on continue avec les actions sur le monde
        if (e.getSource() != vue) return;


        Joueur joueur = modele.getJoueur();
        double camX = joueur.getX() - (double) vue.getWidth() / 2;
        double camY = joueur.getY() - (double) vue.getHeight() / 2;
        double destX = camX + e.getX();
        double destY = camY + e.getY();

        // --- 1. DÉPLACEMENT (Clic Droit) ---
        if (SwingUtilities.isRightMouseButton(e)){
            if (!modele.getPartieTerminee()){
                DeplaceJoueur deplacement = new DeplaceJoueur(destX, destY, joueur);
                joueur.setThreadActuel(deplacement);
                deplacement.start();
            }
        }
        // --- 2. ACTION (Clic Gauche) ---
        else if (SwingUtilities.isLeftMouseButton(e)){
            if (!modele.getPartieTerminee()){
                if (modele.getSortEnAttente() != null) {
                    int centerX = vue.getWidth() / 2;
                    int centerY = vue.getHeight() / 2;
                    double angle = Math.atan2(mouseY - centerY, mouseX - centerX);
                    double directionX = Math.cos(angle);
                    double directionY = Math.sin(angle);

                    modele.getJoueur().utiliserSort(modele.getSortEnAttente(), directionX, directionY);
                    modele.setSortEnAttente();
                }
                // A. MODE CONSTRUCTION (RTS)
                else if (modele.getModeConstruction() != Modele.TypeConstruction.AUCUN) {
                    boolean succes = modele.finaliserConstruction(destX, destY);
                    System.out.println(succes);
                    if (!succes) {
                        // Feedback visuel : on détermine le message selon la cause de l'échec
                        String message = resoudreMessageErreurConstruction(joueur, destX, destY);
                        vue.afficherTexteErreur(message, destX, destY);
                    }
                }
                // B. MODE COMBAT (Classique)
                else {
                    if (joueur.peutAttaquer()){
                        int centerX = vue.getWidth() / 2;
                        int centerY = vue.getHeight() / 2;
                        double angleAttaque = Math.atan2(mouseY - centerY, mouseX - centerX);

                        modele.joueurAttaque(angleAttaque);
                        joueur.setDernierTempsAttaque();

                        int cadence = joueur.getArmeEquipee().getCadence();
                        AnimationArme animation = new AnimationArme(vue.getVueArme(), cadence, modele);
                        vue.getVueArme().setEnAnimation(true);
                        animation.start();
                    } else {
                        // Feedback visuel : l'arme est en rechargement
                        //vue.afficherTexteErreur("Rechargement...", destX, destY);
                    }
                }
            }
        }
    }

    /**
     * Analyse la cause de l'échec de construction et retourne un message adapté.
     * Permet d'afficher un feedback précis plutôt qu'un message générique.
     */
    private String resoudreMessageErreurConstruction(Joueur joueur, double x, double y) {
        Modele.TypeConstruction mode = modele.getModeConstruction();

        // Cas 1 : Tente déjà construite
        if (mode == Modele.TypeConstruction.TENTE &&
                modele.getGestionnaireBatiments().aDejaUneTente()) {
            return "Tente déjà construite !";
        }

        // Cas 2 : C'est la nuit
        if (!modele.getLeCycleJourNuit().isDay()) {
            return "Construisez le jour !";
        }

        // Cas 3 : Ressources insuffisantes
        boolean aLesFonds = (mode == Modele.TypeConstruction.TOUR)
                ? joueur.aAssezDeRessources(COUT_TOUR)
                : joueur.aAssezDeRessources(COUT_TENTE);
        if (!aLesFonds) {
            return "Ressources insuffisantes !";
        }

        // Cas 4 : Collision ou hors limites
        return "Emplacement invalide !";
    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!modele.getPartieTerminee()) {
            Joueur joueur = modele.getJoueur();

            mouseX = e.getX();
            mouseY = e.getY();

            double camX = joueur.getX() - (double) vue.getWidth() / 2;
            double camY = joueur.getY() - (double) vue.getHeight() / 2;

            double sourisMondeX = camX + mouseX;
            double sourisMondeY = camY + mouseY;

            // Transmission des coordonnées au Modèle pour le Fantôme et l'UI
            modele.setPositionSourisMonde(sourisMondeX, sourisMondeY);
            modele.verifierSurvol(sourisMondeX, sourisMondeY);
        }
    }

    public int getMX(){ return this.mouseX; }
    public int getMY(){ return this.mouseY; }
}