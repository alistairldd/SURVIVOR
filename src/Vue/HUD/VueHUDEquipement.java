package Vue.HUD;

import Modele.Joueur;
import Modele.Armes.Arme;
import Modele.Items.Item;
import java.awt.*;

public class VueHUDEquipement {

    private final int TAILLE_ICONE = 30;

    public int dessiner(Graphics g, int y, Joueur j, int xOffset) {
        Graphics2D g2d = (Graphics2D) g;

        int xEquip = xOffset ;
        int yEquip = y;

        // --- SLOT ARME (Classe Arme) ---
        Arme arme = j.getArmeEquipee();
        if (arme != null) {
            dessinerLigne(g2d, "Arme", arme.getNom(), arme.getImage(), xEquip, yEquip);
        } else {
            dessinerLigneVide(g2d, "Arme : Poings", xEquip, yEquip);
        }

        yEquip += TAILLE_ICONE + 10;

        // --- SLOT ARMURE (Classe Item) ---
        Item armure = j.getArmureEquipee(); // Ici on traite l'armure comme un Item
        if (armure != null) {
            dessinerLigne(g2d, "Armure", armure.getNom(), armure.getImage(), xEquip, yEquip);
        } else {
            dessinerLigneVide(g2d, "Armure : Aucune", xEquip, yEquip);
        }

        return y +  (yEquip - y) + TAILLE_ICONE;
    }

    private void dessinerLigne(Graphics2D g2d, String type, String nom, Image img, int x, int y) {
        g2d.setColor(new Color(0, 0, 0, 20));
        g2d.fillRect(x, y, TAILLE_ICONE, TAILLE_ICONE);

        if (img != null) {
            g2d.drawImage(img, x, y, TAILLE_ICONE, TAILLE_ICONE, null);
        }

        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString(type + " : " + nom, x + TAILLE_ICONE + 8, y + 25);
    }

    private void dessinerLigneVide(Graphics2D g2d, String texte, int x, int y) {
        g2d.setColor(new Color(0, 0, 0, 80));
        g2d.setFont(new Font("Arial", Font.ITALIC, 11));
        g2d.drawString(texte, x + 5, y + 25);
    }
}