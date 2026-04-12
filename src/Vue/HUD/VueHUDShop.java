package Vue.HUD;

import Modele.Items.Item;
import Modele.Joueur;
import Modele.Armes.Arme;
import Modele.Items.Armure;
import Modele.Objets;
import Modele.GestionnaireShop;
import Modele.Modele;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static Modele.Constantes.xOffset;

public class VueHUDShop {

    private final int HAUTEUR_ITEM = 100;
    private final int TAILLE_IMG = 80;
    private Map<Rectangle, Object> zonesCliquables = new HashMap<>();

    public int dessiner(Graphics g, int yDebut, Modele modele) {
        zonesCliquables.clear();
        Graphics2D g2d = (Graphics2D) g;
        int yCourant = yDebut;
        Joueur joueur = modele.getJoueur();
        GestionnaireShop shop = modele.getGestionnaireShop();

        // Titre principal
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 22));
        g2d.drawString("LA BOUTIQUE", xOffset, yCourant);

        yCourant += 30;
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("Vos Pièces : " + joueur.getPieces(), xOffset, yCourant);

        yCourant += 40;

        // --- SECTION 1 : ARMES ---
        yCourant = dessinerCategorie(g2d, yCourant, "ARMES", shop.getArmes());

        // --- SECTION 2 : ARMURES ---
        yCourant = dessinerCategorie(g2d, yCourant, "ARMURES", shop.getArmures());

        // --- SECTION 3 : OBJETS ---
        yCourant = dessinerCategorie(g2d, yCourant, "UTILITAIRES", shop.getObjets());

        // Instructions de bas de page
        yCourant += 20;
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.ITALIC, 12));
        g2d.drawString("Utilisez le pavé numérique pour acheter", xOffset, yCourant);

        return yCourant;
    }

    /**
     * Méthode générique pour dessiner n'importe quelle catégorie d'objets (Armes, Armures, etc.)
     */
    private int dessinerCategorie(Graphics2D g2d, int y, String titre, ArrayList<?> liste) {
        if (liste == null || liste.isEmpty()) return y;

        // Titre de la catégorie
        g2d.setColor(new Color(40, 40, 40));
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.drawString(titre, xOffset, y);
        y += 25; // Petit espace sous le titre

        for (int i = 0; i < liste.size(); i++) {
            Object obj = liste.get(i);
            String nom = "";
            String stats = "";
            String ressources = "";
            Image img = null;

            Rectangle rect = new Rectangle(xOffset, y, 260, HAUTEUR_ITEM);
            zonesCliquables.put(rect, obj);

            // --- 1. EXTRACTION DES DONNÉES ---
            if (obj instanceof Arme) {
                Arme a = (Arme) obj;
                nom = a.getNom();
                stats = "Atk: " + a.getDegats() + " | Portée: " + a.getPortee();
                ressources = "Coût: " + String.join(", ", a.getRessourcesNecessaires());
                img = a.getImage();
            } else if (obj instanceof Armure) {
                Armure arm = (Armure) obj;
                nom = arm.getNom();
                stats = "Bonus Vie: +" + arm.getBonusVie();
                ressources = "Coût: " + String.join(", ", arm.getRessourcesNecessaires());
                img = arm.getImage();
            } else if (obj instanceof Item) {
                Item it = (Item) obj;
                nom = it.getNom();
                stats = "Consommable";
                ressources = "Prix: " + it.getPrix() + " Or";
                img = it.getImage();
            }

            // --- 2. ENREGISTREMENT DE LA ZONE DE CLIC ---
            // On enregistre tout le rectangle de l'item (260 de large, HAUTEUR_ITEM de haut)
            Rectangle rectItem = new Rectangle(xOffset, y, 260, HAUTEUR_ITEM);
            zonesCliquables.put(rectItem, obj);

            // --- 3. DESSIN DU BOUTON "ACHETER" ---
            int btnLargeur = 75;
            int btnHauteur = 25;
            int btnX = xOffset + 180;
            // btnY est calculé pour être aligné avec le milieu des textes
            int btnY = y + 35;

            // Fond du bouton (Vert si achetable, gris sinon)
            g2d.setColor(new Color(34, 139, 34));
            g2d.fillRoundRect(btnX, btnY, btnLargeur, btnHauteur, 8, 8);

            // Texte du bouton
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 11));
            FontMetrics fm = g2d.getFontMetrics();
            int tx = btnX + (btnLargeur - fm.stringWidth("ACHETER")) / 2;
            int ty = btnY + ((btnHauteur - fm.getHeight()) / 2) + fm.getAscent();
            g2d.drawString("ACHETER", tx, ty);

            // --- 4. DESSIN DU BLOC ITEM (IMAGE + TEXTES) ---
            // Image à gauche
            if (img != null) {
                g2d.drawImage(img, xOffset, y, TAILLE_IMG, TAILLE_IMG, null);
            } else {
                g2d.setColor(new Color(0, 0, 0, 30));
                g2d.fillRect(xOffset, y, TAILLE_IMG, TAILLE_IMG);
            }

            // Textes à droite de l'image
            int xTexte = xOffset + TAILLE_IMG + 12;
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            g2d.drawString(nom, xTexte, y + 20);

            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            g2d.drawString(stats, xTexte, y + 40);

            g2d.setColor(new Color(139, 69, 19)); // Marron
            g2d.setFont(new Font("Arial", Font.BOLD, 11));
            g2d.drawString(ressources, xTexte, y + 65);

            // Ligne de séparation
            g2d.setColor(new Color(0, 0, 0, 20));
            g2d.drawLine(xOffset, y + HAUTEUR_ITEM - 5, xOffset + 255, y + HAUTEUR_ITEM - 5);

            y += HAUTEUR_ITEM;
        }

        return y + 20;
    }

    public Object getObjetAuClic(int x, int y) {
        for (Rectangle r : zonesCliquables.keySet()) {
            if (r.contains(x, y)) return zonesCliquables.get(r);
        }
        return null;
    }
}