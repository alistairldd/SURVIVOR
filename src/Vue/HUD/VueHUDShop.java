package Vue.HUD;

import Modele.Items.Item;
import Modele.Joueur;
import Modele.Armes.Arme;
import Modele.Armure.Armure;
import Modele.GestionnaireShop;
import Modele.Modele;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Modele.Constantes.TAILLE_IMG;
import static Modele.Constantes.xOffset;

/**
 * Moteur de rendu graphique de la Boutique.
 * Conserve la segmentation métier du shop tout en projetant un feedback visuel clair sur l'achat possible.
 */
public class VueHUDShop {

    /** ---------- [Propriétés] ---------- **/

    private static final int HAUTEUR_ITEM = 100;
    private static final int LARGEUR_ITEM = 255;

    private final Map<Rectangle, Object> zonesCliquables = new HashMap<>();

    /** ---------- [Méthodes Publiques - Rendu & Interaction] ---------- **/

    /**
     * Point d'entrée principal pour le rendu du catalogue.
     * Ordonne l'affichage en blocs métier distincts pour préserver la lecture du shop côté joueur.
     *
     * @param g - Le contexte graphique
     * @param yDebut - Position verticale de départ
     * @param modele - Référence au modèle global
     * @return La position Y finale après rendu
     */
    public int dessiner(Graphics g, int yDebut, Modele modele) {
        zonesCliquables.clear();

        Graphics2D g2d = (Graphics2D) g;
        int yCourant = yDebut;

        Joueur joueur = modele.getJoueur();
        GestionnaireShop shop = modele.getGestionnaireShop();

        // --- TITRE ET ÉTAT ÉCONOMIQUE DU JOUEUR ---
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 22));
        g2d.drawString("LA BOUTIQUE", xOffset, yCourant);

        yCourant += 30;
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("Vos Pièces : " + joueur.getPieces(), xOffset, yCourant);

        yCourant += 40;

        // --- EMPILAGE DES CATÉGORIES DANS LEUR ORDRE MÉTIER ---
        yCourant = dessinerCategorie(g2d, yCourant, "ARMES", shop.getArmesDansShop(), joueur);
        yCourant = dessinerCategorie(g2d, yCourant, "ARMURES", shop.getArmuresDansShop(), joueur);
        yCourant = dessinerCategorie(g2d, yCourant, "UTILITAIRES", shop.getObjets(), joueur);

        yCourant += 20;
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.ITALIC, 12));
        g2d.drawString("Cliquez sur un objet pour l'acquérir", xOffset, yCourant);

        return yCourant;
    }

    /**
     * Résout l'objet interactif ciblé à partir d'une coordonnée écran.
     * Fait le lien entre la projection graphique et l'intention d'achat.
     *
     * @param x - Position horizontale de la souris
     * @param y - Position verticale de la souris
     * @return L'objet détecté ou null si aucun bloc n'est touché
     */
    public Object getObjetAuClic(int x, int y) {
        for (Map.Entry<Rectangle, Object> entry : zonesCliquables.entrySet()) {
            if (entry.getKey().contains(x, y)) {
                return entry.getValue();
            }
        }
        return null;
    }

    /** ---------- [Méthodes Privées - Construction des Sections] ---------- **/

    /**
     * Dessine une catégorie complète du shop.
     * Préserve la hiérarchie visuelle des familles d'objets pour éviter un catalogue plat et confus.
     *
     * @param g2d - Contexte graphique 2D
     * @param y - Position Y courante
     * @param titre - Titre de la catégorie
     * @param liste - Contenu de la catégorie
     * @param joueur - Référence joueur pour l'évaluation d'achat
     * @return La nouvelle position Y après rendu de la catégorie
     */
    private int dessinerCategorie(Graphics2D g2d, int y, String titre, List<?> liste, Joueur joueur) {
        if (liste == null || liste.isEmpty()) {
            return y;
        }

        g2d.setColor(new Color(40, 40, 40));
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.drawString(titre, xOffset, y);

        y += 25;

        for (Object obj : liste) {
            y = dessinerItem(g2d, y, obj, joueur);
        }

        return y + 10;
    }

    /**
     * Construit le bloc visuel d'un article unique.
     * Injecte dans une même carte l'identité de l'objet, son coût et la lisibilité immédiate de son accessibilité.
     *
     * @param g2d - Contexte graphique 2D
     * @param y - Position Y actuelle
     * @param obj - Objet à rendre
     * @param joueur - Référence joueur pour la solvabilité
     * @return La nouvelle position Y après rendu
     */
    private int dessinerItem(Graphics2D g2d, int y, Object obj, Joueur joueur) {
        String nom = "";
        String stats = "";
        String ressourcesStr = "";
        Image img = null;
        boolean peutAcheter = false;

        // --- RÉSOLUTION MÉTIER DE L'OBJET À RENDRE ---
        if (obj instanceof Arme) {
            Arme arme = (Arme) obj;
            nom = arme.getNom();
            stats = "Dégâts : " + arme.getDegats() + " | Portée : " + arme.getPortee();
            ressourcesStr = formaterPrixRessources(arme.getRessourcesNecessaires());
            img = arme.getImage();
            peutAcheter = joueur.aAssezDeRessources(arme.getRessourcesNecessaires());
        } else if (obj instanceof Armure) {
            Armure armure = (Armure) obj;
            nom = armure.getNom();
            stats = "Réduction : " + armure.getReduction() + " | Vitesse : " + armure.getVitesse();
            ressourcesStr = formaterPrixRessources(armure.getRessourcesNecessaires());
            img = armure.getImage();
            peutAcheter = joueur.aAssezDeRessources(armure.getRessourcesNecessaires());
        } else if (obj instanceof Item) {
            Item item = (Item) obj;
            nom = item.getNom();
            stats = "Effet : " + item.getEffet();
            ressourcesStr = "Prix : " + item.getPrix() + " Or";
            img = item.getImage();
            peutAcheter = joueur.getPieces() >= item.getPrix();
        }

        // --- ENREGISTREMENT DE LA SURFACE INTERACTIVE ---
        Rectangle rectItem = new Rectangle(xOffset, y, LARGEUR_ITEM, HAUTEUR_ITEM);
        zonesCliquables.put(rectItem, obj);

        // --- FOND DE CARTE : FEEDBACK IMMÉDIAT SUR L'ACCESSIBILITÉ ---
        if (peutAcheter) {
            g2d.setColor(new Color(200, 255, 200, 150));
        } else {
            g2d.setColor(new Color(255, 200, 200, 150));
        }
        g2d.fillRoundRect(xOffset, y, LARGEUR_ITEM, HAUTEUR_ITEM - 10, 10, 10);

        // --- SOCLE VISUEL DE L'ICÔNE ---
        g2d.setColor(new Color(0, 0, 0, 40));
        g2d.fillRect(xOffset + 5, y + 5, TAILLE_IMG, TAILLE_IMG);

        if (img != null) {
            int imgW = img.getWidth(null);
            int imgH = img.getHeight(null);

            if (imgW > 0 && imgH > 0) {
                float ratio = Math.min((float) TAILLE_IMG / imgW, (float) TAILLE_IMG / imgH);
                int drawW = Math.round(imgW * ratio);
                int drawH = Math.round(imgH * ratio);

                g2d.drawImage(
                        img,
                        xOffset + (TAILLE_IMG - drawW) / 2,
                        y + (TAILLE_IMG - drawH) / 2,
                        drawW,
                        drawH,
                        null
                );
            }
        }

        // --- INFORMATIONS PRINCIPALES DE L'OBJET ---
        int xTexte = xOffset + TAILLE_IMG + 12;

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        g2d.drawString(nom, xTexte, y + 20);

        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.drawString(stats, xTexte, y + 40);

        g2d.setColor(new Color(139, 69, 19));
        g2d.setFont(new Font("Arial", Font.BOLD, 11));
        g2d.drawString(ressourcesStr, xTexte, y + 65);

        // --- SÉPARATEUR DE RYTHME ENTRE LES ENTRÉES ---
        g2d.setColor(new Color(0, 0, 0, 20));
        g2d.drawLine(xOffset, y + HAUTEUR_ITEM - 5, xOffset + LARGEUR_ITEM, y + HAUTEUR_ITEM - 5);

        return y + HAUTEUR_ITEM;
    }

    /** ---------- [Méthodes Privées - Formatage] ---------- **/

    /**
     * Traduit une map de ressources techniques en coût lisible par le joueur.
     * Réconcilie l'identité interne des ressources avec une lecture immédiatement exploitable dans le HUD.
     *
     * @param prix - Map des ressources nécessaires
     * @return Une chaîne formatée prête à être affichée
     */
    private String formaterPrixRessources(Map<Integer, Integer> prix) {
        if (prix == null || prix.isEmpty()) {
            return "Coût : Gratuit";
        }

        List<String> fragments = new ArrayList<>();

        for (Map.Entry<Integer, Integer> entry : prix.entrySet()) {
            fragments.add(entry.getValue() + " " + getNomRessource(entry.getKey()));
        }

        return "Coût : " + String.join(", ", fragments);
    }

    /**
     * Traduit l'identifiant technique d'une ressource en son nom d'affichage.
     * Évite de laisser remonter dans l'interface des codes internes sans valeur pour le joueur.
     *
     * @param id - Identifiant de la ressource
     * @return Le nom lisible de la ressource
     */
    private String getNomRessource(int id) {
        switch (id) {
            case 0:
                return "Bois";
            case 1:
                return "Pierre";
            case 2:
                return "Fer";
            case 3:
                return "Or";
            default:
                return "?";
        }
    }
}