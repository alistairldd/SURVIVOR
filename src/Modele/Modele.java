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
 * Gère l'état global du monde, les entités, la logique physique et l'état de l'UI.
 */
public class Modele {

    /** ---------- [Propriétés - Énumérations] ---------- **/

    public enum TypeConstruction { AUCUN, TOUR, TENTE, ABATIS, MORTIER }

    /** ---------- [Propriétés - UI & Affichage] ---------- **/

    private int hudPageActuelle = 1;
    private boolean instructionsOuvert = false;
    private boolean affichagePV = true;
    private boolean rotationAbatis = false;
    private boolean flashRougeActif = false;

    // Coordonnées monde du pointeur pour rendu et interactions
    private double sourisMondeX = 0;
    private double sourisMondeY = 0;

    // Éléments d'interface dynamiques
    private Localisable cibleAffichage;
    private List<int[]> pendingFloatingTexts = new ArrayList<>();

    /** ---------- [Propriétés - Moteur de Jeu] ---------- **/

    private Joueur joueur;
    private GestionnaireBatiments gestionnaireBatiments;
    private CycleJourNuit leCycleJourNuit;
    private GestionnaireShop gestionnaireShop;
    private GestionnaireSorts gestionnaireSorts;
    private UpdateJN updateJN;

    private Item sortEnAttente = null;
    private TypeConstruction modeConstruction = TypeConstruction.AUCUN;
    private boolean partieTerminee = false;
    private boolean jeuDemarre = false;

    /** ---------- [Constructeurs] ---------- **/

    /**
     * Initialise le modèle, les systèmes autonomes (cycle, bâtiments) et le joueur.
     */
    public Modele() {
        this.joueur = new Joueur(this);
        this.updateJN = new UpdateJN(this);
        this.leCycleJourNuit = new CycleJourNuit(updateJN);
        this.gestionnaireBatiments = new GestionnaireBatiments(this);
        this.gestionnaireShop = new GestionnaireShop(this);
        this.gestionnaireSorts = new GestionnaireSorts(this);
        this.cibleAffichage = joueur;
    }


    /** ---------- [Getters & Setters - Interface & UI] ---------- **/

    public int getHudPageActuelle() { return hudPageActuelle; }
    public void setHudPageActuelle(int page) { this.hudPageActuelle = page; }

    public boolean isInstructionsOuvert() { return instructionsOuvert; }
    public void toggleInstructions() { this.instructionsOuvert = !this.instructionsOuvert; }

    public boolean isAffichagePV() { return affichagePV; }
    public void toggleAffichagePV() { this.affichagePV = !this.affichagePV; }

    public boolean isRotationAbatis() { return rotationAbatis; }
    public void toggleRotationAbatis() { this.rotationAbatis = !this.rotationAbatis; }

    public double getSourisMondeX() { return sourisMondeX; }
    public double getSourisMondeY() { return sourisMondeY; }

    public void setPositionSourisMonde(double x, double y) {
        this.sourisMondeX = x;
        this.sourisMondeY = y;
    }

    public Localisable getCibleAffichage() { return cibleAffichage; }
    public boolean isFlashRougeActif() { return flashRougeActif; }

    public List<int[]> getPendingFloatingTexts() { return pendingFloatingTexts; }
    public void clearPendingFloatingTexts() { pendingFloatingTexts.clear(); }

    public boolean isJeuDemarre() { return jeuDemarre; }
    public void demarrerJeu() { this.jeuDemarre = true; }


    /** ---------- [Getters & Setters - Système & Métier] ---------- **/

    public Joueur getJoueur() { return joueur; }
    public CycleJourNuit getLeCycleJourNuit() { return leCycleJourNuit; }
    public UpdateJN getUpdateJN() { return updateJN; }
    public GestionnaireShop getGestionnaireShop() { return gestionnaireShop; }
    public GestionnaireBatiments getGestionnaireBatiments() { return gestionnaireBatiments; }
    public GestionnaireSorts getGestionnaireSorts() { return gestionnaireSorts; }
    public boolean getPartieTerminee() { return partieTerminee; }

    public void preparerSort(Item sort) { this.sortEnAttente = sort; }
    public Item getSortEnAttente() { return this.sortEnAttente; }
    public void setSortEnAttente() { this.sortEnAttente = null; }

    public TypeConstruction getModeConstruction() { return modeConstruction; }
    public void setModeConstruction(TypeConstruction mode) { this.modeConstruction = mode; }
    public void annulerConstruction() { this.modeConstruction = TypeConstruction.AUCUN; }


    /** ---------- [Méthodes Publiques - Core Loop & Événements] ---------- **/

    /**
     * Stoppe les processus et marque la partie comme terminée.
     */
    public void declencherGameOver() {
        if (!partieTerminee) {
            partieTerminee = true;
            stopperTousLesThreadsDuJeu();
        }
    }

