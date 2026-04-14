package Vue.HUD;

import Modele.Items.Armure;
import Modele.Joueur;
import Modele.Armes.Arme;
import Modele.Items.Item;
import java.awt.*;
import java.util.ArrayList;

import static Modele.Constantes.xOffset;
import static Modele.Constantes.TAILLE_ICONE;

import static Modele.Constantes.xOffset;
import static Modele.Constantes.TAILLE_ICONE;

public class VueHUDEquipement {


    public int dessiner(Graphics g, int y, Joueur j) {
        Graphics2D g2d = (Graphics2D) g;

        int xEquip = xOffset ;
        int yEquip = y;

        // --- SLOT ARMES (Classe Arme) ---

        dessinerArmes(g2d, j, xEquip, yEquip);

        yEquip += TAILLE_ICONE*3 + 10;

        // --- SLOT ARMURE (Classe Item) ---
        Item armure = j.getArmureEquipee(); // Ici on traite l'armure comme un Item
        if (armure != null) {
            //dessinerLigne(g2d, armure, j.getInventaire(), xEquip, yEquip);
            //dessinerLigne(g2d, "Armure", armure.getNom(), armure.getImage(), xEquip, yEquip);
        } else {
            dessinerLigneVide(g2d, "Armure : Aucune", xEquip, yEquip);
        }

        return y +  (yEquip - y) + TAILLE_ICONE;
    }

    private void dessinerArmes(Graphics2D g2d, Joueur j, int x, int y){

        // Récupération des armes équipées et non équipées

        Arme armePrincipale = j.getArmeEquipee();
        Arme armeSecondaire = j.getArmePasEquipee();

        // Récupération des images (avec gestion du cas où l'arme secondaire est absente)

        Image img = armePrincipale != null ? armePrincipale.getImage() : null;
        Image imgSecondaire = armeSecondaire != null ? armeSecondaire.getImage() : null;

        String nomArmePrincipale = armePrincipale.getNom();
        String nomArmeSecondaire = armeSecondaire != null ? armeSecondaire.getNom() : "Aucune";

        int TailleIconePrinc = TAILLE_ICONE * 3;

        // Fond semi-transparent pour différencier les slots

    private void dessinerArmes(Graphics2D g2d, Joueur j, int x, int y){

        // Récupération des armes équipées et non équipées

        Arme armePrincipale = j.getArmeEquipee();
        Arme armeSecondaire = j.getArmePasEquipee();

        // Récupération des images (avec gestion du cas où l'arme secondaire est absente)

        Image img = armePrincipale != null ? armePrincipale.getImage() : null;
        Image imgSecondaire = armeSecondaire != null ? armeSecondaire.getImage() : null;

        String nomArmePrincipale = armePrincipale.getNom();
        String nomArmeSecondaire = armeSecondaire != null ? armeSecondaire.getNom() : "Aucune";

        int TailleIconePrinc = TAILLE_ICONE * 3;

        // Fond semi-transparent pour différencier les slots

        g2d.setColor(new Color(0, 0, 0, 20));
        g2d.fillRect(x, y, TailleIconePrinc, TailleIconePrinc);
        g2d.fillRect(x + TailleIconePrinc + 10, y, TAILLE_ICONE, TAILLE_ICONE);

        // Dimensions originales de l'image
        int imgW = imgSecondaire.getWidth(null);
        int imgH = imgSecondaire.getHeight(null);

        // Calcul du ratio pour tenir dans TAILLE_IMG x TAILLE_IMG
        float ratio = Math.min((float) TailleIconePrinc / imgW, (float) TailleIconePrinc / imgH);

        int drawW = Math.round(imgW * ratio);
        int drawH = Math.round(imgH * ratio);

        // Centrage dans le carré
        int offsetX = x + (TailleIconePrinc - drawW) / 2;
        int offsetY = y + (TailleIconePrinc - drawH) / 2;

        g2d.setColor(Color.BLACK);
        g2d.drawString(nomArmePrincipale, x, y - 5);

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
    }

    private void dessinerLigne(Graphics2D g2d, String type, String nom, Image img, int x, int y) {
        g2d.setColor(new Color(0, 0, 0, 20));
        g2d.fillRect(x, y, TailleIconePrinc, TailleIconePrinc);
        g2d.fillRect(x + TailleIconePrinc + 10, y, TAILLE_ICONE, TAILLE_ICONE);

        // Dimensions originales de l'image
        int imgW = imgSecondaire.getWidth(null);
        int imgH = imgSecondaire.getHeight(null);

        // Dimensions originales de l'image
        int imgW = img.getWidth(null);
        int imgH = img.getHeight(null);

        // Calcul du ratio pour tenir dans TAILLE_IMG x TAILLE_IMG
        float ratio = Math.min((float) TAILLE_ICONE / imgW, (float) TAILLE_ICONE / imgH);

        int drawW = Math.round(imgW * ratio);
        int drawH = Math.round(imgH * ratio);

        // Centrage dans le carré
        int offsetX = x + (TAILLE_ICONE - drawW) / 2;
        int offsetY = y + (TAILLE_ICONE - drawH) / 2;

        if (img != null) {
            g2d.drawImage(img, offsetX, offsetY, drawW, drawH, null);
        }
        // Calcul du ratio pour tenir dans TAILLE_IMG x TAILLE_IMG
        float ratio = Math.min((float) TailleIconePrinc / imgW, (float) TailleIconePrinc / imgH);

        int drawW = Math.round(imgW * ratio);
        int drawH = Math.round(imgH * ratio);

        // Centrage dans le carré
        int offsetX = x + (TailleIconePrinc - drawW) / 2;
        int offsetY = y + (TailleIconePrinc - drawH) / 2;

        g2d.setColor(Color.BLACK);
        g2d.drawString(nomArmePrincipale, x, y - 5);

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
    }

    private void dessinerLigne(Graphics2D g2d, Armure armure, ArrayList<Item> items, int x, int y) {
        g2d.setColor(new Color(0, 0, 0, 20));
        g2d.fillRect(x, y, TAILLE_ICONE, TAILLE_ICONE);
        // On affiche une ligne avec : en premier l'armure du joueur, puis ensuite les autres items qu'il a


    }

    private void dessinerLigneVide(Graphics2D g2d, String texte, int x, int y) {
        g2d.setColor(new Color(0, 0, 0, 80));
        g2d.setFont(new Font("Arial", Font.ITALIC, 11));
        g2d.drawString(texte, x + 5, y + 25);
    }


}