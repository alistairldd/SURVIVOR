package Modele;

import Controleur.ControleurSouris;
import static Modele.Constantes.*;

import java.util.ArrayList;
import static java.lang.Math.abs;

/**
 * Cœur interactif du jeu.
 * Gère l'état du joueur (PV, position, inventaire, arme) ainsi que ses actions
 * principales (se déplacer, ramasser, attaquer, construire).
 */
public class Joueur implements Localisable {

    //Stats du joueur
    // Argent du joueur
    private int pieces = 0;
    // Points de vie actuels
    private int hp;
    // Points de vie maximum
    private int hpMax = HP_JOUEUR;
    // Dégâts de base du joueur (indépendants de l'arme)
    private int attack;
    // Liste représentant le sac à dos du joueur contenant les ressources ramassées
    private static ArrayList<Ressource> inventaire;
    // Arme actuellement tenue en main
    private Arme armeEquipee;
    // Référence au modèle global pour interagir avec l'environnement (monstres, cycle jour/nuit)
    private final Modele modele;
    // (Non utilisé directement ici mais prévu pour l'architecture)
    private ControleurSouris controleurSouris;

    // Attaque
    // Chronomètre interne pour gérer la cadence de tir (cooldown)
    private long dernierTempsAttaque = 0;

    // Position
    // Coordonnée horizontale précise sur la carte globale
    private double positionX;
    // Coordonnée verticale précise sur la carte globale
    private double positionY;

    // Déplacement
    // Garde une trace du thread de déplacement en cours pour pouvoir l'interrompre si un nouveau clic est fait
    private static DeplaceJoueur threadActuel = null;

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
        // Boucle de triche/test : donne 10 ressources de chaque type au joueur dès le début
        for (int i = 0; i < 10; i++) {
            inventaire.add(new Ressource(0)); // 0: Bois
            inventaire.add(new Ressource(1)); // 1: Pierre
            inventaire.add(new Ressource(2)); // 2: Fer
            inventaire.add(new Ressource(3)); // 3: Or
        }
        // Équipe l'arme de départ
        armeEquipee = new Epee();
        // Lie le joueur à son monde
        this.modele = modele;
    }

    public int getPieces() {
        return pieces;
    }

    public void ajouterPieces(int montant) {
        this.pieces += montant;
    }

    public void retirerPieces(int montant) {
        this.pieces -= montant;
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

    // Getter pour le modèle (utile pour les vues qui ont besoin d'infos globales comme le cycle temporel)
    public Modele getModele() {
        return this.modele;
    }
}