package sn.dev.sponsorship_app;

import java.time.LocalDateTime;

public class Parrainage {

    private int id, electeur, candidat;
    private LocalDateTime dateParrainage;

    public Parrainage() {
    }

    public Parrainage(int id, int electeur, int candidat, LocalDateTime dateParrainage) {
        this.id = id;
        this.electeur = electeur;
        this.candidat = candidat;
        this.dateParrainage = dateParrainage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getElecteur() {
        return electeur;
    }

    public void setElecteur(int electeur) {
        this.electeur = electeur;
    }

    public int getCandidat() {
        return candidat;
    }

    public void setCandidat(int candidat) {
        this.candidat = candidat;
    }

    public LocalDateTime getDateParrainage() {
        return dateParrainage;
    }

    public void setDateParrainage(LocalDateTime dateParrainage) {
        this.dateParrainage = dateParrainage;
    }
}
