package Modele;
import Modele.Armes.*;
import Modele.Armure.Armure;
import Modele.Armure.ArmureLegere;
import Modele.Armure.ArmureLourde;
import Modele.Items.Item;
import Modele.Items.PotionDegats;
import Modele.Items.PotionVie;
import Modele.Items.PotionVitesse;

import java.util.ArrayList;
import java.util.Map; // Ajout de l'import pour le dictionnaire


public class GestionnaireShop {
    private Modele modele;

    // Listes des équipements disponibles à l'achat dans le shop
    private ArrayList <Arme> armesDansShop;
    private ArrayList<Armure> armuresDansShop;

    // Liste des objets (potions, outils) disponibles à l'achat tout le temps
    private ArrayList<Item> objets;

    // Liste des armes dans l'ordre de disponibilité d'achat;
    private ArrayList<Arme> armes;
    // Liste des armures dans l'ordre de disponibilité d'achat;
    private ArrayList<Armure> armures;

    public GestionnaireShop(Modele modele) {
        this.modele = modele;
        this.armesDansShop = new ArrayList<>();
        this.armuresDansShop = new ArrayList<>();
        this.objets = new ArrayList<Item>();

        this.armes = new ArrayList<Arme>();
        this.armures = new ArrayList<Armure>();

        // --- Initialisation des armes disponibles ---
        armes.add(new EpeeBois());
        armes.add(new Epee());
        armes.add(new Hache());
        armes.add(new Lance());
        armes.add(new EpeeLourde());

        // --- Initialisation des armures disponibles ---
        armures.add(new ArmureLegere());
        armures.add(new ArmureLourde());
        // faut en mettre d'autres

        // --- Initialisation des armesDansShop disponibles ---

        armesDansShop.add(armes.get(0));
        armesDansShop.add(armes.get(1));

        // --- Initialisation des armures disponibles ---

        armuresDansShop.add(armures.get(0));
        armuresDansShop.add(armures.get(1));

        // --- Initialisation des objets disponibles ---
        PotionVie pv = new PotionVie();
        PotionDegats pd = new PotionDegats();
        PotionVitesse pvi = new PotionVitesse();
        objets.add(pv);
        objets.add(pd);
        objets.add(pvi);
    }

    public ArrayList<Arme> getArmesDansShop() {
        return armesDansShop;
    }

    public ArrayList<Armure> getArmuresDansShop() {
        return armuresDansShop;
    }

    public ArrayList<Item> getObjets() {
        return objets;
    }

    public void enleverArmeDuShop(Arme a) {
        armesDansShop.remove(a);
        armes.remove(a);
    }

    public void enleverArmureDuShop(Armure a) {
        armuresDansShop.remove(a);
        armures.remove(a);
    }

    public void updateArmesDansShop() {
        if (!armesDansShop.isEmpty()){
            armesDansShop.remove(0);
        }
        if (!armes.isEmpty()){
            armesDansShop.add(armes.get(0));
        } if (armes.size()>1){
            armesDansShop.add(armes.get(1));
        }
    }

    public void updateArmuresDansShop() {
        if (!armuresDansShop.isEmpty()){
            armuresDansShop.remove(0);
        }
        if (!armures.isEmpty()){
            armuresDansShop.add(armures.get(0));
        } if (armures.size()>1){
            armuresDansShop.add(armures.get(1));
        }
    }

    public void setObjets(ArrayList<Item> objets) {
        this.objets = objets;
    }

    // ACHAT D'ARME (Classe Arme)

    public void acheterArme(Arme a) {
        Joueur j = modele.getJoueur();
        // NOUVEAU : On récupère le dictionnaire des ressources de l'arme (ex: {2=10} pour 10 Fer)
        Map<Integer, Integer> besoins = a.getRessourcesNecessaires();

        if (j.aAssezDeRessources(besoins)) {
            j.consommerListeRessources(besoins);

            if (j.getArmePasEquipee() == null){
                j.switchArmes();
                j.setArmeEquipee(a);
            }
            else {
                j.setArmeEquipee(a);
            }
            enleverArmeDuShop(a);
            updateArmesDansShop();

        } else {
            System.out.println("Ressources insuffisantes pour fabriquer " + a.getNom());
        }
    }

    // ACHAT D'ARMURE (Classe Armure)
    public void acheterArmure(Armure a) {
        Joueur j = modele.getJoueur();
        // NOUVEAU : On récupère le dictionnaire des ressources de l'armure
        Map<Integer, Integer> besoins = a.getRessourcesNecessaires();

        if (j.aAssezDeRessources(besoins)) {
            j.consommerListeRessources(besoins);

            if (j.getArmureSecondaire() == null){
                j.switchArmures();
                j.equiperArmure(a);
            }
            else {
                j.setArmureEquipee(a);
            }
            enleverArmureDuShop(a);
            updateArmuresDansShop();
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
            if (i instanceof Item) {
                j.addToInventaire(i);
            }
        } else {
            System.out.println("Pas assez d'or pour cet objet !");
        }


    }
}