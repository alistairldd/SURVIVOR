package Modele.Armes;

import java.util.List;

import static Modele.Constantes.IMAGE_EPEE_LOURDE;

public class EpeeLourde extends Arme{

    public EpeeLourde() {
        super(
                "Epee Lourde",
                50,
                80,
                1500,
                Math.PI,
                IMAGE_EPEE_LOURDE,
                List.of("Fer:25, Pierre:10")
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
