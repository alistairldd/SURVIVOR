/** ---------- [CORRECTION DEV MODE] ---------- **/
// Restauration stricte de la logique fonctionnelle d'origine.
// Les conditions (if) et la logique des méthodes ont été remises exactement
// comme dans le fichier fourni pour éliminer les erreurs.
// Seules la réorganisation et la documentation sont appliquées.

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
 * Gère l'état du joueur (PV, position, inventaire, arme) ainsi que ses actions
 * principales (se déplacer, ramasser, attaquer, construire).
 */
public class Joueur implements Localisable {

    /** ---------- [Propriétés - Statistiques & État] ---------- **/

    private int hp;
    private int hpMax = HP_JOUEUR;
    private int attack;
    private int vitesse;
    private int reduction;
    private long dernierTempsAttaque = 0;

    private boolean enReparation = false;
    private Batiment batimentEnReparation = null;

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

    /** ---------- [Propriétés - Position & Moteur] ---------- **/

    private double positionX;
    private double positionY;

    /** ---------- [Propriétés - Système & Threads] ---------- **/

    private final Modele modele;
    private static DeplaceJoueur threadActuel = null;
    private ThreadReparation threadReparation = null;

    /** ---------- [Constructeurs] ---------- **/

    /**
     * Initialise le joueur avec ses statistiques de base, son inventaire de départ
     * et le place au centre de la carte.
     *
     * @param modele - Le modèle principal du jeu
     */
    public Joueur(Modele modele) {
        positionX = (double) LARGEUR_MAP /2;
        positionY = (double) HAUTEUR_MAP /2 + 200;
        hp = HP_JOUEUR;
        attack = 0;
        inventaire = new ArrayList<Item>();
        this.aPioche = false;

        // Boucle de triche/test : donne 10 ressources de chaque type au joueur dès le début
        for (int i = 0; i < 10; i++) {
            ressources.add(new Ressource(0));
            ressources.add(new Ressource(1));
            ressources.add(new Ressource(2));
            ressources.add(new Ressource(3));
        }

        pieces = 20000;
        armeEquipee = new Baton();
        armePasEquipee = null;
        armurePrincipale = null;
        armureSecondaire = null;
        vitesse = 0;

        reduction = 0;
        this.modele = modele;
    }

    /** ---------- [Accesseurs & Modificateurs - Statistiques] ---------- **/

    @Override
    public int getMaxHp() {
        return HP_JOUEUR;
    }

    @Override
    public String getNom() {
        return "Joueur";
    }

    public int getHp() {return hp;}

    @Override
    public void setHp(int hp) {this.hp = hp;}

    public int getHpMax() {return hpMax;}

    public void setHpMax(int hpMax) {this.hpMax = hpMax;}

    public int getAttack() {return this.attack;}

    public void setAttack(int attack) {this.attack = attack;}

    public int getVitesse() {return vitesse;}

    private void incrVitesse(int i) {vitesse += i;}

    public int getReductionDegats() {
        return this.reduction;
    }

    public void setReductionDegats(int reduction) {
        this.reduction = reduction;
    }

    /** ---------- [Accesseurs & Modificateurs - Position] ---------- **/

    public synchronized double getX() {return positionX;}

    public synchronized void setPositionX(double positionX) {this.positionX = positionX;}

    public synchronized double getY() {return positionY;}

    public synchronized void setPositionY(double positionY) {this.positionY = positionY;}

    /** ---------- [Accesseurs & Modificateurs - Économie & Inventaire] ---------- **/

    public int getPieces() {
        return pieces;
    }

    public void setPieces(int pieces) {
        this.pieces = pieces;
    }

    public void addPieces(int montant) {
        pieces += montant;
    }

    public boolean hasPioche() {
        return aPioche;
    }

    public void setaPioche(boolean aPioche) {
        this.aPioche = aPioche;
    }

    public ArrayList<Item> getInventaire() {
        return inventaire;
    }

    public void addToInventaire(Item item) {
        inventaire.add(item);
    }

    public void removeFromInventaire(Item item) {
        inventaire.remove(item);
    }

    public ArrayList<Ressource> getRessources(){
        return ressources;
    }

    private void addToRessource(Ressource r) {
        this.ressources.add(r);
    }

    public void ajouterARessources(Ressource r) {
        ressources.add(r);
    }

    /**
     * @return Une Map regroupant les items par quantité.
     */
    public LinkedHashMap<Item, Integer> getInventaireGroupe() {
        LinkedHashMap<Item, Integer> map = new LinkedHashMap<>();
        for (Item item : inventaire) {
            map.merge(item, 1, Integer::sum);
        }
        return map;
    }

    /** ---------- [Accesseurs & Modificateurs - Équipement & Système] ---------- **/

    public Arme getArmeEquipee() {return armeEquipee;}

    public void setArmeEquipee(Arme armeEquipee) {this.armeEquipee = armeEquipee;}

    public Arme getArmePasEquipee() {return armePasEquipee;}

    public void setArmePasEquipee(Arme armePasEquipee) {this.armePasEquipee = armePasEquipee;}

    public Armure getArmurePrincipale(){return armurePrincipale;}

    public void setArmureEquipee(Armure armureEquipee){
        this.armurePrincipale = armureEquipee;
        setReductionDegats(armureEquipee.getReduction());
    }

    public Armure getArmureSecondaire(){return armureSecondaire;}

