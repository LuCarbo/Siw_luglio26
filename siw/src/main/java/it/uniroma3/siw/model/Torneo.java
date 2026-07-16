package it.uniroma3.siw.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tornei")
public class Torneo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Integer anno;

    @Column(length = 500)
    private String descrizione;

    // Torneo è l'owner della relazione N:M
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "tornei_squadre",
        joinColumns = @JoinColumn(name = "torneo_id"),
        inverseJoinColumns = @JoinColumn(name = "squadra_id")
    )
    private Set<Squadra> squadre = new HashSet<>();

    @OneToMany(mappedBy = "torneo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Partita> partite = new HashSet<>();
}