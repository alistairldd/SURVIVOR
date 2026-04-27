package Vue;

import Modele.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.TexturePaint;

import static Modele.Constantes.*;

/**
 * Responsable du rendu de la toile de fond (le sol du monde).
 * Dessine les limites de l'arène de jeu avec un motif répétitif proportionnel.
 */
public class VueCarte {
    private final Joueur joueur;

    public VueCarte(Modele modele) {
        this.joueur = modele.getJoueur();
    }

    /**
     * Dessine le sol du monde en utilisant un motif répétitif (TexturePaint)
     * configuré pour se répéter 8 fois en hauteur, avec une largeur proportionnelle.
     */
    protected void dessiner(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // --- 1. RENDU DU SOL (Texture répétitive proportionnelle) ---
        if (IMAGE_FOND_MAP != null) {
            // Nombre de répétitions souhaitées sur l'axe vertical
            double nbRepetitionsY = 8.0;

            // Calcul de la hauteur d'une "tuile"
            double hauteurTuile = (double) HAUTEUR_MAP / nbRepetitionsY;

            // Calcul du ratio de l'image originale pour ne pas la déformer
            double ratioImage = (double) IMAGE_FOND_MAP.getWidth() / IMAGE_FOND_MAP.getHeight();

            // Calcul de la largeur de la tuile pour conserver les proportions exactes
            double largeurTuile = hauteurTuile * ratioImage;

            // Création de l'ancre de texture avec les nouvelles dimensions proportionnelles
            Rectangle2D ancre = new Rectangle2D.Double(0, 0, largeurTuile, hauteurTuile);

            // Configuration du pinceau texturé
            TexturePaint texturePattern = new TexturePaint(IMAGE_FOND_MAP, ancre);
            g2d.setPaint(texturePattern);

            // Remplissage de la surface totale de la carte
            g2d.fillRect(0, 0, LARGEUR_MAP, HAUTEUR_MAP);
        } else {
            // Couleur de secours (Vert forêt)
            g2d.setColor(new Color(34, 139, 34));
            g2d.fillRect(0, 0, LARGEUR_MAP, HAUTEUR_MAP);
        }

        // --- 2. COUCHE D'ASSOMBRISSEMENT ---
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, LARGEUR_MAP, HAUTEUR_MAP);

        // (Optionnel) Ajout d'une bordure rouge pour bien voir les limites de la map
        // Aide le joueur à comprendre pourquoi il ne peut pas aller plus loin (collision dans Joueur.deplaceX/Y)
        g2d.setColor(Color.RED);
        g2d.drawRect(0, 0, LARGEUR_MAP, HAUTEUR_MAP);
        // On dessiner des arbres sur les bordures de la carte pour renforcer l'immersion et masquer les limites
        // (Note : Ces arbres sont purement décoratifs et n'ont pas de collision, ils sont dessinés par-dessus la bordure rouge pour la masquer visuellement)
        int deb = -900; // Décalage pour commencer à dessiner les arbres avant le bord de la carte
        int fin = LARGEUR_MAP + 900; // Décalage pour continuer à dessiner les arbres après le bord de la carte
        int haut = -600;
        int bas = HAUTEUR_MAP - ( ARBRE1.getHeight() / 2 ) ;
            // Tire un nombre aléatoire entre 1 et 4 pour choisir aléatoirement entre 4 types d'arbres différents (ARBRE1, ARBRE2, ARBRE3, ARBRE4)


            for (int h = haut; h < 0; h += ARBRE1.getHeight()) {
                for (int i = deb; i < fin; i += ARBRE1.getWidth()) {
                    int i2 = i + ARBRE1.getWidth() / 2; // Décalage pour centrer les arbres sur les bords
                    g2d.drawImage(ARBRE1, i, h, ARBRE1.getWidth(), ARBRE1.getHeight(), null); // Arbres en haut
                    g2d.drawImage(ARBRE2, i2, h, ARBRE2.getWidth(), ARBRE2.getHeight(), null); // Arbres en haut

                    g2d.drawImage(ARBRE1, i, bas, ARBRE1.getWidth(), ARBRE1.getHeight(), null); // Arbres en bas
                    g2d.drawImage(ARBRE2, i2, bas, ARBRE2.getWidth(), ARBRE2.getHeight(), null); // Arbres en bas
                }
            }

        for (int i = 0; i < HAUTEUR_MAP; i += 100) {
            g2d.drawImage(ARBRE1, 10 - 50, 10 + i, 50, 50, null); // Arbres à gauche
            g2d.drawImage(ARBRE1, 10 + LARGEUR_MAP, 10 + i, 50, 50, null); // Arbres à droite
        }

    }

}