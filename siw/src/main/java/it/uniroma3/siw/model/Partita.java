package it.uniroma3.siw.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import it.uniroma3.siw.model.enums.StatoPartita;

@Entity
@Table(name = "partite")
public class Partita {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "data_ora", nullable = false)
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dataOra;

    private String luogo;

    @Column(name = "goals_home")
    private Integer goalsHome = 0;

    @Column(name = "goals_away")
    private Integer goalsAway = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoPartita stato = StatoPartita.SCHEDULED;

    // Relazione con Torneo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "torneo_id", nullable = false)
    private Torneo torneo;

    // Relazione con Squadra in casa
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "squadra_casa_id", nullable = false)
    private Squadra squadraCasa;

    // Relazione con Squadra in trasferta
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "squadra_trasferta_id", nullable = false)
    private Squadra squadraTrasferta;

    // Relazione con Arbitro
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arbitro_id")
    private Arbitro arbitro;

    // Relazione con Commenti
    @OneToMany(mappedBy = "partita", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<Commento> commenti;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataOra() {
        return dataOra;
    }

    public void setDataOra(LocalDateTime dataOra) {
        this.dataOra = dataOra;
    }

    public String getLuogo() {
        return luogo;
    }

    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    public Integer getGoalsHome() {
        return goalsHome;
    }

    public void setGoalsHome(Integer goalsHome) {
        this.goalsHome = goalsHome;
    }

    public Integer getGoalsAway() {
        return goalsAway;
    }

    public void setGoalsAway(Integer goalsAway) {
        this.goalsAway = goalsAway;
    }

    public StatoPartita getStato() {
        return stato;
    }

    public void setStato(StatoPartita stato) {
        this.stato = stato;
    }

    public Torneo getTorneo() {
        return torneo;
    }

    public void setTorneo(Torneo torneo) {
        this.torneo = torneo;
    }

    public Squadra getSquadraCasa() {
        return squadraCasa;
    }

    public void setSquadraCasa(Squadra squadraCasa) {
        this.squadraCasa = squadraCasa;
    }

    public Squadra getSquadraTrasferta() {
        return squadraTrasferta;
    }

    public void setSquadraTrasferta(Squadra squadraTrasferta) {
        this.squadraTrasferta = squadraTrasferta;
    }

    public Arbitro getArbitro() {
        return arbitro;
    }

    public void setArbitro(Arbitro arbitro) {
        this.arbitro = arbitro;
    }
}