package Modele;

public class Ressource {
    int PositionX;
    int PositionY;
    int[] typeRessource = {0,1,2}; // 0 = bois, 1 = pierre, 2 = nourriture

    public Ressource(){
        	this.PositionX = (int)(Math.random() * Map.LARGEUR_MAP);
        	this.PositionY = (int)(Math.random() * Map.HAUTEUR_MAP);
        	int index = (int)(Math.random() * typeRessource.length);
        	int type = typeRessource[index];
        	System.out.println("Ressource créée : " + type + " à la position (" + PositionX + ", " + PositionY + ")");
    }
}
