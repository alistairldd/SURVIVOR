package Vue;

import Modele.Joueur;
import Modele.Armes.Arme;
import Modele.Items.Armure;
import Modele.Objets;
import Modele.GestionnaireShop;
import Modele.Modele;
import java.awt.*;
import java.util.ArrayList;

public class VueHUDShop {
    private final int xOffset = 20;

    public int dessiner(Graphics g, int yDebut, Modele modele) {
        Graphics2D g2d = (Graphics2D) g;
        int yCourant = yDebut;
        Joueur joueur = modele.getJoueur();
        GestionnaireShop shop = modele.getGestionnaireShop();

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString("--- Shop ---", 40, yCourant);

        yCourant += 30;
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        g2d.drawString("Pièces : " + joueur.getPieces(), xOffset, yCourant);

        yCourant += 40;
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));

        // --- SECTION ARMES ---
        yCourant = dessinerCategorieArmes(g2d, yCourant, shop.getArmes());

        // --- SECTION ARMURES ---
        yCourant = dessinerCategorieArmures(g2d, yCourant, shop.getArmures());

        // --- SECTION OBJETS ---
        yCourant = dessinerCategorieObjets(g2d, yCourant, shop.getObjets());

        yCourant += 30;
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString("[I] Quitter | [Pavé Num] Acheter", xOffset, yCourant);

        return yCourant;
    }

    private int dessinerCategorieArmes(Graphics2D g2d, int y, ArrayList<Arme> list) {
        if (list == null || list.isEmpty()) return y;

        g2d.setFont(new Font("Arial", Font.BOLD, 15));
        g2d.drawString("ARMES :", xOffset, y);
        y += 25;

        for (int i = 0; i < list.size(); i++) {
            Arme a = list.get(i);
            g2d.setFont(new Font("Arial", Font.PLAIN, 14));
            // On prend le nom directement depuis l'objet
            g2d.drawString((i + 1) + ". " + a.getNom() + " (Atk: " + a.getDegats() + ")", xOffset, y);
            y += 20;
        }
        return y + 10;
    }

    private int dessinerCategorieArmures(Graphics2D g2d, int y, ArrayList<Armure> list) {
        if (list == null || list.isEmpty()) return y;

        g2d.setFont(new Font("Arial", Font.BOLD, 15));
        g2d.drawString("ARMURES :", xOffset, y);
        y += 25;

        for (int i = 0; i < list.size(); i++) {
            Armure arm = list.get(i);
            g2d.setFont(new Font("Arial", Font.PLAIN, 14));
            // On prend le nom directement depuis l'objet
            g2d.drawString((i + 1) + ". " + arm.getNom(), xOffset, y);
            y += 20;
        }
        return y + 10;
    }

    private int dessinerCategorieObjets(Graphics2D g2d, int y, ArrayList<Objets> list) {
        if (list == null || list.isEmpty()) return y;

        g2d.setFont(new Font("Arial", Font.BOLD, 15));
        g2d.drawString("OBJETS :", xOffset, y);
        y += 25;

        for (int i = 0; i < list.size(); i++) {
            Objets obj = list.get(i);
            g2d.setFont(new Font("Arial", Font.PLAIN, 14));
            // On prend le nom directement depuis l'objet
            g2d.drawString((i + 1) + ". " + obj.getNom(), xOffset, y);
            y += 20;
        }
        return y + 10;
    }
}