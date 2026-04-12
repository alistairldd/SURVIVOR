package Modele;

import Modele.Batiments.Batiment;
import Modele.Monstres.Monstre;

import java.util.ArrayList;
import java.util.List;

/**
 * Cœur du système (Architecture MVC).
 * Le Modèle orchestre toutes les données du jeu. Il instancie le Joueur, la Carte,
 * lance les threads autonomes (Cycle temporel, Intelligence des bâtiments) et centralise
 * la logique des combats complexes (calcul des cônes d'attaque).
 */
public class Modele {

    // Indicateur de la page actuellement affichée dans le HUD (1: Etat du jeu, 2: Inventaire/Action, 3: Shop)
    private int hudPageActuelle = 1;

    // L'entité contrôlée par l'utilisateur
    private Joueur joueur;
    private GestionnaireBatiments gestionnaireBatiments;

    // Variables de structure
    private Ressource ressource;
    private Batiment batiment;

    // Le gestionnaire autonome du temps (Thread)
    private CycleJourNuit leCycleJourNuit;
    private GestionnaireShop gestionnaireShop;

    // Entité actuellement survolée par la souris (pour affichage d'infos)
    private Localisable cibleAffichage;

    private UpdateJN updateJN;

    // Indicateur de fin de partie
    private boolean partieTerminee = false;

    // Dans les attributs de Modele
    private boolean instructionsOuvert = false;

    // Constructeur de la classe Modele
    public Modele() {
        // Instancie le joueur et lui donne la référence à ce Modèle
        this.joueur = new Joueur(this);
        this.joueur.setHp(10);

        // Initialisation du jour et de la nuit
        this.updateJN = new UpdateJN(this);
        leCycleJourNuit = new CycleJourNuit(updateJN);

        this.gestionnaireBatiments = new GestionnaireBatiments(this);
        this.gestionnaireShop = new GestionnaireShop(this);
        this.cibleAffichage = joueur; // valeur initiale
    }

    /*---- GETTERS ET SETTERS ---- */

    public Joueur getJoueur() { return joueur; }
    public CycleJourNuit getLeCycleJourNuit() { return leCycleJourNuit; }
    public UpdateJN getUpdateJN() { return updateJN; }
    public int getHudPageActuelle() { return hudPageActuelle; }
    public void setHudPageActuelle(int page) { this.hudPageActuelle = page; }
    public GestionnaireShop getGestionnaireShop() { return gestionnaireShop; }
    public GestionnaireBatiments getGestionnaireBatiments() { return gestionnaireBatiments; }
    public boolean getPartieTerminee() { return partieTerminee; }

    /**
     * NOUVELLE MÉTHODE : Le "Kill Switch" (Bouton d'arrêt d'urgence).
     * Interrompt immédiatement tous les processus actifs du monde.
     */
    private void stopperTousLesThreadsDuJeu() {
        // 1. Arrêter le chronomètre principal
        if (leCycleJourNuit != null && leCycleJourNuit.isAlive()) {
            leCycleJourNuit.interrupt();
        }

        // 2. Arrêter tous les monstres
        if (updateJN != null && updateJN.getMonstres() != null) {
            for (Monstre m : updateJN.getMonstres()) {
                if (m != null && m.isAlive()) {
                    m.interrupt();
                }
            }
        }

        // 3. Arrêter tous les bâtiments
        if (gestionnaireBatiments != null) {
            gestionnaireBatiments.stopperTousLesThreads();
        }

        // 4. Arrêter les actions du joueur (Réparation)
        if (joueur != null) {
            joueur.stopperReparation();
        }
    }

    /**
     * Déclenche la fin de partie et gèle immédiatement le monde.
     */
    public void declencherGameOver() {
        if (!partieTerminee) {
            partieTerminee = true;
            System.out.println("GAME OVER ! Le joueur est mort.");

            // GEL DU JEU : On tue les threads dès maintenant, pas seulement au redémarrage
            stopperTousLesThreadsDuJeu();
        }
    }

    /**
     * Fonction mathématique utilitaire.
     */
    public double map(int debut, int fin, double valDebut, double valFin, double val){
        return (val - debut) * (valFin - valDebut) / (fin - debut) + valDebut;
    }

    public Localisable getCibleAffichage() { return cibleAffichage; }

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
     * Gère la logique d'attaque du joueur.
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
            double distance = Math.sqrt(vecteurMonstreX * vecteurMonstreX + vecteurMonstreY * vecteurMonstreY);

            if (distance >= 0 && distance <= portee) {
                double normMonstreX = vecteurMonstreX / distance;
                double normMonstreY = vecteurMonstreY / distance;
                double produitScalaire = (dirX * normMonstreX) + (dirY * normMonstreY);
                double seuilCosinus = Math.cos(angle / 2.0);

                if (produitScalaire >= seuilCosinus) {
                    m.perdreHp(joueur.getArmeEquipee().getDegats());
                    if (m.getHp() <= 0) { // Donne une récompense d'Or
                        joueur.addPieces(m.getDrop());
                    }
                }
            }
        }
    }

    public Monstre batTrouverMonstre(Batiment b) {
        List<Monstre> monstres = updateJN.getMonstres();
        for (Monstre m : monstres) {
            double distance = Math.hypot(m.getX() - b.getX(), m.getY() - b.getY());
            if (distance <= b.getRange()) {
                return m;
            }
        }
        return null;
    }

    public Joueur batTrouverJoueur(Batiment b) {
        double distance = Math.hypot(joueur.getX() - b.getX(), joueur.getY() - b.getY());
        if (distance <= b.getRange()) {
            return joueur;
        }
        return null;
    }

    public Batiment trouverBatimentSoignable() {
        double positionX = this.joueur.getX();
        double positionY = this.joueur.getY();

        for (Batiment b : gestionnaireBatiments.getBatiments()) {
            double distance = Math.hypot(b.getX() - positionX, b.getY() - positionY);
            if (distance <= b.getHealingRange() && b.getHp() < b.getMaxHp()) {
                return b;
            }
        }
        return null;
    }

    /**
     * Méthode pour réinitialiser le jeu après un Game Over.
     */
    public void reinitialiserJeu() {
        System.out.println("--- RELANCE DE LA PARTIE ---");

        // On s'assure que tout est bien arrêté avant de recréer les objets
        stopperTousLesThreadsDuJeu();

        this.partieTerminee = false;
        this.hudPageActuelle = 1;

        // Ordre d'instanciation sécurisé : Le plateau AVANT le temps
        this.joueur = new Joueur(this);

        this.gestionnaireBatiments = new GestionnaireBatiments(this);
        this.gestionnaireShop = new GestionnaireShop(this);

        this.updateJN = new UpdateJN(this);
        this.leCycleJourNuit = new CycleJourNuit(this.updateJN);

        this.cibleAffichage = joueur;
    }

    // Dans les getters/setters
    public boolean isInstructionsOuvert() { return instructionsOuvert; }
    public void toggleInstructions() { this.instructionsOuvert = !this.instructionsOuvert; }
}