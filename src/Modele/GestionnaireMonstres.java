package Modele;

import Modele.Monstres.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static Modele.Constantes.*;

/**
 * Classe responsable de l'apparition (spawn), du stockage et du nettoyage des monstres.
 * Fonctionne en coordination avec le cycle temporel pour gérer les vagues d'ennemis.
 */
public class GestionnaireMonstres {

    /** ---------- [Propriétés] ---------- **/

    private List<Monstre> monstres;
    private UpdateJN updateJN;
    private int nbMonstresMorts = 0;

    /** ---------- [Constructeurs] ---------- **/

    /**
     * Initialise le gestionnaire d'entités hostiles.
     *
     * @param updateJN - L'interface de communication avec la boucle temporelle du jeu
     */
    public GestionnaireMonstres(UpdateJN updateJN) {
        this.monstres = new CopyOnWriteArrayList<>();
        this.updateJN = updateJN;
    }

    /** ---------- [Accesseurs / Getters] ---------- **/

    public List<Monstre> getMonstres() {
        return monstres;
    }

    public int getNbMonstresMorts() {
        return nbMonstresMorts;
    }

    /** ---------- [Méthodes Publiques - Moteur Métier (Cycle de Vie)] ---------- **/

    /**
     * Enregistre le décès d'une entité et assure la distribution des récompenses
     * via la propagation de l'événement au modèle parent.
     *
     * @param m - Le monstre éliminé
     */
    public void supprimerMonstre(Monstre m){
        updateJN.monstreMort(m);
        monstres.remove(m);
    }

    public void incrementerMonstresMorts() {
        nbMonstresMorts++;
    }

    /**
     * Purge intégrale des listes d'entités hostiles (Typiquement exécuté au lever du soleil).
     */
    public void clearMonstres() {
        monstres.clear();
    }

    /**
     * Interroge le moteur de résolution spatial pour trouver la cible valide la plus proche.
     *
     * @param m - L'entité hostile cherchant une cible
     * @return La cible identifiée (Joueur ou Bâtiment), null si aucune cible n'est valide
     */
    public Localisable trouverCible(Localisable m){
        return updateJN.monstreTrouverCible(m);
    }

    /** ---------- [Méthodes Publiques - Moteur de Spawn] ---------- **/

    /**
     * Lit la configuration depuis un fichier JSON pour orchestrer le déploiement
     * algorithmique d'une vague d'ennemis relative au niveau de difficulté (Nuit).
     *
     * @param numeroNuit - L'index de la nuit en cours dictant la difficulté de la vague
     */
    public void genererMonstre(int numeroNuit) {
        try {
            String contenu = new String(Files.readAllBytes(Paths.get("src/Modele/Monstres/monstreNuit.json")));
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

    /**
     * Instancie et place un groupe défini d'un type d'entité spécifique.
     *
     * @param type - L'identifiant de la classe d'ennemi ("Slime", "Ogre", etc.)
     * @param quantite - Le nombre d'instances à générer
     */
    private void genererTypeMonstre(String type, int quantite) {
        for (int i = 0; i < quantite; i++) {
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

    /**
     * Calcule un point d'apparition aléatoire strictly localisé
     * sur les limites extérieures de la carte de jeu.
     *
     * @return Tableau contenant les coordonnées [X, Y] générées
     */
    private int[] calculerPositionAleatoireBords() {
        int x, y;
        int edge = (int) (Math.random() * 4);

        switch (edge) {
            case 0: // Bord Haut
                x = (int) (Math.random() * LARGEUR_MAP);
                y = 0;
                break;
            case 1: // Bord Droit
                x = LARGEUR_MAP;
                y = (int) (Math.random() * HAUTEUR_MAP);
                break;
            case 2: // Bord Bas
                x = (int) (Math.random() * LARGEUR_MAP);
                y = HAUTEUR_MAP;
                break;
            case 3: // Bord Gauche
                x = 0;
                y = (int) (Math.random() * HAUTEUR_MAP);
                break;
            default:
                x = 0;
                y = (int) (Math.random() * HAUTEUR_MAP);
        }
        return new int[]{x, y};
    }
}