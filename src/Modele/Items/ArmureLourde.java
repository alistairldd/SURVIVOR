package Modele.Items;

import java.awt.*;
import java.util.List;

import static Modele.Constantes.IMAGE_ARMURE_LOURDE;

public class ArmureLourde extends Armure{
    public ArmureLourde(){
        super(
                "Armure lourde",
                50,
                IMAGE_ARMURE_LOURDE,
                List.of("Fer:20","Pierre:10")
        );
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
