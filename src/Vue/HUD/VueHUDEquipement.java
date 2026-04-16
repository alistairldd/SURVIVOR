package Vue.HUD;

import Modele.Armure.Armure;
import Modele.Joueur;
import Modele.Armes.Arme;
import Modele.Items.Item;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static Modele.Constantes.xOffset;
import static Modele.Constantes.TAILLE_ICONE;

public class VueHUDEquipement {


    public int dessiner(Graphics g, int y, Joueur j) {
        Graphics2D g2d = (Graphics2D) g;

        int xEquip = xOffset;
        int yEquip = y;

        // On met à jour yEquip à chaque fois pour que les éléments ne se chevauchent pas

        // --- SLOT ARMES (Classe Arme) ---

        yEquip = dessinerArmes(g2d, j, xEquip, yEquip);

        // --- SLOT ARMURE (Classe Armure) ---

        yEquip = dessinerArmure(g2d, j, xEquip, yEquip);

        // --- SLOT ITEMS (Classe Item) ---

        yEquip = dessinerItem(g2d, j, xEquip, yEquip);

        return y + (yEquip - y) + TAILLE_ICONE;
    }

    private int dessinerArmes(Graphics2D g2d, Joueur j, int x, int y) {

        // Récupération des armes équipées et non équipées

        Arme armePrincipale = j.getArmeEquipee();
        Arme armeSecondaire = j.getArmePasEquipee();

        // Récupération des images (avec gestion du cas où l'arme secondaire est absente)

        Image img = armePrincipale != null ? armePrincipale.getImage() : null;
        Image imgSecondaire = armeSecondaire != null ? armeSecondaire.getImage() : null;

        String nomArmePrincipale = armePrincipale.getNom();

        int TailleIconePrinc = TAILLE_ICONE * 3;

        // Fond semi-transparent pour différencier les slots
        g2d.setColor(new Color(0, 0, 0, 20));
        g2d.fillRect(x, y, TailleIconePrinc, TailleIconePrinc);
        g2d.fillRect(x + TailleIconePrinc + 10, y, TAILLE_ICONE, TAILLE_ICONE);

        // Dimensions originales de l'image
        int imgW = img.getWidth(null);
        int imgH = img.getHeight(null);


        // Calcul du ratio pour tenir dans TAILLE_IMG x TAILLE_IMG
        float ratio = Math.min((float) TailleIconePrinc / imgW, (float) TailleIconePrinc / imgH);

        int drawW = Math.round(imgW * ratio);
        int drawH = Math.round(imgH * ratio);

        // Centrage dans le carré
        int offsetX = x + (TailleIconePrinc - drawW) / 2;
        int offsetY = y + (TailleIconePrinc - drawH) / 2;

        g2d.setColor(Color.BLACK);
        g2d.drawString(nomArmePrincipale, x, y - 5);


        int degats = armePrincipale.getDegats();
        int portee = armePrincipale.getPortee();
        int cadence = armePrincipale.getCadence();
        // On convertit la cadence en secondes
        String cad = String.format("%.2f", cadence/600.0);
        Font font = g2d.getFont();
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        if (j.getAttack()>0){
            g2d.drawString("Degats  : " + degats + " + " + j.getAttack(),  x+TailleIconePrinc + 5, y + TAILLE_ICONE + 30);
        }
        else {
            g2d.drawString("Degats  : " + degats,  x+TailleIconePrinc + 5, y + TAILLE_ICONE + 30);
        }
        g2d.drawString("Portée  : " + portee,  x+TailleIconePrinc + 5, y + TAILLE_ICONE + 50);
        g2d.drawString("Vit. Atq : " + cad + "s", x+TailleIconePrinc + 5, y + TAILLE_ICONE + 70);

        g2d.setFont(font);

        g2d.drawImage(img, offsetX, offsetY, drawW, drawH, null);
        if (imgSecondaire != null) {

            int img2W = img.getWidth(null);
            int img2H = img.getHeight(null);

            // Calcul du ratio pour tenir dans TAILLE_IMG x TAILLE_IMG
            float ratio2 = Math.min((float) TAILLE_ICONE / img2W, (float) TAILLE_ICONE / img2H);

            int draw2W = Math.round(img2W * ratio2);
            int draw2H = Math.round(img2H * ratio2);

            // Centrage dans le carré
            int offsetX2 = x + (TAILLE_ICONE - draw2W) / 2;
            int offsetY2 = y + (TAILLE_ICONE - draw2H) / 2;

            g2d.drawImage(imgSecondaire, offsetX2 + TailleIconePrinc + 10, offsetY2, draw2W, draw2H, null);
        }

        return y + 3*TAILLE_ICONE + 25;
    }

