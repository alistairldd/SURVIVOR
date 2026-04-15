package Modele.Armes;

import java.util.Map;

import static Modele.Constantes.IMAGE_EPEE_LOURDE;

public class EpeeLourde extends Arme{

    public EpeeLourde() {
        super(
                "Epee Lourde",
                50,
                150,
                1500,
                Math.PI,
                IMAGE_EPEE_LOURDE,
                Map.of(2, 25, 1, 10) // 2 = Fer, 1 = Pierre
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
    public Map<Integer, Integer> getRessourcesNecessaires() {
        return super.getRessourcesNecessaires();
    }
}