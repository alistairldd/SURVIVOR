package Modele;
import Modele.Armes.Arme;
import Modele.Armes.Epee;
import Modele.Armes.EpeeBois;
import Modele.Items.Armure;
import Modele.Items.ArmureLegere;

import java.util.ArrayList;
import java.util.List;


public class GestionnaireShop {
    private Modele modele;
    private ArrayList <Arme> armes;
    private ArrayList<Armure> armures;
    private ArrayList <Objets> objets;

    public GestionnaireShop(Modele modele) {
        this.modele = modele;
        this.armes = new ArrayList<>();
        this.armures = new ArrayList<>();
        this.objets = new ArrayList<>();

        // --- Initialisation des armes disponibles ---
        Epee ep = new Epee();
        armes.add(ep);

        EpeeBois epBois = new EpeeBois();
        armes.add(epBois);

        // --- Initialisation des armures disponibles ---

        ArmureLegere al = new ArmureLegere();
        armures.add(al);

        // --- Initialisation des objets disponibles ---

    }

    public ArrayList<Arme> getArmes() {
        return armes;
    }

    public ArrayList<Armure> getArmures() {
        return armures;
    }

    public ArrayList<Objets> getObjets() {
        return objets;
    }

    public void setArmes(ArrayList<Arme> armes) {
        this.armes = armes;
    }

    public void setArmures(ArrayList<Armure> armures) {
        this.armures = armures;
    }

    public void setObjets(ArrayList<Objets> objets) {
        this.objets = objets;
    }

    // ACHAT D'ARME (Classe Arme)
    public void acheterArme(Arme nouvelleArme, int prixPieces) {
        Joueur j = modele.getJoueur();

        if (j.getPieces() >= prixPieces) {
            j.acheter(prixPieces);
            j.setArmeEquipee(nouvelleArme); // Met à jour l'arme et ses stats
        }else{
            System.out.println("Pas assez de pièces pour acheter cette arme !");
        }
    }

    public void fabriquerArme(Arme a) {
        Joueur j = modele.getJoueur();
        if (j.aAssezDeRessources(a.getRessourcesNecessaires())) {
            j.consommerListeRessources(a.getRessourcesNecessaires());
            j.setArmeEquipee(a);
            System.out.println("Succès : Vous avez fabriqué " + a.getNom());
        } else {
            System.out.println("Échec : Ressources insuffisantes pour " + a.getNom());
        }
    }

    // ACHAT D'ARMURE (Classe Armure)
    public void acheterArmure(Armure nouvelleArmure, int prixPieces) {
        Joueur j = modele.getJoueur();
        if (j.getPieces() >= prixPieces) {
            j.acheter(prixPieces);
            j.equiperArmure(nouvelleArmure); // Augmente PV Max ou Défense
        }else{
            System.out.println("Pas assez de pièces pour acheter cette armure !");
        }
    }

    // ACHAT D'OBJETS (Potions, Outils)
    public void acheterItem(String nom, int prixPieces) {
        Joueur j = modele.getJoueur();
        if (j.getPieces() >= prixPieces) {
            j.acheter(prixPieces);

        }else{
            System.out.println("Pas assez de pièces pour acheter cette objet !");
        }
    }
}