package Modele;

import Modele.Armes.*;
import Modele.Batiments.Batiment;
import Modele.Batiments.Mine;
import Modele.Batiments.Tower;
import Modele.Items.Armure;
import Modele.Items.Item;

import static Modele.Constantes.*;

import java.util.ArrayList;
import java.util.List;

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
    private static ArrayList<Ressource> inventaire;
    // Argent du joueur
    private int pieces;
    // Arme actuellement tenue en main
    private Arme armeEquipee;
    // Armure du joueur
    private Item armureEquipee;
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

    // Garde une trace du thread de soin en cours pour pouvoir l'interrompre si besoin
    private ThreadReparation threadReparation = null;

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
        attack = ATTAQUE_BASE;
        // Initialisation de l'inventaire
        inventaire = new ArrayList<>();
        this.aPioche = false;
        // Boucle de triche/test : donne 10 ressources de chaque type au joueur dès le début
        for (int i = 0; i < 10; i++) {
            inventaire.add(new Ressource(0)); // 0: Bois
            inventaire.add(new Ressource(1)); // 1: Pierre
            inventaire.add(new Ressource(2)); // 2: Fer
            inventaire.add(new Ressource(3)); // 3: Or
        }
        // triche/test : on donne 50 pieces au joueur au départ
        pieces = 50;
        // Équipe l'arme de départ
        armeEquipee = new Hache();
        // Pas d'armure au départ
        armureEquipee = null;
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

    public ArrayList<Ressource> getInventaire() {
        return inventaire;
    }

    public void addToInventaire(Ressource item) {
        inventaire.add(item);
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

    public Item getArmureEquipee(){return armureEquipee;}

    public void setArmureEquipee(Item armureEquipee){this.armureEquipee = armureEquipee;}

    public boolean hasPioche() {
        return aPioche;
    }

    public void setaPioche(boolean aPioche) {
        this.aPioche = aPioche;
    }

    public void soigner(int soin){
        this.hp += soin;
        if(this.hp > HP_JOUEUR){
            this.hp = HP_JOUEUR;
        }
    }

    public void ajouterAInventaire(Ressource r) {
        inventaire.add(r);
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
                addToInventaire(r);
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
        for (Ressource r : inventaire) {
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
     * Tente de placer une Tour Défensive à la position actuelle du joueur.
     * Vérifie les conditions temporelles (nuit uniquement) et les ressources (coûts).
     * @return True si la construction a réussi, False sinon.
     */
    public boolean construireTour() {
        /*
            Cette méthode permet de construire une Tower à la position du joueur.
            Coût : 4 Bois (0), 4 Pierre (1), 2 Fer (2), 1 Or (3)
            Condition : Uniquement pendant la nuit !
        */

        // 0. Vérification du cycle jour/nuit (Impossible de construire le jour)
        if (!modele.getLeCycleJourNuit().isDay()) {
            System.out.println("Impossible de construire une tour le jour ! Attendez la tombée de la nuit.");
            return false; // On annule la construction
        }

        for (Batiment b : modele.getGestionnaireBatiments().getBatiments()) {
            // Calcul de la distance euclidienne entre le joueur et le bâtiment inspecté
            double distance = Math.hypot(b.getX() - this.positionX, b.getY() - this.positionY);
            // La distance minimale requise est la somme du rayon du bâtiment existant et du rayon de la future tour
            double distanceMinimaleRequise = b.getRayonHitbox() + RAYON_HITBOX_TOUR;

            if (distance < distanceMinimaleRequise) {
                System.out.println("Construction annulée : Espace insuffisant, un bâtiment est trop proche !");
                return false;
            }
        }

        // 1. On compte ce qu'il y a dans l'inventaire en triant par type
        int nbBois = 0, nbPierre = 0, nbFer = 0, nbOr = 0;
        for (Ressource r : inventaire) {
            // Associe l'ID du type à son compteur
            switch (r.getType()) {
                case 0: nbBois++; break;   // Bois
                case 1: nbPierre++; break; // Pierre
                case 2: nbFer++; break;    // Fer
                case 3: nbOr++; break;     // Or
            }
        }

        // 2. On vérifie si on a les quantités suffisantes pour le "prix" de la tour
        if (nbBois >= 4 && nbPierre >= 4 && nbFer >= 2 && nbOr >= 1) {

            // 3. On consomme (retire) les ressources de l'inventaire via la méthode utilitaire
            consommerRessource(0, 4); // Retire 4 Bois
            consommerRessource(1, 4); // Retire 4 Pierre
            consommerRessource(2, 2); // Retire 2 Fer
            consommerRessource(3, 1); // Retire 1 Or

            // 4. On crée la tour exactement sous les pieds du joueur (conversion double -> int requise pour la grille)
            Tower nouvelleTour = new Tower((int) positionX, (int) positionY, modele.getGestionnaireBatiments());

            // La tour a directement tous ses PV max grâce à son constructeur

            // 5. On demande au Modèle d'ajouter ce nouveau bâtiment à la liste globale (Map)
            modele.getGestionnaireBatiments().getBatiments().add(nouvelleTour);

            System.out.println("Tour construite avec succès en (" + (int)positionX + ", " + (int)positionY + ") !");
            System.out.println("Inventaire restant : " + inventaire.size() + " objets.");
            // Confirme la réussite de l'action
            return true;

        } else {
            // Echec par manque de fonds
            System.out.println("Ressources insuffisantes pour construire une tour !");
            System.out.println("Il te faut : 4 Bois, 4 Pierre, 2 Fer, 1 Or.");
            return false;
        }
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
        for (int i = inventaire.size() - 1; i >= 0; i--) {
            // Si l'objet correspond au type cherché
            if (inventaire.get(i).getType() == type) {
                // On le détruit
                inventaire.remove(i);
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
                        this.inventaire.addAll(mine.getRessources());
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
        setHpMax(getHpMax()+nouvelleArmure.getBonusVie());
    }

    public boolean aAssezDeRessources(List<String> besoins) {
        // 1. Comptage des stocks actuels
        int bois = 0, pierre = 0, fer = 0, or = 0;
        for (Ressource r : inventaire) {
            switch (r.getType()) {
                case 0 -> bois++;
                case 1 -> pierre++;
                case 2 -> fer++;
                case 3 -> or++;
            }
        }

        // 2. Vérification des besoins
        for (String besoin : besoins) {
            // On sépare par le caractère ":"
            String[] parties = besoin.split(":");
            if (parties.length < 2) continue;

            String nomRessource = parties[0].trim().toLowerCase();
            int quantiteRequise;

            try {
                quantiteRequise = Integer.parseInt(parties[1].trim());
            } catch (NumberFormatException e) {
                System.err.println("Erreur format prix sur : " + besoin);
                return false;
            }

            // 3. Test de satisfaction
            switch (nomRessource) {
                case "bois"   -> { if (bois < quantiteRequise) return false; }
                case "pierre" -> { if (pierre < quantiteRequise) return false; }
                case "fer"    -> { if (fer < quantiteRequise) return false; }
                case "or"     -> { if (or < quantiteRequise) return false; }
            }
        }
        return true;
    }

    public void consommerListeRessources(List<String> besoins) {
        for (String besoin : besoins) {
            String[] parties = besoin.split(":");
            if (parties.length < 2) continue;

            String nom = parties[0].trim().toLowerCase();
            int quantite = Integer.parseInt(parties[1].trim());

            int type = -1;
            switch (nom) {
                case "bois"   -> type = 0;
                case "pierre" -> type = 1;
                case "fer"    -> type = 2;
                case "or"     -> type = 3;
            }
            if (type != -1) this.consommerRessource(type, quantite);
        }
    }
}