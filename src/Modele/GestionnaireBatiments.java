package Modele;

import java.util.ArrayList;
import static Modele.Constantes.*;

public class GestionnaireBatiments {
    ArrayList<Batiment> batiments;
    Modele m;
    HQ hq = new HQ(this);

    public GestionnaireBatiments(Modele m) {
        this.m = m;
        //Liste des batiments présents sur la carte
        batiments = new ArrayList<Batiment>();
        batiments.add(hq); //Par défaut on ne peut construire que le HQ, les autres bâtiments sont construits par le joueur
        batiments.add(new Mine(this));

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

    public HQ getHQ() {
        return hq;
    }
}