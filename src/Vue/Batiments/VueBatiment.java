package Vue.Batiments;

import java.awt.*;

import Modele.Batiments.Batiment;
import Modele.Batiments.HQ;
import Modele.Batiments.Mine;
import Modele.Batiments.Tower;
import Modele.Monstres.Monstre;

import static Modele.Constantes.*;

/**
 * Gère le rendu visuel de toutes les structures fixes (Bâtiments).
 * Dessine leur état de santé (changement de couleur), leur zone d'effet (cercle de portée)
 * et les effets visuels de leurs attaques (laser/projectile calculé géométriquement).
 */
public class VueBatiment {

    public VueBatiment() {
    }

    /**
     * Dessine un bâtiment à l'écran, avec adaptation automatique si on dessine sur la minimap.
     * @param g Le contexte graphique principal.
     * @param b L'instance du bâtiment (HQ, Tower...) contenant les données.
     * @param x Coordonnée absolue X de placement.
     * @param y Coordonnée absolue Y de placement.
     * @param minimap Booléen (true = réduit pour le radar, false = taille réelle sur la carte).
     */
    public static void dessinerBatiment(Graphics g, Batiment b, int x, int y, boolean minimap) {
        // Crée une copie du pinceau pour gérer les transparences et épaisseurs sans impacter le reste
        Graphics2D g2d = (Graphics2D) g.create();
        // Lissage des formes géométriques
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Taille par défaut d'un bâtiment sur la carte principale
        int taille = 30;

        // Réduction drastique de la taille si on dessine sur le petit radar en haut à droite
        if (minimap) {
            taille = 6;
        }

        // On calcule la moitié de la taille pour pouvoir centrer le dessin (le point x,y sera le centre exact)
        int demiTaille = taille / 2;

        // Dessin spécifique pour le Quartier Général
        if (b instanceof HQ) {
            g2d.setColor(Color.WHITE);

            int tailleHQ = minimap ? taille : 45; // Le HQ est plus grand que les autres bâtiments (45px)
            int demiTailleHQ = tailleHQ / 2;

            // On décale de la moitié de la taille pour que (x, y) soit le centre exact du carré
            g2d.fillRect(x - demiTailleHQ, y - demiTailleHQ, tailleHQ, tailleHQ);

            // Dessin spécifique pour les Tours Défensives
        } else if (b instanceof Tower) {
            Tower t = (Tower) b;

            // --- DESSIN DU CERCLE DE PORTÉE ---
            // On ne dessine pas les zones de portée sur le petit radar pour ne pas le surcharger
            if (!minimap) {
                int portee = t.getRange();

                // Active la transparence (Alpha) à 20% d'opacité (0.2f) pour le fond du cercle
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));

                // Alerte visuelle : Si la tour a moins de 10% de ses PV max, son aura devient rouge
                if (t.getHp() <= 0.1 * HP_TOWER) {
                    g2d.setColor(Color.RED);
                } else {
                    g2d.setColor(Color.CYAN); // Sinon, aura classique cyan
                }

                // Dessine le fond transparent du cercle (rayonné depuis le centre x,y)
                g2d.fillOval(x - portee, y - portee, portee * 2, portee * 2);

                // Remonte l'opacité à 60% pour dessiner la bordure du cercle
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
                // Épaissit le trait à 2 pixels
                g2d.setStroke(new BasicStroke(2));
                // Dessine le contour extérieur
                g2d.drawOval(x - portee, y - portee, portee * 2, portee * 2);

                // Remet l'opacité à 100% pour la suite des dessins (bâtiment dur)
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            }

            // --- DESSIN DE LA TOUR ---
            // Même logique d'alerte visuelle pour la structure elle-même (Rouge = Critique)
            if (t.getHp() <= 0.1 * HP_TOWER) {
                g2d.setColor(Color.RED);
            } else {
                g2d.setColor(Color.CYAN);
            }
            g2d.fillRect(x - demiTaille, y - demiTaille, taille, taille);


