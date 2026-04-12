package Modele;
import Modele.Armes.Arme;
import Modele.Armes.Epee;
import Modele.Armes.EpeeBois;
import Modele.Items.Armure;
import Modele.Items.ArmureLegere;
import Modele.Items.Item;
import Modele.Items.PotionVie;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class GestionnaireShop {
    private Modele modele;
    private ArrayList <Arme> armes;
    private ArrayList<Armure> armures;
    private ArrayList<Item> objets;

    public GestionnaireShop(Modele modele) {
        this.modele = modele;
        this.armes = new ArrayList<>();
        this.armures = new ArrayList<>();
        this.objets = new ArrayList<Item>();

        // --- Initialisation des armes disponibles ---
        Epee ep = new Epee();
        armes.add(ep);

        EpeeBois epBois = new EpeeBois();
        armes.add(epBois);

        // --- Initialisation des armures disponibles ---

        ArmureLegere al = new ArmureLegere();
        armures.add(al);

        // --- Initialisation des objets disponibles ---
        PotionVie pv = new PotionVie();
        objets.add(pv);
    }

    public ArrayList<Arme> getArmes() {
        return armes;
    }

    public ArrayList<Armure> getArmures() {
        return armures;
    }

    public ArrayList<Item> getObjets() {
        return objets;
    }

    public void setArmes(ArrayList<Arme> armes) {
        this.armes = armes;
    }

    public void setArmures(ArrayList<Armure> armures) {
        this.armures = armures;
    }

    public void setObjets(ArrayList<Item> objets) {
        this.objets = objets;
    }

    // ACHAT D'ARME (Classe Arme)

    public void acheterArme(Arme a) {
        Joueur j = modele.getJoueur();
        // On récupère directement la liste des ressources de l'arme (ex: ["Bois:10"])
        List<String> besoins = a.getRessourcesNecessaires();

        if (j.aAssezDeRessources(besoins)) {
            j.consommerListeRessources(besoins);
            j.setArmeEquipee(a);
            System.out.println("Succès : " + a.getNom() + " fabriquée et équipée !");
        } else {
            System.out.println("Ressources insuffisantes pour fabriquer " + a.getNom());
        }
    }

    // ACHAT D'ARMURE (Classe Armure)
    public void acheterArmure(Armure nouvelleArmure) {
        Joueur j = modele.getJoueur();
        List<String> besoins = nouvelleArmure.getRessourcesNecessaires();

        if (j.aAssezDeRessources(besoins)) {
            j.consommerListeRessources(besoins);
            j.equiperArmure(nouvelleArmure);
            System.out.println("Succès : " + nouvelleArmure.getNom() + " équipée !");
        } else {
            System.out.println("Ressources insuffisantes pour l'armure !");
        }
    }

    // ACHAT D'OBJETS (Potions, Outils)
    public void acheterItem(Item i) {
        Joueur j = modele.getJoueur();
        int besoins = i.getPrix();
        // Pour les potions, on utilise soit des ressources, soit le prix en Or
        // Si votre classe Objets utilise getPrix(), on peut simuler une liste "Or:X"
        // Ou mieux : ajouter getRessourcesNecessaires() à la classe parente Objets.

        if (j.getPieces()>= besoins) {
            j.setPieces(j.getPieces()-besoins);
            if (i instanceof PotionVie) {
                j.soigner(((PotionVie) i).getSoin());
                System.out.println("Potion de vie utilisée !");
            }
        } else {
            System.out.println("Pas assez d'or pour cet objet !");
        }
    }
}