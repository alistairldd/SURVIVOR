package Modele;

import java.util.ArrayList;
import java.util.List;
import static Modele.Constantes.*;
/**
 * Classe responsable de l'apparition (spawn), du stockage et du nettoyage des monstres.
 * Elle agit comme une "usine" à monstres pendant la nuit et maintient la liste à jour
 * pour que le Modèle puisse vérifier les collisions et les attaques.
 */
// Classe pour gérer les monstres dans le jeu
public class GestionnaireMonstres {

    // Liste dynamique stockant tous les monstres actuellement en vie sur la carte
    private ArrayList<Monstre> monstres;

    public GestionnaireMonstres() {
        // Initialise la liste vide au démarrage
        this.monstres = new ArrayList<>();
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
                    monstres.add(new Slime(x,y));
                    break;
                default:
                    // Par défaut (si type = 1 ou 2), crée un Slime pour le moment
                    monstres.add(new Slime(x,y)); // Par défaut, retourne un Slime
            }
        }
    }

    // Retourne la liste complète des monstres actuels (utilisée par la vue pour les dessiner)
    public ArrayList<Monstre> getMonstres() {
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
     * Parcourt la liste pour identifier et retirer les monstres dont les PV sont tombés à zéro.
     * Appelé régulièrement pendant la nuit par UpdateJN.
     */
    public void supprimerMonstresMorts (){
        // Crée une liste temporaire pour éviter l'erreur de modification concourante (ConcurrentModificationException)
        List<Monstre> monstresMorts = new ArrayList<>();

        // Phase 1 : Identification
        for (Monstre m : monstres) {
            // Si le monstre n'a plus de PV
            if (m.getHp() <= 0) {
                // On l'ajoute à la liste des condamnés
                monstresMorts.add(m);
            }
        }

        // Phase 2 : Suppression
        for (Monstre m : monstresMorts) {
            // Retire effectivement le monstre mort de la liste principale
            monstres.remove(m);
        }
    }

    /**
     * Calcule la distance entre deux entités localisables
     */
    public double calculerDistance(Localisable a, Localisable b) {
        // Calcul de la différence sur l'axe X et Y
        double diffX = a.getX() - b.getX();
        double diffY = a.getY() - b.getY();

        // Théorème de Pythagore pour la distance
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    public void chercheCible(Localisable joueur, List<Batiment> batiments) {
        // On crée une liste de tout ce qui est attaquable par les monstres
        List<Localisable> ciblesPotentielles = new ArrayList<>();
        ciblesPotentielles.add(joueur);
        ciblesPotentielles.addAll(batiments);

        for (Monstre m : monstres) {
            Localisable plusProche = null;
            double distMin = Double.MAX_VALUE;

            for (Localisable cible : ciblesPotentielles) {
                double d = calculerDistance(m, cible);
                if (d < distMin) {
                    distMin = d;
                    plusProche = cible;
                }
            }

            if (plusProche != null) {
                m.mettreAJourPosition(plusProche);
            }
        }
    }

}