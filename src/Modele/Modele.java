package Modele;

import Controleur.Controleur;
import Vue.Vue;

import java.util.ArrayList;
import java.util.List;

/*
 * La clase générale du modèle, elle contient les classes de données et les méthodes pour manipuler ces données.
 * Elle est utilisée pour stocker les données de l'application et pour effectuer des opérations sur ces données.
 * On va initialiser les threads du modèle à partir d'ici
 *
 */
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
    // (Variables potentiellement inutilisées ici, prévues pour l'architecture)
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

    // Constructeur de la classe Modele, il initialise les données du modèle.
    public Modele() {

        // Instancie le joueur et lui donne la référence à ce Modèle
        this.joueur = new Joueur(this);
        this.joueur.setHp(10);

        // Initialisation du jour et de la nuit
        // (Démarre automatiquement son propre thread interne)
        this.updateJN = new UpdateJN(this);
        leCycleJourNuit = new CycleJourNuit(updateJN);

        this.gestionnaireBatiments = new GestionnaireBatiments(this);
        this.gestionnaireShop = new GestionnaireShop(this);
        this.cibleAffichage = joueur; // valeur initiale
    }



    /*---- GETTERS ET SETTERS ---- */

    // Getter du joueur
    public Joueur getJoueur() {
        return joueur;
    }

    // Getter du cycle jour/nuit
    public CycleJourNuit getLeCycleJourNuit() {
        return leCycleJourNuit;
    }

    // Getter UpdateJN
    public UpdateJN getUpdateJN() {
        return updateJN;}

    // Getter et Setter pour la pagination du HUD
    public int getHudPageActuelle() { return hudPageActuelle; }
    public void setHudPageActuelle(int page) { this.hudPageActuelle = page; }

    public GestionnaireShop getGestionnaireShop() { return gestionnaireShop; }

    public GestionnaireBatiments getGestionnaireBatiments() {
        return gestionnaireBatiments;
    }

    // Getter pour l'indicateur de fin de partie
    public boolean getPartieTerminee() {
        return partieTerminee;
    }

    // Méthode pour déclencher la fin de partie (ex: lorsque le joueur meurt)
    public void declencherGameOver() {
        if (!partieTerminee) {
            partieTerminee = true;
            System.out.println("GAME OVER ! Le joueur est mort.");
        }
    }

    /**
     * Fonction mathématique utilitaire (équivalente à la fonction map() de Processing/Arduino).
     * Re-projette un nombre d'un intervalle de référence vers un nouvel intervalle.
     * Utilisé notamment par la Vue pour calculer les coordonnées sur la Minimap.
     */
    public double map(int debut, int fin, double valDebut, double valFin, double val){
        // Produit en croix pour adapter l'échelle
        return (val - debut) * (valFin - valDebut) / (fin - debut) + valDebut;
    }

    public Localisable getCibleAffichage() {
        return cibleAffichage;
    }

    public void verifierSurvol(double sourisMondeX, double sourisMondeY) {
         /*
            Cette méthode est appelée à chaque mouvement de la souris pour vérifier si le curseur survole une entité du jeu (joueur, bâtiment ou monstre).
            Elle prend en paramètre les coordonnées de la souris dans le monde du jeu, elle parcourt la liste des entités et retourne celle qui est la plus proche du curseur.
         */
        List<Localisable> ciblesPotentielles = new ArrayList<>();
        ciblesPotentielles.add(joueur);
        ciblesPotentielles.addAll(gestionnaireBatiments.getBatiments());
        ciblesPotentielles.addAll(updateJN.getMonstres());
        // Parcourt toutes les entités potentiellement survolables et calcule la distance entre chacune d'elles et la position de la souris
        for (Localisable cible : ciblesPotentielles) {
            double d = Math.hypot(sourisMondeX - cible.getX(), sourisMondeY - cible.getY());
            if (d < 20) {
                cibleAffichage = cible;
            }
        }

    }
    /**
     * Gère la logique complexe de l'attaque du joueur (calcul de collisions en cône).
     * @param angleAttaque L'angle en radians vers lequel le joueur a cliqué (calculé dans le contrôleur).
     */
    // Attaque du joueur
    public void joueurAttaque(double angleAttaque) {
         /*
            Cette méthode permet au joueur d'attaquer les monstres qui sont à proximité.
            Elle prend en paramètre les coordonnées de la souris, elle calcule l'angle entre le joueur et la souris,
            puis elle parcourt la liste des monstres du modèle et applique les dégâts à ceux qui sont dans le cône d'attaque de l'arme équipée.
         */

        // Récupérer les caractéristiques de l'arme équipée (pour savoir jusqu'où et à quelle largeur on frappe)
        double portee = joueur.getArmeEquipee().getPortee();
        double angle = joueur.getArmeEquipee().getAngle();
        // Récupérer la position centrale du joueur d'où part l'attaque
        double positionX = this.joueur.getX();
        double positionY = this.joueur.getY();


        // Récupérer la liste complète des cibles potentielles
        List<Monstre> monstres = updateJN.getMonstres();


        double dirX = Math.cos(angleAttaque);
        double dirY = Math.sin(angleAttaque);

        // Parcourir la liste des monstres du modèle et appliquer les dégâts à ceux qui sont dans le cône d'attaque de l'arme équipée
        for (Monstre m : monstres) {

            // Calculer le vecteur entre le joueur et ce monstre (composantes X et Y)
            double vecteurMonstreX = m.getX() - positionX;
            double vecteurMonstreY = m.getY() - positionY;

            // Calculer la distance euclidienne directe (hypoténuse) entre le joueur et ce monstre
            double distance = Math.sqrt(vecteurMonstreX * vecteurMonstreX + vecteurMonstreY * vecteurMonstreY);

            // 1ère vérification : Le monstre est-il suffisamment proche ?
            if (distance >= 0 && distance <= portee) {

                double normMonstreX = vecteurMonstreX / distance;
                double normMonstreY = vecteurMonstreY / distance;

                double produitScalaire = (dirX * normMonstreX) + (dirY * normMonstreY);

                double seuilCosinus = Math.cos(angle / 2.0);

                // 2ème vérification : La différence d'angle est-elle plus petite que la moitié de la largeur du cône de l'arme ?
                if (produitScalaire >= seuilCosinus) {
                    // Les deux conditions sont remplies : le monstre est touché !
                    m.perdreHp( joueur.getArmeEquipee().getDegats()); // On applique les dégâts
                    // Affiche l'information dans la console pour debug
                    System.out.println("Monstre touché ! " + m.getID() + "  HP restant : " + m.getHp());
                    if (m.getHp() <= 0) {
                        joueur.ajouterPieces(m.getDrop()); // Ajout des pièces au joueur
                        System.out.println("Monstre tué ! Gain de : " + m.getDrop() + " pièces.");}
                }
            }
        }
    }

    public Monstre batTrouverMonstre(Batiment b) {
        List<Monstre> monstres = updateJN.getMonstres();
        for (Monstre m : monstres) {
            // Calcule la distance directe (hypoténuse) entre le centre de la tour et le monstre
            double distance = Math.hypot(m.getX() - b.getX(), m.getY() - b.getY());

            // Si le monstre entre dans le périmètre de défense de la tour
            if (distance <= b.getRange()) {
                return m;
            }
        }
        return null;
    }

    public Joueur batTrouverJoueur(Batiment b) {
        // Calcule la distance directe (hypoténuse) entre le centre de la tour et le monstre
        double distance = Math.hypot(joueur.getX() - b.getX(), joueur.getY() - b.getY());

        // Si le monstre entre dans le périmètre de défense de la tour
        if (distance <= b.getRange()) {
            return joueur;
        }
        return null;
    }

    // ==========================================================
    // --- GESTION DES RÉPARATIONS ---
    // ==========================================================

    /**
     * Cherche le premier bâtiment à portée du joueur qui a besoin d'être soigné.
     * @return Le bâtiment blessé, ou null s'il n'y a rien à soigner à proximité.
     */
    public Batiment trouverBatimentSoignable() {
        // Récupération des coordonnées actuelles du joueur
        double positionX = this.joueur.getX();
        double positionY = this.joueur.getY();

        // On boucle sur tous les bâtiments présents sur la carte
        for (Batiment b : gestionnaireBatiments.getBatiments()) {

            // 1. Calcul de la distance géométrique exacte
            double distance = Math.hypot(b.getX() - positionX, b.getY() - positionY);

            // 2. Vérification de la portée (healingRange) ET de l'état de santé
            if (distance <= b.getHealingRange() && b.getHp() < b.getMaxHp()) {
                return b; // Cible valide trouvée, on la renvoie immédiatement
            }
        }

        // Aucun bâtiment blessé n'est à portée
        return null;
    }

    // Méthode pour réinitialiser le jeu après un Game Over
    public void reinitialiserJeu() {
        System.out.println("--- RELANCE DE LA PARTIE ---");

        // 1. ARRÊTER LES ANCIENS THREADS (OBLIGATOIRE !)
        // Si on ne fait pas ça, le jeu va ralentir à chaque partie et planter
        if (leCycleJourNuit != null && leCycleJourNuit.isAlive()) {
            leCycleJourNuit.interrupt();
        }
        for (Monstre m : updateJN.getMonstres()) {
            if (m.isAlive()) m.interrupt();
        }
        for (Batiment b : gestionnaireBatiments.getBatiments()) {
            if (b.isAlive()) b.interrupt();
        }

        // 2. Réinitialiser l'état du jeu
        this.partieTerminee = false;
        this.hudPageActuelle = 1;

        // 2. RECRÉER TOUS LES GESTIONNAIRES À NEUF
        this.joueur = new Joueur(this);
        this.updateJN = new UpdateJN(this);
        this.leCycleJourNuit = new CycleJourNuit(this.updateJN); // Relance le temps
        this.gestionnaireBatiments = new GestionnaireBatiments(this);
        this.gestionnaireShop = new GestionnaireShop(this);

        this.cibleAffichage = joueur;
    }


}