package Vue.HUD;

import Modele.Joueur;
import Modele.Modele;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

import static Modele.Constantes.*;

public class VueHUDBat {

    // Coordonnées Y de chaque bâtiment pour le placement des boutons
    private int yTour;
    private int yTente;
    private int yAbatis;

    // Getters pour les coordonnées Y (utiles pour positionner les boutons de construction)
    public int getYTour(){return yTour;}
    public int getYTente(){return yTente;}
    public int getyAbatis(){return yAbatis;}

    // Setters pour les coordonnées Y (appelés après le dessin pour mémoriser les positions)
    public void setyTour(int y){yTour = y;}
    public void setyTente(int y){yTente = y;}
    public void setyAbatis(int y){yAbatis = y;}

    /**
     * Dessine les informations de construction.
     * @return La coordonnée Y finale après dessin.
     */
    public int dessiner(Graphics g, int yDebut, Modele modele, Joueur joueur) {
        int yCourant = yDebut;
        Graphics2D g2d = (Graphics2D) g;

        boolean isDay = modele.getLeCycleJourNuit().isDay();
        Color couleurTexte = isDay ? Color.BLACK : Color.WHITE;

        g2d.setColor(couleurTexte);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));

        // --- TITRE ---
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

        for (String bat : iterBat) {
            yCourant = dessinerBatimentSelectionne(g2d, yCourant, bat, couleurTexte, modele);
            if (Objects.equals(bat, "• Tour de défense")){
                setyTour(yCourant);
            }
            else if (Objects.equals(bat, "• Tente de soin")){
                setyTente(yCourant);
            }
            else {
                setyAbatis(yCourant);
            }
        }

        return yCourant+30;
    }

    /**
     * Dessine les détails d'un bâtiment sélectionné, y compris son nom, son coût et son icône.
     * @param g2d
     * @param yCourant
     * @param bat
     * @param couleurTexte
     * @param modele
     * @return La coordonnée Y après avoir dessiné ce bâtiment, pour continuer à empiler les suivants.
     */
    public int dessinerBatimentSelectionne(Graphics2D g2d, int yCourant, String bat, Color couleurTexte, Modele modele){

        int TAILLE_ICONE_BAT = TAILLE_ICONE * 3; // Taille plus grande pour les bâtiments
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
                couleurTexte = Color.GRAY; // Grisé pour indiquer l'indisponibilité

            }else {
                cout = "(Cout: 7 Bois, 2 Pierre, 4 Fer, 5 Or)";
            }
            img = IMAGE_TENTE;
        }
        else {
            cout = "(Cout: 20 Bois)";
            img = IMAGE_ABATIS_1;
        }

        // on met le nom du batiment
        yCourant +=30;
        g2d.setFont(new Font("Arial", Font.BOLD,13));
        g2d.setColor(couleurTexte);
        g2d.drawString(bat, xOffset +5, yCourant);

        // on met le cout du batiment
        yCourant += 15;
        g2d.setFont(new Font("Arial", Font.ITALIC, 11));
        g2d.drawString(cout, xOffset + 15, yCourant);

        // on dessine l'image du batiment
        dessinerImage(g2d,xOffset+15, yCourant+10, img, TAILLE_ICONE_BAT);
        yCourant += TAILLE_ICONE_BAT + 20;

        return yCourant;

    }


    /**
        * Dessine une image centrée dans un carré de taille donnée, avec un fond semi-transparent.
        * @param g2d Contexte graphique.
        * @param x Coordonnée X du coin supérieur gauche de la zone de dessin.
        * @param y Coordonnée Y du coin supérieur gauche de la zone de dessin.
        * @param imgBat L'image du bâtiment à dessiner.
    **/
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