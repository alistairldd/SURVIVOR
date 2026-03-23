package Modele;

import java.util.ArrayList;
import static Modele.Constantes.*;

/**
 * Thread autonome responsable de "l'intelligence" des bâtiments.
 * Il tourne en continu en arrière-plan (Tower Defense mécanique) pour analyser
 * les cibles à portée et ordonner aux tours de tirer indépendamment des actions du joueur.
 */
public class ThreadBatiments extends Thread {

    // Référence au modèle global pour pouvoir lire la liste des monstres et des bâtiments
    private final Modele modele;


    /**
     * Constructeur du thread d'intelligence des bâtiments.
     * @param modele Le modèle contenant les données du jeu.
     */
    public ThreadBatiments(Modele modele) {
        this.modele = modele;
    }

    /**
     * Boucle principale d'analyse défensive.
     */
    @Override
    public void run() {
        // Tourne indéfiniment tant que le jeu est actif
        while (true) {
            // 1. Snapshot des données : On récupère l'état actuel du plateau
            // Liste de tous les bâtiments posés sur la carte
            ArrayList<Batiment> batiments = modele.getMap().getBatiments();
            // Liste de tous les monstres actuellement en vie
            ArrayList<Monstre> monstres = modele.getMonstres();

            // 2. Traitement : On demande à chaque bâtiment de faire son action s'il le peut
            // Parcours classique de la liste (évite parfois les erreurs d'accès concurrent par rapport au foreach)
            for (int i = 0; i < batiments.size(); i++) {
                Batiment b = batiments.get(i);

                // 3. Identification du type de bâtiment
                // On vérifie si ce bâtiment spécifique est une instance de Tour défensive
                if (b instanceof Tower) {
                    // Si oui, on force la conversion de type (cast) pour utiliser ses méthodes spécifiques
                    // et on lui demande de vérifier si elle peut tirer sur un des monstres de la liste
                    ((Tower) b).attaquerSiPossible(monstres);
                }
            }

            try {
                // 4. Temporisation : Met le thread en pause pendant 50ms pour laisser respirer l'ordinateur
                Thread.sleep(BAT_DELAY); // Petite pause pour ne pas surcharger le processeur
            } catch (InterruptedException e) {
                // Capture l'erreur si le thread est tué inopinément
                e.printStackTrace();
            }
        }
    }
}