package Modele;

import java.util.ArrayList;
import java.util.List;

// Classe pour gérer les monstres dans le jeu
public class GestionnaireMonstres {

    // Liste des monstres présents sur la carte
    private ArrayList<Monstre> monstres = new ArrayList<>();


    // Méthode pour générer un monstre aléatoire
    public ArrayList<Monstre> genererMonstre(int nombre) {
        for (int i = 0; i < nombre; i++) {
            int type = (int) (Math.random() * 3); // Génère un nombre entre 0 et 2
            switch (type) {
                case 0:
                    monstres.add(new Slime());
                default:
                    monstres.add(new Slime()); // Par défaut, retourne un Slime
            }
        }
        return monstres;
    }

}
