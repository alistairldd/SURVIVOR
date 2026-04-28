package Modele;

import Modele.Items.Item;
import Modele.Items.Sort;
import Modele.Items.SortFeu;
import Modele.Items.SortTempete;
import Modele.Monstres.Monstre;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Système de suivi et de résolution du moteur de magie.
 * Orchestre les projectiles générés et résout la physique de collision
 * asynchrone entre ces sorts et les entités ennemies.
 */
public class GestionnaireSorts {

    /** ---------- [Propriétés] ---------- **/

    private List<Sort> sortsActifs;
    private Modele modele;

    /** ---------- [Constructeurs] ---------- **/

    public GestionnaireSorts(Modele modele) {
        this.sortsActifs = new CopyOnWriteArrayList<>();
        this.modele = modele;
    }

    /** ---------- [Accesseurs / Getters] ---------- **/

    public List<Sort> getSortsActifs() {
        return new ArrayList<>(sortsActifs);
    }

    public List<Sort> getSorts() {
        return sortsActifs;
    }

    /** ---------- [Méthodes Publiques - Métier] ---------- **/

    public void ajouterSort(Sort sort) {
        sortsActifs.add(sort);
    }

    /**
     * Vérifie les collisions entre les sorts et les monstres.
     * Détruit le sort et endommage le monstre en cas de collision.
     */
    public void verifierCollisions(List<Monstre> monstres) {
        List<Sort> sortsCopie = new ArrayList<>(sortsActifs);

        for (Sort sort : sortsCopie) {
            if (!sort.isActif()) continue;

            for (Monstre monstre : monstres) {
                if (monstre.getHp() <= 0) continue;

                double dX = sort.getX() - monstre.getX();
                double dY = sort.getY() - monstre.getY();
                double distance = Math.hypot(dX, dY);

                // On applique l'effet (dégâts ou recul)
                if (sort instanceof SortTempete) {
                    if (distance < Constantes.TEMPETE_RANGE){
                        double force =200;
                        double pushX = (monstre.getX() - sort.getX()) / distance;
                        double pushY = (monstre.getY() - sort.getY()) / distance;
                        monstre.setPositionX(monstre.getX() + pushX * force);
                        monstre.setPositionY(monstre.getY() + pushY * force);
                    }}

                if (sort instanceof SortFeu){
                    if (distance < Constantes.FEU_RANGE) {
                        monstre.prendreDegats(sort.getEffet());

                    }
                }
            }
        }
    }

    public void clear() {
        for (Sort sort : sortsActifs) {
            sort.arreterSort();
        }
        sortsActifs.clear();
    }
}