package Modele;

import Modele.Batiments.Batiment;
import Modele.Monstres.Monstre;
import static Modele.Constantes.*;
import Modele.Batiments.Tower;
import Modele.Batiments.TenteDeSoin;
import Modele.Batiments.Abatis;

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
    private boolean rotationAbatis = false;

    // --- ÉTAT DE CONSTRUCTION (RTS) ---
    public enum TypeConstruction { AUCUN, TOUR, TENTE, ABATIS }
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
     * Vérifie si l'emplacement (x, y) est libre, à l'intérieur de la carte et constructible.
     * @param x Coordonnée X de la souris
     * @param y Coordonnée Y de la souris
     * @param rayonHitbox Le rayon d'encombrement du bâtiment qu'on veut placer
     * @return true si la place est libre, dans les limites ET qu'il fait jour.
     */
    /**
     * Vérifie si l'emplacement (x, y) est libre, à l'intérieur de la carte et constructible.
     */
    public boolean peutConstruireIci(double x, double y, int rayonHitbox) {
        if (!leCycleJourNuit.isDay()) return false;

        if (modeConstruction == TypeConstruction.TENTE && gestionnaireBatiments.aDejaUneTente()) {
            return false;
        }

        if (x - rayonHitbox < 0 || x + rayonHitbox > LARGEUR_MAP ||
                y - rayonHitbox < 0 || y + rayonHitbox > HAUTEUR_MAP) {
            return false; // Bords de carte
        }

        // --- MOTEUR DE COLLISION HYBRIDE ---
        for (Batiment b : gestionnaireBatiments.getBatiments()) {

            // CAS 1 : On tient le fantôme d'un ABATIS (OBB) à la souris
            if (modeConstruction == TypeConstruction.ABATIS) {
                double angleGhost = rotationAbatis ? -Constantes.ANGLE_ABATIS : Constantes.ANGLE_ABATIS;

                // Projection du bâtiment 'b' dans le repère local du fantôme
                double dx = b.getX() - x;
                double dy = b.getY() - y;
                double cos = Math.cos(-angleGhost);
                double sin = Math.sin(-angleGhost);
                double localX = dx * cos - dy * sin;
                double localY = dx * sin + dy * cos;

                double marge = b.getRayonHitbox();
                if (Math.abs(localX) <= (Constantes.LARGEUR_HITBOX_ABATIS / 2.0 + marge) &&
                        Math.abs(localY) <= (Constantes.HAUTEUR_HITBOX_ABATIS / 2.0 + marge)) {
                    return false; // Collision !
                }
            }
            // CAS 2 : On tient un objet circulaire (Tour, Tente...) et on frôle un Abatis existant
            else if (b instanceof Abatis) {
                Abatis ab = (Abatis) b;
                if (ab.contientPointIncline(x, y, rayonHitbox)) {
                    return false; // Collision !
                }
            }
            // CAS 3 : Classique (Cercle vs Cercle)
            else {
                double distance = Math.hypot(b.getX() - x, b.getY() - y);
                if (distance < b.getRayonHitbox() + rayonHitbox) {
                    return false; // Collision !
                }
            }
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
    public boolean finaliserConstruction(double x, double y) {
        if (modeConstruction == TypeConstruction.TOUR) {
            if (joueur.aAssezDeRessources(COUT_TOUR) &&
                    peutConstruireIci(x, y, Constantes.RAYON_HITBOX_TOUR)) {

                joueur.consommerListeRessources(COUT_TOUR);
                Tower t = new Tower((int)x, (int)y, gestionnaireBatiments);
                gestionnaireBatiments.ajouterBatiment(t);

                if (!(joueur.aAssezDeRessources(COUT_TOUR))) annulerConstruction();
                return true;
            }
            return false;
        }
        else if (modeConstruction == TypeConstruction.TENTE) {
            if (joueur.aAssezDeRessources(COUT_TENTE) &&
                    peutConstruireIci(x, y, Constantes.RAYON_HITBOX_TENTE) &&
                    !gestionnaireBatiments.aDejaUneTente()) {

                joueur.consommerListeRessources(COUT_TENTE);
                TenteDeSoin t = new TenteDeSoin((int)x, (int)y, gestionnaireBatiments);
                gestionnaireBatiments.ajouterBatiment(t);
                annulerConstruction();
                return true;
            }
            return false;
        } // --- CAS : ABATIS (REM PART) ---
        else if (modeConstruction == TypeConstruction.ABATIS) {
            // On utilise la largeur de la hitbox comme rayon de sécurité pour les bords de la map
            int rayonSecurite = (int)(Constantes.LARGEUR_HITBOX_ABATIS / 2);

            if (joueur.aAssezDeRessources(COUT_ABATIS) &&
                    peutConstruireIci(x, y, rayonSecurite)) {

                joueur.consommerListeRessources(COUT_ABATIS);
                // On passe l'état de rotation actuel au constructeur
                Abatis a = new Abatis((int)x, (int)y, gestionnaireBatiments, rotationAbatis);
                gestionnaireBatiments.ajouterBatiment(a);

                // Construction à la chaîne : le fantôme reste si on a encore 20 Bois !
                if (!(joueur.aAssezDeRessources(COUT_ABATIS))) {
                    annulerConstruction();
                }
                return true;
            }
            return false;
        }
        return false;
    }
}