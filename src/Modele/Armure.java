package Modele;

import java.awt.*;
import java.util.List;
import java.awt.image.BufferedImage;

public abstract class Armure {
        // Nom affiché de l'armure
        private String nom;
        // Quantité de points de vie ajoutés au joueur lorsqu'il équipe cette armure
        private int bonusVie;
        // Image représentant l'arme (optionnel, peut être utilisé pour l'affichage)
        private Image image;
        // Liste des ressources nécessaires pour acheter / fabriquer l'armure
        private List ressourcesNecessaires;

        public Armure(String nom, int bonusVie, Image image, List ressourcesNecessaires) {
            this.nom = nom;
            this.bonusVie = bonusVie;
            this.image = image;
            this.ressourcesNecessaires = ressourcesNecessaires;
        }

        // Retourne le bonus de vie de l'armure
        public int getBonusVie() {
            return bonusVie;
        }

        // Retourne le nom de l'armure
        public String getNom() {
            return nom;
        }

        // Retourne l'image de l'armure
        public Image getImage() {return image;}

        // Retourne la liste des ressources nécessaires pour acheter / fabriquer l'armure
        public List<String> getRessourcesNecessaires() {return ressourcesNecessaires;}


}