            // =====================================================================
            // --- AJOUT : EFFET D'ATTAQUE (Traînée et Boule touchant le bord) ---
            // =====================================================================
            if (!minimap) {
                // Récupère l'ennemi pris pour cible par cette tour spécifique
                Monstre cible = t.getMonstreCible();

                // Vérifie s'il y a eu un tir très récent.
                // L'effet laser/boule ne s'affiche que pendant 150ms pour donner un effet de "flash" ou de coup rapide
                if (cible != null && (System.currentTimeMillis() - t.getDernierTempsAttaque() < 150)) {

                    int cibleX = (int) cible.getX(); // Centre X du monstre
                    int cibleY = (int) cible.getY(); // Centre Y du monstre
                    int tailleProjectile = 8;

                    // --- AJOUT : Taille du monstre pour l'intersection ---
                    // Supposons que ton monstre est un carré de 24 pixels.
                    // Remplace par la vraie valeur si disponible (ex: cible.getTaille())
                    int tailleMonstre = 24;
                    int demiTailleMonstre = tailleMonstre / 2;

                    // -- CALCUL DES COORDONNÉES VECTORIELLES --
                    // Calcul des composantes X et Y du vecteur allant de la tour au centre du monstre
                    double dx = cibleX - x;
                    double dy = cibleY - y;
                    // Calcule la longueur totale de ce vecteur
                    double distance = Math.hypot(dx, dy);

                    if (distance > 1) { // Sécurité pour éviter les divisions par zéro si les entités se superposent

                        // --- AJOUT : Calcul du point d'impact précis sur le bord de la hitbox du monstre ---
                        // On cherche le facteur 't' d'intersection avec les bords (mathématiques de Raycasting basique)
                        // Combien de fois on peut diviser le demi-monstre par le vecteur avant de sortir du cadre ?
                        double tx = (dx == 0) ? Double.MAX_VALUE : Math.abs(demiTailleMonstre / dx);
                        double ty = (dy == 0) ? Double.MAX_VALUE : Math.abs(demiTailleMonstre / dy);
                        // On garde la plus petite valeur : c'est le premier bord (X ou Y) que le projectile rencontre
                        double tIntersection = Math.min(tx, ty); // On prend la première intersection trouvée

                        // Coordonnées du centre de la boule (exactement sur le bord extérieur du monstre)
                        // Note : '1 - tIntersection' car le vecteur dx part de la tour. On cible le centre (100%), puis on recule de tIntersection vers la tour.
                        int posBouleX = (int) (x + dx * (1 - tIntersection));
                        int posBouleY = (int) (y + dy * (1 - tIntersection));

                        // 2. Dessiner la trajectoire/traînée (JAUNE, translucent)
                        // Trait semi-transparent (50%) pour faire un effet de laser ou traînée
                        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                        g2d.setColor(Color.YELLOW);
                        g2d.setStroke(new BasicStroke(3)); // Trait épais de 3px
                        // On trace la ligne du centre de la tour (x,y) exactement jusqu'au point d'impact calculé (bord du monstre)
                        g2d.drawLine(x, y, posBouleX, posBouleY);

                        // 3. Dessiner la "boule" (le projectile jaune, opaque)
                        // On remet l'opacité à 100% pour que la boule d'énergie soit très visible
                        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                        // On dessine le point d'impact en le centrant sur posBouleX/Y
                        g2d.fillOval(posBouleX - (tailleProjectile/2), posBouleY - (tailleProjectile/2), tailleProjectile, tailleProjectile);
                    }

                    // Reset de l'épaisseur du trait par défaut pour ne pas impacter les dessins suivants
                    g2d.setStroke(new BasicStroke(1));
                }
            }
            // =====================================================================


        } else if (b instanceof Mine) {
            Mine m = (Mine) b;

            // Couleur spécifique pour la mine (un orange foncé/marron)
            g2d.setColor(new Color(150, 75, 0));
            g2d.fillRect(x - demiTaille, y - demiTaille, taille, taille);

            // Affichage du nombre de minerais stockés (uniquement sur la carte, pas sur le radar)
            if (!minimap) {
                int stock = m.getRessources().size();
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 12));
                // Dessine le texte centré au-dessus du bâtiment
                g2d.drawString(stock + " Minerais", x - 30, y - demiTaille - 8);
            }


        } else if (b instanceof Modele.Batiments.TenteDeSoin) {
            Modele.Batiments.TenteDeSoin tente = (Modele.Batiments.TenteDeSoin) b;

            // --- 1. DESSIN DE L'AURA ROUGE (Zone d'effet) ---
            if (!minimap) {
                int portee = tente.getRange();

                // Si la tente est en train de soigner (moins de 600ms depuis le dernier soin)
                if (System.currentTimeMillis() - tente.getDernierTempsSoin() < 600) {
                    // Fond rouge très transparent (15%)
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.15f));
                    g2d.setColor(Color.RED);
                    g2d.fillOval(x - portee, y - portee, portee * 2, portee * 2);

                    // Bordure rouge plus marquée (40%)
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawOval(x - portee, y - portee, portee * 2, portee * 2);

                    // Reset de l'épaisseur du trait
                    g2d.setStroke(new BasicStroke(1));
                }
            }

            // --- 2. DESSIN DE LA TENTE ---
            // On remet l'opacité à 100% pour le corps du bâtiment
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

            // Couleur Khaki / Jaune foncé (RGB: 200, 200, 0)
            g2d.setColor(new Color(200, 200, 0));
            g2d.fillRect(x - demiTaille, y - demiTaille, taille, taille);


        } else {
            // Sécurité : Dessin par défaut si on ajoute de nouveaux Bâtiments non reconnus plus tard
            g2d.setColor(Color.GRAY);
            g2d.fillRect(x - demiTaille, y - demiTaille, taille, taille);
        }

        // Libère la mémoire du contexte graphique cloné
        g2d.dispose();
    }
}