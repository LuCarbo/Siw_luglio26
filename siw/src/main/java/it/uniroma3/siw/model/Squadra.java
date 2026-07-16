package it.uniroma3.siw.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "squadre")
public class Squadra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "anno_fondazione")
    private Integer annoFondazione;

    @Column(nullable = false)
    private String citta;

    // Relazione bidirezionale gestita da Torneo
    @ManyToMany(mappedBy = "squadre", fetch = FetchType.LAZY)
    private Set<Torneo> tornei = new HashSet<>();

    @OneToMany(mappedBy = "squadra", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Giocatore> giocatori = new HashSet<>();
}