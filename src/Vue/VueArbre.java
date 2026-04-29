package Vue;

import java.awt.*;

import static Modele.Constantes.*;



public class VueArbre {

    /** ---------- [Méthodes Protégées - Rendu décoratif] ---------- **/

    /**
     * Dessine une ceinture d'arbres décoratifs autour de la carte.
     * Ces éléments n'ont pas de rôle gameplay : ils servent à fermer visuellement
     * l'espace jouable et à masquer les limites artificielles du terrain.
     *
     * @param g - Contexte graphique principal
     */
    protected void dessiner(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // On dessine des arbres sur les bordures de la carte pour renforcer l'immersion et masquer les limites
        // (Note : Ces arbres sont purement décoratifs et n'ont pas de collision, ils sont dessinés par-dessus la bordure rouge pour la masquer visuellement)
        int larg_deb = -1000; // Décalage pour commencer à dessiner les arbres avant le bord de la carte
        int larg_fin = LARGEUR_MAP + Math.abs(larg_deb); // Décalage pour continuer à dessiner les arbres après le bord de la carte
        int haut_deb = -600;
        int haut_fin = HAUTEUR_MAP + Math.abs(haut_deb);

        int min_haut = Math.min(ARBRE1.getHeight(), Math.min(ARBRE2.getHeight(), Math.min(ARBRE3.getHeight(), ARBRE4.getHeight())));
        int max_larg = Math.max(ARBRE1.getWidth(), Math.max(ARBRE2.getWidth(), Math.max(ARBRE3.getWidth(), ARBRE4.getWidth())));

        for (int h = haut_deb; h < haut_fin; h += min_haut){
            // Deuxième ligne d'arbres entre les arbres en hauteur
            int h2 = h + ARBRE1.getHeight() / 2;
            for (int l = larg_deb; l < larg_fin; l += max_larg) {
                int l2 = l + ARBRE1.getWidth() / 2; // Décalage pour coller les arbres sur la même ligne
                //  On dessine si on est en dehors des limites de la map
                // Si h < 0 (haut), h > HAUTEUR_MAP (bas), l < 0 (gauche) ou l > LARGEUR_MAP (droite)
                if (h2 < 0 || h > HAUTEUR_MAP - 100 || l2 < 0 || l > LARGEUR_MAP - 100) { // Décalage de 100 pour poser les arbres sur la map

                    // On dessine la première variante
                    g2d.drawImage(ARBRE1, l, h, ARBRE1.getWidth(), ARBRE1.getHeight(), null);
                    g2d.drawImage(ARBRE2, l2, h, ARBRE2.getWidth(), ARBRE2.getHeight(), null);

                    // On dessine la deuxième variante décalée (h2)
                    g2d.drawImage(ARBRE3, l, h2, ARBRE3.getWidth(), ARBRE3.getHeight(), null);
                    g2d.drawImage(ARBRE4, l2, h2, ARBRE4.getWidth(), ARBRE4.getHeight(), null);
                }
                if (l <= LARGEUR_MAP - 100 && l2 > LARGEUR_MAP - 100 ){
                    g2d.drawImage(ARBRE1, l + 50, h, ARBRE1.getWidth(), ARBRE1.getHeight(), null);
                    g2d.drawImage(ARBRE2, l2 + 50, h, ARBRE2.getWidth(), ARBRE2.getHeight(), null);
                    g2d.drawImage(ARBRE3, l + 50, h2, ARBRE3.getWidth(), ARBRE3.getHeight(), null);
                    g2d.drawImage(ARBRE4, l2 +  50, h2, ARBRE4.getWidth(), ARBRE4.getHeight(), null);
                }
            }
        }
    }
}