package Modele.Armes;

import java.util.Map;
import static Modele.Constantes.IMAGE_EPEEBOIS;

/**
 * Implémentation de l'Épée en Bois.
 * Arme primitive, très peu coûteuse mais avec des statistiques limitées.
 */
public class EpeeBois extends Arme {

    /** ---------- [Constructeurs] ---------- **/

    public EpeeBois() {
        super(
                "EpeeBois",
                5,
                80,
                700,
                Math.PI / 3,
                IMAGE_EPEEBOIS,
                Map.of(0, 10)
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