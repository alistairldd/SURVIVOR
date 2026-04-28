package Vue.HUD;

import Modele.Armure.Armure;
import Modele.Joueur;
import Modele.Armes.Arme;
import Modele.Items.Item;
import java.awt.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static Modele.Constantes.xOffset;
import static Modele.Constantes.TAILLE_ICONE;

/**
 * Responsable de l'affichage de l'état de l'équipement du joueur (Armes, Armure, Consommables).
 * Construit dynamiquement des zones cliquables (Rectangles) associées à des actions contextuelles.
 */
public class VueHUDEquipement {

    /** ---------- [Constantes - Actions d'UI] ---------- **/

    public static final String ACTION_SWITCH_ARME   = "switchArmes";
    public static final String ACTION_SWITCH_ARMURE = "switchArmure";
    public static final String ACTION_UTILISER_CONSOMMABLE = "utiliserConsommable";

    /** ---------- [Propriétés - Mappage Spatial] ---------- **/

    private final Map<Rectangle, String> zonesCliquables = new HashMap<>();
    private final Map<Rectangle, Item> zonesItemsCliquables = new HashMap<>();

    /** ---------- [Méthodes Publiques - Moteur de Rendu] ---------- **/

    /**
     * Point d'entrée principal. Dessine les trois sous-sections (Armes, Armure, Inventaire)
     * en empilant les Y, et regénère les zones interactives.
     *
     * @param g - Contexte graphique 2D
     * @param y - Coordonnée Y de départ
     * @param j - Instance du joueur contenant les données de l'équipement
     * @return L'encombrement final calculé (Coordonnée Y résultante)
     */
    public int dessiner(Graphics g, int y, Joueur j) {
        zonesCliquables.clear();
        zonesItemsCliquables.clear();

        Graphics2D g2d = (Graphics2D) g;
        int xEquip = xOffset;
        int yEquip = y;

        yEquip = dessinerArmes(g2d, j, xEquip, yEquip);
        yEquip = dessinerArmure(g2d, j, xEquip, yEquip);
        yEquip = dessinerItem(g2d, j, xEquip, yEquip);

        return y + (yEquip - y) + TAILLE_ICONE;
    }

    /** ---------- [Méthodes Publiques - Événements Souris] ---------- **/

