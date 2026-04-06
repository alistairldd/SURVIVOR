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
        batiments.add(new Tower(HAUTEUR_MAP/2, LARGEUR_MAP/3, this));
        batiments.get(batiments.size() - 1).setHp(0);
        batiments.add(new TenteDeSoin(HAUTEUR_MAP/3, LARGEUR_MAP/2,this));
    }

    public ArrayList<Batiment> getBatiments() {
        return batiments;
    }

    public Monstre trouverCible(Tower t) {
        return m.batTrouverMonstre(t);
    }

    public Joueur trouverJoueur(TenteDeSoin tente) { return m.batTrouverJoueur(tente); }

    public void clearBatiments() {
        // Vide l'ArrayList
        batiments.clear();
    }

    public HQ getHQ() {
        return hq;
    }

    public boolean getPartieTerminee() { return m.getPartieTerminee();}
}