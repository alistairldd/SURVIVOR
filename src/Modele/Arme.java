package Modele;

public abstract class Arme {

    private String nom;
    private int degats;
    private int portee;
    private int cadence;

    public Arme(String nom, int degats, int portee, int cadence) {
        this.nom = nom;
        this.degats = degats;
        this.portee = portee;
        this.cadence = cadence;
    }

    public int getDegats(){return degats;};
    public int getPortee(){return portee;};
    public int getCadence(){return cadence;};
    public String getNom(){return nom;};

}

