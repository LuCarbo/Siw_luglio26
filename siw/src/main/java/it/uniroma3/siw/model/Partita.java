package it.uniroma3.siw.model;

import it.uniroma3.siw.model.enums.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "partite")
public class Partita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_ora", nullable = false)
    private LocalDateTime dataOra;

    @Column(nullable = false)
    private String luogo;

    @Column(name = "goals_home")
    private Integer goalsHome = 0;

    @Column(name = "goals_away")
    private Integer goalsAway = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Stato stato;

    // Relazioni (Tutte LAZY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "torneo_id", nullable = false)
    private Torneo torneo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "squadra_casa_id", nullable = false)
    private Squadra squadraCasa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "squadra_trasferta_id", nullable = false)
    private Squadra squadraTrasferta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arbitro_id", nullable = false)
    private Arbitro arbitro;

    @OneToMany(mappedBy = "partita", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Commento> commenti = new HashSet<>();
}