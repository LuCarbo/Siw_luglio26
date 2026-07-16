package it.uniroma3.siw.model;

import jakarta.persistence.*;

@Entity
@Table(name = "arbitri")
public class Arbitro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cognome;

    @Column(name = "codice_arbitrale", nullable = false, unique = true)
    private String codiceArbitrale;
}