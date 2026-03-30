package Modele;

import java.awt.image.BufferedImage;

public abstract class Armure {
        // Nom affiché de l'armure
        private String nom;
        // Quantité de points de vie ajoutés au joueur lorsqu'il équipe cette armure
        private int bonusVie;
         // Image représentant l'arme (optionnel, peut être utilisé pour l'affichage)
         private BufferedImage image;

        public Armure(String nom, int bonusVie) {
            this.nom = nom;
            this.bonusVie = bonusVie;
            this.image = image;
        }

        public int getBonusVie() {
            return bonusVie;
        }

        public String getNom() {
            return nom;
        }


}