    /**
     * Identifie si les coordonnées fournies interceptent l'une des zones d'action système.
     *
     * @param x - Position X du clic souris
     * @param y - Position Y du clic souris
     * @return Le nom de l'action ciblée, ou null si hors zone
     */
    public String getActionAuClic(int x, int y) {
        for (Map.Entry<Rectangle, String> entry : zonesCliquables.entrySet()) {
            if (entry.getKey().contains(x, y)) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * Identifie si les coordonnées fournies interceptent un slot contenant un Item.
     *
     * @param x - Position X du clic souris
     * @param y - Position Y du clic souris
     * @return L'instance de l'Item cliqué, ou null si hors zone
     */
    public Item getItemAuClic(int x, int y) {
        for (Map.Entry<Rectangle, Item> entry : zonesItemsCliquables.entrySet()) {
            if (entry.getKey().contains(x, y)) {
                return entry.getValue();
            }
        }
        return null;
    }

    /** ---------- [Méthodes Privées - Sous-Rendus] ---------- **/

    /**
     * Dessine la section des armes équipées (Principale + Secondaire + Statistiques).
     */
    private int dessinerArmes(Graphics2D g2d, Joueur j, int x, int y) {
        Arme armePrincipale  = j.getArmeEquipee();
        Arme armeSecondaire  = j.getArmePasEquipee();

        Image img           = armePrincipale  != null ? armePrincipale.getImage()  : null;
        Image imgSecondaire = armeSecondaire  != null ? armeSecondaire.getImage()  : null;

        assert armePrincipale != null;
        String nomArmePrincipale = armePrincipale.getNom() != null ? armePrincipale.getNom() : "Aucune arme équipée";

        int TailleIconePrinc = TAILLE_ICONE * 3;

        g2d.setColor(new Color(0, 0, 0, 20));
        g2d.fillRect(x, y, TailleIconePrinc, TailleIconePrinc);
        g2d.fillRect(x + TailleIconePrinc + 10, y, TAILLE_ICONE, TAILLE_ICONE);

        int imgW = img.getWidth(null);
        int imgH = img.getHeight(null);

        float ratio = Math.min((float) TailleIconePrinc / imgW, (float) TailleIconePrinc / imgH);
        int drawW = Math.round(imgW * ratio);
        int drawH = Math.round(imgH * ratio);

        int offsetX = x + (TailleIconePrinc - drawW) / 2;
        int offsetY = y + (TailleIconePrinc - drawH) / 2;

        g2d.setColor(Color.BLACK);
        g2d.drawString(nomArmePrincipale, x, y - 5);

        int degats  = armePrincipale.getDegats();
        int portee  = armePrincipale.getPortee();
        int cadence = armePrincipale.getCadence();
        String cad = String.format("%.2f", cadence / 600.0);

        Font font = g2d.getFont();
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        if (j.getAttack() > 0) {
            g2d.drawString("Degats  : " + degats + " + " + j.getAttack(), x + TailleIconePrinc + 5, y + TAILLE_ICONE + 30);
        } else {
            g2d.drawString("Degats  : " + degats,                          x + TailleIconePrinc + 5, y + TAILLE_ICONE + 30);
        }
        g2d.drawString("Portée  : " + portee,    x + TailleIconePrinc + 5, y + TAILLE_ICONE + 50);
        g2d.drawString("Vit. Atq : " + cad + "s", x + TailleIconePrinc + 5, y + TAILLE_ICONE + 70);
        g2d.setFont(font);

        g2d.drawImage(img, offsetX, offsetY, drawW, drawH, null);

        if (imgSecondaire != null) {
            int img2W = img.getWidth(null);
            int img2H = img.getHeight(null);

            float ratio2 = Math.min((float) TAILLE_ICONE / img2W, (float) TAILLE_ICONE / img2H);
            int draw2W = Math.round(img2W * ratio2);
            int draw2H = Math.round(img2H * ratio2);

            int offsetX2 = x + (TAILLE_ICONE - draw2W) / 2;
            int offsetY2 = y + (TAILLE_ICONE - draw2H) / 2;

            g2d.drawImage(imgSecondaire, offsetX2 + TailleIconePrinc + 10, offsetY2, draw2W, draw2H, null);
        }

        if (armeSecondaire != null) {
            int btnX = x + TailleIconePrinc + 10 + TAILLE_ICONE +3;
            int btnY = y + 5;
            dessinerBoutonSwitch(g2d, btnX, btnY, ACTION_SWITCH_ARME);
        }

        return y + 3 * TAILLE_ICONE + 25;
    }

    /**
     * Dessine la section d'armures (Principale + Secondaire + Statistiques modifiées).
     */
    private int dessinerArmure(Graphics2D g2d, Joueur j, int x, int y) {
        int TailleIconeArmure = TAILLE_ICONE * 3;

        g2d.setColor(new Color(0, 0, 0, 20));
        g2d.fillRect(x, y, TailleIconeArmure, TailleIconeArmure);
        g2d.fillRect(x + TailleIconeArmure + 10, y, TAILLE_ICONE, TAILLE_ICONE);

        Armure armure           = j.getArmurePrincipale();
        Armure armureSecondaire = j.getArmureSecondaire();

        if (armure != null) {
            Image img = armure.getImage();
            int imgW = img.getWidth(null);
            int imgH = img.getHeight(null);

            float ratio = Math.min((float) TailleIconeArmure / imgW, (float) TailleIconeArmure / imgH);
            int drawW = Math.round(imgW * ratio);
            int drawH = Math.round(imgH * ratio);

            int offsetX = x + (TailleIconeArmure - drawW) / 2;
            int offsetY = y + (TailleIconeArmure - drawH) / 2;

            g2d.setColor(Color.BLACK);
            g2d.drawString(armure.getNom(), x, y - 5);
            g2d.drawImage(img, offsetX, offsetY, drawW, drawH, null);

            int reduction   = armure.getReduction();
            int bonusVitesse = armure.getVitesse();
            Font font = g2d.getFont();
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            g2d.drawString("Reduction : +" + reduction, x + TailleIconeArmure + 5, y + TAILLE_ICONE + 30);
            if (bonusVitesse > 0) {
                g2d.drawString("Vit : +" + bonusVitesse, x + TailleIconeArmure + 5, y + TAILLE_ICONE + 50);
            } else {
                g2d.drawString("Vit : "  + bonusVitesse, x + TailleIconeArmure + 5, y + TAILLE_ICONE + 50);
            }
            g2d.setFont(font);
        }

        if (armureSecondaire != null) {
            Image img2 = armureSecondaire.getImage();
            int img2W = img2.getWidth(null);
            int img2H = img2.getHeight(null);

            float ratio2 = Math.min((float) TAILLE_ICONE / img2W, (float) TAILLE_ICONE / img2H);
            int draw2W = Math.round(img2W * ratio2);
            int draw2H = Math.round(img2H * ratio2);

            int offsetX2 = x + (TAILLE_ICONE - draw2W) / 2;
            int offsetY2 = y + (TAILLE_ICONE - draw2H) / 2;

            g2d.drawImage(img2, offsetX2 + TailleIconeArmure + 10, offsetY2, draw2W, draw2H, null);

            int btnX = x + TailleIconeArmure + 10 + TAILLE_ICONE + 8;
            int btnY = y + (TAILLE_ICONE - 25) / 2;
            dessinerBoutonSwitch(g2d, btnX, btnY, ACTION_SWITCH_ARMURE);
        }

        return y + 3 * TAILLE_ICONE + 40;
    }

    /**
     * Dessine la grille de l'inventaire des consommables et items utilitaires.
     */
    private int dessinerItem(Graphics2D g2d, Joueur j, int x, int y) {
        g2d.setColor(Color.BLACK);
        g2d.drawString("Inventaire", x, y - 5);

        LinkedHashMap<Item, Integer> inventaire = j.getInventaireGroupe();
        int cpt = 0;

        for (int i = 0; i < 4; i++) {
            g2d.setColor(new Color(0, 0, 0, 20));
            g2d.fillRect(x + i * (TAILLE_ICONE + 10), y, TAILLE_ICONE, TAILLE_ICONE);
        }

        for (Map.Entry<Item, Integer> entry : inventaire.entrySet()) {
            Item item  = entry.getKey();
            int  count = entry.getValue();

            if (cpt > 0 && cpt % 4 == 0) {
                y += TAILLE_ICONE + 10;
                for (int i = 0; i < 4; i++) {
                    g2d.setColor(new Color(0, 0, 0, 20));
                    g2d.fillRect(x + i * (TAILLE_ICONE + 10), y, TAILLE_ICONE, TAILLE_ICONE);
                }
            }

            int col   = cpt % 4;
            int slotX = x + col * (TAILLE_ICONE + 10);

            Image img = item.getImage();
            float ratio = Math.min((float) TAILLE_ICONE / img.getWidth(null), (float) TAILLE_ICONE / img.getHeight(null));
            int drawW = Math.round(img.getWidth(null) * ratio);
            int drawH = Math.round(img.getHeight(null) * ratio);

            int offsetX = slotX + (TAILLE_ICONE - drawW) / 2;
            int offsetY = y + (TAILLE_ICONE - drawH) / 2;

            g2d.drawImage(img, offsetX, offsetY, drawW, drawH, null);

            if (count > 1) {
                g2d.setColor(Color.WHITE);
                g2d.fillOval(slotX + TAILLE_ICONE - 20, y + TAILLE_ICONE - 20, 20, 20);
                g2d.setColor(Color.BLACK);
                g2d.drawString(String.valueOf(count), slotX + TAILLE_ICONE - 15, y + TAILLE_ICONE - 3);
            }

            Rectangle zoneItem = new Rectangle(slotX, y, TAILLE_ICONE, TAILLE_ICONE);
            zonesCliquables.put(zoneItem, ACTION_UTILISER_CONSOMMABLE);
            zonesItemsCliquables.put(zoneItem, item);
            cpt++;
        }

        return y + TAILLE_ICONE + 10;
    }

    /** ---------- [Méthodes Privées - Utilitaires UI] ---------- **/

    /**
     * Génère le rendu visuel d'un bouton d'action et lie sa géométrie à une clé comportementale.
     *
     * @param g2d - Contexte graphique 2D
     * @param btnX - Coordonnée X de dessin
     * @param btnY - Coordonnée Y de dessin
     * @param action - Chaîne d'identification de l'action déclenchée au clic
     */
    private void dessinerBoutonSwitch(Graphics2D g2d, int btnX, int btnY, String action) {
        final int btnLargeur = 45;
        final int btnHauteur = 40;

        g2d.setColor(Color.gray);
        g2d.fillRoundRect(btnX, btnY, btnLargeur, btnHauteur, 8, 8);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 11));
        FontMetrics fm = g2d.getFontMetrics();
        int tx = btnX + (btnLargeur - fm.stringWidth("SWITCH")+2) / 2;
        int ty = btnY + ((btnHauteur - fm.getHeight()) / 2) + fm.getAscent();
        g2d.drawString("SWITCH", tx, ty);

        zonesCliquables.put(new Rectangle(btnX, btnY, btnLargeur, btnHauteur), action);
    }
}