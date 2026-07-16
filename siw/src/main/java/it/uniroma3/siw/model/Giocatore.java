package it.uniroma3.siw.model;

import it.uniroma3.siw.model.enums.*;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "giocatori")
public class Giocatore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cognome;

    @Column(name = "data_nascita")
    private LocalDate dataNascita;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RuoloGiocatore ruolo;

    private Double altezza;

    // Fondamentale esplicitare LAZY per prevenire il caricamento automatico della squadra
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "squadra_id", nullable = false)
    private Squadra squadra;
}