package sn.dev.sponsorship_app;

public class Etudiant {
    int id;
    String nom, prenom, matiere;

    public Etudiant(int id, String nom, String prenom, String matiere) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.matiere = matiere;
    }
    public Etudiant() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getMatiere() {
        return matiere;
    }

    public void setMatiere(String matiere) {
        this.matiere = matiere;
    }
}
