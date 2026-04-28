package Modele;

import Modele.Armes.*;
import Modele.Batiments.Batiment;
import Modele.Batiments.Mine;
import Modele.Armure.Armure;
import Modele.Items.*;

import static Modele.Constantes.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import static java.lang.Math.abs;

/**
 * Cœur interactif du jeu.
 * Gère l'état global du joueur (statistiques, inventaire, équipement) et encapsule
 * les logiques métier de ses actions (déplacement, combat, récolte, réparation).
 */
public class Joueur implements Localisable {

    /** ---------- [Propriétés - Statistiques Vitales & Attributs] ---------- **/

    private int hp;
    private int hpMax = HP_JOUEUR;
    private int attack;
    private int vitesse;
    private int reduction;

    /** ---------- [Propriétés - Économie & Inventaire] ---------- **/

    private ArrayList<Item> inventaire;
    private ArrayList<Ressource> ressources = new ArrayList<>();
    private int pieces;
    private boolean aPioche;

    /** ---------- [Propriétés - Équipement] ---------- **/

    private Arme armeEquipee;
    private Arme armePasEquipee;
    private Armure armurePrincipale;
    private Armure armureSecondaire;

    /** ---------- [Propriétés - Position & Moteur Physique] ---------- **/

    private double positionX;
    private double positionY;

    /** ---------- [Propriétés - État & Chronomètres] ---------- **/

    private long dernierTempsAttaque = 0;
    private boolean enReparation = false;
    private Batiment batimentEnReparation = null;

    /** ---------- [Propriétés - Dépendances Système & Threading] ---------- **/

    private final Modele modele;
    private static DeplaceJoueur threadActuel = null;
    private ThreadReparation threadReparation = null;

    /** ---------- [Constructeurs] ---------- **/

    /**
     * Initialise le joueur au centre de la carte avec ses statistiques,
     * son équipement de base et son économie de départ.
     *
     * @param modele - Le modèle principal du jeu
     */
    public Joueur(Modele modele) {
        this.modele = modele;

        this.positionX = (double) LARGEUR_MAP / 2;
        this.positionY = (double) HAUTEUR_MAP / 2 + 200;

        this.hp = HP_JOUEUR;
        this.attack = 0;
        this.vitesse = 0;
        this.reduction = 0;

        this.inventaire = new ArrayList<>();
        this.aPioche = false;

        // Économie initiale (Test / Debug)
        for (int i = 0; i < 10; i++) {
            ressources.add(new Ressource(0));
            ressources.add(new Ressource(1));
            ressources.add(new Ressource(2));
            ressources.add(new Ressource(3));
        }
        this.pieces = 20000;

        // Équipement par défaut
        this.armeEquipee = new Baton();
        this.armePasEquipee = null;
        this.armurePrincipale = null;
        this.armureSecondaire = null;
    }

    /** ---------- [Accesseurs - Statistiques] ---------- **/

    @Override
    public int getMaxHp() { return HP_JOUEUR; }

    @Override
    public String getNom() { return "Joueur"; }

