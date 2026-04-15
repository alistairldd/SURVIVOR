package Modele;

import Modele.Armes.*;
import Modele.Batiments.Batiment;
import Modele.Batiments.Mine;
import Modele.Batiments.Tower;
import Modele.Armure.Armure;
import Modele.Armure.ArmureLegere;
import Modele.Armure.ArmureLourde;
import Modele.Items.Item;
import Modele.Items.PotionDegats;
import Modele.Items.PotionVie;
import Modele.Items.PotionVitesse;

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

    //Stats du joueur

    // Points de vie actuels
    private int hp;
    // Points de vie maximum
    private int hpMax = HP_JOUEUR;
    // Dégâts de base du joueur (indépendants de l'arme)
    private int attack;
    // Liste représentant le sac à dos du joueur contenant les ressources ramassées
    private ArrayList<Item> inventaire;
    // Liste des ressources ramassées
    private ArrayList<Ressource> ressources = new ArrayList<>();
    // Argent du joueur
    private int pieces;
    // Arme actuellement tenue en main
    private Arme armeEquipee;
    // Arme non équipée mais possédée par le joueur
    private Arme armePasEquipee;
    // Armure du joueur
    private Armure armurePrincipale;
    // Armure secondaire
    private Armure armureSecondaire;
    // Booléen pour déterminer si le joueur a la pioche ou non
    private boolean aPioche;
    // Référence au modèle global pour interagir avec l'environnement (monstres, cycle jour/nuit)
    private final Modele modele;
    // Attaque
    // Chronomètre interne pour gérer la cadence de tir (cooldown)
    private long dernierTempsAttaque = 0;

    private boolean enReparation = false;
    private Batiment batimentEnReparation = null;

    // Position
    // Coordonnée horizontale précise sur la carte globale
    private double positionX;
    // Coordonnée verticale précise sur la carte globale
    private double positionY;

    // Déplacement
    // Garde une trace du thread de déplacement en cours pour pouvoir l'interrompre si un nouveau clic est fait
    private static DeplaceJoueur threadActuel = null;
    private int vitesse;
    // Garde une trace du thread de soin en cours pour pouvoir l'interrompre si besoin
    private ThreadReparation threadReparation = null;

    private int reduction;

    /**
     * Constructeur principal du joueur.
     * Initialise ses statistiques, le place au centre de la carte et lui donne un inventaire de départ.
     * @param modele Le modèle principal du jeu.
     */
    // Constructeur de la classe Joueur, il initialise les données du joueur.
    public Joueur(Modele modele) {
        // on initialise la position au centre exact de la carte
        positionX = LARGEUR_MAP /2;
        positionY = HAUTEUR_MAP /2;
        // Statistiques de base
        hp = HP_JOUEUR;
        attack = 0;
        // Initialisation de l'inventaire
        inventaire = new ArrayList<Item>();
        this.aPioche = false;

        // Boucle de triche/test : donne 10 ressources de chaque type au joueur dès le début
        for (int i = 0; i < 10; i++) {
            ressources.add(new Ressource(0)); // 0: Bois
            ressources.add(new Ressource(1)); // 1: Pierre
            ressources.add(new Ressource(2)); // 2: Fer
            ressources.add(new Ressource(3)); // 3: Or
        }
        // triche/test : on donne 50 pieces au joueur au départ
        pieces = 10000;
        // Équipe l'arme de départ
        armeEquipee = new Baton();
        // Arme non équipée existe pas encore
        armePasEquipee = null;
        // Pas d'armure au départ
        armurePrincipale = null;
        // armureSecondaire
        armureSecondaire = null;
        // Vitesse de déplacement
        vitesse = 0;

        // Réduction de dégâts
        reduction = 0;
        // Lie le joueur à son monde
        this.modele = modele;
    }

    public int getPieces() {
        return pieces;
    }

    public void addPieces(int montant) {
        pieces += montant;
    }

    public void acheter(int montant){
        this.consommerRessource(3, montant);
    }

    public void setPieces(int pieces) {
        this.pieces = pieces;
    }

    public int getHpMax() {return hpMax;}
    public void setHpMax(int hpMax) {this.hpMax = hpMax;}

    public int getHp() {return hp;}

    @Override
    public int getMaxHp() {
        return HP_JOUEUR;
    }

    @Override
    public String getNom() {
        return "Joueur";
    }

    @Override
    public void setHp(int hp) {this.hp = hp;}

    public int getAttack() {return this.attack;}

    public void setAttack(int attack) {this.attack = attack;}

    public ArrayList<Item> getInventaire() {
        return inventaire;
    }

    public ArrayList<Ressource> getRessources(){
        return ressources;
    }

    public void addToInventaire(Item item) {
        inventaire.add(item);
    }

    public void removeFromInventaire(Item item) {
        inventaire.remove(item);
    }

    public LinkedHashMap<Item, Integer> getInventaireGroupé() {
        LinkedHashMap<Item, Integer> map = new LinkedHashMap<>();
        for (Item item : inventaire) {
            map.merge(item, 1, Integer::sum); // nécessite que Item implémente equals() + hashCode() sur le nom
        }
        return map;
    }

    private void addToRessource(Ressource r) {
        this.ressources.add(r);
    }

    // L'utilisation de 'synchronized' évite les conflits si le thread de déplacement et la boucle principale y accèdent en même temps
    // Getter pour la position X du joueur
    public synchronized double getX() {return positionX;}
    // Getter pour la position Y du joueur
    public synchronized double getY() {return positionY;}
    // Setter pour la position X du joueur
    public synchronized void setPositionX(double positionX) {this.positionX = positionX;}
    // Setter pour la position Y du joueur
    public synchronized void setPositionY(double positionY) {this.positionY = positionY;}
    // Getter pour l'arme équipée du joueur
    public Arme getArmeEquipee() {return armeEquipee;}
    // Setter pour l'arme équipée du joueur
    public void setArmeEquipee(Arme armeEquipee) {this.armeEquipee = armeEquipee;}
    // Getter pour l'arme non équipée du joueur
    public Arme getArmePasEquipee() {return armePasEquipee;}
    // Setter pour l'arme non équipée du joueur
    public void setArmePasEquipee(Arme armePasEquipee) {this.armePasEquipee = armePasEquipee;}
    // Méthode pour échanger les armes équipée et non équipée
    public void switchArmes() {
        Arme temp = armeEquipee;
        armeEquipee = armePasEquipee;
        armePasEquipee = temp;
    }

    // Getter pour l'armure équipée du joueur
    public Armure getArmurePrincipale(){return armurePrincipale;}

    // Setter pour l'armure équipée du joueur
    public void setArmureEquipee(Armure armureEquipee){
        this.armurePrincipale = armureEquipee;
        setReductionDegats(armureEquipee.getReduction());
    }

    public void setReductionDegats(int reduction) {
        this.reduction = reduction;
    }

    public int getReductionDegats() {
        return this.reduction;
    }

    // Getter pour l'armure secondaire du joueur
    public Armure getArmureSecondaire(){return armureSecondaire;}

    // Setter pour l'armure secondaire du joueur
    public void setArmureSecondaire(Armure armureSecondaire){this.armureSecondaire = armureSecondaire;}

    // Méthode pour échanger les armures principale et secondaire
    public void switchArmures() {
        Armure temp = armurePrincipale;
        armurePrincipale = armureSecondaire;
        armureSecondaire = temp;
    }

    // Getter pour l'armure la pioche
    public boolean hasPioche() {
        return aPioche;
    }

    // Setter pour la pioche
    public void setaPioche(boolean aPioche) {
        this.aPioche = aPioche;
    }

    public void utilierConsommable(Item item) {
        if (item instanceof PotionVie) {
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
    }

    // Méthode pour augmenter (ou baisser) la vitesse du joueur
    private void incrVitesse(int i) {vitesse += i;}

    // Getter pour la vitesse du joueur
    public int getVitesse() {return vitesse;}

    public void soigner(int soin){
        this.hp += soin;
        if(this.hp > HP_JOUEUR){
            this.hp = HP_JOUEUR;
        }
    }
    // Méthode pour ajouter une ressource à l'inventaire du joueur
    public void ajouterARessources(Ressource r) {
        ressources.add(r);
    }

    public ThreadReparation getThreadReparation() { return threadReparation; }
    /**
     * Met à jour la position X tout en empêchant le joueur de sortir des limites de la carte.
     * @param x La nouvelle coordonnée X voulue.
     */
    // Méthode pour déplacer le joueur en x,
    // elle prend en paramètre le déplacement en x,
    // elle met à jour la position du joueur en x.
    public synchronized void deplaceX(double x) {
        // On vérifie que le déplacement en x est dans les limites de la carte (en tenant compte de la taille du sprite), sinon on le met à la limite.
        if (x >= 10+J_TAILLE/2 && x <= LARGEUR_MAP) {
            // Mouvement valide
            setPositionX(x);
        }
        else if (x <= 10+J_TAILLE/2) {
            // Bloqué au bord gauche
            setPositionX(10+J_TAILLE/2);
        }
        else {
            // Bloqué au bord droit
            setPositionX(LARGEUR_MAP);
        }
    }

    /**
     * Met à jour la position Y tout en empêchant le joueur de sortir des limites de la carte.
     * @param y La nouvelle coordonnée Y voulue.
     */
    // Méthode pour déplacer le joueur en y,
    // elle prend en paramètre le déplacement en y,
    // elle met à jour la position du joueur en y.
    public synchronized void deplaceY(double y) {
        // On vérifie que le déplacement en y est dans les limites de la carte, sinon on le met à la limite.
        if (y >= 10+J_TAILLE/2 && y <= HAUTEUR_MAP) {
            // Mouvement valide
            setPositionY(y);
        }
        else if (y <= 10+J_TAILLE/2) {
            // Bloqué au bord supérieur
            setPositionY(10+J_TAILLE/2);
        }
        else {
            // Bloqué au bord inférieur
            setPositionY(HAUTEUR_MAP);
        }
    }

    /**
     * Tente de ramasser toutes les ressources situées dans le rayon d'interaction du joueur.
     */
    // quand le joueur est sur la ressource et qu'il appuie sur e, le joueur ajoute à son inventaire la ressource.
    public void ramasseRessource(){
        ArrayList<Ressource> ressourcesDispo = modele.getUpdateJN().getRessources();
        // Parcourt la liste à l'envers pour éviter les bugs d'index lors de la suppression d'un élément
        for (int i = ressourcesDispo.size() - 1; i >= 0; i--) {
            Ressource r = ressourcesDispo.get(i);
            // Vérifie si la ressource est à moins de 30 pixels de distance en X et en Y (Hitbox carrée)
            if (abs(r.getPositionY() - positionY) <= 30 && abs(r.getPositionX() - positionX)<= 30){// à modifier à terme (zone d'interaction du joueur)
                // Ajoute au sac à dos
                addToRessource(r);
                // Retire du sol
                ressourcesDispo.remove(i);
                // Affiche l'inventaire complet dans la console pour debug
                System.out.println(inventaire);
            }
        }
    }




    /**
     * Vérifie si le joueur a le droit d'attaquer en fonction du temps de recharge (cooldown) de son arme.
     * @return True si l'arme est prête, False si elle est encore en cooldown.
     */
    public boolean peutAttaquer(){
        /*
            Cette méthode vérifie si le joueur peut attaquer,
            c'est à dire si le temps écoulé depuis la dernière attaque est supérieur ou égal au cooldown de l'arme équipée.
         */
        // Récupère l'heure exacte du clic
        long tempsActuel = System.currentTimeMillis();
        // Récupère la cadence requise par l'arme
        long cooldown = armeEquipee.getCadence(); // Convertir le cooldown en millisecondes
        // Si la différence est plus grande que la cadence, le joueur est autorisé à frapper
        return (tempsActuel - dernierTempsAttaque) >= cooldown;
    }

    /**
     * Réinitialise le chronomètre d'attaque (appelé juste après avoir porté un coup).
     */
    public void setDernierTempsAttaque() {
        /*
            Cette méthode met à jour le temps de la dernière attaque du joueur en le définissant à l'heure actuelle.
         */
        this.dernierTempsAttaque = System.currentTimeMillis();
    }

    /**
     * Enregistre et lance un nouveau thread de déplacement. Si un déplacement était déjà en cours, il l'annule.
     * @param thread Le nouveau thread contenant la destination.
     */
    public void setThreadActuel(DeplaceJoueur thread) {
        // Si un thread tourne déjà, on l'arrête
        // utilisé pour le déplacement du joueur, pour éviter que plusieurs threads de déplacement soient actifs en même temps,
        // ce qui pourrait causer des problèmes de synchronisation et de performance.
        if (threadActuel != null && threadActuel.isAlive()) {
            // Interrompt proprement la boucle "while" du thread précédent
            threadActuel.interrupt();
        }
        // Enregistre le nouveau
        threadActuel = thread;
    }

    // ==========================================================
    // --- SYSTÈME DE CONSTRUCTION ---
    // ==========================================================

    /**
     * Calcule le nombre maximum de tours que le joueur peut construire avec son inventaire actuel.
     * @return Le nombre de tours fabricables.
     */
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
     * Supprime un nombre exact d'un type de ressource spécifique de l'inventaire.
     * @param type L'ID de la ressource à détruire (0: Bois, etc).
     * @param quantiteARetirer Le nombre d'instances à retirer.
     */
    private void consommerRessource(int type, int quantiteARetirer) {
        /*
            Méthode utilitaire qui parcourt l'inventaire à l'envers pour retirer
            un nombre précis d'une ressource donnée sans faire bugger la liste.
        */
        // Compteur de ressources déjà supprimées
        int supprimes = 0;
        // Parcours inversé indispensable quand on utilise un .remove() sur une liste dynamique
        for (int i = ressources.size() - 1; i >= 0; i--) {
            // Si l'objet correspond au type cherché
            if (ressources.get(i).getType() == type) {
                // On le détruit
                ressources.remove(i);
                // On incrémente le compteur
                supprimes++;
                // Dès qu'on a atteint la quantité exigée, on arrête de boucler
                if (supprimes == quantiteARetirer) {
                    break; // On a retiré la quantité voulue, on s'arrête
                }
            }
        }
    }

    // ==========================================================
    // --- INTERACTION AVEC LA MINE ---
    // ==========================================================

    /**
     * Permet au joueur de récolter les minerais stockés dans la mine.
     * Conditions : Il doit faire jour et le joueur doit être à proximité.
     */
    public void recolterMine() {
        // 1. Vérification temporelle
        if (!modele.getLeCycleJourNuit().isDay()) {
            System.out.println("Récolte impossible : c'est la nuit !");
            return;
        }

        // 2. Recherche de la mine et vérification de la distance
        for (Batiment b : modele.getGestionnaireBatiments().getBatiments()) {
            if (b instanceof Mine) {
                Mine mine = (Mine) b;

                // Calcul de la distance entre le joueur et le centre de la mine
                double distance = Math.hypot(mine.getX() - this.positionX, mine.getY() - this.positionY);

                // Rayon d'interaction (Rayon de la mine + marge de manœuvre de 50 pixels)
                if (distance <= mine.getRange() + 50) {

                    int nbRessources = mine.getRessources().size();

                    if (nbRessources > 0) {
                        // Transfert de la liste de la mine vers l'inventaire du joueur
                        this.ressources.addAll(mine.getRessources());
                        // Vidage de la mine
                        mine.getRessources().clear();
                        System.out.println("Succès : " + nbRessources + " minerais récoltés !");
                    } else {
                        System.out.println("La mine est vide pour le moment.");
                    }
                    return; // On a trouvé la mine, on stoppe la recherche
                } else {
                    System.out.println("Échec : Tu es trop loin de la mine.");
                }
            }
        }
    }

    /**
     * Demande au modèle s'il y a un bâtiment à soigner à proximité.
     * Si oui, lance le thread autonome de réparation.
     */
    public void lancerReparation() {

        // Sécurité 0 : Évite le NullPointerException si le Thread démarre avant le chargement complet du jeu
        if (modele == null || modele.getGestionnaireBatiments() == null) return;

        // Sécurité 1 : On ne peut initier une réparation que le jour
        if (!modele.getUpdateJN().isDay()) return;

        // Sécurité 2 : On ne lance pas une nouvelle réparation si on est déjà en train de le faire
        if (enReparation) return;

        Batiment batimentAReparer = null;
        double distMin = REPARATION_RANGE;

        // On cherche le bâtiment endommagé le plus proche dans le rayon de réparation
        for (Batiment b : modele.getGestionnaireBatiments().getBatiments()) {
            if (b.getHp() < b.getMaxHp()) {
                double diffX = this.positionX - b.getX();
                double diffY = this.positionY - b.getY();
                double dist = Math.hypot(diffX, diffY);

                if (dist <= distMin) {
                    distMin = dist;
                    batimentAReparer = b;
                }
            }
        }

        // Si on a trouvé un bâtiment à réparer à portée
        if (batimentAReparer != null) {
            enReparation = true;
            Batiment cible = batimentAReparer;
            this.batimentEnReparation = cible;

            Thread threadReparation = new Thread(() -> {
                try {
                    while (cible.getHp() < cible.getMaxHp()) {
                        // Vérification 1 : Est-ce qu'il fait toujours jour ?
                        if (!modele.getUpdateJN().isDay()) {
                            break; // La nuit tombe, on arrête le marteau
                        }

                        // Vérification 2 : Le joueur est-il toujours à portée ?
                        double dX = this.positionX - cible.getX();
                        double dY = this.positionY - cible.getY();
                        if (Math.hypot(dX, dY) > REPARATION_RANGE) {
                            break; // Le joueur s'est éloigné, on arrête de réparer
                        }

                        // Réparation (Rend des PV). Ajuste le "+ 1" si tu veux que ça répare plus vite
                        cible.setHp(cible.getHp() + 1);

                        // Dès qu'il a un peu de vie, il peut attaquer
                        if (!cible.isFonctionnel()) cible.setFonctionnel(true);

                        // Il redevient attaquable
                        if (!cible.isAttaquable()) cible.setAttaquable(true);

                        // Vérification 3 : Le bâtiment est-il réparé à 100% ?
                        if (cible.getHp() >= cible.getMaxHp()) {
                            cible.setHp(cible.getMaxHp());
                            break;
                        }

                        // Pause entre chaque "coup de marteau" (Vitesse de réparation)
                        Thread.sleep(100);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    enReparation = false; // Quoi qu'il arrive, on libère le joueur pour une autre réparation
                    batimentEnReparation = null;
                }
            });
            threadReparation.start();
        }
    }

    /**
     * Arrête de force tout processus de réparation en cours.
     * Utilisé lors du Game Over pour éviter les animations fantômes au redémarrage.
     */
    public void stopperReparation() {
        this.enReparation = false;
        this.batimentEnReparation = null;
        // Si tu utilisais un threadReparation stocké, on l'interrompt ici.
    }

    // Getter pour le modèle (utile pour les vues qui ont besoin d'infos globales comme le cycle temporel)
    public Modele getModele() {
        return this.modele;
    }
    public Batiment getBatimentEnReparation() {
        return batimentEnReparation;
    }

    public void equiperArmure(Armure nouvelleArmure) {
        this.armurePrincipale = nouvelleArmure;
        setReductionDegats(nouvelleArmure.getReduction());
    }

    /**
     * Vérifie si l'inventaire contient les quantités requises via un Dictionnaire.
     */
    public boolean aAssezDeRessources(Map<Integer, Integer> couts) {
        // 1. On compte les stocks actuels rapidement dans un tableau
        int[] stocks = new int[4];
        for (Ressource r : ressources) {
            if (r.getType() >= 0 && r.getType() < 4) {
                stocks[r.getType()]++;
            }
        }

        // 2. On compare avec le dictionnaire de prix
        for (Map.Entry<Integer, Integer> cout : couts.entrySet()) {
            int typeRessource = cout.getKey();
            int quantiteRequise = cout.getValue();

            if (stocks[typeRessource] < quantiteRequise) {
                return false; // Manque de fonds (Bearish)
            }
        }
        return true; // Fonds suffisants (Bullish)
    }

    /**
     * Consomme les ressources en lisant le Dictionnaire.
     */
    public void consommerListeRessources(Map<Integer, Integer> couts) {
        for (Map.Entry<Integer, Integer> cout : couts.entrySet()) {
            int typeRessource = cout.getKey();
            int quantiteRequise = cout.getValue();

            this.consommerRessource(typeRessource, quantiteRequise);
        }
    }
}