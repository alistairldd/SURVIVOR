package Modele.Armure;

import java.awt.*;
import java.util.Map;

import static Modele.Constantes.IMAGE_ARMURE_LOURDE;

public class ArmureLourde extends Armure {
    public ArmureLourde() {
        super(
                "Armure lourde",
                10,
                IMAGE_ARMURE_LOURDE,
                -10,
                Map.of(2, 20, 1, 10) // 2 = Fer, 1 = Pierre
        );
    }

    @Override
    public int getReduction() {
        return super.getReduction();
    }

    @Override
    public String getNom() {
        return super.getNom();
    }

    @Override
    public Image getImage() {
        return super.getImage();
    }

    @Override
    public Map<Integer, Integer> getRessourcesNecessaires() {
        return super.getRessourcesNecessaires();
    }
}