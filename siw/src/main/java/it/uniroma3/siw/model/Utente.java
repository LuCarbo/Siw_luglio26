package it.uniroma3.siw.model;

import it.uniroma3.siw.model.enums.*;
import jakarta.persistence.*;


@Entity
@Table(name = "utenti")
public class Utente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RuoloUtente ruolo;
}