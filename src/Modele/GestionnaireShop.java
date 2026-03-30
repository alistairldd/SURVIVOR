package Modele;
import Modele.Arme;

import java.util.ArrayList;


public class GestionnaireShop {
    private Modele modele;
    private ArrayList <Arme> armes;
    private ArrayList<Armure> armures;
    private ArrayList <Objets> objets;

    public GestionnaireShop(Modele modele) {
        this.modele = modele;
    }

    // ACHAT D'ARME (Classe Arme)
    public void acheterArme(Arme nouvelleArme, int prixPieces) {
        Joueur j = modele.getJoueur();
        if (j.getPieces() >= prixPieces) {
            j.retirerPieces(prixPieces);
            j.setArmeEquipee(nouvelleArme); // Met à jour l'arme et ses stats
        }else{
            System.out.println("Pas assez de pièces pour acheter cette arme !");
        }
    }

    // ACHAT D'ARMURE (Classe Armure)
    public void acheterArmure(Armure nouvelleArmure, int prixPieces) {
        Joueur j = modele.getJoueur();
        if (j.getPieces() >= prixPieces) {
            j.retirerPieces(prixPieces);
            j.equiperArmure(nouvelleArmure); // Augmente PV Max ou Défense
        }else{
            System.out.println("Pas assez de pièces pour acheter cette armure !");
        }
    }

    // ACHAT D'OBJETS (Potions, Outils)
    public void acheterItem(String nom, int prixPieces) {
        Joueur j = modele.getJoueur();
        if (j.getPieces() >= prixPieces) {
            j.retirerPieces(prixPieces);

        }else{
            System.out.println("Pas assez de pièces pour acheter cette objet !");
        }
    }
}