    private int dessinerArmure(Graphics2D g2d, Joueur j, int x, int y) {

        int TailleIconeArmure = TAILLE_ICONE * 3;


        // Fond semi-transparent pour différencier les slots
        g2d.setColor(new Color(0, 0, 0, 20));
        g2d.fillRect(x, y, TailleIconeArmure, TailleIconeArmure);
        g2d.fillRect(x + TailleIconeArmure + 10, y, TAILLE_ICONE, TAILLE_ICONE);

        Armure armure = j.getArmurePrincipale();
        Armure armureSecondaire = j.getArmureSecondaire();

        if (armure != null) {
            Image img = armure.getImage();
            int imgW = img.getWidth(null);
            int imgH = img.getHeight(null);

            // Calcul du ratio pour tenir dans TAILLE_ICONE x TAILLE_ICONE
            float ratio = Math.min((float) TailleIconeArmure / imgW, (float) TailleIconeArmure / imgH);

            int drawW = Math.round(imgW * ratio);
            int drawH = Math.round(imgH * ratio);

            // Centrage dans le carré
            int offsetX = x + (TailleIconeArmure - drawW) / 2;
            int offsetY = y + (TailleIconeArmure - drawH) / 2;

            // Affichage du nom de l'armure au-dessus de l'icône
            g2d.setColor(Color.BLACK);
            g2d.drawString(armure.getNom(), x, y-5);
            g2d.drawImage(img, offsetX, offsetY, drawW, drawH, null);


            // affichage des stats de l'armure à droite de l'icône
            int reduction = armure.getReduction();
            int bonusVitesse = armure.getVitesse();
            Font font = g2d.getFont();
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));

            g2d.drawString("Reduction : +" + reduction,  x+TailleIconeArmure + 5, y + TAILLE_ICONE + 30);
            if (bonusVitesse >0){
                g2d.drawString("Vit : +" + bonusVitesse,  x+TailleIconeArmure + 5, y + TAILLE_ICONE + 50);
            }
            else {
                g2d.drawString("Vit : " + bonusVitesse,  x+TailleIconeArmure + 5, y + TAILLE_ICONE + 50);
            }
            g2d.setFont(font);
        }

        if (armureSecondaire != null){
            Image img2 = armureSecondaire.getImage();
            int img2W = img2.getWidth(null);
            int img2H = img2.getHeight(null);

            // Calcul du ratio pour tenir dans TAILLE_ICONE x TAILLE_ICONE
            float ratio2 = Math.min((float) TAILLE_ICONE / img2W, (float) TAILLE_ICONE / img2H);

            int draw2W = Math.round(img2W * ratio2);
            int draw2H = Math.round(img2H * ratio2);

            // Centrage dans le carré
            int offsetX2 = x + (TAILLE_ICONE - draw2W) / 2;
            int offsetY2 = y + (TAILLE_ICONE - draw2H) / 2;

            g2d.drawImage(img2, offsetX2 + TailleIconeArmure + 10, offsetY2, draw2W, draw2H, null);
        }



        return y+3*TAILLE_ICONE + 40;
    }

    private int dessinerItem(Graphics2D g2d, Joueur j, int x, int y) {
        g2d.setColor(Color.BLACK);

        g2d.drawString("Inventaire", x, y - 5);

        LinkedHashMap<Item, Integer> inventaire = j.getInventaireGroupé();
        int cpt = 0;

        // Première ligne de slots
        for (int i = 0; i < 4; i++) {
            g2d.setColor(new Color(0, 0, 0, 20));
            g2d.fillRect(x + i * (TAILLE_ICONE + 10), y, TAILLE_ICONE, TAILLE_ICONE);
        }

        // Parcours de l'inventaire groupé pour dessiner les items
        for (Map.Entry<Item, Integer> entry : inventaire.entrySet()) {
            Item item = entry.getKey(); // on parcourt les items groupés
            int count = entry.getValue(); // et on récupère leur quantité

            // Nouvelle ligne tous les 4 items
            if (cpt > 0 && cpt % 4 == 0) {
                y += TAILLE_ICONE + 10;
                for (int i = 0; i < 4; i++) {
                    g2d.setColor(new Color(0, 0, 0, 20));
                    g2d.fillRect(x + i * (TAILLE_ICONE + 10), y, TAILLE_ICONE, TAILLE_ICONE);
                }
            }

            int col = cpt % 4;
            int slotX = x + col * (TAILLE_ICONE + 10);

            // Dessin de l'image avec ratio
            Image img = item.getImage();
            float ratio = Math.min((float) TAILLE_ICONE / img.getWidth(null), (float) TAILLE_ICONE / img.getHeight(null));
            int drawW = Math.round(img.getWidth(null) * ratio);
            int drawH = Math.round(img.getHeight(null) * ratio);

            int offsetX = slotX + (TAILLE_ICONE - drawW) / 2;
            int offsetY = y + (TAILLE_ICONE - drawH) / 2;

            g2d.drawImage(img, offsetX, offsetY, drawW, drawH, null);

            // Badge de quantité
            if (count > 1) {
                g2d.setColor(Color.WHITE);
                g2d.fillOval(slotX + TAILLE_ICONE - 20, y + TAILLE_ICONE - 20, 20, 20);
                g2d.setColor(Color.BLACK);
                g2d.drawString(String.valueOf(count), slotX + TAILLE_ICONE - 15, y + TAILLE_ICONE - 3);
            }

            cpt++;
        }

        return y + TAILLE_ICONE + 10;
    }

}
