package Modele;
import Modele.Map;

/**
 * Représente le Quartier Général (HeadQuarters) du joueur.
 * C'est un bâtiment spécifique qui hérite des propriétés de base d'un Batiment.
 */
public class HQ extends Batiment {

    /**
     * Constructeur par défaut.
     * Place automatiquement le HQ exactement au centre géographique de la carte.
     */
    public HQ() {
        // Appelle le constructeur parent (Batiment) en lui passant le centre de la Map calculé dynamiquement
        super(Map.LARGEUR_MAP/2, Map.HAUTEUR_MAP/2);
    }
    // Récupère la position horizontale sur la carte
    public double getX(){ return x; }

    // Récupère la position verticale sur la carte
    public double getY(){ return y; }
}