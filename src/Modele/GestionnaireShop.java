package Modele;

import Modele.Arme;
import Modele.Joueur;
import java.util.ArrayList;

public class GestionnaireShop {
    private Modele modele;

    public GestionnaireShop(Modele modele) {
        this.modele = modele;
    }

    // --- OBJETS DU SHOP ---

    public void acheterEpeeAcieree() {
        // Prix : 10 Fer, 5 Or
        if (tenterAchat(0, 0, 10, 5)) {
            // Améliore les dégâts du joueur
            Joueur j = modele.getJoueur();
            j.setAttack(j.getAttack() + 5);
        }
    }

    public void acheterArmure() {
        // Prix : 15 Pierre, 5 Fer
        if (tenterAchat(0, 15, 5, 0)) {
            // Améliore les points de vie max
            Joueur j = modele.getJoueur();
            j.setHpMax(j.getHpMax() + 20);
        }
    }

    public void acheterArmureLourde() {
        // Prix : 20 Pierre, 10 Fer
        if (tenterAchat(0, 20, 10, 0)) {
            Joueur j = modele.getJoueur();
            j.setHpMax(j.getHpMax() + 40);
        }
    }

    public void acheterEpee() {
        // Prix : 10 Bois, 5 Or
        if (tenterAchat(10, 0, 0, 5)) {
            Joueur j = modele.getJoueur();
            Arme a = j.getArmeEquipee();
            a.setNom("Épée améliorée");
            a.setPortee(a.getPortee() + 10);
            a.setDegats(a.getDegats()+10);
            j.setArmeEquipee(a);
        }
    }

    public void acheterPotionDeVie(){
        // Prix : 5 Bois, 5 Pierre
        if (tenterAchat(5, 5, 0, 0)) {
            Joueur j = modele.getJoueur();
            j.setHp(Math.min(j.getHp() + 30, j.getHpMax()));
        }
    }


    // --- LOGIQUE INTERNE ---

    private boolean tenterAchat(int b, int p, int f, int o) {
        ArrayList<Ressource> inv = modele.getJoueur().getInventaire();
        if (aAssez(inv, b, p, f, o)) {
            retirer(inv, b, p, f, o);
            return true;
        }
        return false;
    }

    private boolean aAssez(ArrayList<Ressource> inv, int b, int p, int f, int o) {
        int[] counts = new int[4];
        for (Ressource r : inv) counts[r.getType()]++;
        return counts[0] >= b && counts[1] >= p && counts[2] >= f && counts[3] >= o;
    }

    private void retirer(ArrayList<Ressource> inv, int b, int p, int f, int o) {
        int[] aRetirer = {b, p, f, o};
        for (int type = 0; type < 4; type++) {
            int compte = 0;
            for (int i = inv.size() - 1; i >= 0 && compte < aRetirer[type]; i--) {
                if (inv.get(i).getType() == type) {
                    inv.remove(i);
                    compte++;
                }
            }
        }
    }
}