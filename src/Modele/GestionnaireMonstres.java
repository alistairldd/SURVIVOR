package Modele;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static Modele.Constantes.*;
/**
 * Classe responsable de l'apparition (spawn), du stockage et du nettoyage des monstres.
 * Elle agit comme une "usine" à monstres pendant la nuit et maintient la liste à jour
 * pour que le Modèle puisse vérifier les collisions et les attaques.
 */
// Classe pour gérer les monstres dans le jeu
public class GestionnaireMonstres {

    // Liste dynamique stockant tous les monstres actuellement en vie sur la carte
    private List<Monstre> monstres;
    private UpdateJN updateJN;

    private int nbMonstresMorts = 0; // Compteur de monstres morts, peut être utilisé pour des statistiques ou des récompenses

    public GestionnaireMonstres(UpdateJN updateJN) {
        // Initialise la liste vide au démarrage
        this.monstres = new CopyOnWriteArrayList<>();
        this.updateJN = updateJN;
    }

    // Méthode pour incrémenter le compteur de monstres morts, appelée par les monstres eux-mêmes lorsqu'ils meurent
    public void incrementerMonstresMorts() {
        nbMonstresMorts++;
    }

    // Getter pour le nombre de monstres morts, utile pour les statistiques ou les récompenses
    public int getNbMonstresMorts() {
        return nbMonstresMorts;
    }

    /**
     * Fait apparaître un nombre précis de monstres aléatoirement sur les bords de la carte.
     * @param nombre Le nombre d'ennemis à générer (appelé par UpdateJN à la tombée de la nuit).
     */
    // Méthode pour générer un monstre aléatoire
    public void genererMonstre(int nombre) {

        // Boucle pour créer autant de monstres que demandé
        for (int i = 0; i < nombre; i++) {
            // Variables pour stocker les futures coordonnées d'apparition
            int x, y;
            // Génère un nombre entre 0 et 3 (inclus) pour choisir aléatoirement l'un des 4 bords de la carte
            int edge = (int) (Math.random() * 4); // 0: haut, 1: droite, 2: bas, 3: gauche

            // Applique les coordonnées en fonction du bord choisi
            switch (edge) {
                case 0: // Haut
                    // X aléatoire sur toute la largeur, Y tout en haut (0)
                    x = (int) (Math.random() * LARGEUR_MAP);
                    y = 0;
                    break;
                case 1: // Droite
                    // X collé à droite, Y aléatoire sur toute la hauteur
                    x = LARGEUR_MAP;
                    y = (int) (Math.random() * HAUTEUR_MAP);
                    break;
                case 2: // Bas
                    // X aléatoire sur toute la largeur, Y tout en bas
                    x = (int) (Math.random() * LARGEUR_MAP);
                    y = HAUTEUR_MAP;
                    break;
                case 3: // Gauche
                    // X collé à gauche (0), Y aléatoire sur toute la hauteur
                    x = 0;
                    y = (int) (Math.random() * HAUTEUR_MAP);
                    break;
                default: // Sécurité (normalement inatteignable) : par défaut à gauche
                    x = 0;
                    y = (int) (Math.random() * HAUTEUR_MAP);
            }

            // Génère un nombre pour choisir le type d'ennemi (prévu pour ajouter d'autres monstres plus tard)
            int type = (int) (Math.random() * 3); // Génère un nombre entre 0 et 2 pour savoir quel monstre faire apparaître
            switch (type) {
                case 0:
                    // Ajoute un nouveau Slime à la liste aux coordonnées calculées
                    monstres.add(new Slime(x,y, this));
                    break;
                default:
                    // Par défaut (si type = 1 ou 2), crée un Slime pour le moment
                    monstres.add(new Slime(x,y, this)); // Par défaut, retourne un Slime
            }
        }
    }

    // Retourne la liste complète des monstres actuels (utilisée par la vue pour les dessiner)
    public List<Monstre> getMonstres() {
        return monstres;
    }

    /**
     * Supprime instantanément tous les monstres de la carte.
     * Utilisé principalement au lever du jour pour nettoyer la carte.
     */
    // Méthode pour supprimer tous les monstres de la liste, utilisée pour faire disparaître les monstres à la fin de la nuit
    public void clearMonstres() {
        // Vide l'ArrayList
        monstres.clear();
    }

    /**
     *  Retirer les monstres dont les PV sont tombés à zéro.
     */
    public void supprimerMonstre (Monstre m){
            monstres.remove(m);
    }


    public Localisable trouverCible(Localisable m){
        return updateJN.monstreTrouverCible(m);
    }

}