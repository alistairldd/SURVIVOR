package Modele;
import Modele.Armure;

import java.util.List;

import java.awt.*;

import static Modele.Constantes.IMAGE_ARMURE_LEGERE;

public class ArmureLegere extends Armure{
    public ArmureLegere() {
        super(
                "Armure légère",
                20,
                IMAGE_ARMURE_LEGERE,
                List.of("Bois:5, Pierre:5")
        ); // Bonus de 20 PV pour une armure légère
    }

    @Override
    public int getBonusVie() {
        return super.getBonusVie(); // Récupère le bonus de vie défini dans la classe parente
    }

    @Override
    public String getNom() {
        return super.getNom(); // Récupère le nom de l'armure défini dans la classe parente
    }

    @Override
    public Image getImage() {
        return super.getImage(); // Récupère l'image de l'armure définie dans la classe parente
    }

    @Override
    public List<String> getRessourcesNecessaires() {
        return super.getRessourcesNecessaires(); // Récupère les ressources nécessaires définies dans la classe parente
    }

}
