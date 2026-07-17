package it.uniroma3.siw.model;

public class RigaClassifica {
    private Squadra squadra;
    private int punti = 0;
    private int partiteGiocate = 0;
    private int vittorie = 0;
    private int pareggi = 0;
    private int sconfitte = 0;
    private int golFatti = 0;
    private int golSubiti = 0;

    public RigaClassifica(Squadra squadra) {
        this.squadra = squadra;
    }

    public int getDifferenzaReti() {
        return golFatti - golSubiti;
    }

    public void aggiungiVittoria(int golF, int golS) {
        this.partiteGiocate++;
        this.vittorie++;
        this.punti += 3;
        this.golFatti += golF;
        this.golSubiti += golS;
    }

    public void aggiungiPareggio(int gol) {
        this.partiteGiocate++;
        this.pareggi++;
        this.punti += 1;
        this.golFatti += gol;
        this.golSubiti += gol;
    }

    public void aggiungiSconfitta(int golF, int golS) {
        this.partiteGiocate++;
        this.sconfitte++;
        this.golFatti += golF;
        this.golSubiti += golS;
    }

    // --- GETTER FONDAMENTALI PER THYMELEAF ---
    public Squadra getSquadra() { return squadra; }
    public int getPunti() { return punti; }
    public int getPartiteGiocate() { return partiteGiocate; }
    public int getVittorie() { return vittorie; }
    public int getPareggi() { return pareggi; }
    public int getSconfitte() { return sconfitte; }
    public int getGolFatti() { return golFatti; }
    public int getGolSubiti() { return golSubiti; }
}