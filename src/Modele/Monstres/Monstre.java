package Modele.Monstres;

import Modele.Joueur;
import Modele.Localisable;

import java.awt.*;

import static Modele.Constantes.*;

/**
 * Modèle de base pour toutes les entités hostiles (Ennemis).
 * Gère l'identification unique, les statistiques vitales, et la logique
 * mathématique des déplacements (parcours de vecteurs 2D/2.5D).
 */
public abstract class Monstre extends Thread implements Localisable {

    /** ---------- [Propriétés - Système & Identification] ---------- **/

    private static int compteurID = 0;
    private final int id;
    private final String nom;

    /** ---------- [Propriétés - Statistiques & Combat] ---------- **/

    private int hp;
    private int maxHp;
    private int attack;
    private int portee;
    private double vitesse;
    protected int drop;
    private double cadenceAttaque = 1.0;
    private double tempsDepuisDerniereAttaque = 0;

    /** ---------- [Propriétés - Déplacement & Animation] ---------- **/

    protected double x, y;
    protected double directionX;
    protected boolean marche = true;
    protected double animation = 0;
    protected double animationAtt = 0;
    protected boolean animationMarche = true;
    protected boolean animationAttaque = false;


    /** ---------- [Constructeurs] ---------- **/

    /**
     * Initialise un monstre avec ses statistiques de base et lui attribue un identifiant unique.
     *
     * @param nom - Le nom de l'espèce du monstre
     * @param maxHp - Les points de vie maximum (et initiaux)
     * @param attack - La puissance de frappe brute
     * @param portee - La distance minimale requise pour déclencher une attaque
     * @param vitesse - La vitesse de déplacement en pixels par itération
     * @param drop - La quantité d'or lâchée à la mort
     */
    public Monstre(String nom, int maxHp, int attack, int portee, int vitesse, int drop) {
        this.id = compteurID++;
        this.nom = nom;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.attack = attack;
        this.portee = portee;
        this.vitesse = vitesse;
        this.drop = drop;
    }


    /** ---------- [Accesseurs / Getters & Setters] ---------- **/

    public int getID() { return id; }
    public String getNom() { return nom; }

    public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = hp; }
    public int getMaxHp() { return maxHp; }
    public boolean estVivant() { return hp > 0; }

    public int getAttack() { return attack; }
    public void setAttack(int attack) { this.attack = attack; }

    public int getPortee() { return portee; }
    public int getDrop() { return drop; }

    public double getX() { return x; }
    public void setPositionX(double x) { this.x = x; }

    public double getY() { return y; }
    public void setPositionY(double y) { this.y = y; }

    public boolean regardeGauche() { return directionX < 0; }

    public boolean isMarche () { return marche; }
    public double getAnimation() { return this.animation; }
    public boolean getAnimationMarche() { return animationMarche; }
    public boolean getAnimationAttaque() { return animationAttaque; }

    public void ajouterAnimation(double delta) { this.animation += delta; }


    /** ---------- [Méthodes Abstraites] ---------- **/

    public abstract Image getImage();


    /** ---------- [Méthodes Publiques - Moteur Métier] ---------- **/

    public void perdreHp(int hpPerdus) { this.hp -= hpPerdus; }

    public void prendreDegats(int i) { this.hp -= i; }

    /**
     * Calcule le vecteur de déplacement vers la cible et l'applique.
     * Gère la transition entre l'état de marche et d'attaque en fonction de la distance,
     * en adaptant la hitbox si la cible est un bâtiment (collision 2.5D).
     *
     * @param cible - L'entité à poursuivre (Joueur ou Bâtiment)
     * @param dt - Le delta-time (temps écoulé depuis la dernière frame)
     */
    public void mettreAJourPosition(Localisable cible, double dt) {
        if (cible == null) return;

        double diffX = cible.getX() - this.x;
        double diffY = cible.getY() - this.y;
        double distanceActuelle = Math.sqrt(diffX * diffX + diffY * diffY);

        double distanceDArretNecessaire = this.portee;

        // Adaptation de la zone d'arrêt pour la perspective 2.5D des bâtiments
        if (cible instanceof Modele.Batiments.Batiment) {
            Modele.Batiments.Batiment b = (Modele.Batiments.Batiment) cible;
            double bLargeur = b.getLargeurHitbox();
            double bHauteur = b.getHauteurHitbox();

            double centreBaseY = cible.getY() + b.getOffsetYHitbox();
            double vraiDiffY = centreBaseY - this.y;

            // Calcul de la distance vers le bord rectangulaire le plus proche
            double dx = Math.max(Math.abs(diffX) - bLargeur / 2.0, 0);
            double dy = Math.max(Math.abs(vraiDiffY) - bHauteur / 2.0, 0);
            double distanceAuBord = Math.sqrt(dx * dx + dy * dy);

            if (distanceAuBord <= this.portee) {
                distanceActuelle = this.portee;
            }
        }
        else if (cible instanceof Joueur) {
            distanceDArretNecessaire = this.portee + (J_TAILLE / 2.0);
        }

        // Exécution de l'action selon la distance
        if (distanceActuelle > distanceDArretNecessaire) {
            this.marche = true;
            this.directionX = diffX / distanceActuelle;

            this.x += (diffX / distanceActuelle) * this.vitesse;
            this.y += (diffY / distanceActuelle) * this.vitesse;
        } else {
            this.marche = false;
            attaquer(cible, dt);
        }
    }


    /** ---------- [Méthodes Privées - Logique Interne] ---------- **/

    /**
     * Applique les dégâts à la cible si le temps de recharge est écoulé.
     * Tient compte des réductions de dégâts spécifiques (ex: Armure du joueur).
     */
    private void attaquer(Localisable cible, double dt) {
        tempsDepuisDerniereAttaque += dt;

        if (tempsDepuisDerniereAttaque >= cadenceAttaque) {
            if (cible instanceof Joueur){
                Joueur j = (Joueur) cible;
                int reduction = j.getReductionDegats();
                cible.setHp(Math.max(cible.getHp() - this.attack*(100-reduction)/100, 0));
            }
            else {
                cible.setHp(Math.max(cible.getHp() - this.attack, 0));
            }
            tempsDepuisDerniereAttaque = 0;
        }
    }
}