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

public class VueHUDEquipement {

    // Identifiants des actions de switch — même principe que zonesCliquables dans VueHUDShop
    public static final String ACTION_SWITCH_ARME   = "switchArmes";
    public static final String ACTION_SWITCH_ARMURE = "switchArmure";

    // Zones cliquables : Rectangle → nom de l'action à déclencher
    private final Map<Rectangle, String> zonesCliquables = new HashMap<>();


    // -------------------------------------------------------------------------
    //  Point d'entrée principal
    // -------------------------------------------------------------------------

    public int dessiner(Graphics g, int y, Joueur j) {
        zonesCliquables.clear();                      // on refrais le mapping à chaque frame
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

    // -------------------------------------------------------------------------
    //  Résolution du clic — à appeler depuis le contrôleur / écouteur de souris
    // -------------------------------------------------------------------------

    /**
     * Retourne l'action associée aux coordonnées du clic, ou {@code null} si
     * aucun bouton n'est touché.
     * Valeurs possibles : {@link #ACTION_SWITCH_ARME}, {@link #ACTION_SWITCH_ARMURE}.
     */
    public String getActionAuClic(int x, int y) {
        for (Map.Entry<Rectangle, String> entry : zonesCliquables.entrySet()) {
            if (entry.getKey().contains(x, y)) {
                return entry.getValue();
            }
        }
        return null;
    }

    // -------------------------------------------------------------------------
    //  Dessin des armes
    // -------------------------------------------------------------------------

    private int dessinerArmes(Graphics2D g2d, Joueur j, int x, int y) {

        // Récupération des armes équipées et non équipées
        Arme armePrincipale  = j.getArmeEquipee();
        Arme armeSecondaire  = j.getArmePasEquipee();

        // Récupération des images (avec gestion du cas où l'arme secondaire est absente)
        Image img           = armePrincipale  != null ? armePrincipale.getImage()  : null;
        Image imgSecondaire = armeSecondaire  != null ? armeSecondaire.getImage()  : null;

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

        int degats  = armePrincipale.getDegats();
        int portee  = armePrincipale.getPortee();
        int cadence = armePrincipale.getCadence();
        // On convertit la cadence en secondes
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

        // --- Bouton SWITCH ARME (à droite du slot secondaire) ---
        // Affiché uniquement si une arme secondaire existe
        if (armeSecondaire != null) {
            int btnX = x + TailleIconePrinc + 10 + TAILLE_ICONE +3;
            int btnY = y + 5;   // centré verticalement sur le petit slot
            dessinerBoutonSwitch(g2d, btnX, btnY, ACTION_SWITCH_ARME);
        }

        return y + 3 * TAILLE_ICONE + 25;
    }

    // -------------------------------------------------------------------------
    //  Dessin de l'armure
    // -------------------------------------------------------------------------

    private int dessinerArmure(Graphics2D g2d, Joueur j, int x, int y) {

        int TailleIconeArmure = TAILLE_ICONE * 3;

        // Fond semi-transparent pour différencier les slots
        g2d.setColor(new Color(0, 0, 0, 20));
        g2d.fillRect(x, y, TailleIconeArmure, TailleIconeArmure);
        g2d.fillRect(x + TailleIconeArmure + 10, y, TAILLE_ICONE, TAILLE_ICONE);

        Armure armure          = j.getArmurePrincipale();
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

            // --- Bouton SWITCH ARMURE (à droite du slot secondaire) ---
            int btnX = x + TailleIconeArmure + 10 + TAILLE_ICONE + 8;
            int btnY = y + (TAILLE_ICONE - 25) / 2;   // centré verticalement sur le petit slot
            dessinerBoutonSwitch(g2d, btnX, btnY, ACTION_SWITCH_ARMURE);
        }

        return y + 3 * TAILLE_ICONE + 40;
    }

    // -------------------------------------------------------------------------
    //  Utilitaire : dessin d'un bouton SWITCH + enregistrement de la zone
    // -------------------------------------------------------------------------

    /**
     * Dessine un bouton "SWITCH" à la position (btnX, btnY) et enregistre sa zone
     * dans {@link #zonesCliquables} avec l'action {@code action}.
     * Style calqué sur le bouton ACHETER de VueHUDShop.
     */
    private void dessinerBoutonSwitch(Graphics2D g2d, int btnX, int btnY, String action) {
        final int btnLargeur = 45;
        final int btnHauteur = 40;

        // Fond gris
        g2d.setColor(Color.gray);
        g2d.fillRoundRect(btnX, btnY, btnLargeur, btnHauteur, 8, 8);

        // Libellé centré
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 11));
        FontMetrics fm = g2d.getFontMetrics();
        int tx = btnX + (btnLargeur - fm.stringWidth("SWITCH")+2) / 2;
        int ty = btnY + ((btnHauteur - fm.getHeight()) / 2) + fm.getAscent();
        g2d.drawString("SWITCH", tx, ty);

        // Enregistrement de la zone cliquable
        zonesCliquables.put(new Rectangle(btnX, btnY, btnLargeur, btnHauteur), action);
    }

    // -------------------------------------------------------------------------
    //  Dessin des items
    // -------------------------------------------------------------------------

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
            Item item  = entry.getKey();
            int  count = entry.getValue();

            // Nouvelle ligne tous les 4 items
            if (cpt > 0 && cpt % 4 == 0) {
                y += TAILLE_ICONE + 10;
                for (int i = 0; i < 4; i++) {
                    g2d.setColor(new Color(0, 0, 0, 20));
                    g2d.fillRect(x + i * (TAILLE_ICONE + 10), y, TAILLE_ICONE, TAILLE_ICONE);
                }
            }

            int col   = cpt % 4;
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