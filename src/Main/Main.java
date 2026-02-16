package Main;



import Modele.Modele;
import Vue.Vue;
import Controleur.Controleur;

public class Main {


    public static void main(String[] args) {
        Modele monModele = new Modele();
        Vue maVue = new Vue(monModele);
        new Controleur(monModele, maVue);
    }
}