package Modele;
import Modele.Armure;

public class ArmureLegere extends Armure{
    public ArmureLegere(String nom, int bonusVie) {
        super("Armure légère", 20); // Bonus de 20 PV pour une armure légère
    }

    public int getBonusVie() {
        return super.getBonusVie(); // Récupère le bonus de vie défini dans la classe parente
    }
}
