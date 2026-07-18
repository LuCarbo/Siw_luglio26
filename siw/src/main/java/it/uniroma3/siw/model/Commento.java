package it.uniroma3.siw.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Base64;

@Entity
@Table(name = "commenti")
public class Commento {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String testo;

    @Column(name = "data_creazione", nullable = false, updatable = false)
    private LocalDateTime dataCreazione;


    @Column(columnDefinition = "BYTEA")
    private byte[] immagine;

    // Relazione: un commento appartiene a un utente (autore)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente autore;

    // Relazione: un commento è riferito a una specifica partita
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partita_id", nullable = false)
    private Partita partita;

    // Metodo eseguito automaticamente da Hibernate prima di salvare nel DB per la prima volta
    @PrePersist
    protected void onCreate() {
        this.dataCreazione = LocalDateTime.now();
    }


    // ----------- GETTER E SETTER -----------
    
    public byte[] getImmagine() {
        return immagine;
    }

    public void setImmagine(byte[] immagine) {
        this.immagine = immagine;
    }

    // --- METODO PER THYMELEAF ---
    @Transient // Non viene salvato nel DB
    public String getImmagineBase64() {
        if (this.immagine != null && this.immagine.length > 0) {
            return Base64.getEncoder().encodeToString(this.immagine);
        }
        return null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(LocalDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public Utente getAutore() {
        return autore;
    }

    public void setAutore(Utente autore) {
        this.autore = autore;
    }

    public Partita getPartita() {
        return partita;
    }

    public void setPartita(Partita partita) {
        this.partita = partita;
    }

    
}