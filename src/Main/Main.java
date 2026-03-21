package Main;

import Modele.Modele;
import Vue.Vue;
import Controleur.Controleur;

/**
 * Point d'entrée principal de l'application.
 * Cette classe est responsable de l'initialisation de l'architecture MVC (Modèle-Vue-Contrôleur)
 * et du lancement du jeu.
 */
public class Main {

    /**
     * Méthode principale exécutée au lancement du programme.
     * Elle instancie les trois composants majeurs dans un ordre strict pour garantir
     * que la vue et le contrôleur disposent des données nécessaires dès le départ.
     * * @param args Arguments de la ligne de commande (non utilisés ici).
     */
    public static void main(String[] args) {
        // 1. Initialisation des données, threads autonomes et logique métier (cœur du jeu)
        Modele monModele = new Modele();

        // 2. Initialisation de l'interface graphique, qui a besoin du modèle pour s'afficher
        Vue maVue = new Vue(monModele);

        // 3. Initialisation du contrôleur (écouteurs clavier/souris) pour lier les actions de la vue au modèle
        new Controleur(monModele, maVue);
    }
}