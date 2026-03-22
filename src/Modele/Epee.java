package Modele;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Implémentation concrète d'une arme : l'Épée.
 * Elle hérite de la classe abstraite Arme et définit ses propres statistiques
 * (dégâts moyens, courte portée, balayage large, cadence modérée).
 */
public class Epee extends Arme {

    /**
     * Constructeur par défaut de l'Épée.
     * Fait appel au constructeur de la classe parente (super) pour injecter ses constantes.
     */


    public Epee() {
        // Appelle le constructeur de "Arme" avec les valeurs :
        // Nom: "Epee"
        // Dégâts: 10
        // Portée: 100 pixels
        // Cadence: 500 millisecondes (0.5s entre chaque coup)
        // Angle: PI/3 radians (environ 60 degrés, crée un cône d'attaque assez large)

        super("Epee", 10, 100, 500, Math.PI / 3);
    }

    // Récupère la valeur des dégâts définis dans la classe parente
    @Override
    public int getDegats() {
        return super.getDegats();
    }

    // Récupère la valeur de la portée définie dans la classe parente
    @Override
    public int getPortee() {
        return super.getPortee();
    }

    // Récupère la cadence de frappe définie dans la classe parente
    @Override
    public int getCadence() {
        return super.getCadence();
    }

    // Récupère le nom défini dans la classe parente
    @Override
    public String getNom() {
        return super.getNom();
    }
}