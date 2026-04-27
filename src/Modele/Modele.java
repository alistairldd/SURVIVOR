package Modele;

import Modele.Batiments.Batiment;
import Modele.Items.Item;
import Modele.Batiments.*;
import Modele.Monstres.Monstre;
import static Modele.Constantes.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Cœur du système (Architecture MVC).
 * Gère l'état global du monde, les entités et les flags d'affichage de l'interface.
 */
public class Modele {

    // --- ÉTAT DE L'INTERFACE (UI FLAGS) ---
    private int hudPageActuelle = 1;
    private boolean instructionsOuvert = false;

    // NOUVEAU : Flag pour l'affichage des jauges de vie au-dessus des entités
    private boolean affichagePV = true;

    // --- ENTITÉS ET GESTIONNAIRES ---
    private Joueur joueur;
    private GestionnaireBatiments gestionnaireBatiments;
    private CycleJourNuit leCycleJourNuit;
    private GestionnaireShop gestionnaireShop;
    private GestionnaireSorts gestionnaireSorts;
    private Item sortEnAttente = null;

    private List<int[]> pendingFloatingTexts = new ArrayList<>();

    private UpdateJN updateJN;

    // Entité actuellement ciblée par l'interface
    private Localisable cibleAffichage;

    private boolean partieTerminee = false;
    private boolean rotationAbatis = false;



    // --- ÉTAT DE CONSTRUCTION (RTS) ---
    public enum TypeConstruction { AUCUN, TOUR, TENTE, ABATIS, MORTIER }
    private TypeConstruction modeConstruction = TypeConstruction.AUCUN;

    public Modele() {
        // Initialisation de l'entité joueur
        this.joueur = new Joueur(this);
        //this.joueur.setHp(10); // HP initial de test

        // Initialisation des systèmes autonomes
        this.updateJN = new UpdateJN(this);
        this.leCycleJourNuit = new CycleJourNuit(updateJN);
        this.gestionnaireBatiments = new GestionnaireBatiments(this);
        this.gestionnaireShop = new GestionnaireShop(this);
        this.gestionnaireSorts = new GestionnaireSorts(this);


        this.cibleAffichage = joueur;
    }

    /* ---- GETTERS ET SETTERS D'INTERFACE ---- */

    public int getHudPageActuelle() { return hudPageActuelle; }
    public void setHudPageActuelle(int page) { this.hudPageActuelle = page; }

    public boolean isInstructionsOuvert() { return instructionsOuvert; }
    public void toggleInstructions() { this.instructionsOuvert = !this.instructionsOuvert; }

    // --- LOGIQUE D'AFFICHAGE DES PV ---
    public boolean isAffichagePV() { return affichagePV; }
    public void toggleAffichagePV() { this.affichagePV = !this.affichagePV; }

    // NOUVEAU : Getters et bascule pour la rotation
    public boolean isRotationAbatis() { return rotationAbatis; }
    public void toggleRotationAbatis() { this.rotationAbatis = !this.rotationAbatis; }

    // Suivi de la souris pour le rendu du fantôme
    private double sourisMondeX = 0;
    private double sourisMondeY = 0;

    public double getSourisMondeX() { return sourisMondeX; }
    public double getSourisMondeY() { return sourisMondeY; }
    public void setPositionSourisMonde(double x, double y) {
        this.sourisMondeX = x;
        this.sourisMondeY = y;
    }

    /* ---- LOGIQUE MÉTIER ET GESTION DU MONDE ---- */

    public Joueur getJoueur() { return joueur; }
    public CycleJourNuit getLeCycleJourNuit() { return leCycleJourNuit; }
    public UpdateJN getUpdateJN() { return updateJN; }
    public GestionnaireShop getGestionnaireShop() { return gestionnaireShop; }
    public GestionnaireBatiments getGestionnaireBatiments() { return gestionnaireBatiments; }
    public GestionnaireSorts getGestionnaireSorts() { return gestionnaireSorts; }
    public void preparerSort(Item sort) { this.sortEnAttente = sort; }
    public Item getSortEnAttente() { return this.sortEnAttente; }
    public void setSortEnAttente() { this.sortEnAttente = null; }
    public boolean getPartieTerminee() { return partieTerminee; }

    public TypeConstruction getModeConstruction() { return modeConstruction; }
    public void setModeConstruction(TypeConstruction mode) { this.modeConstruction = mode; }
    public void annulerConstruction() { this.modeConstruction = TypeConstruction.AUCUN; }

    //méthode pour récuperer les textes flottants à afficher
    public List<int[]> getPendingFloatingTexts() {
        return pendingFloatingTexts;
    }
    //méthode pour vider la liste des textes flottants après les avoir affichés
    public void clearPendingFloatingTexts() {
        pendingFloatingTexts.clear();
    }