    public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = hp; }

    public int getHpMax() { return hpMax; }
    public void setHpMax(int hpMax) { this.hpMax = hpMax; }

    public int getAttack() { return this.attack; }
    public void setAttack(int attack) { this.attack = attack; }

    public int getVitesse() { return vitesse; }
    public void incrVitesse(int i) { vitesse += i; }

    public int getReductionDegats() { return this.reduction; }
    public void setReductionDegats(int reduction) { this.reduction = reduction; }

    /** ---------- [Accesseurs - Position (Thread-Safe)] ---------- **/

    public synchronized double getX() { return positionX; }
    public synchronized void setPositionX(double positionX) { this.positionX = positionX; }

    public synchronized double getY() { return positionY; }
    public synchronized void setPositionY(double positionY) { this.positionY = positionY; }

    /** ---------- [Accesseurs - Économie & Inventaire] ---------- **/

    public int getPieces() { return pieces; }
    public void setPieces(int pieces) { this.pieces = pieces; }
    public void addPieces(int montant) { pieces += montant; }

    public boolean hasPioche() { return aPioche; }
    public void setaPioche(boolean aPioche) { this.aPioche = aPioche; }

    public ArrayList<Item> getInventaire() { return inventaire; }
    public void addToInventaire(Item item) { inventaire.add(item); }
    public void removeFromInventaire(Item item) { inventaire.remove(item); }

    public ArrayList<Ressource> getRessources(){ return ressources; }
    public void addToRessource(Ressource r) { this.ressources.add(r); }

    /**
     * @return Une Map regroupant les items par quantité (utile pour le rendu UI de l'inventaire).
     */
    public LinkedHashMap<Item, Integer> getInventaireGroupe() {
        LinkedHashMap<Item, Integer> map = new LinkedHashMap<>();
        for (Item item : inventaire) {
            map.merge(item, 1, Integer::sum);
        }
        return map;
    }

    /** ---------- [Accesseurs - Équipement & Threads] ---------- **/

    public Arme getArmeEquipee() { return armeEquipee; }
    public void setArmeEquipee(Arme armeEquipee) { this.armeEquipee = armeEquipee; }

    public Arme getArmePasEquipee() { return armePasEquipee; }
    public void setArmePasEquipee(Arme armePasEquipee) { this.armePasEquipee = armePasEquipee; }

    public Armure getArmurePrincipale(){ return armurePrincipale; }
    public void setArmureEquipee(Armure armureEquipee){
        this.armurePrincipale = armureEquipee;
        setReductionDegats(armureEquipee.getReduction());
    }

    public Armure getArmureSecondaire(){ return armureSecondaire; }
    public void setArmureSecondaire(Armure armureSecondaire){ this.armureSecondaire = armureSecondaire; }

    public ThreadReparation getThreadReparation() { return threadReparation; }
    public Modele getModele() { return this.modele; }
    public Batiment getBatimentEnReparation() { return batimentEnReparation; }

    /** ---------- [Méthodes Publiques - Gestion de l'Équipement] ---------- **/

    public void switchArmes() {
        if (armePasEquipee != null) {
            Arme temp = armeEquipee;
            armeEquipee = armePasEquipee;
            armePasEquipee = temp;
        }
    }

    public void ajouterDeuxiemeArme(Arme nouvelleArme){
        armePasEquipee = armeEquipee;
        armeEquipee = nouvelleArme;
    }

    public void switchArmures() {
        if (armureSecondaire != null) {
            Armure temp = armurePrincipale;
            armurePrincipale = armureSecondaire;
            armureSecondaire = temp;
        }
    }

    public void ajouterDeuxiemeArmure(Armure a) {
        armureSecondaire = armurePrincipale;
        setArmureEquipee(a);
    }

    public void equiperArmure(Armure nouvelleArmure) {
        this.armurePrincipale = nouvelleArmure;
        setReductionDegats(nouvelleArmure.getReduction());
    }

    /** ---------- [Méthodes Publiques - Consommables & Magie] ---------- **/

    /**
     * Applique l'effet de l'item sélectionné et le retire de l'inventaire.
     */
    public void utiliserConsommable(Item item) {
        if (item instanceof PotionVie || item instanceof PotionVieGrande) {
            soigner(item.getEffet());
            removeFromInventaire(item);
        } else if (item instanceof PotionVitesse){
            incrVitesse(item.getEffet());
            removeFromInventaire(item);
        } else if (item instanceof PotionDegats){
            setAttack(getAttack() + item.getEffet());
            removeFromInventaire(item);
        } else if (item instanceof Armageddon) {
            modele.declencherArmageddon();
            removeFromInventaire(item);
        }
    }

    /**
     * Instancie et lance un projectile magique dans la direction indiquée.
     */
    public void utiliserSort(Item item, double directionX, double directionY) {
        if (item instanceof SortFeu) {
            SortFeu sort = new SortFeu(this.positionX, this.positionY, directionX, directionY);
            modele.getGestionnaireSorts().ajouterSort(sort);
            removeFromInventaire(item);
        } else if (item instanceof SortTempete) {
            SortTempete sort = new SortTempete(this.positionX, this.positionY, directionX, directionY);
            modele.getGestionnaireSorts().ajouterSort(sort);
            removeFromInventaire(item);
        }
    }

    /** ---------- [Méthodes Publiques - Système de Combat & Mouvement] ---------- **/

    public void soigner(int soin){
        this.hp += soin;
        if(this.hp > HP_JOUEUR){
            this.hp = HP_JOUEUR;
        }
    }

    /**
     * @return True si le cooldown de l'arme est terminé, autorisant une nouvelle frappe.
     */
    public boolean peutAttaquer(){
        long tempsActuel = System.currentTimeMillis();
        long cooldown = armeEquipee.getCadence();
        return (tempsActuel - dernierTempsAttaque) >= cooldown;
    }

    public void setDernierTempsAttaque() {
        this.dernierTempsAttaque = System.currentTimeMillis();
    }

    /**
     * Démarre un nouveau déplacement fluide asynchrone, écrasant le précédent s'il existe.
     */
    public void setThreadActuel(DeplaceJoueur thread) {
        if (threadActuel != null && threadActuel.isAlive()) {
            threadActuel.interrupt();
        }
        threadActuel = thread;
    }

    /**
     * Applique le déplacement sur l'axe X tout en gérant les collisions avec les bords et les bâtiments (glissement).
     */
    public synchronized void deplaceX(double x) {
        if (x >= 10 + J_TAILLE / 2 && x <= LARGEUR_MAP) {
            if (!modele.collisionAvecBatimentSolide(x, this.positionY)) {
                setPositionX(x);
            }
        } else if (x <= 10 + J_TAILLE / 2) {
            setPositionX(10 + J_TAILLE / 2);
        } else {
            setPositionX(LARGEUR_MAP);
        }
    }

    /**
     * Applique le déplacement sur l'axe Y tout en gérant les collisions avec les bords et les bâtiments (glissement).
     */
    public synchronized void deplaceY(double y) {
        if (y >= 10 + J_TAILLE / 2 && y <= HAUTEUR_MAP) {
            if (!modele.collisionAvecBatimentSolide(this.positionX, y)) {
                setPositionY(y);
            }
        } else if (y <= 10 + J_TAILLE / 2) {
            setPositionY(10 + J_TAILLE / 2);
        } else {
            setPositionY(HAUTEUR_MAP);
        }
    }

    /** ---------- [Méthodes Publiques - Interactions & Environnement] ---------- **/

    public void ramasseRessource(){
        List<Ressource> ressourcesDispo = modele.getUpdateJN().getRessources();

        for (int i = ressourcesDispo.size() - 1; i >= 0; i--) {
            Ressource r = ressourcesDispo.get(i);
            if (abs(r.getPositionY() - positionY) <= 30 && abs(r.getPositionX() - positionX) <= 30) {
                addToRessource(r);
                ressourcesDispo.remove(i);
            }
        }
    }

    /**
     * Transfère le minerai stocké dans la mine vers l'inventaire du joueur s'il est à portée (De jour uniquement).
     */
    public void recolterMine() {
        if (!modele.getLeCycleJourNuit().isDay()) {
            System.out.println("Récolte impossible : c'est la nuit !");
            return;
        }

        for (Batiment b : modele.getGestionnaireBatiments().getBatiments()) {
            if (b instanceof Mine) {
                Mine mine = (Mine) b;
                double distance = Math.hypot(mine.getX() - this.positionX, mine.getY() - this.positionY);

                if (distance <= mine.getRange() + 50) {
                    int nbRessources = mine.getRessources().size();
                    if (nbRessources > 0) {
                        this.ressources.addAll(mine.getRessources());
                        mine.getRessources().clear();
                        System.out.println("Succès : " + nbRessources + " minerais récoltés !");
                    } else {
                        System.out.println("La mine est vide pour le moment.");
                    }
                    return;
                } else {
                    System.out.println("Échec : Tu es trop loin de la mine.");
                }
            }
        }
    }

    /** ---------- [Méthodes Publiques - Système de Réparation] ---------- **/

    /**
     * Recherche le bâtiment endommagé le plus proche et lance un thread dédié pour le soigner progressivement.
     */
    public void lancerReparation() {
        if (modele == null || modele.getGestionnaireBatiments() == null) return;
        if (!modele.getUpdateJN().isDay()) return;
        if (enReparation) return;

        Batiment batimentAReparer = null;
        double plusProcheDist = Double.MAX_VALUE;

        for (Batiment b : modele.getGestionnaireBatiments().getBatiments()) {
            if (b.getHp() < b.getMaxHp()) {
                double diffX = this.positionX - b.getX();
                double diffY = this.positionY - b.getY();
                double distCentrale = Math.hypot(diffX, diffY);

                double dimensionMaxBatiment = Math.max(b.getLargeurHitbox(), b.getHauteurHitbox());
                double porteeDynamique = REPARATION_RANGE + (dimensionMaxBatiment / 2.0);

                if (distCentrale <= porteeDynamique) {
                    if (distCentrale < plusProcheDist) {
                        plusProcheDist = distCentrale;
                        batimentAReparer = b;
                    }
                }
            }
        }

        if (batimentAReparer != null) {
            enReparation = true;
            Batiment cible = batimentAReparer;
            this.batimentEnReparation = cible;

            Thread threadReparation = new Thread(() -> {
                try {
                    while (cible.getHp() < cible.getMaxHp()) {
                        if (!modele.getUpdateJN().isDay()) break;

                        double dX = this.positionX - cible.getX();
                        double dY = this.positionY - cible.getY();
                        double dimMaxBat = Math.max(cible.getLargeurHitbox(), cible.getHauteurHitbox());
                        double porteeDyn = REPARATION_RANGE + (dimMaxBat / 2.0);

                        if (Math.hypot(dX, dY) > porteeDyn) break;

                        cible.setHp(cible.getHp() + 1);

                        if (!cible.isFonctionnel()) cible.setFonctionnel(true);
                        if (!cible.isAttaquable()) cible.setAttaquable(true);

                        if (cible.getHp() >= cible.getMaxHp()) {
                            cible.setHp(cible.getMaxHp());
                            break;
                        }

                        Thread.sleep(100);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    enReparation = false;
                    batimentEnReparation = null;
                }
            });
            threadReparation.start();
        }
    }

    public void stopperReparation() {
        this.enReparation = false;
        this.batimentEnReparation = null;
    }

    /** ---------- [Méthodes Publiques - Économie & RTS] ---------- **/

    public void acheter(int montant){
        this.consommerRessource(3, montant);
    }

    public int calculerMaxToursConstructibles() {
        int nbBois = 0, nbPierre = 0, nbFer = 0, nbOr = 0;
        for (Ressource r : ressources) {
            switch (r.getType()) {
                case 0: nbBois++; break;
                case 1: nbPierre++; break;
                case 2: nbFer++; break;
                case 3: nbOr++; break;
            }
        }
        return Math.min(
                Math.min(nbBois / 4, nbPierre / 4),
                Math.min(nbFer / 2, nbOr / 1)
        );
    }

    /**
     * Compare les ressources en stock avec un dictionnaire de coûts.
     */
    public boolean aAssezDeRessources(Map<Integer, Integer> couts) {
        int[] stocks = new int[4];
        for (Ressource r : ressources) {
            if (r.getType() >= 0 && r.getType() < 4) {
                stocks[r.getType()]++;
            }
        }

        for (Map.Entry<Integer, Integer> cout : couts.entrySet()) {
            int typeRessource = cout.getKey();
            int quantiteRequise = cout.getValue();

            if (stocks[typeRessource] < quantiteRequise) {
                return false;
            }
        }
        return true;
    }

    public void consommerListeRessources(Map<Integer, Integer> couts) {
        for (Map.Entry<Integer, Integer> cout : couts.entrySet()) {
            int typeRessource = cout.getKey();
            int quantiteRequise = cout.getValue();
            this.consommerRessource(typeRessource, quantiteRequise);
        }
    }

    /** ---------- [Méthodes Privées - Utilitaires] ---------- **/

    /**
     * Supprime de l'inventaire le montant exact d'une ressource spécifique.
     * Parcourt la liste à l'envers pour prévenir les décalages d'index.
     */
    private void consommerRessource(int type, int quantiteARetirer) {
        int supprimes = 0;

        for (int i = ressources.size() - 1; i >= 0; i--) {
            if (ressources.get(i).getType() == type) {
                ressources.remove(i);
                supprimes++;

                if (supprimes == quantiteARetirer) {
                    break;
                }
            }
        }
    }
}