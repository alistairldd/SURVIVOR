package Modele;

import java.util.ArrayList;
import java.util.List;

// Classe pour gérer les monstres dans le jeu
public class GestionnaireMonstres {

    // Liste des monstres présents sur la carte
    private ArrayList<Monstre> monstres;

    public GestionnaireMonstres() {
        this.monstres = new ArrayList<>();
    }

    // Méthode pour générer un monstre aléatoire
    public void genererMonstre(int nombre) {

        for (int i = 0; i < nombre; i++) {
            // Positionnement aléatoire du monstre sur le bord de la carte
            int x, y;
            // Génère un nombre entre 0 et 4 pour déterminer sur quel bord faire apparaître le monstre
            int edge = (int) (Math.random() * 4); // 0: haut, 1: droite, 2: bas, 3: gauche
            switch (edge) {
                case 0: // Haut
                    x = (int) (Math.random() * Map.LARGEUR_MAP);
                    y = 0;
                    break;
                case 1: // Droite
                    x = Map.LARGEUR_MAP;
                    y = (int) (Math.random() * Map.HAUTEUR_MAP);
                    break;
                case 2: // Bas
                    x = (int) (Math.random() * Map.LARGEUR_MAP);
                    y = Map.HAUTEUR_MAP;
                    break;
                case 3: // Gauche
                    x = 0;
                    y = (int) (Math.random() * Map.HAUTEUR_MAP);
                    break;
                default: // Gauche par défaut mais n'arrivera jamais normalement
                    x = 0;
                    y = (int) (Math.random() * Map.HAUTEUR_MAP);
            }
            int type = (int) (Math.random() * 3); // Génère un nombre entre 0 et 2 pour savoir quel monstre faire apparaître
            switch (type) {
                case 0:
                    monstres.add(new Slime(x,y));
                    break;
                default:
                    monstres.add(new Slime(x,y)); // Par défaut, retourne un Slime
            }
        }
    }

    public ArrayList<Monstre> getMonstres() {
        return monstres;
    }

    // Méthode pour supprimer tous les monstres de la liste, utilisée pour faire disparaître les monstres à la fin de la nuit
    public void clearMonstres() {
        monstres.clear();
    }

    public void supprimerMonstresMorts (){
        List<Monstre> monstresMorts = new ArrayList<>();
        for (Monstre m : monstres) {
            if (m.getHp() <= 0) {
                monstresMorts.add(m);
            }
        }
        for (Monstre m : monstresMorts) {
            monstres.remove(m);
        }
    }

}

