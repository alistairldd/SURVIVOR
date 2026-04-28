package Modele.Armes;

import java.util.Map;
import static Modele.Constantes.IMAGE_EPEE_LOURDE;

/**
 * Implémentation de l'Épée Lourde.
 * Arme lente mais dévastatrice avec un angle de balayage très large.
 */
public class EpeeLourde extends Arme{

    /** ---------- [Constructeurs] ---------- **/

    public EpeeLourde() {
        super(
                "Epee Lourde",
                50,
                150,
                1500,
                Math.PI,
                IMAGE_EPEE_LOURDE,
                Map.of(2, 25, 1, 10)
        );
    }

    /** ---------- [Méthodes Héritées] ---------- **/

    @Override public int getDegats() { return super.getDegats(); }
    @Override public int getPortee() { return super.getPortee(); }
    @Override public int getCadence() { return super.getCadence(); }
    @Override public String getNom() { return super.getNom(); }
    @Override public double getAngle() { return super.getAngle(); }
    @Override public Map<Integer, Integer> getRessourcesNecessaires() { return super.getRessourcesNecessaires(); }
}