    /**
     * Stoppe tous les processus actifs du jeu lors d'un Game Over.
     */
    private void stopperTousLesThreadsDuJeu() {
        if (leCycleJourNuit != null && leCycleJourNuit.isAlive()) leCycleJourNuit.interrupt();
        if (updateJN != null && updateJN.getMonstres() != null) {
            for (Monstre m : updateJN.getMonstres()) {
                if (m != null && m.isAlive()) m.interrupt();
            }
        }
        if (gestionnaireBatiments != null) gestionnaireBatiments.stopperTousLesThreads();
        if (joueur != null) joueur.stopperReparation();
    }

    public void declencherGameOver() {
        if (!partieTerminee) {
            partieTerminee = true;
            stopperTousLesThreadsDuJeu();
        }
    }

    /**
     * Utilitaire de mise à l'échelle pour le rendu (ex: Minimap).
     */
    public double map(int debut, int fin, double valDebut, double valFin, double val){
        return (val - debut) * (valFin - valDebut) / (fin - debut) + valDebut;
    }

    public Localisable getCibleAffichage() { return cibleAffichage; }

    /**
     * Vérifie quelle entité est survolée par le curseur.
     */
    public void verifierSurvol(double sourisMondeX, double sourisMondeY) {
        List<Localisable> ciblesPotentielles = new ArrayList<>();
        ciblesPotentielles.add(joueur);
        ciblesPotentielles.addAll(gestionnaireBatiments.getBatiments());
        ciblesPotentielles.addAll(updateJN.getMonstres());

        for (Localisable cible : ciblesPotentielles) {
            double d = Math.hypot(sourisMondeX - cible.getX(), sourisMondeY - cible.getY());
            if (d < 20) {
                cibleAffichage = cible;
            }
        }
    }

    /**
     * Gère la détection de collision et l'application des dégâts lors d'une attaque.
     */
    public void joueurAttaque(double angleAttaque) {
        double portee = joueur.getArmeEquipee().getPortee();
        double angle = joueur.getArmeEquipee().getAngle();
        double positionX = this.joueur.getX();
        double positionY = this.joueur.getY();

        List<Monstre> monstres = updateJN.getMonstres();
        double dirX = Math.cos(angleAttaque);
        double dirY = Math.sin(angleAttaque);

        for (Monstre m : monstres) {
            double vecteurMonstreX = m.getX() - positionX;
            double vecteurMonstreY = m.getY() - positionY;
            double distance = Math.hypot(vecteurMonstreX, vecteurMonstreY);

            if (distance <= portee) {
                double normX = vecteurMonstreX / distance;
                double normY = vecteurMonstreY / distance;
                double produitScalaire = (dirX * normX) + (dirY * normY);
                double seuilCosinus = Math.cos(angle / 2.0);

                if (produitScalaire >= seuilCosinus) {
                    m.perdreHp(joueur.getArmeEquipee().getDegats() + joueur.getAttack());
                    if (m.getHp() <= 0) {
                        joueur.addPieces(m.getDrop());
                        // Ajout d'un texte flottant pour voir les pièces récoltés vive la richesse (après on remplace 10 par la qt d'or)
                        pendingFloatingTexts.add(new int[]{(int) m.getX(), (int) m.getY(), 10});

                    }
                }
            }
        }
    }

    /**
     * Utilitaires de recherche d'entités pour les bâtiments.
     */
    public Monstre batTrouverMonstre(Batiment b) {
        for (Monstre m : updateJN.getMonstres()) {
            if (Math.hypot(m.getX() - b.getX(), m.getY() - b.getY()) <= b.getRange()) return m;
        }
        return null;
    }

    public Joueur batTrouverJoueur(Batiment b) {
        if (Math.hypot(joueur.getX() - b.getX(), joueur.getY() - b.getY()) <= b.getRange()) return joueur;
        return null;
    }

    public Monstre batTrouverMonstreMortier(Mortier m) {
        for (Monstre monstre : updateJN.getMonstres()) {
            double distance = Math.hypot(monstre.getX() - m.getX(), monstre.getY() - m.getY());
            // Condition cruciale : Le monstre doit être ENTRE la portée min et la portée max
            if (distance >= m.getMinRange() && distance <= m.getRange()) {
                return monstre;
            }
        }
        return null;
    }

    /**
     * Réinitialisation complète du jeu.
     */
    public void reinitialiserJeu() {
        stopperTousLesThreadsDuJeu();
        this.partieTerminee = false;
        this.hudPageActuelle = 1;
        this.joueur = new Joueur(this);
        this.gestionnaireBatiments = new GestionnaireBatiments(this);
        this.gestionnaireShop = new GestionnaireShop(this);
        this.updateJN = new UpdateJN(this);
        this.leCycleJourNuit = new CycleJourNuit(this.updateJN);
        this.cibleAffichage = joueur;
    }

