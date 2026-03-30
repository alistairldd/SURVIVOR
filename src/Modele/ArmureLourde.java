package Modele;

public class ArmureLourde extends Armure{
    public ArmureLourde(){
        super("Armure lourde", 50); // Bonus de 50 PV pour une armure lourde
    }
    public int getBonusVie() {
        return super.getBonusVie(); // Récupère le bonus de vie défini dans la classe parente
}
}
