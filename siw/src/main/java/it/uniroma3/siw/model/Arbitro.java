package it.uniroma3.siw.model;


import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "arbitri")
public class Arbitro {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nome;

    @Column(nullable = false, length = 50)
    private String cognome;

    @Column(name = "codice_arbitrale", unique = true, nullable = false)
    private String codiceArbitrale;

    // Relazione con Partita
    @OneToMany(mappedBy = "arbitro")
    private List<Partita> partiteDirette = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getCodiceArbitrale() {
        return codiceArbitrale;
    }

    public void setCodiceArbitrale(String codiceArbitrale) {
        this.codiceArbitrale = codiceArbitrale;
    }

    public List<Partita> getPartiteDirette() {
        return partiteDirette;
    }

    public void setPartiteDirette(List<Partita> partiteDirette) {
        this.partiteDirette = partiteDirette;
    }

    
}