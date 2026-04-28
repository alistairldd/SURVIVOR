package Modele.Armes;

import java.util.Map;
import static Modele.Constantes.IMAGE_LANCE;

/**
 * Implémentation de la Lance.
 * Arme d'estoc offrant une grande portée mais avec un angle de frappe très étroit.
 */
public class Lance extends Arme{

    /** ---------- [Constructeurs] ---------- **/

    public Lance() {
        super(
                "Lance",
                40,
                150,
                1000,
                Math.PI / 12,
                IMAGE_LANCE,
                Map.of(0, 20, 1, 10)
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