    public void setArmureSecondaire(Armure armureSecondaire){this.armureSecondaire = armureSecondaire;}

    public Modele getModele() {
        return this.modele;
    }

    public ThreadReparation getThreadReparation() { return threadReparation; }

    public Batiment getBatimentEnReparation() {
        return batimentEnReparation;
    }

    /** ---------- [Méthodes Publiques - Gestion d'Équipement] ---------- **/

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
     * Applique l'effet d'un item consommable sur le joueur.
     *
     * @param item - L'item à consommer
     */
    public void utiliserConsommable(Item item) {
        if (item instanceof PotionVie) {
            soigner(item.getEffet());
            removeFromInventaire(item);
        }

        if (item instanceof PotionVieGrande) {
            soigner(item.getEffet());
            removeFromInventaire(item);
        }

        if (item instanceof PotionVitesse){
            incrVitesse(item.getEffet());
            removeFromInventaire(item);
        }

        if (item instanceof PotionDegats){
            setAttack(getAttack() + item.getEffet());
            removeFromInventaire(item);
        }
        if (item instanceof Armageddon) {
            modele.declencherArmageddon();
            removeFromInventaire(item);
        }
    }

    /**
     * Instancie un projectile magique selon l'item sélectionné.
     *
     * @param item - Le type de sort
     * @param directionX - Composante X du vecteur de tir
     * @param directionY - Composante Y du vecteur de tir
     */
    public void utiliserSort(Item item, double directionX, double directionY) {
        if (item instanceof SortFeu) {
            SortFeu sort = new SortFeu(this.positionX, this.positionY, directionX, directionY);
            modele.getGestionnaireSorts().ajouterSort(sort);
            removeFromInventaire(item);
        }
        if (item instanceof SortTempete) {
            SortTempete sort = new SortTempete(this.positionX, this.positionY, directionX, directionY);
            modele.getGestionnaireSorts().ajouterSort(sort);
            removeFromInventaire(item);
        }
    }

    /** ---------- [Méthodes Publiques - Combat & Soins] ---------- **/

    public void soigner(int soin){
        this.hp += soin;
        if(this.hp > HP_JOUEUR){
            this.hp = HP_JOUEUR;
        }
    }

    /**
     * Vérifie si le joueur a le droit d'attaquer en fonction du temps de recharge de son arme.
     *
     * @return True si l'arme est prête
     */
    public boolean peutAttaquer(){
        long tempsActuel = System.currentTimeMillis();
        long cooldown = armeEquipee.getCadence();
        return (tempsActuel - dernierTempsAttaque) >= cooldown;
    }

    public void setDernierTempsAttaque() {
        this.dernierTempsAttaque = System.currentTimeMillis();
    }

    /** ---------- [Méthodes Publiques - Système de Réparation] ---------- **/

    /**
     * Cherche un bâtiment endommagé à proximité et lance un thread pour le réparer.
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
                        if (!modele.getUpdateJN().isDay()) {
                            break;
                        }

                        double dX = this.positionX - cible.getX();
                        double dY = this.positionY - cible.getY();
                        double dimMaxBat = Math.max(cible.getLargeurHitbox(), cible.getHauteurHitbox());
                        double porteeDyn = REPARATION_RANGE + (dimMaxBat / 2.0);

                        if (Math.hypot(dX, dY) > porteeDyn) {
                            break;
                        }

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

    /** ---------- [Méthodes Publiques - Déplacements Spatiaux] ---------- **/

    public void setThreadActuel(DeplaceJoueur thread) {
        if (threadActuel != null && threadActuel.isAlive()) {
            threadActuel.interrupt();
        }
        threadActuel = thread;
    }

    /**
     * Gère le déplacement sur X avec collision des bâtiments solides (glissement).
     */
    public synchronized void deplaceX(double x) {
        if (x >= 10+J_TAILLE/2 && x <= LARGEUR_MAP) {
            if (!modele.collisionAvecBatimentSolide(x, this.positionY)) {
                setPositionX(x);
            }
        }
        else if (x <= 10+J_TAILLE/2) {
            setPositionX(10+J_TAILLE/2);
        }
        else {
            setPositionX(LARGEUR_MAP);
        }
    }

    /**
     * Gère le déplacement sur Y avec collision des bâtiments solides (glissement).
     */
    public synchronized void deplaceY(double y) {
        if (y >= 10+J_TAILLE/2 && y <= HAUTEUR_MAP) {
            if (!modele.collisionAvecBatimentSolide(this.positionX, y)) {
                setPositionY(y);
            }
        }
        else if (y <= 10+J_TAILLE/2) {
            setPositionY(10+J_TAILLE/2);
        }
        else {
            setPositionY(HAUTEUR_MAP);
        }
    }

    /** ---------- [Méthodes Publiques - Interactions & Ressources] ---------- **/

    public void ramasseRessource(){
        List<Ressource> ressourcesDispo = modele.getUpdateJN().getRessources();
        for (int i = ressourcesDispo.size() - 1; i >= 0; i--) {
            Ressource r = ressourcesDispo.get(i);
            if (abs(r.getPositionY() - positionY) <= 30 && abs(r.getPositionX() - positionX)<= 30){
                addToRessource(r);
                ressourcesDispo.remove(i);
                System.out.println(inventaire);
            }
        }
    }

    /**
     * Transfère le contenu miné dans l'inventaire si proche du bâtiment.
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

    /** ---------- [Méthodes Publiques - Économie & Achat] ---------- **/

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