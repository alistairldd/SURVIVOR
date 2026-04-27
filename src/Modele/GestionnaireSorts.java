package Modele;

import Modele.Items.Item;
import Modele.Items.Sort;
import Modele.Items.SortFeu;
import Modele.Monstres.Monstre;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GestionnaireSorts {
    private List<Sort> sortsActifs;
    private Modele modele;

    public GestionnaireSorts(Modele modele) {
        this.sortsActifs = new CopyOnWriteArrayList<>();
        this.modele = modele;
    }

    public void ajouterSort(SortFeu sort) {
            sortsActifs.add(sort);
    }

    /**
     * Vérifie les collisions entre les sorts et les monstres.
     * Détruit le sort et endommage le monstre en cas de collision.
     */
    public void verifierCollisions(List<Monstre> monstres) {
        // 1. Créer une copie pour éviter les modifications concurrentes
        List<Sort> sortsCopie = new ArrayList<>(sortsActifs);

        for (Sort sort : sortsCopie) {
            if (!sort.isActif()) continue;

            // 2. Vérifier chaque monstre
            for (Monstre monstre : new ArrayList<>(monstres)) {
                if (monstre.getHp() <= 0) continue;

                // 3. Calculer la distance
                double dX = sort.getX() - monstre.getX();
                double dY = sort.getY() - monstre.getY();
                double distance = Math.hypot(dX, dY);

                // 4. Collision détectée
                if (distance < 30) { // 30 = rayon du sort + rayon du monstre
                    monstre.prendreDegats(sort.getEffet());
                    sort.desactiver(); // Désactiver le sort
                    sortsActifs.remove(sort);
                    break; // Passer au sort suivant
                }
            }
        }
    }


    public List<Sort> getSortsActifs() {
        return new ArrayList<>(sortsActifs);
    }

    public void clear() {
        for (Sort sort : sortsActifs) {
            sort.arreterSort();
        }
        sortsActifs.clear();
    }

    public List<Sort> getSorts() {
        return sortsActifs;
    }
}
