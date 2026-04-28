package Modele.Armure;

import java.awt.*;
import java.util.Map;

/**
 * Classe abstraite définissant la base de toute armure équipable par le joueur.
 * Centralise les statistiques de protection, l'impact sur la mobilité et les coûts de fabrication.
 */
public abstract class Armure {

    /** ---------- [Propriétés] ---------- **/

    private String nom;
    private int reduction;
    private Image image;
    private Map<Integer, Integer> ressourcesNecessaires;
    private int vitesse;

    /** ---------- [Constructeurs] ---------- **/

    /**
     * Initialise les caractéristiques communes d'une pièce d'armure.
     *
     * @param nom - Le nom d'affichage de l'équipement
     * @param reduction - Le montant de dégâts absorbés ou le bonus de PV accordé
     * @param image - L'image associée pour le rendu visuel dans l'inventaire ou le HUD
     * @param vitesse - L'impact sur la vitesse de déplacement (valeur négative = ralentissement)
     * @param ressourcesNecessaires - Dictionnaire des coûts de fabrication (ID Ressource : Quantité requise)
     */
    public Armure(String nom, int reduction, Image image, int vitesse, Map<Integer, Integer> ressourcesNecessaires) {
        this.nom = nom;
        this.reduction = reduction;
        this.image = image;
        this.vitesse = vitesse;
        this.ressourcesNecessaires = ressourcesNecessaires;
    }

    /** ---------- [Accesseurs / Getters] ---------- **/

    public String getNom() {
        return nom;
    }

    public int getReduction() {
        return reduction;
    }

    public int getVitesse() {
        return vitesse;
    }

    public Map<Integer, Integer> getRessourcesNecessaires() {
        return ressourcesNecessaires;
    }

    public Image getImage() {
        return image;
    }
}