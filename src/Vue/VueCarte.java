package Vue;

import Modele.Modele;

import java.awt.*;

public class VueCarte {

    private final Modele modele;

    public VueCarte(Modele modele) {
        this.modele = modele;
    }


    protected void dessiner(Graphics g) {
        g.setColor(Color.green);
        g.fillRect(0,0,2000,2000);
    }
}
