package Modele;

import static Vue.VueJoueur.TAILLE;

public class Ressource {
    public static final int[] TYPE_RESSOURCE = {0, 1, 2, 3}; // 0 : bois, 1 : pierre, 2 : fer, 3: or, 4 : redstone
    public static final int NB_RESSOURCES = 20;

    private int positionX;
    private int positionY;
    private int type;

    public Ressource() {
        /*
        constructeur de base de ressource
        on génère une ressource à une position aléatoire sur la carte, en évitant les bords
        on appelle ce constructeur dans la méthode de generation des ressources
         */
        int offsetDecale = 10 + TAILLE / 2;

        this.positionX = offsetDecale + (int) (Math.random() * (Map.LARGEUR_MAP - 2 * (double) offsetDecale)); // On ajoute un offset pour éviter que les ressources soient générées trop près des bords de la carte
        this.positionY = offsetDecale + (int) (Math.random() * (Map.HAUTEUR_MAP - 2 * (double) offsetDecale)); // Idem

        int index = (int) (Math.random() * TYPE_RESSOURCE.length);
        this.type = TYPE_RESSOURCE[index];
    }

    public static void genereRessources(int nbRessources) {
        Map carte = Modele.getMap();
        carte.viderRessources();

        for (int i = 0; i < nbRessources; i++) {
            carte.getRessources().add(new Ressource());
        }
    }

    public static void viderRessources() {
        Modele.getMap().viderRessources();
    }

    public int getPositionX() { return positionX; }
    public int getPositionY() { return positionY; }
    public int getType() { return type; }
}