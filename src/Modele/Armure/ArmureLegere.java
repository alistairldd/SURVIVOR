package Modele.Armure;

import java.util.Map;
import java.awt.*;

import static Modele.Constantes.IMAGE_ARMURE_LEGERE;

public class ArmureLegere extends Armure {
    public ArmureLegere() {
        super(
                "Armure légère",
                5,
                IMAGE_ARMURE_LEGERE,
                -2,
                Map.of(0, 5, 1, 5) // 0 = Bois, 1 = Pierre
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