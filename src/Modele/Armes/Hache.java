package Modele.Armes;

import java.util.Map;
import static Modele.Constantes.IMAGE_HACHE;

/**
 * Implémentation de la Hache.
 * Arme à courte portée mais capable de toucher autour d'elle (360°).
 */
public class Hache extends Arme {

    /** ---------- [Constructeurs] ---------- **/

    public Hache() {
        super(
                "Hache",
                15,
                90,
                700,
                Math.PI * 2,
                IMAGE_HACHE,
                Map.of(0, 10, 2, 5)
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