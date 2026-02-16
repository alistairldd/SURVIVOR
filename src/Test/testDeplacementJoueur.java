package Test;

import Modele.Modele;
import Modele.Joueur;
import Controleur.Jeu;
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
        assertEquals(0, Joueur.getPositionX(), "Position X initiale");
        assertEquals(0, Joueur.getPositionY(), "Position Y initiale");

        // Simuler un clic dans la carte
        System.out.println("Test: déplacement à (100,150)");
        joueur.deplaceJoueur(100, 150);
        assertEquals(100, Joueur.getPositionX(), "Position X après déplacement");
        assertEquals(150, Joueur.getPositionY(), "Position Y après déplacement");

        // Simuler un clic hors limites pour vérifier la saturation
        System.out.println("Test: déplacement hors limites à (9999,-9999)");
        joueur.deplaceJoueur(9999, -9999);
        assertEquals(taille, Joueur.getPositionX(), "Position X après déplacement hors limites (max)");
        assertEquals(-taille, Joueur.getPositionY(), "Position Y après déplacement hors limites (min)");

        System.out.println("Tests automatiques OK. Ouverture de la fenêtre pour test manuel...");

        // Ouvrir la fenêtre du jeu pour que le prof puisse tester le clic manuellement
        new Jeu();
        System.out.println("La fenêtre du jeu est ouverte. Cliquez avec le bouton droit pour déplacer le joueur.");
        System.out.println("Appuyez sur Entrée dans ce terminal pour terminer le test et fermer l'application.");

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
