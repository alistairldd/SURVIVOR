package Modele;

import java.util.ArrayList;
import static Modele.Constantes.*;

public class GestionnaireBatiments {
    private ArrayList<Batiment> batiments;
    private Modele m;
    private HQ hq;

    public GestionnaireBatiments(Modele m) {
        this.m = m;
        this.batiments = new ArrayList<Batiment>();

        // On initialise le HQ en lui passant l'instance actuelle du gestionnaire
        this.hq = new HQ(this);
        ajouterBatiment(this.hq);

        ajouterBatiment(new Mine(this));

        Tower tourEndommagee = new Tower(HAUTEUR_MAP/2, LARGEUR_MAP/3, this);
        tourEndommagee.setHp(0);
        ajouterBatiment(tourEndommagee);

        ajouterBatiment(new TenteDeSoin(HAUTEUR_MAP/3, LARGEUR_MAP/2, this));
    }

    /**
     * NOUVELLE MÉTHODE : Centralise l'ajout ET le démarrage du Thread.
     * C'est crucial pour ne pas oublier de démarrer un bâtiment et pour pouvoir le traquer.
     */
    public void ajouterBatiment(Batiment b) {
        this.batiments.add(b);
        // On vérifie que le thread n'est pas déjà lancé avant de le démarrer
        if (!b.isAlive()) {
            b.start();
        }
    }

    public ArrayList<Batiment> getBatiments() {
        return batiments;
    }

    public Monstre trouverCible(Tower t) {
        return m.batTrouverMonstre(t);
    }

    public Joueur trouverJoueur(TenteDeSoin tente) {
        return m.batTrouverJoueur(tente);
    }

    /**
     * NOUVELLE MÉTHODE : Arrêt propre de tous les threads avant de vider la mémoire.
     */
    public void stopperTousLesThreads() {
        for (Batiment b : batiments) {
            if (b != null && b.isAlive()) {
                b.interrupt(); // Envoie le signal de Game Over au Thread
            }
        }
    }

    public void clearBatiments() {
        stopperTousLesThreads(); // Sécurité : on tue tout avant de vider la liste
        batiments.clear();
    }

    public HQ getHQ() {
        return hq;
    }

    public boolean getPartieTerminee() {
        return m.getPartieTerminee();
    }
}