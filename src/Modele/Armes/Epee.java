package Modele.Armes;

import java.util.Map;
import static Modele.Constantes.IMAGE_EPEE;

/**
 * Implémentation de l'Épée standard.
 * Équilibre entre dégâts, portée et cadence de frappe.
 */
public class Epee extends Arme {

    /** ---------- [Constructeurs] ---------- **/

    public Epee() {
        super(
                "Epee",
                15,
                100,
                500,
                Math.PI / 3,
                IMAGE_EPEE,
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