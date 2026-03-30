package Modele;

import java.util.List;

import static Modele.Constantes.IMAGE_HACHE;

public class Hache extends Arme {

    public Hache() {
        // Appelle le constructeur de "Arme" avec les valeurs :
        // Nom: "Hache"
        // Dégâts: 15 (plus élevée que l'épée, mais avec une portée plus courte)
        // Portée: 80 pixels (plus courte que l'épée, reflétant la nature plus lourde et moins maniable de la hache)
        // Cadence: 700 millisecondes (plus lente que l'épée, représentant le temps nécessaire pour balancer une hache)
        // Angle: 2*PI radians (360 degrés, la hache peut attaquer dans toutes les directions autour du joueur, mais avec une portée plus courte)
        // Image: IMAGE_HACHE (à définir dans les ressources, peut être une image de hache)
        // Ressources nécessaires: "Bois:10, Fer:5"
        super(
                "Hache",
                15,
                80,
                700,
                Math.PI * 2,
                IMAGE_HACHE, // Image non définie pour l'instant
                (java.awt.List) List.of("Bois:10, Fer:5") // Coût en ressources pour fabriquer la hache
        );
    }

    @Override
    public int getDegats() {
        return super.getDegats();
    }

    @Override
    public int getPortee() {
        return super.getPortee();
    }

    @Override
    public int getCadence() {
        return super.getCadence();
    }

    @Override
    public String getNom() {
        return super.getNom();
    }

    @Override
    public double getAngle() {
        return super.getAngle();
    }

    @Override
    public java.awt.List getRessourcesNecessaires() {
        return super.getRessourcesNecessaires();
    }
}
