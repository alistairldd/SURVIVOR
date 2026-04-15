package Modele;

import Modele.Batiments.Batiment;
import Modele.Monstres.Monstre;
import static Modele.Constantes.*;
import Modele.Batiments.Tower;
import Modele.Batiments.TenteDeSoin;

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
    private UpdateJN updateJN;

    // Entité actuellement ciblée par l'interface
    private Localisable cibleAffichage;

    private boolean partieTerminee = false;

    // --- ÉTAT DE CONSTRUCTION (RTS) ---
    public enum TypeConstruction { AUCUN, TOUR, TENTE }
    private TypeConstruction modeConstruction = TypeConstruction.AUCUN;

    public Modele() {
        // Initialisation de l'entité joueur
        this.joueur = new Joueur(this);
        this.joueur.setHp(10); // HP initial de test

        // Initialisation des systèmes autonomes
        this.updateJN = new UpdateJN(this);
        this.leCycleJourNuit = new CycleJourNuit(updateJN);
        this.gestionnaireBatiments = new GestionnaireBatiments(this);
        this.gestionnaireShop = new GestionnaireShop(this);

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
    public boolean getPartieTerminee() { return partieTerminee; }

    public TypeConstruction getModeConstruction() { return modeConstruction; }
    public void setModeConstruction(TypeConstruction mode) { this.modeConstruction = mode; }
    public void annulerConstruction() { this.modeConstruction = TypeConstruction.AUCUN; }

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

    /**
     * Vérifie si l'emplacement (x, y) est libre et constructible.
     * @param x Coordonnée X de la souris
     * @param y Coordonnée Y de la souris
     * @param rayonHitbox Le rayon d'encombrement du bâtiment qu'on veut placer
     * @return true si la place est libre ET qu'il fait jour.
     */
    /**
     * Vérifie si l'emplacement (x, y) est libre, à l'intérieur de la carte et constructible.
     * @param x Coordonnée X de la souris
     * @param y Coordonnée Y de la souris
     * @param rayonHitbox Le rayon d'encombrement du bâtiment qu'on veut placer
     * @return true si la place est libre, dans les limites ET qu'il fait jour.
     */
    public boolean peutConstruireIci(double x, double y, int rayonHitbox) {
        // 1. On ne construit que le jour
        if (!leCycleJourNuit.isDay()) return false;

        // --- NOUVEAU : Unicité de la Tente ---
        if (modeConstruction == TypeConstruction.TENTE && gestionnaireBatiments.aDejaUneTente()) {
            return false; // La tente existe déjà, zone non constructible !
        }

        // 2. Vérification des limites de la carte
        if (x - rayonHitbox < 0 || x + rayonHitbox > LARGEUR_MAP ||
                y - rayonHitbox < 0 || y + rayonHitbox > HAUTEUR_MAP) {
            return false; // Emplacement hors limites
        }

        // 3. Vérification des collisions avec les autres bâtiments existants
        for (Batiment b : gestionnaireBatiments.getBatiments()) {
            double distance = Math.hypot(b.getX() - x, b.getY() - y);
            double distanceMinimaleRequise = b.getRayonHitbox() + rayonHitbox;

            if (distance < distanceMinimaleRequise) {
                return false; // Il y a déjà un bâtiment trop proche
            }
        }
        return true; // La zone est libre, valide et dans la carte !
    }

    /**
     * Valide l'achat et place le bâtiment sur la carte si le joueur a les ressources.
     * @param x Coordonnée X du clic
     * @param y Coordonnée Y du clic
     */
    /**
     * Valide l'achat et place le bâtiment sur la carte si le joueur a les ressources ET la place.
     */
    /**
     * Valide l'achat et place le bâtiment sur la carte si le joueur a les ressources ET la place.
     */
    public void finaliserConstruction(double x, double y) {
        if (modeConstruction == TypeConstruction.TOUR) {
            // On vérifie le prix ET la collision
            if (joueur.aAssezDeRessources(COUT_TOUR) &&
                    peutConstruireIci(x, y, Constantes.RAYON_HITBOX_TOUR)) {

                joueur.consommerListeRessources(COUT_TOUR);
                Tower t = new Tower((int)x, (int)y, gestionnaireBatiments);
                gestionnaireBatiments.ajouterBatiment(t);
                annulerConstruction(); // On désactive le mode
            } else {
                System.out.println("Impossible de construire la Tour ici ou ressources insuffisantes !");
            }
        }
        else if (modeConstruction == TypeConstruction.TENTE) {
            // NOUVEAU : On ajoute !gestionnaireBatiments.aDejaUneTente()
            if (joueur.aAssezDeRessources(COUT_TENTE) &&
                    peutConstruireIci(x, y, Constantes.RAYON_HITBOX_TENTE) &&
                    !gestionnaireBatiments.aDejaUneTente()) {

                joueur.consommerListeRessources(COUT_TENTE);
                TenteDeSoin t = new TenteDeSoin((int)x, (int)y, gestionnaireBatiments);
                gestionnaireBatiments.ajouterBatiment(t);
                annulerConstruction(); // On désactive le mode
            } else {
                System.out.println("Impossible de construire la Tente : ressources insuffisantes, collision, ou déjà construite !");
            }
        }
    }
}