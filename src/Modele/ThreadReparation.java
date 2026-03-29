package Modele;
import static Modele.Constantes.*;

/**
 * Thread autonome gérant la réparation continue d'un bâtiment par le joueur.
 * Vérifie en temps réel la distance et les points de vie restants.
 */
public class ThreadReparation extends Thread {

    private Joueur joueur;
    private Batiment batiment;



    public ThreadReparation(Joueur joueur, Batiment batiment) {
        this.joueur = joueur;
        this.batiment = batiment;
    }

    public Batiment getBatiment() { return batiment; }

    @Override
    public void run() {
        //System.out.println("Début de la réparation sur : " + batiment.getNom());

        // La boucle tourne tant que le thread n'est pas tué de l'extérieur
        while (!isInterrupted()) {

            // --- CONDITION 1 : Le bâtiment a-t-il besoin de soins ? ---
            if (batiment.getHp() >= batiment.getMaxHp()) {
                //System.out.println("Réparation terminée : Le bâtiment est à 100% !");
                batiment.setHp(batiment.getMaxHp()); // Sécurité pour verrouiller au max
                break; // Casse la boucle, fin du thread
            }

            // --- CONDITION 2 : Le joueur est-il toujours à portée ? ---
            double distance = Math.hypot(joueur.getX() - batiment.getX(), joueur.getY() - batiment.getY());
            if (distance > batiment.getHealingRange()) {
                System.out.println("Réparation annulée : Le joueur est trop loin !");
                break; // Casse la boucle, fin du thread
            }

            // --- APPLICATION DU SOIN ---
            batiment.setHp(batiment.getHp() + SOIN_BAT);

            // Sécurité anti-débordement (ne pas dépasser les HP max)
            if (batiment.getHp() > batiment.getMaxHp()) {
                batiment.setHp(batiment.getMaxHp());
            }

            //System.out.println("Soin appliqué... HP du bâtiment : " + batiment.getHp() + "/" + batiment.getMaxHp());

            // --- PAUSE DU THREAD ---
            try {
                Thread.sleep(REPARATION_DELAY);
            } catch (InterruptedException e) {
                // Si le thread est interrompu pendant son sommeil (ex: le joueur bouge, attaque, ou annule l'action)
                System.out.println("Réparation interrompue par une autre action du joueur.");
                break; // On quitte proprement la boucle
            }
        }
    }
}