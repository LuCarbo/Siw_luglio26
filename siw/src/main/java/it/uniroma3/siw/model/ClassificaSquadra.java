package it.uniroma3.siw.model;

public class ClassificaSquadra implements Comparable<ClassificaSquadra> {

    private Squadra squadra;
    private int punti;

    public ClassificaSquadra() {
        this.punti = 0;
    }

    public ClassificaSquadra(Squadra squadra) {
        this.squadra = squadra;
        this.punti = 0;
    }

    public Squadra getSquadra() {
        return squadra;
    }

    public void setSquadra(Squadra squadra) {
        this.squadra = squadra;
    }

    public int getPunti() {
        return punti;
    }

    public void setPunti(int punti) {
        this.punti = punti;
    }

    public void aggiungiPunti(int puntiDaAggiungere) {
        this.punti += puntiDaAggiungere;
    }

    @Override
    public int compareTo(ClassificaSquadra altra) {
        return Integer.compare(altra.getPunti(), this.punti);
    }
}