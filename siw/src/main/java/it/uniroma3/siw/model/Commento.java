package it.uniroma3.siw.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "commenti")
public class Commento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String testo;

    @Column(nullable = false)
    private LocalDateTime data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partita_id", nullable = false)
    private Partita partita;
    
    // Metodo di callback JPA per impostare la data automaticamente
    @PrePersist
    protected void onCreate() {
        this.data = LocalDateTime.now();
    }
}