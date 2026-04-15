package Modele.Armes;

import java.util.Map;

import static Modele.Constantes.IMAGE_EPEEBOIS;
public class EpeeBois extends Arme {
    public EpeeBois() {
        super(
                "EpeeBois",
                5,
                80,
                700,
                Math.PI / 3,
                IMAGE_EPEEBOIS, // Image à définir pour l'épée en bois
                Map.of(0, 10) // Ressources nécessaires pour fabriquer l'épée en bois (0 = Bois)
        );
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

    @Override
    public double getAngle() {
        return super.getAngle();
    }

    @Override
    public Map<Integer, Integer> getRessourcesNecessaires() {
        return super.getRessourcesNecessaires();
    }
}