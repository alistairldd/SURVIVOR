package Modele.Batiments;

import Modele.GestionnaireBatiments;
import Modele.Localisable;
import Modele.Ressource;

import java.util.ArrayList;
import java.util.Random;

import static Modele.Constantes.*;

/**
 * Bâtiment de production automatisé (Mine).
 * Génère des ressources périodiquement. Se place aléatoirement sur la carte
 * tout en évitant les bordures et la position du Quartier Général (HQ).
 */
public class Mine extends Batiment implements Localisable {

    private int range;
    private ArrayList<Ressource> ressources;
    private Random randomNumbers = new Random();

    /**
     * Construit une mine à des coordonnées aléatoires sécurisées.
     */
    public Mine(GestionnaireBatiments gB) {
        // 1. Initialisation temporaire en (0, 0) pour satisfaire la classe mère
        super(0, 0, gB, TOWER_BASE_RANGE);

        // 2. Recherche d'une position de Spawn valide
        int marge = 300; // Distance minimum des bords de la map
        double distanceSecuriteHQ = 500.0; // Rayon d'exclusion autour du HQ (car le HQ est énorme)
        boolean positionValide = false;

        HQ hq = gB.getHQ();

        // Boucle de recherche : on tire des coordonnées au sort jusqu'à en trouver des bonnes
        while (!positionValide) {
            // X et Y bornés entre [marge] et [TAILLE_MAX - marge]
            this.x = marge + randomNumbers.nextInt(LARGEUR_MAP - 2 * marge);
            this.y = marge + randomNumbers.nextInt(HAUTEUR_MAP - 2 * marge);

            // Si le HQ est bien présent sur la carte, on vérifie l'éloignement
            if (hq != null) {
                double distance = Math.hypot(this.x - hq.getX(), this.y - hq.getY());
                if (distance >= distanceSecuriteHQ) {
                    positionValide = true; // Assez loin, on valide !
                }
            } else {
                positionValide = true; // Sécurité si aucun HQ n'est trouvé
            }
        }

        // 3. Initialisation des autres caractéristiques
        this.hp = HP_MINE;
        this.range = MINE_BASE_RANGE;
        this.largeurEncombrement = MINE_LARGEUR_ENC;
        this.hauteurEncombrement = MINE_HAUTEUR_ENC;
        this.largeurHitbox = MINE_LARGEUR_HIT;
        this.hauteurHitbox = MINE_HAUTEUR_HIT;
        this.offsetYHitbox = MINE_OFFSET_Y;
        this.ressources = new ArrayList<>();
        this.attaquable = false; // La mine n'est pas attaquable, elle ne peut pas être détruite par les monstres
        this.setFonctionnel(false);
    }

    public int getRange() { return range; }

    public ArrayList<Ressource> getRessources() {
        return ressources;
    }

    public void setRessources(ArrayList<Ressource> ressources) {
        this.ressources = ressources;
    }

    public void genererRessources() {
        int tirage = (int) (Math.random() * 100);
        int typeChoisi;

        if (tirage < PROBA_PIERRE) {
            typeChoisi = 1; // Pierre
        }
        else if (tirage < PROBA_PIERRE + PROBA_FER) {
            typeChoisi = 2; // Fer
        }
        else {
            typeChoisi = 3; // Or
        }

        this.ressources.add(new Ressource(typeChoisi));
    }

    @Override
    public void run() {
        while (!gBatiments.getPartieTerminee()) {


            // Si les PV tombent à 0 ou moins, la mine disjoncte et arrête de produire
            if (this.hp <= 0 && isFonctionnel()) {
                setFonctionnel(false);
            }

            if (isFonctionnel()) {
                try {
                    genererRessources();
                    Thread.sleep(MINE_DELAY);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            } else {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    @Override
    public int getMaxHp() {
        return HP_MINE;
    }

    @Override
    public String getNom() {
        return "Mine";
    }
}