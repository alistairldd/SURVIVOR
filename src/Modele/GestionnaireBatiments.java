package Modele;

import Modele.Batiments.*;
import Modele.Monstres.Monstre;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static Modele.Constantes.*;

/**
 * Orchestrateur centralisant la gestion du cycle de vie des bâtiments.
 * Gère le déploiement, la destruction, les interactions et l'évaluation spatiale.
 */
public class GestionnaireBatiments {

    /** ---------- [Propriétés] ---------- **/

    private List<Batiment> batiments;
    private Modele m;
    private HQ hq;

    /** ---------- [Constructeurs] ---------- **/

    /**
     * Initialise le gestionnaire et déploie les bâtiments vitaux de base (HQ et Mine).
     *
     * @param m - L'instance du modèle principal du jeu
     */
    public GestionnaireBatiments(Modele m) {
        this.m = m;
        this.batiments = new CopyOnWriteArrayList<>();

        this.hq = new HQ(this);
        ajouterBatiment(this.hq);
        ajouterBatiment(new Mine(this));
    }

    /** ---------- [Accesseurs / Getters] ---------- **/

    public List<Batiment> getBatiments() {
        return batiments;
    }

    public HQ getHQ() {
        return hq;
    }

    public boolean getPartieTerminee() {
        return m.getPartieTerminee();
    }

    /** ---------- [Méthodes Publiques - Gestion du Cycle de Vie] ---------- **/

    /**
     * Ajoute un bâtiment à la liste active et démarre son processus asynchrone s'il est inactif.
     *
     * @param b - Le bâtiment à déployer sur le terrain
     */
    public void ajouterBatiment(Batiment b) {
        this.batiments.add(b);
        if (!b.isAlive()) {
            b.start();
        }
    }

    /**
     * Envoie un signal d'interruption à l'ensemble des processus asynchrones des bâtiments.
     * Généralement appelé lors d'un Game Over ou d'une réinitialisation.
     */
    public void stopperTousLesThreads() {
        for (Batiment b : batiments) {
            if (b != null && b.isAlive()) {
                b.interrupt();
            }
        }
    }

    /**
     * Stoppe les processus actifs puis vide intégralement la liste des bâtiments.
     */
    public void clearBatiments() {
        stopperTousLesThreads();
        batiments.clear();
    }

    /** ---------- [Méthodes Publiques - Logique Métier & Détection] ---------- **/

    /**
     * Calcule et applique les dégâts de zone (AoE) suite à une explosion.
     * Applique un système de dégâts dégressifs selon la distance depuis l'épicentre.
     *
     * @param impactX - Coordonnée X de l'épicentre
     * @param impactY - Coordonnée Y de l'épicentre
     * @param coreR - Rayon de la zone de dégâts critiques
     * @param outerR - Rayon de la zone de dégâts secondaires
     * @param coreDmg - Montant des dégâts infligés au centre
     * @param outerDmg - Montant des dégâts infligés en périphérie
     */
    public void declencherExplosion(double impactX, double impactY, int coreR, int outerR, int coreDmg, int outerDmg) {
        List<Monstre> cibles = m.getUpdateJN().getMonstres();

        for (Monstre monstre : cibles) {
            double distance = Math.hypot(monstre.getX() - impactX, monstre.getY() - impactY);

            if (distance <= coreR) {
                monstre.perdreHp(coreDmg);
            }
            else if (distance <= outerR) {
                monstre.perdreHp(outerDmg);
            }
        }
    }

    /**
     * Réactive l'ensemble des mines présentes sur la carte.
     * Typiquement appelé suite à l'acquisition d'un équipement de type "Pioche".
     */
    public void activerLaMine() {
        for (Batiment b : batiments) {
            if (b instanceof Mine) {
                b.setFonctionnel(true);
            }
        }
    }

    /** ---------- [Méthodes Publiques - Recherche Spatiale] ---------- **/

    public Monstre trouverCible(Tower t) {
        return m.batTrouverMonstre(t);
    }

    public Monstre trouverCibleMortier(Mortier m) {
        return this.m.batTrouverMonstreMortier(m);
    }

    public Joueur trouverJoueur(TenteDeSoin tente) {
        return m.batTrouverJoueur(tente);
    }

    /**
     * @return true si au moins une instance de TenteDeSoin est déployée sur la carte.
     */
    public boolean aDejaUneTente() {
        for (Batiment b : batiments) {
            if (b instanceof TenteDeSoin) {
                return true;
            }
        }
        return false;
    }
}