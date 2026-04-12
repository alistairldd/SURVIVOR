package Vue.HUD;

import Modele.Joueur;
import Modele.Armes.Arme;
import Modele.Items.Armure;
import Modele.Objets;
import Modele.GestionnaireShop;
import Modele.Modele;
import java.awt.*;
import java.util.ArrayList;

import static Modele.Constantes.xOffset;

public class VueHUDShop {

    private final int HAUTEUR_ITEM = 100;
    private final int TAILLE_IMG = 80;

    public int dessiner(Graphics g, int yDebut, Modele modele) {
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
        y += 20;

        for (int i = 0; i < liste.size(); i++) {
            Object obj = liste.get(i);
            String nom = "";
            String stats = "";
            String ressources = "";
            Image img = null;

            // Extraction des données selon le type d'objet
            if (obj instanceof Arme) {
                Arme a = (Arme) obj;
                nom = a.getNom();
                stats = "Dégâts: " + a.getDegats() + " | Portée: " + a.getPortee();
                ressources = "Coût: " + String.join(", ", a.getRessourcesNecessaires());
                img = a.getImage();
            }
            else if (obj instanceof Armure) {
                Armure arm = (Armure) obj;
                nom = arm.getNom();
                stats = "Bonus Vie: +" + arm.getBonusVie();
                ressources = "Coût: " + String.join(", ", arm.getRessourcesNecessaires());
                img = arm.getImage();
            }
            else if (obj instanceof Objets) {
                Objets o = (Objets) obj;
                nom = o.getNom();
                stats = "Consommable";
                ressources = "Prix: " + o.getPrix() + " Pièces";
                // Si vous n'avez pas d'image pour les objets, img restera null
            }

            // --- DESSIN DU BLOC ITEM ---

            // Image
            if (img != null) {
                g2d.drawImage(img, xOffset, y, TAILLE_IMG, TAILLE_IMG, null);
            } else {
                g2d.setColor(new Color(0, 0, 0, 30));
                g2d.fillRect(xOffset, y, TAILLE_IMG, TAILLE_IMG);
                g2d.setColor(Color.GRAY);
                g2d.setFont(new Font("Arial", Font.ITALIC, 10));
                g2d.drawString("No Img", xOffset + 20, y + 45);
            }

            // Textes
            int xTexte = xOffset + TAILLE_IMG + 15;
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 15));
            g2d.drawString((i + 1) + ". " + nom, xTexte, y + 20);

            g2d.setFont(new Font("Arial", Font.PLAIN, 13));
            g2d.drawString(stats, xTexte, y + 40);

            g2d.setColor(new Color(139, 69, 19)); // Marron pour les ressources
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            g2d.drawString(ressources, xTexte, y + 65);

            // Séparateur
            g2d.setColor(new Color(0, 0, 0, 20));
            g2d.drawLine(xOffset, y + HAUTEUR_ITEM - 5, xOffset + 260, y + HAUTEUR_ITEM - 5);

            y += HAUTEUR_ITEM;
        }

        return y + 20; // Espace après la catégorie
    }
}