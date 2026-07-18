package it.uniroma3.siw.model;

import jakarta.persistence.*; // Se usi Spring Boot 3.x (altrimenti usa javax.persistence.* per Spring Boot 2.x)
import java.util.*;


@Entity
@Table(name = "tornei")
public class Torneo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false)
    private Integer anno;

    @Column(columnDefinition = "TEXT")
    private String descrizione;

    //Relazione con Squadre
    @ManyToMany
    @JoinTable(
        name = "torneo_squadra", 
        joinColumns = @JoinColumn(name = "torneo_id"), 
        inverseJoinColumns = @JoinColumn(name = "squadra_id")
    )
    private List<Squadra> squadre = new ArrayList<>();

    // Relazione con Partita
    @OneToMany(mappedBy = "torneo", cascade = CascadeType.ALL)
    private List<Partita> partite = new ArrayList<>();


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

    public Integer getAnno() {
        return anno;
    }

    public void setAnno(Integer anno) {
        this.anno = anno;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public List<Squadra> getSquadre() {
        return squadre;
    }

    public void setSquadre(List<Squadra> squadre) {
        this.squadre = squadre;
    }

    public List<Partita> getPartite() {
        return partite;
    }

    public void setPartite(List<Partita> partite) {
        this.partite = partite;
    }

    
}