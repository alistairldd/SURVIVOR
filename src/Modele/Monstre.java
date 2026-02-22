package Modele;

import java.util.ArrayList;

public class Monstre {
    private int positionX;
    private int positionY;
    private int vie;
    private int attaque;

    public Monstre(int positionX, int positionY, int vie, int attaque) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.vie = vie;
        this.attaque = attaque;
    }

    public ArrayList<Monstre> genereMonstre(){
        /*Génère les monstres aléatoirement sur les bordures 2000x2000*/
        ArrayList<Monstre> monstres = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int x = (int) (Math.random() * 2000);
            int y = (int) (Math.random() * 2000);
            monstres.add(new Monstre(x, y, vie, attaque));
        }
        return monstres;
    }

}
