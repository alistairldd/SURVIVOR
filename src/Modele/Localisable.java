package Modele;

/**
 * Interface garantissant qu'une entité possède des coordonnées spatiales
 * et des points de vie, permettant au moteur de gérer les ciblages et interactions.
 */
public interface Localisable {

    /** ---------- [Méthodes Abstraites] ---------- **/

    double getX();
    double getY();
    int getHp();
    int getMaxHp();
    String getNom();
    void setHp(int hp);
}