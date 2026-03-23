package Modele;

import java.util.ArrayList;
import java.util.List;

/*
 * La clase générale du modèle, elle contient les classes de données et les méthodes pour manipuler ces données.
 * Elle est utilisée pour stocker les données de l'application et pour effectuer des opérations sur ces données.
 * On va initialiser les threads du modèle à partir d'ici
 *
 */
/**
 * Cœur du système (Architecture MVC).
 * Le Modèle orchestre toutes les données du jeu. Il instancie le Joueur, la Carte,
 * lance les threads autonomes (Cycle temporel, Intelligence des bâtiments) et centralise
 * la logique des combats complexes (calcul des cônes d'attaque).
 */
public class Modele {

    private boolean shopOuvert = false;
    // L'entité contrôlée par l'utilisateur
    private Joueur joueur;
    private GestionnaireBatiments batiments;
    // (Variables potentiellement inutilisées ici, prévues pour l'architecture)
    private Ressource ressource;
    private Batiment batiment;

    // Le gestionnaire autonome du temps (Thread)
    private CycleJourNuit leCycleJourNuit;
    private GestionnaireShop gestionnaireShop;

    // Entité actuellement survolée par la souris (pour affichage d'infos)
    private Localisable cibleAffichage;

    private UpdateJN updateJN;

    // Constructeur de la classe Modele, il initialise les données du modèle.
    public Modele() {

        // Instancie le joueur et lui donne la référence à ce Modèle
        this.joueur = new Joueur(this);

        // Initialisation du jour et de la nuit
        // (Démarre automatiquement son propre thread interne)
        this.updateJN = new UpdateJN(this);
        leCycleJourNuit = new CycleJourNuit(updateJN);

        this.batiments = new GestionnaireBatiments(this);
        this.gestionnaireShop = new GestionnaireShop(this);
        this.cibleAffichage = joueur; // valeur initiale

    }

    /*---- GETTERS ET SETTERS ---- */

    // Getter du joueur
    public Joueur getJoueur() {
        return joueur;
    }

    // Getter du cycle jour/nuit
    public CycleJourNuit getLeCycleJourNuit() {
        return leCycleJourNuit;
    }

    // Getter UpdateJN
    public UpdateJN getUpdateJN() {
        return updateJN;}
    // Getter du Shop
    public boolean isShopOuvert() { return shopOuvert; }
    public void toggleShop() { this.shopOuvert = !this.shopOuvert; }
    public GestionnaireShop getGestionnaireShop() { return gestionnaireShop; }



    public GestionnaireBatiments getGestionnaireBatiments() {
        return batiments;
    }

    /**
     * Fonction mathématique utilitaire (équivalente à la fonction map() de Processing/Arduino).
     * Re-projette un nombre d'un intervalle de référence vers un nouvel intervalle.
     * Utilisé notamment par la Vue pour calculer les coordonnées sur la Minimap.
     */
    public int map(int debut, int fin, int valDebut, int valFin, int val){
        // Produit en croix pour adapter l'échelle
        return (val - debut) * (valFin - valDebut) / (fin - debut) + valDebut;
    }

    public Localisable getCibleAffichage() {
        return cibleAffichage;
    }

    public void verifierSurvol(double sourisMondeX, double sourisMondeY) {
         /*
            Cette méthode est appelée à chaque mouvement de la souris pour vérifier si le curseur survole une entité du jeu (joueur, bâtiment ou monstre).
            Elle prend en paramètre les coordonnées de la souris dans le monde du jeu, elle parcourt la liste des entités et retourne celle qui est la plus proche du curseur.
         */
        List<Localisable> ciblesPotentielles = new ArrayList<>();
        ciblesPotentielles.add(joueur);
        //ciblesPotentielles.addAll(gestionnaireBatiments.getBatiments());
        ciblesPotentielles.addAll(updateJN.getMonstres());
        // Parcourt toutes les entités potentiellement survolables et calcule la distance entre chacune d'elles et la position de la souris
        for (Localisable cible : ciblesPotentielles) {
            double d = Math.hypot(sourisMondeX - cible.getX(), sourisMondeY - cible.getY());
            if (d < 20) {
                cibleAffichage = cible;
            }
        }

    }
    /**
     * Gère la logique complexe de l'attaque du joueur (calcul de collisions en cône).
     * @param angleAttaque L'angle en radians vers lequel le joueur a cliqué (calculé dans le contrôleur).
     */
    // Attaque du joueur
    public void joueurAttaque(double angleAttaque) {
         /*
            Cette méthode permet au joueur d'attaquer les monstres qui sont à proximité.
            Elle prend en paramètre les coordonnées de la souris, elle calcule l'angle entre le joueur et la souris,
            puis elle parcourt la liste des monstres du modèle et applique les dégâts à ceux qui sont dans le cône d'attaque de l'arme équipée.
         */

        // Récupérer les caractéristiques de l'arme équipée (pour savoir jusqu'où et à quelle largeur on frappe)
        double portee = joueur.getArmeEquipee().getPortee();
        double angle = joueur.getArmeEquipee().getAngle();
        // Récupérer la position centrale du joueur d'où part l'attaque
        double positionX = this.joueur.getX();
        double positionY = this.joueur.getY();


        // Récupérer la liste complète des cibles potentielles
        ArrayList<Monstre> monstres = updateJN.getMonstres();


        // Parcourir la liste des monstres du modèle et appliquer les dégâts à ceux qui sont dans le cône d'attaque de l'arme équipée
        for (Monstre m : monstres) {

            // Calculer la distance euclidienne directe (hypoténuse) entre le joueur et ce monstre
            double distance = Math.hypot(m.getX() - positionX, m.getY() - positionY);

            // 1ère vérification : Le monstre est-il suffisamment proche ?
            if (distance <= portee) {
                // Calculer l'angle absolu entre le joueur et la position exacte du monstre
                double angleMonstre = Math.atan2(m.getY() - positionY, m.getX() - positionX);

                // Calculer la différence entre la direction visée par le joueur et la direction réelle du monstre
                double diffAngle = angleMonstre - angleAttaque;

                // Normaliser cette différence angulaire pour qu'elle reste toujours comprise entre -PI et PI
                // (Évite les bugs si l'angle fait plus d'un tour complet, ex: passage de 359° à 1°)
                diffAngle = Math.atan2(Math.sin(diffAngle), Math.cos(diffAngle));


                // 2ème vérification : La différence d'angle est-elle plus petite que la moitié de la largeur du cône de l'arme ?
                if (Math.abs(diffAngle) <= angle / 2) {
                    // Les deux conditions sont remplies : le monstre est touché !
                    m.perdreHp( joueur.getAttack()); // On applique les dégâts
                    // Affiche l'information dans la console pour debug
                    System.out.println("Monstre touché ! " + m.getID() + "  HP restant : " + m.getHp());
                }
            }
        }
    }

    public Monstre batTrouverMonstre(Batiment b) {
        ArrayList<Monstre> monstres = updateJN.getMonstres();
        for (Monstre m : monstres) {
            // Calcule la distance directe (hypoténuse) entre le centre de la tour et le monstre
            double distance = Math.hypot(m.getX() - b.getX(), m.getY() - b.getY());

            // Si le monstre entre dans le périmètre de défense de la tour
            if (distance <= b.getRange()) {
                return m;
            }
        }
        return null;
    }

}