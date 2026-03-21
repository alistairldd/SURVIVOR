package Test;

import Modele.Modele;
import Modele.Joueur;
import Modele.Map;

import java.io.IOException;

public class testDeplacementJoueur {

    public static void main(String[] args) {
        System.out.println("Lancement du test de déplacement du joueur...");

        // Initialisation du modèle et récupération du joueur
        Modele modele = new Modele();
        Joueur joueur = modele.getJoueur();

        int taille = Map.LARGEUR_MAP; // La carte est carrée, donc largeur = hauteur
        System.out.println("Taille de la carte: " + taille);

        // Réinitialiser la position du joueur
        joueur.setPositionX(0);
        joueur.setPositionY(0);
        assertEquals(0, (int) joueur.getPositionX(), "Position X initiale");
        assertEquals(0, (int) joueur.getPositionY(), "Position Y initiale");

        // Simuler un clic dans la carte
        System.out.println("Test: déplacement à (100,150)");
        joueur.deplaceX(100);
        joueur.deplaceY(150);
        assertEquals(100, (int) joueur.getPositionX(), "Position X après déplacement");
        assertEquals(150, (int) joueur.getPositionY(), "Position Y après déplacement");

        // Simuler un clic hors limites pour vérifier la saturation
        System.out.println("Test: déplacement hors limites à (9999,-9999)");
        joueur.deplaceX(9999);
        joueur.deplaceY(-9999);
        assertEquals(taille, (int) joueur.getPositionX(), "Position X après déplacement hors limites (max)");
        assertEquals(20, (int) joueur.getPositionY(), "Position Y après déplacement hors limites (min)");


        try {
            int read = System.in.read(); // stocke la valeur lue pour éviter l'avertissement
            System.out.println("Entrée terminal lue: " + read);
        } catch (IOException e) {
            // Rien à faire
        }

        System.out.println("Fin du test manuel.");
    }

    private static void assertEquals(int expected, int actual, String message) {
        if (expected != actual) {
            System.err.println("ÉCHEC: " + message + " attendu=" + expected + " mais obtenu=" + actual);
            throw new AssertionError(message + " attendu=" + expected + " mais obtenu=" + actual);
        } else {
            System.out.println("OK: " + message + " = " + actual);
        }
    }
}
