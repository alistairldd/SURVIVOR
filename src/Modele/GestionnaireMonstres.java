package Modele;

import Modele.Monstres.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static Modele.Constantes.*;

/**
 * Classe responsable de l'apparition (spawn), du stockage et du nettoyage des monstres.
 * Elle agit comme une "usine" à monstres pendant la nuit et maintient la liste à jour
 * pour que le Modèle puisse vérifier les collisions et les attaques.
 */
public class GestionnaireMonstres {

    /** ---------- [Propriétés] ---------- **/

    // Liste dynamique stockant tous les monstres actuellement en vie sur la carte
    private List<Monstre> monstres;
    private UpdateJN updateJN;
    private int nbMonstresMorts = 0;

    /** ---------- [Constructeurs] ---------- **/

    public GestionnaireMonstres(UpdateJN updateJN) {
        // Initialise la liste vide au démarrage
        this.monstres = new CopyOnWriteArrayList<>();
        this.updateJN = updateJN;
    }

    /** ---------- [Accesseurs / Getters] ---------- **/

    // Retourne la liste complète des monstres actuels (utilisée par la vue pour les dessiner)
    public List<Monstre> getMonstres() {
        return monstres;
    }

    // Getter pour le nombre de monstres morts, utile pour les statistiques ou les récompenses
    public int getNbMonstresMorts() {
        return nbMonstresMorts;
    }

    /** ---------- [Méthodes Publiques - Moteur Métier (Cycle de Vie)] ---------- **/

    // Méthode pour incrémenter le compteur de monstres morts, appelée par les monstres eux-mêmes lorsqu'ils meurent
    public void incrementerMonstresMorts() {
        nbMonstresMorts++;
    }

    /**
     * Retirer les monstres dont les PV sont tombés à zéro.
     */
    public void supprimerMonstre(Monstre m){
        updateJN.monstreMort(m);
        monstres.remove(m);
    }

    /**
     * Supprime instantanément tous les monstres de la carte.
     * Utilisé principalement au lever du jour pour nettoyer la carte.
     */
    public void clearMonstres() {
        // Vide l'ArrayList
        monstres.clear();
    }

    public Localisable trouverCible(Localisable m){
        return updateJN.monstreTrouverCible(m);
    }

    /** ---------- [Méthodes Publiques - Moteur de Spawn] ---------- **/

    /**
     * Fait apparaître un nombre précis de monstres en fonction de la nuit aléatoirement sur les bords de la carte.
     * @param numeroNuit Le nombre d'ennemis de la nuit à générer (appelé par UpdateJN à la tombée de la nuit).
     */
    public void genererMonstre(int numeroNuit) {
        try {
            if (numeroNuit > 10) numeroNuit = 10;

            InputStream is = Monstre.class.getResourceAsStream("/Modele/Monstres/monstreNuit.json");

            if (is == null) {
                throw new RuntimeException("Le JSON n'est pas dans le JAR !");
            }

            String contenu;
            try (java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A")) {
                contenu = s.hasNext() ? s.next() : "";
            }

            JsonArray nuits = JsonParser.parseString(contenu).getAsJsonArray();

            JsonObject nuitActuelle = null;
            for (JsonElement element : nuits) {
                JsonObject obj = element.getAsJsonObject();
                if (obj.get("id").getAsInt() == numeroNuit) {
                    nuitActuelle = obj;
                    break;
                }
            }

            if (nuitActuelle != null) {
                int nbSlimes = nuitActuelle.get("slime").getAsInt();
                int nbSlimeMutants = nuitActuelle.get("slimeMutant").getAsInt();
                int nbOgres = nuitActuelle.get("ogre").getAsInt();
                int nbGobelins = nuitActuelle.get("gobelin").getAsInt();

                genererTypeMonstre("Slime", nbSlimes);
                genererTypeMonstre("SlimeMutant", nbSlimeMutants);
                genererTypeMonstre("Ogre", nbOgres);
                genererTypeMonstre("Gobelin", nbGobelins);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** ---------- [Méthodes Privées - Utilitaires de Spawn] ---------- **/

    private void genererTypeMonstre(String type, int quantite) {
        for (int i = 0; i < quantite; i++) {
            // On calcule toujours une position aléatoire sur les bords de la map
            int[] pos = calculerPositionAleatoireBords();

            switch (type) {
                case "Slime":
                    monstres.add(new Slime(pos[0], pos[1], this));
                    break;
                case "SlimeMutant":
                    monstres.add(new SlimeMutant(pos[0], pos[1], this));
                    break;
                case "Ogre":
                    monstres.add(new Ogre(pos[0], pos[1], this));
                    break;
                case "Gobelin":
                    monstres.add(new Gobelin(pos[0], pos[1], this));
                    break;
            }
        }
    }

    private int[] calculerPositionAleatoireBords() {
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
        return new int[]{x, y};
    }
}