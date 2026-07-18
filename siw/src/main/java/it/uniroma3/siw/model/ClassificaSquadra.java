package it.uniroma3.siw.model;

public class ClassificaSquadra implements Comparable<ClassificaSquadra> {

    private Squadra squadra;
    private int punti;

    public ClassificaSquadra(Squadra squadra) {
        this.squadra = squadra;
        this.punti = 0; // Si parte sempre da 0 punti
    }

    // Unico metodo che ci serve per ora per aggiornare la classifica
    public void aggiungiPunti(int puntiDaAggiungere) {
        this.punti += puntiDaAggiungere;
    }

    // Ordina la lista confrontando SOLO i punti (dal più grande al più piccolo)
    @Override
    public int compareTo(ClassificaSquadra altra) {
        return Integer.compare(altra.getPunti(), this.punti);
    }

    // --- GETTER E SETTER ---
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
}