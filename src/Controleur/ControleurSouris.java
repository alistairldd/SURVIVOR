package Controleur;

import Modele.Items.Sort;
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
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import static Modele.Constantes.*;
import static Vue.HUD.VueHUDEquipement.*;

/**
 * Contrôleur dédié à la gestion des événements de la souris.
 * Gère les interactions avec l'UI, le mode RTS (Construction)
 * et les actions classiques du joueur (Attaque/Déplacement).
 */
public class ControleurSouris implements MouseListener, MouseMotionListener {

    /** ---------- [Propriétés] ---------- **/

    private Modele modele;
    private Vue vue;

    private int mouseX = 0;
    private int mouseY = 0;

    /** ---------- [Constructeurs] ---------- **/

    /**
     * Initialise le contrôleur d'événements souris.
     *
     * @param vue - L'instance de la vue pour identifier les éléments cliqués et le référentiel spatial
     * @param modele - L'instance du modèle pour transmettre les actions et les coordonnées
     */
    public ControleurSouris(Vue vue, Modele modele) {
        this.modele = modele;
        this.vue = vue;
    }

    /** ---------- [Accesseurs] ---------- **/

    /**
     * @return La position X actuelle de la souris sur l'écran
     */
    public int getMX(){ return this.mouseX; }

    /**
     * @return La position Y actuelle de la souris sur l'écran
     */
    public int getMY(){ return this.mouseY; }

    /** ---------- [Méthodes Publiques / Écouteurs Clics] ---------- **/

    @Override
    public void mouseClicked(MouseEvent e) {}

    /**
     * Intercepte les clics pour gérer la navigation UI, les achats en boutique,
     * les déplacements et les interactions in-game (sorts, constructions, attaques).
     *
     * @param e - L'événement souris contenant la position et le type de clic
     */
    @Override
    public void mousePressed(MouseEvent e) {

        // Vérification prioritaire des interactions avec l'interface d'équipement (HUD)
        if (e.getSource() == vue.getVueHUD().getPageEtat()) {
            String action = vue.getVueHUDEquipement().getActionAuClic(e.getX(), e.getY());

            if (ACTION_SWITCH_ARME.equals(action))   { modele.getJoueur().switchArmes();   return; }
            if (ACTION_SWITCH_ARMURE.equals(action)) { modele.getJoueur().switchArmures(); return; }

            if (ACTION_UTILISER_CONSOMMABLE.equals(action)) {
                Item itemClique = vue.getVueHUDEquipement().getItemAuClic(e.getX(), e.getY());
                if (itemClique instanceof Sort) {
                    modele.preparerSort(itemClique);
                } else {
                    modele.getJoueur().utiliserConsommable(itemClique);
                }
            }
        }

        // Vérification des interactions avec la boutique (UI)
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

        // Si le clic ne concerne pas la vue principale du jeu, on ignore la suite
        if (e.getSource() != vue) return;

        Joueur joueur = modele.getJoueur();

        // Calcul des coordonnées du clic transposées dans le repère du monde physique
        double camX = joueur.getX() - (double) vue.getWidth() / 2;
        double camY = joueur.getY() - (double) vue.getHeight() / 2;
        double destX = camX + e.getX();
        double destY = camY + e.getY();

        // Gestion du déplacement du joueur
        if (SwingUtilities.isRightMouseButton(e)){
            if (!modele.getPartieTerminee()){
                DeplaceJoueur deplacement = new DeplaceJoueur(destX, destY, joueur);
                joueur.setThreadActuel(deplacement);
                deplacement.start();
            }
        }
        // Gestion des actions contextuelles (Sorts, Construction, Attaque)
        else if (SwingUtilities.isLeftMouseButton(e)){
            if (!modele.getPartieTerminee()){

                // Lancement d'un sort préalablement ciblé
                if (modele.getSortEnAttente() != null) {
                    int centerX = vue.getWidth() / 2;
                    int centerY = vue.getHeight() / 2;
                    double angle = Math.atan2(mouseY - centerY, mouseX - centerX);
                    double directionX = Math.cos(angle);
                    double directionY = Math.sin(angle);

                    modele.getJoueur().utiliserSort(modele.getSortEnAttente(), directionX, directionY);
                    modele.setSortEnAttente();
                }
                // Mode placement de bâtiments (RTS)
                else if (modele.getModeConstruction() != Modele.TypeConstruction.AUCUN) {
                    boolean succes = modele.finaliserConstruction(destX, destY);
                    System.out.println(succes);

                    if (!succes) {
                        String message = resoudreMessageErreurConstruction(joueur, destX, destY);
                        vue.afficherTexteErreur(message, destX, destY, Color.RED);
                    }
                }
                // Action par défaut : Attaque à l'arme
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
                    }
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    /** ---------- [Méthodes Publiques / Écouteurs Mouvements] ---------- **/

    @Override
    public void mouseDragged(MouseEvent e) {}

    /**
     * Suit en continu la position de la souris pour actualiser les éléments visuels dynamiques
     * comme le fantôme de construction ou les feedbacks de survol.
     *
     * @param e - L'événement de mouvement souris
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        if (!modele.getPartieTerminee()) {
            Joueur joueur = modele.getJoueur();

            mouseX = e.getX();
            mouseY = e.getY();

            // Transformation de la position écran en coordonnées monde
            double camX = joueur.getX() - (double) vue.getWidth() / 2;
            double camY = joueur.getY() - (double) vue.getHeight() / 2;

            double sourisMondeX = camX + mouseX;
            double sourisMondeY = camY + mouseY;

            modele.setPositionSourisMonde(sourisMondeX, sourisMondeY);
            modele.verifierSurvol(sourisMondeX, sourisMondeY);
        }
    }

    /** ---------- [Méthodes Privées / Utilitaires] ---------- **/

    /**
     * Analyse la cause de l'échec d'une tentative de construction afin
     * de retourner le message d'erreur le plus pertinent à l'utilisateur.
     *
     * @param joueur - Le joueur tentant la construction
     * @param x - Coordonnée X de la tentative
     * @param y - Coordonnée Y de la tentative
     * @return Le message d'erreur formaté à afficher
     */
    private String resoudreMessageErreurConstruction(Joueur joueur, double x, double y) {
        Modele.TypeConstruction mode = modele.getModeConstruction();

        if (mode == Modele.TypeConstruction.TENTE &&
                modele.getGestionnaireBatiments().aDejaUneTente()) {
            return "Tente déjà construite !";
        }

        if (!modele.getLeCycleJourNuit().isDay()) {
            return "Construisez le jour !";
        }

        boolean aLesFonds = false;
        if (mode == Modele.TypeConstruction.TOUR) aLesFonds = joueur.aAssezDeRessources(COUT_TOUR);
        else if (mode == Modele.TypeConstruction.TENTE) aLesFonds = joueur.aAssezDeRessources(COUT_TENTE);
        else if (mode == Modele.TypeConstruction.ABATIS) aLesFonds = joueur.aAssezDeRessources(COUT_ABATIS);
        else if (mode == Modele.TypeConstruction.MORTIER) aLesFonds = joueur.aAssezDeRessources(COUT_MORTIER);

        if (!aLesFonds) {
            return "Ressources insuffisantes !";
        }

        return "Emplacement invalide !";
    }
}