    // ==========================================================
    // --- MOTEUR DE COLLISION RECTANGULAIRE (Théorème SAT) ---
    // ==========================================================

    /**
     * Calcule les coordonnées exactes des 4 coins d'un rectangle en tenant compte de sa rotation.
     * x, y : Centre du rectangle. w, h : Largeur et Hauteur. angle : Rotation en radians.
     */
    private double[][] getCoinsRectangle(double cx, double cy, double w, double h, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double[][] coins = new double[4][2];

        // Demi-largeur et demi-hauteur depuis le centre
        double hw = w / 2.0;
        double hh = h / 2.0;

        // Les 4 coins relatifs au centre (avant rotation)
        double[][] relatifs = {
                {-hw, -hh}, {hw, -hh}, {hw, hh}, {-hw, hh}
        };

        for (int i = 0; i < 4; i++) {
            double rx = relatifs[i][0];
            double ry = relatifs[i][1];
            // Application de la matrice de rotation 2D et translation vers (cx, cy)
            coins[i][0] = cx + (rx * cos - ry * sin);
            coins[i][1] = cy + (rx * sin + ry * cos);
        }
        return coins;
    }

    /**
     * NOUVEAU : Vérifie si le joueur entre en collision avec la HITBOX d'un bâtiment solide.
     * @param testX La future position X du joueur.
     * @param testY La future position Y du joueur.
     * @return true si la position chevauche la Hitbox d'un bâtiment (hors Abatis).
     */
    public boolean collisionAvecBatimentSolide(double testX, double testY) {
        // Le joueur est un carré de taille J_TAILLE x J_TAILLE
        double[][] coinsJoueur = getCoinsRectangle(testX, testY, Constantes.J_TAILLE, Constantes.J_TAILLE, 0);

        for (Batiment b : gestionnaireBatiments.getBatiments()) {
            // EXCEPTION : Le joueur passe à travers l'Abatis, on l'ignore de la détection
            if (b instanceof Abatis) {
                continue;
            }

            // On utilise la Hitbox de combat (et non l'encombrement global)
            // IMPORTANT : On applique le décalage 2.5D (offset Y) pour cibler la BASE du bâtiment
            double centreHitboxY = b.getY() + b.getOffsetYHitbox();

            double[][] coinsHitbox = getCoinsRectangle(
                    b.getX(),
                    centreHitboxY,
                    b.getLargeurHitbox(),
                    b.getHauteurHitbox(),
                    b.getAngleRotation()
            );

            // Si le polygone du joueur touche le polygone du bâtiment, c'est un mur !
            if (chevauchementPolygones(coinsJoueur, coinsHitbox)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Vérifie si deux rectangles (droits ou orientés) se chevauchent.
     * poly1 et poly2 sont les tableaux des 4 coins générés par getCoinsRectangle().
     */
    private boolean chevauchementPolygones(double[][] poly1, double[][] poly2) {
        double[][][] polygones = {poly1, poly2};
        for (int i = 0; i < polygones.length; i++) {
            double[][] polygone = polygones[i];
            for (int i1 = 0; i1 < polygone.length; i1++) {
                int i2 = (i1 + 1) % polygone.length;
                double p1X = polygone[i1][0];
                double p1Y = polygone[i1][1];
                double p2X = polygone[i2][0];
                double p2Y = polygone[i2][1];

                // Calcul de la normale (l'axe sur lequel on va projeter)
                double normalX = p2Y - p1Y;
                double normalY = p1X - p2X;

                // Projection du premier polygone
                double minA = Double.POSITIVE_INFINITY;
                double maxA = Double.NEGATIVE_INFINITY;
                for (double[] p : poly1) {
                    double proj = normalX * p[0] + normalY * p[1];
                    if (proj < minA) minA = proj;
                    if (proj > maxA) maxA = proj;
                }

                // Projection du deuxième polygone
                double minB = Double.POSITIVE_INFINITY;
                double maxB = Double.NEGATIVE_INFINITY;
                for (double[] p : poly2) {
                    double proj = normalX * p[0] + normalY * p[1];
                    if (proj < minB) minB = proj;
                    if (proj > maxB) maxB = proj;
                }

                // Si on trouve un axe où les projections ne se touchent pas, c'est qu'il n'y a pas de collision !
                if (maxA < minB || maxB < minA) {
                    return false;
                }
            }
        }
        return true; // Aucune ligne de séparation trouvée : les rectangles se chevauchent.
    }

    /**
     * Vérifie si un bâtiment peut être construit à une position donnée en utilisant des rectangles.
     * @param x Coordonnée X du centre.
     * @param y Coordonnée Y du centre.
     * @param w Largeur du rectangle d'encombrement.
     * @param h Hauteur du rectangle d'encombrement.
     * @param angle Angle de rotation en radians.
     * @return true si l'emplacement est libre.
     */
    public boolean peutConstruireIci(double x, double y, double w, double h, double angle) {
        // 1. Calculer les coins du futur bâtiment (le fantôme)
        double[][] coinsNouveau = getCoinsRectangle(x, y, w, h, angle);

        // 2. Vérifie si le bâtiment est dans les limites de la carte
        for (double[] coin : coinsNouveau) {
            if (coin[0] < 0 || coin[0] > LARGEUR_MAP || coin[1] < 0 || coin[1] > HAUTEUR_MAP) {
                return false;
            }
        }

        // 3. Vérifie si l'emplacement est déjà occupé par un autre bâtiment
        for (Batiment b : gestionnaireBatiments.getBatiments()) {
            double[][] coinsExistant = getCoinsRectangle(
                    b.getX(),
                    b.getY(),
                    b.getLargeurEncombrement(),
                    b.getHauteurEncombrement(),
                    b.getAngleRotation()
            );
            if (chevauchementPolygones(coinsNouveau, coinsExistant)) {
                return false;
            }
        }

        // 4. Vérifie si l'emplacement est trop proche du joueur
        // On traite le joueur comme un rectangle de J_TAILLE x J_TAILLE pour la collision SAT
        double[][] coinsJoueur = getCoinsRectangle(joueur.getX(), joueur.getY(), J_TAILLE, J_TAILLE, 0);
        if (chevauchementPolygones(coinsNouveau, coinsJoueur)) {
            return false;
        }

        return true;
    }

    /**
     * Valide l'achat et place le bâtiment sur la carte si le joueur a les ressources ET la place.
     *
     * @param x Coordonnée X du clic
     * @param y Coordonnée Y du clic
     * @return true si la construction a réussi, false sinon (pour déclencher un feedback visuel)
     */
    /**
     * Tente de placer définitivement le bâtiment sur la carte.
     * @param x Coordonnée X de la souris.
     * @param y Coordonnée Y de la souris.
     * @return true si la construction a réussi.
     */
    public boolean finaliserConstruction(double x, double y) {
        // --- CAS : TOUR ---
        if (modeConstruction == TypeConstruction.TOUR) {
            if (joueur.aAssezDeRessources(COUT_TOUR) &&
                    peutConstruireIci(x, y, TOUR_LARGEUR_ENC, TOUR_HAUTEUR_ENC, 0)) {

                joueur.consommerListeRessources(COUT_TOUR);
                Tower t = new Tower((int)x, (int)y, gestionnaireBatiments);
                gestionnaireBatiments.ajouterBatiment(t);

                /*if (!(joueur.aAssezDeRessources(COUT_ABATIS))) {
                    annulerConstruction();
                }*/
                return true;
            }
            return false;
        }
        // --- CAS : TENTE ---
        else if (modeConstruction == TypeConstruction.TENTE) {
            if (joueur.aAssezDeRessources(COUT_TENTE) &&
                    peutConstruireIci(x, y, TENTE_LARGEUR_ENC, TENTE_HAUTEUR_ENC, 0) &&
                    !gestionnaireBatiments.aDejaUneTente()) {

                joueur.consommerListeRessources(COUT_TENTE);
                TenteDeSoin t = new TenteDeSoin((int)x, (int)y, gestionnaireBatiments);
                gestionnaireBatiments.ajouterBatiment(t);
                annulerConstruction();
                return true;
            }
            return false;
        }
        // --- CAS : ABATIS ---
        else if (modeConstruction == TypeConstruction.ABATIS) {
            double angle = rotationAbatis ? -ABATIS_ANGLE_RAD : ABATIS_ANGLE_RAD;

            if (joueur.aAssezDeRessources(COUT_ABATIS) &&
                    peutConstruireIci(x, y, ABATIS_LARGEUR, ABATIS_HAUTEUR, angle)) {

                joueur.consommerListeRessources(COUT_ABATIS);
                Abatis a = new Abatis((int)x, (int)y, gestionnaireBatiments, rotationAbatis);
                gestionnaireBatiments.ajouterBatiment(a);

                /*if (!(joueur.aAssezDeRessources(COUT_ABATIS))) {
                    annulerConstruction();
                }*/
                return true;
            }
            return false;
        }

        if (modeConstruction == TypeConstruction.MORTIER) {
            if (joueur.aAssezDeRessources(COUT_MORTIER) &&
                    peutConstruireIci(x, y, MORTIER_LARGEUR_ENC, MORTIER_HAUTEUR_ENC, 0)) {

                joueur.consommerListeRessources(COUT_MORTIER);
                Mortier m = new Mortier((int)x, (int)y, gestionnaireBatiments);
                gestionnaireBatiments.ajouterBatiment(m);
                annulerConstruction();
                return true;
            }
            return false;
        }
        return false;
    }
}