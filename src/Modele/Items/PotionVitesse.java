package Modele.Items;

import static Modele.Constantes.IMAGE_POTION_VITESSE;

public class PotionVitesse extends Item{
    private int bonusVitesse;
    private int duree;

    public PotionVitesse() {
        super("Potion de Vitesse",
                1,
                IMAGE_POTION_VITESSE, // Image à définir
                20
        );
        this.bonusVitesse = 2; // Multiplie la vitesse par 2
        this.duree = 5000; // Durée en millisecondes (5 secondes)
    }

    public int getBonusVitesse() {
        return bonusVitesse;
    }

    public int getDuree() {
        return duree;
    }
}
