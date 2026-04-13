package Modele.Armes;

import java.util.List;

import static Modele.Constantes.IMAGE_LANCE;

public class Lance extends Arme{

    public Lance() {
        super(
                "Lance",
                40,
                150,
                1000,
                Math.PI / 12, // Angle d'attaque plus étroit que l'épée, reflétant la nature plus précise et linéaire de la lance
                IMAGE_LANCE, // Image non définie pour l'instant
                List.of("Bois:20", "Pierre:10") // Coût en ressources pour fabriquer la lance
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
    public List<String> getRessourcesNecessaires() {
        return super.getRessourcesNecessaires();
    }
}
