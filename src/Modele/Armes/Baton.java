package Modele.Armes;

import java.util.Map;
import static Modele.Constantes.IMAGE_BATON;

/**
 * Implémentation de l'arme de base : le Bâton.
 * Arme initiale avec des dégâts faibles mais une cadence rapide.
 */
public class Baton extends Arme {

    /** ---------- [Constructeurs] ---------- **/

    public Baton() {
        super(
                "Baton",
                10,
                100,
                500,
                Math.PI / 3,
                IMAGE_BATON,
                Map.of(2, 10)
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