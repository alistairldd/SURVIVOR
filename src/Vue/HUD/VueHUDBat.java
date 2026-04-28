package Vue.HUD;

import Modele.Joueur;
import Modele.Modele;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

import static Modele.Constantes.*;

/**
 * Gestionnaire du rendu des bâtiments dans le HUD.
 * Affiche dynamiquement les options de construction, leurs coûts et leurs icônes,
 * tout en mémorisant leurs coordonnées d'affichage pour la détection de clics (Boutons Swing).
 */
public class VueHUDBat {

    /** ---------- [Propriétés - Mémorisation Spatiale] ---------- **/

    private int yTour;
    private int yTente;
    private int yAbatis;
    private int yMortier;

    /** ---------- [Accesseurs - Getters] ---------- **/

    public int getYTour(){return yTour;}
    public int getYTente(){return yTente;}
    public int getyAbatis(){return yAbatis;}
    public int getyMortier(){return yMortier;}

    /** ---------- [Accesseurs - Setters] ---------- **/

    public void setyTour(int y){yTour = y;}
    public void setyTente(int y){yTente = y;}
    public void setyAbatis(int y){yAbatis = y;}
    public void setyMortier(int y){yMortier = y;}

    /** ---------- [Méthodes Publiques - Moteur de Rendu] ---------- **/

    /**
     * Construit la section complète des bâtiments constructibles.
     * Aligne les éléments textuels et graphiques et met à jour les coordonnées Y de référence.
     *
     * @param g - Contexte graphique 2D
     * @param yDebut - Coordonnée Y initiale du bloc
     * @param modele - Le modèle central pour l'état du cycle temporel
     * @param joueur - Le joueur courant (utilisé pour les vérifications de ressources internes)
     * @return La coordonnée Y finale après le rendu du bloc complet
     */
    public int dessiner(Graphics g, int yDebut, Modele modele, Joueur joueur) {
        int yCourant = yDebut;
        Graphics2D g2d = (Graphics2D) g;

        boolean isDay = modele.getLeCycleJourNuit().isDay();
        Color couleurTexte = isDay ? Color.BLACK : Color.WHITE;

        g2d.setColor(couleurTexte);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));

        g2d.drawString("CONSTRUCTIONS", xOffset, yCourant);

        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        if (isDay) {
            g2d.setColor(new Color(0, 150, 0));
            g2d.drawString("- PRÊT", xOffset + 150, yCourant);
        } else {
            g2d.setColor(Color.RED);
            g2d.drawString("- NUIT (OFF)", xOffset + 150, yCourant);
        }

        yCourant += 30;

        ArrayList<String> iterBat = new ArrayList<String>();
        iterBat.add("• Tour de défense");
        iterBat.add("• Tente de soin");
        iterBat.add("• Abatis");
        iterBat.add("• Mortier");

        for (String bat : iterBat) {
            yCourant = dessinerBatimentSelectionne(g2d, yCourant, bat, couleurTexte, modele);

            if (Objects.equals(bat, "• Tour de défense")) {
                setyTour(yCourant);
            }
            else if (Objects.equals(bat, "• Tente de soin")) {
                setyTente(yCourant);
            }
            else if (Objects.equals(bat, "• Abatis")) {
                setyAbatis(yCourant);
            }
            else if (Objects.equals(bat, "• Mortier")) {
                setyMortier(yCourant);
            }
        }

        return yCourant + 30;
    }

    /**
     * Dessine la carte individuelle d'un bâtiment (Nom, Coût détaillé, Icône de présentation).
     *
     * @param g2d - Contexte graphique 2D
     * @param yCourant - Coordonnée Y de départ pour ce bâtiment
     * @param bat - Identifiant textuel du bâtiment
     * @param couleurTexte - Couleur adaptative selon le cycle jour/nuit
     * @param modele - Le modèle central pour vérifier les contraintes d'unicité (ex: Tente)
     * @return La nouvelle coordonnée Y après avoir dessiné cet élément
     */
    public int dessinerBatimentSelectionne(Graphics2D g2d, int yCourant, String bat, Color couleurTexte, Modele modele){
        int TAILLE_ICONE_BAT = TAILLE_ICONE * 3;
        String cout;
        Image img;

        if (Objects.equals(bat, "• Tour de défense")){
            cout = "(Cout: 4 Bois, 4 Pierre, 2 Fer, 1 Or)";
            img = IMAGE_TOUR;
        }
        else if (Objects.equals(bat, "• Tente de soin")){
            boolean tenteExiste = modele.getGestionnaireBatiments().aDejaUneTente();
            if (tenteExiste) {
                cout = "Déjà construite sur le terrain";
                couleurTexte = Color.GRAY;
            } else {
                cout = "(Cout: 7 Bois, 2 Pierre, 4 Fer, 5 Or)";
            }
            img = IMAGE_TENTE;
        }
        else if (Objects.equals(bat, "• Abatis")) {
            cout = "(Cout: 20 Bois)";
            img = IMAGE_ABATIS_1;
        }
        else if (Objects.equals(bat, "• Mortier")) {
            cout = "(Cout: 5 Bois, 4 Pierre, 10 Fer, 3 Or)";
            img = IMAGE_MORTIER;
        }
        else {
            cout = "(Cout inconnu)";
            img = null;
        }

        yCourant += 30;
        g2d.setFont(new Font("Arial", Font.BOLD, 13));
        g2d.setColor(couleurTexte);
        g2d.drawString(bat, xOffset + 5, yCourant);

        yCourant += 15;
        g2d.setFont(new Font("Arial", Font.ITALIC, 11));
        g2d.drawString(cout, xOffset + 15, yCourant);

        if (img != null) {
            dessinerImage(g2d, xOffset + 15, yCourant + 10, img, TAILLE_ICONE_BAT);
        }
        yCourant += TAILLE_ICONE_BAT + 20;

        return yCourant;
    }

    /**
     * Utilitaire de rendu calculant le ratio de mise à l'échelle pour inscrire
     * une image au centre d'un cadre défini, sans déformation.
     *
     * @param g2d - Contexte graphique 2D
     * @param x - Position X du cadre
     * @param y - Position Y du cadre
     * @param imgBat - Image source à dessiner
     * @param taille - Dimension du cadre conteneur
     */
    public void dessinerImage(Graphics2D g2d, int x, int y, Image imgBat, int taille) {
        int imgW = imgBat.getWidth(null);
        int imgH = imgBat.getHeight(null);

        float ratio = Math.min((float) taille / imgW, (float) taille / imgH);
        int drawW = Math.round(imgW * ratio);
        int drawH = Math.round(imgH * ratio);

        int offsetX = x + (taille - drawW) / 2;
        int offsetY = y + (taille - drawH) / 2;

        g2d.setColor(new Color(0, 0, 0, 20));
        g2d.fillRect(x, y, taille, taille);
        g2d.drawImage(imgBat, offsetX, offsetY, drawW, drawH, null);
    }
}