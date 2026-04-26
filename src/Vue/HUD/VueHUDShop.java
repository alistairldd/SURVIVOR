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
 * Moteur de rendu de la Boutique.
 * Traduit les flux de données (Map) en informations visuelles pour l'investisseur (le joueur).
 */
public class VueHUDShop {

    private final int HAUTEUR_ITEM = 100;
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
        yCourant = dessinerCategorie(g2d, yCourant, "ARMES", shop.getArmesDansShop());

        // --- SECTION 2 : ARMURES ---
        yCourant = dessinerCategorie(g2d, yCourant, "ARMURES", shop.getArmuresDansShop());

        // --- SECTION 3 : OBJETS ---
        yCourant = dessinerCategorie(g2d, yCourant, "UTILITAIRES", shop.getObjets());

        // Instructions de bas de page
        yCourant += 20;
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.ITALIC, 12));
        g2d.drawString("Cliquez sur un actif pour l'acquérir", xOffset, yCourant);

        return yCourant;
    }

    /**
     * Traduit le dictionnaire de ressources en une chaîne de caractères lisible.
     */
    private String formaterPrix(Map<Integer, Integer> prix) {
        if (prix == null || prix.isEmpty()) return "Coût : Gratuit";

        List<String> labels = new ArrayList<>();
        // Mapping des IDs défini dans tes Constantes (0:Bois, 1:Pierre, 2:Fer, 3:Or)
        String[] nomsRessources = {"Bois", "Pierre", "Fer", "Or"};

        for (Map.Entry<Integer, Integer> entry : prix.entrySet()) {
            int id = entry.getKey();
            int quantite = entry.getValue();
            if (id >= 0 && id < nomsRessources.length) {
                labels.add(quantite + " " + nomsRessources[id]);
            }
        }
        return "Coût : " + String.join(", ", labels);
    }

    private int dessinerCategorie(Graphics2D g2d, int y, String titre, ArrayList<?> liste) {
        if (liste == null || liste.isEmpty()) return y;

        g2d.setColor(new Color(40, 40, 40));
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.drawString(titre, xOffset, y);
        y += 25;

        for (int i = 0; i < liste.size(); i++) {
            Object obj = liste.get(i);
            String nom = "";
            String stats = "";
            String ressourcesStr = "";
            Image img = null;

            if (obj instanceof Arme) {
                Arme a = (Arme) obj;
                nom = a.getNom();
                stats = "Atk: " + a.getDegats() + " | Portée: " + a.getPortee();
                ressourcesStr = formaterPrix(a.getRessourcesNecessaires());
                img = a.getImage();
            } else if (obj instanceof Armure) {
                Armure arm = (Armure) obj;
                nom = arm.getNom();
                stats = "Reduction dgts: +" + arm.getReduction();
                ressourcesStr = formaterPrix(arm.getRessourcesNecessaires());
                img = arm.getImage();
            } else if (obj instanceof Item) {
                Item it = (Item) obj;
                nom = it.getNom();
                stats = "Consommable";
                ressourcesStr = "Prix: " + it.getPrix() + " Or";
                img = it.getImage();
            }

            Rectangle rectItem = new Rectangle(xOffset, y, 260, HAUTEUR_ITEM);
            zonesCliquables.put(rectItem, obj);

            // Dessin du bouton ACHETER
            int btnLargeur = 75;
            int btnHauteur = 25;
            int btnX = xOffset + 180;
            int btnY = y + 35;

            g2d.setColor(new Color(34, 139, 34)); // Vert Bullish
            g2d.fillRoundRect(btnX, btnY, btnLargeur, btnHauteur, 8, 8);

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 11));
            FontMetrics fm = g2d.getFontMetrics();
            int tx = btnX + (btnLargeur - fm.stringWidth("ACHETER")) / 2;
            int ty = btnY + ((btnHauteur - fm.getHeight()) / 2) + fm.getAscent();
            g2d.drawString("ACHETER", tx, ty);

            // Sprite et Textes
            if (img != null) {
                int imgW = img.getWidth(null);
                int imgH = img.getHeight(null);
                float ratio = Math.min((float) TAILLE_IMG / imgW, (float) TAILLE_IMG / imgH);
                int drawW = Math.round(imgW * ratio);
                int drawH = Math.round(imgH * ratio);
                g2d.drawImage(img, xOffset + (TAILLE_IMG - drawW) / 2, y + (TAILLE_IMG - drawH) / 2, drawW, drawH, null);
            }

            int xTexte = xOffset + TAILLE_IMG + 12;
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            g2d.drawString(nom, xTexte, y + 20);

            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            g2d.drawString(stats, xTexte, y + 40);

            g2d.setColor(new Color(139, 69, 19)); // Marron Liquidation
            g2d.setFont(new Font("Arial", Font.BOLD, 11));
            g2d.drawString(ressourcesStr, xTexte, y + 65);

            g2d.setColor(new Color(0, 0, 0, 20));
            g2d.drawLine(xOffset, y + HAUTEUR_ITEM - 5, xOffset + 255, y + HAUTEUR_ITEM - 5);

            y += HAUTEUR_ITEM;
        }
        return y + 20;
    }

    /**
     * Retourne l'action associée aux coordonnées du clic, ou {@code null} si
     * aucun bouton n'est touché.
     * Valeurs possibles : Arme, Armure, Item
     **/
    public Object getObjetAuClic(int x, int y) {
        for (Rectangle r : zonesCliquables.keySet()) {
            if (r.contains(x, y)) return zonesCliquables.get(r);
        }
        return null;
    }
}