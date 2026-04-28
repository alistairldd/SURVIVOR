package Modele;

import Modele.Armes.*;
import Modele.Armure.Armure;
import Modele.Armure.ArmureLegere;
import Modele.Armure.ArmureLourde;
import Modele.Items.*;

import java.util.ArrayList;
import java.util.Map;

/**
 * Orchestrateur de l'économie marchande.
 * Construit et actualise le catalogue de la boutique, valide les transactions,
 * et dispatche l'équipement acheté vers l'inventaire du joueur.
 */
public class GestionnaireShop {

    /** ---------- [Propriétés] ---------- **/

    private Modele modele;

    private ArrayList<Arme> armesDansShop;
    private ArrayList<Armure> armuresDansShop;
    private ArrayList<Item> objets;

    private ArrayList<Arme> armes;
    private ArrayList<Armure> armures;

    /** ---------- [Constructeurs] ---------- **/

    /**
     * Charge l'ensemble du catalogue commercial (Équipements, Sorts, Outils)
     * et prépare la file d'attente d'affichage dynamique pour le HUD.
     *
     * @param modele - Le système central pour impacter l'économie du joueur
     */
    public GestionnaireShop(Modele modele) {
        this.modele = modele;

        this.armesDansShop = new ArrayList<>();
        this.armuresDansShop = new ArrayList<>();
        this.objets = new ArrayList<>();
        this.armes = new ArrayList<>();
        this.armures = new ArrayList<>();

        armes.add(new EpeeBois());
        armes.add(new Epee());
        armes.add(new Hache());
        armes.add(new Lance());
        armes.add(new EpeeLourde());

        armures.add(new ArmureLegere());
        armures.add(new ArmureLourde());

        armesDansShop.add(armes.get(0));
        armesDansShop.add(armes.get(1));

        armuresDansShop.add(armures.get(0));
        armuresDansShop.add(armures.get(1));

        objets.add(new Pioche());
        objets.add(new PotionVie());
        objets.add(new PotionDegats());
        objets.add(new PotionVitesse());
        objets.add(new PotionVieGrande());
        objets.add(new SortFeu());
        objets.add(new SortTempete());
        objets.add(new Armageddon());
    }

    /** ---------- [Accesseurs / Getters & Setters] ---------- **/

    public ArrayList<Arme> getArmesDansShop() {
        return armesDansShop;
    }

    public ArrayList<Armure> getArmuresDansShop() {
        return armuresDansShop;
    }

    public ArrayList<Item> getObjets() {
        return objets;
    }

    public void setObjets(ArrayList<Item> objets) {
        this.objets = objets;
    }

    /** ---------- [Méthodes Publiques - Mise à jour du Shop] ---------- **/

    public void enleverArmeDuShop(Arme a) {
        armesDansShop.remove(a);
        armes.remove(a);
    }

    public void enleverArmureDuShop(Armure a) {
        armuresDansShop.remove(a);
        armures.remove(a);
    }

    public void enleverItemDuShop(Item i) {
        objets.remove(i);
    }

    /**
     * Fait glisser la file d'attente globale des armes pour réapprovisionner
     * les slots d'affichage actifs de la vitrine.
     */
    public void updateArmesDansShop() {
        if (!armesDansShop.isEmpty()){
            armesDansShop.remove(0);
        }
        if (!armes.isEmpty()){
            armesDansShop.add(armes.get(0));
        }
        if (armes.size() > 1){
            armesDansShop.add(armes.get(1));
        }
    }

    /**
     * Fait glisser la file d'attente globale des armures pour réapprovisionner
     * les slots d'affichage actifs de la vitrine.
     */
    public void updateArmuresDansShop() {
        if (!armuresDansShop.isEmpty()){
            armuresDansShop.remove(0);
        }
        if (!armures.isEmpty()){
            armuresDansShop.add(armures.get(0));
        }
        if (armures.size() > 1){
            armuresDansShop.add(armures.get(1));
        }
    }

    /** ---------- [Méthodes Publiques - Logique d'Achat] ---------- **/

    /**
     * Valide une transaction pour une arme en consommant les ressources requises,
     * puis la transfère dans l'inventaire tout en réactualisant le marché.
     */
    public void acheterArme(Arme a) {
        Joueur j = modele.getJoueur();
        Map<Integer, Integer> besoins = a.getRessourcesNecessaires();

        if (j.aAssezDeRessources(besoins)) {
            j.consommerListeRessources(besoins);

            if (j.getArmePasEquipee() != null){
                j.setArmeEquipee(a);
            }
            else {
                j.ajouterDeuxiemeArme(a);
            }

            enleverArmeDuShop(a);
            updateArmesDansShop();

        } else {
            System.out.println("Ressources insuffisantes pour fabriquer " + a.getNom());
        }
    }

    /**
     * Valide une transaction pour une armure en consommant les ressources requises,
     * puis l'équipe ou la stocke en secondaire selon l'état actuel du joueur.
     */
    public void acheterArmure(Armure a) {
        Joueur j = modele.getJoueur();
        Map<Integer, Integer> besoins = a.getRessourcesNecessaires();

        if (j.aAssezDeRessources(besoins)) {
            j.consommerListeRessources(besoins);

            if (j.getArmureSecondaire() != null){
                j.equiperArmure(a);
            }
            else {
                j.ajouterDeuxiemeArmure(a);
            }

            enleverArmureDuShop(a);
            updateArmuresDansShop();

        } else {
            System.out.println("Ressources insuffisantes pour l'armure !");
        }
    }

    /**
     * Valide une transaction monétaire pour un outil utilitaire ou consommable.
     * Si l'item est de type "Déblocage Unique" (ex: Pioche), on signale l'événement.
     */
    public void acheterItem(Item i) {
        Joueur j = modele.getJoueur();
        int besoins = i.getPrix();

        if (j.getPieces() >= besoins) {
            j.setPieces(j.getPieces() - besoins);

            if (i instanceof Pioche){
                enleverItemDuShop(i);
                modele.debloquerMinage();
            }

            if (i instanceof Item) {
                j.addToInventaire(i);
            }
        } else {
            System.out.println("Pas assez d'or pour cet objet !");
        }
    }
}