package Modele;

import java.util.ArrayList;
import static Modele.Constantes.*;

public class GestionnaireBatiments {
    ArrayList<Batiment> batiments;
    Modele m;

    public GestionnaireBatiments(Modele m) {
        this.m = m;
        //Liste des batiments présents sur la carte
        batiments = new ArrayList<Batiment>();
        batiments.add(new HQ(this)); //Par défaut on ne peut construire que le HQ, les autres bâtiments sont construits par le joueur

    }

    public ArrayList<Batiment> getBatiments() {
        return batiments;
    }

    public Monstre trouverCible(Tower t) {
        return m.batTrouverMonstre(t);
    }

    public void clearBatiments() {
        // Vide l'ArrayList
        batiments.clear();
    }
}