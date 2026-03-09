package Modele;

public class Tower extends Batiment{
    public final int BASE_DAMAGE = 20; //Dégâts de base pour toutes les tours.
    public final int BASE_RANGE = 100; //Portée de base pour toutes les tours.

    private int range; //Portée de la tour.
    private int damage; //Dégâts que la tour inflige aux ennemis.

    public Tower(int x, int y) {
        super(x,y);
        this.range = BASE_RANGE;
        this.damage = BASE_DAMAGE;
    }

        public int getRange() {
            return range;
        }
}