    /**
     * Réinitialisation complète du jeu après un Game Over.
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
     * Met à jour la cible d'affichage selon la position du curseur en monde.
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
     * Calcule et applique les dégâts d'une attaque selon un cône de visée.
     * * @param angleAttaque - L'angle (en radians) vers lequel le joueur attaque
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

            // Vérifie si le monstre est dans la portée et dans le cône de l'arme
            if (distance <= portee) {
                double normX = vecteurMonstreX / distance;
                double normY = vecteurMonstreY / distance;
                double produitScalaire = (dirX * normX) + (dirY * normY);
                double seuilCosinus = Math.cos(angle / 2.0);

                if (produitScalaire >= seuilCosinus) {
                    m.perdreHp(joueur.getArmeEquipee().getDegats() + joueur.getAttack());
                }
            }
        }
    }

    /**
     * Gère la récompense (drop) lors de l'élimination d'un monstre.
     * * @param m - Le monstre éliminé
     */
    public void monstreMort(Monstre m) {
        joueur.addPieces(m.getDrop());
        pendingFloatingTexts.add(new int[]{(int) m.getX(), (int) m.getY(), m.getDrop()});
    }

    /**
     * Débloque la mécanique de minage et active les bâtiments associés.
     */
    public void debloquerMinage() {
        joueur.setaPioche(true);
        gestionnaireBatiments.activerLaMine();
    }

    /**
     * Compétence spéciale : Élimine tous les monstres présents et active un effet visuel.
     */
    public void declencherArmageddon() {
        this.flashRougeActif = true;
        List<Monstre> monstres = updateJN.getMonstres();

        for (int i = monstres.size() - 1; i >= 0; i--) {
            Monstre m = monstres.get(i);
            joueur.addPieces(m.getDrop());
            pendingFloatingTexts.add(new int[]{(int) m.getX(), (int) m.getY(), m.getDrop()});
            updateJN.getMonGestionnaireMonstres().incrementerMonstresMorts();
            m.interrupt();
            monstres.remove(i);
        }

        new Thread(() -> {
            try {
                Thread.sleep(400);
                this.flashRougeActif = false;
                System.out.println("Flash rouge désactivé.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

        System.out.println("Armageddon déclenché : Terrain nettoyé !");
    }


    /** ---------- [Méthodes Publiques - Recherche Spatiale] ---------- **/

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
            if (distance >= m.getMinRange() && distance <= m.getRange()) {
                return monstre;
            }
        }
        return null;
    }


    /** ---------- [Méthodes Publiques - Moteur de Construction (RTS)] ---------- **/

    /**
     * Vérifie si un emplacement est valide pour placer un bâtiment (collisions map/entités).
     * * @param x - Coordonnée X centrale
     * @param y - Coordonnée Y centrale
     * @param w - Largeur de l'encombrement
     * @param h - Hauteur de l'encombrement
     * @param angle - Angle de rotation
     * @return true si la place est libre
     */
    public boolean peutConstruireIci(double x, double y, double w, double h, double angle) {
        double[][] coinsNouveau = getCoinsRectangle(x, y, w, h, angle);

        // Limites de carte
        for (double[] coin : coinsNouveau) {
            if (coin[0] < 0 || coin[0] > LARGEUR_MAP || coin[1] < 0 || coin[1] > HAUTEUR_MAP) {
                return false;
            }
        }

        // Collision avec bâtiments existants
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

        // Collision avec le joueur
        double[][] coinsJoueur = getCoinsRectangle(joueur.getX(), joueur.getY(), J_TAILLE, J_TAILLE, 0);
        if (chevauchementPolygones(coinsNouveau, coinsJoueur)) {
            return false;
        }

        return true;
    }

    /**
     * Valide les ressources et place le bâtiment sur la carte.
     * * @param x - Coordonnée X de la tentative
     * @param y - Coordonnée Y de la tentative
     * @return true si la construction est effectuée avec succès
     */
    public boolean finaliserConstruction(double x, double y) {
        if (modeConstruction == TypeConstruction.TOUR) {
            if (joueur.aAssezDeRessources(COUT_TOUR) && peutConstruireIci(x, y, TOUR_LARGEUR_ENC, TOUR_HAUTEUR_ENC, 0)) {
                joueur.consommerListeRessources(COUT_TOUR);
                Tower t = new Tower((int)x, (int)y, gestionnaireBatiments);
                gestionnaireBatiments.ajouterBatiment(t);
                return true;
            }
            return false;
        }
        else if (modeConstruction == TypeConstruction.TENTE) {
            if (joueur.aAssezDeRessources(COUT_TENTE) && peutConstruireIci(x, y, TENTE_LARGEUR_ENC, TENTE_HAUTEUR_ENC, 0) && !gestionnaireBatiments.aDejaUneTente()) {
                joueur.consommerListeRessources(COUT_TENTE);
                TenteDeSoin t = new TenteDeSoin((int)x, (int)y, gestionnaireBatiments);
                gestionnaireBatiments.ajouterBatiment(t);
                annulerConstruction();
                return true;
            }
            return false;
        }
        else if (modeConstruction == TypeConstruction.ABATIS) {
            double angle = rotationAbatis ? -ABATIS_ANGLE_RAD : ABATIS_ANGLE_RAD;

            if (joueur.aAssezDeRessources(COUT_ABATIS) && peutConstruireIci(x, y, ABATIS_LARGEUR, ABATIS_HAUTEUR, angle)) {
                joueur.consommerListeRessources(COUT_ABATIS);
                Abatis a = new Abatis((int)x, (int)y, gestionnaireBatiments, rotationAbatis);
                gestionnaireBatiments.ajouterBatiment(a);
                return true;
            }
            return false;
        }
        else if (modeConstruction == TypeConstruction.MORTIER) {
            if (joueur.aAssezDeRessources(COUT_MORTIER) && peutConstruireIci(x, y, MORTIER_LARGEUR_ENC, MORTIER_HAUTEUR_ENC, 0)) {
                joueur.consommerListeRessources(COUT_MORTIER);
                Mortier m = new Mortier((int)x, (int)y, gestionnaireBatiments);
                gestionnaireBatiments.ajouterBatiment(m);
                return true;
            }
            return false;
        }
        return false;
    }


    /** ---------- [Méthodes Publiques - Moteur Physique (Collision & SAT)] ---------- **/

    /**
     * Vérifie si les prochaines coordonnées du joueur chevauchent la Hitbox d'un bâtiment solide.
     * * @param testX - Future coordonnée X du joueur
     * @param testY - Future coordonnée Y du joueur
     * @return true s'il y a collision
     */
    public boolean collisionAvecBatimentSolide(double testX, double testY) {
        double[][] coinsJoueur = getCoinsRectangle(testX, testY, Constantes.J_TAILLE, Constantes.J_TAILLE, 0);

        for (Batiment b : gestionnaireBatiments.getBatiments()) {
            if (b instanceof Abatis) {
                continue; // L'Abatis ne bloque pas les mouvements du joueur
            }

            // Décalage pour simuler la collision avec la base 2.5D du bâtiment
            double centreHitboxY = b.getY() + b.getOffsetYHitbox();

            double[][] coinsHitbox = getCoinsRectangle(
                    b.getX(),
                    centreHitboxY,
                    b.getLargeurHitbox(),
                    b.getHauteurHitbox(),
                    b.getAngleRotation()
            );

            if (chevauchementPolygones(coinsJoueur, coinsHitbox)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Utilitaire de mapping linéaire pour les échelles (ex: calculs Minimap).
     */
    public double map(int debut, int fin, double valDebut, double valFin, double val){
        return (val - debut) * (valFin - valDebut) / (fin - debut) + valDebut;
    }


    /** ---------- [Méthodes Privées - Sous-systèmes] ---------- **/

    /**
     * Met fin à l'exécution de tous les sous-processus liés au cycle ou aux entités.
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

    /**
     * Implémentation du théorème de l'Axe de Séparation (SAT) pour détecter la collision.
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

                double normalX = p2Y - p1Y;
                double normalY = p1X - p2X;

                double minA = Double.POSITIVE_INFINITY;
                double maxA = Double.NEGATIVE_INFINITY;
                for (double[] p : poly1) {
                    double proj = normalX * p[0] + normalY * p[1];
                    if (proj < minA) minA = proj;
                    if (proj > maxA) maxA = proj;
                }

                double minB = Double.POSITIVE_INFINITY;
                double maxB = Double.NEGATIVE_INFINITY;
                for (double[] p : poly2) {
                    double proj = normalX * p[0] + normalY * p[1];
                    if (proj < minB) minB = proj;
                    if (proj > maxB) maxB = proj;
                }

                // S'il existe un axe sans chevauchement, il n'y a pas collision
                if (maxA < minB || maxB < minA) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Calcule la matrice de points (les 4 coins) d'un rectangle orienté.
     */
    private double[][] getCoinsRectangle(double cx, double cy, double w, double h, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double[][] coins = new double[4][2];

        double hw = w / 2.0;
        double hh = h / 2.0;

        double[][] relatifs = {
                {-hw, -hh}, {hw, -hh}, {hw, hh}, {-hw, hh}
        };

        for (int i = 0; i < 4; i++) {
            double rx = relatifs[i][0];
            double ry = relatifs[i][1];
            coins[i][0] = cx + (rx * cos - ry * sin);
            coins[i][1] = cy + (rx * sin + ry * cos);
        }
        return coins;
    }
}