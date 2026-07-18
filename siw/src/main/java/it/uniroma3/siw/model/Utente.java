package it.uniroma3.siw.model;

import it.uniroma3.siw.model.enums.RuoloUtente;
import it.uniroma3.siw.model.enums.AuthProvider;
import jakarta.persistence.*;

@Entity
@Table(name = "utenti")
public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = true)
    private String password; 

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RuoloUtente ruolo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider provider = AuthProvider.LOCAL;

    private String providerId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RuoloUtente getRuolo() {
        return ruolo;
    }

    public void setRuolo(RuoloUtente ruolo) {
        this.ruolo = ruolo;
    }

    public AuthProvider getProvider() {
        return provider;
    }

    public void setProvider(AuthProvider provider) {
        this.provider = provider;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }
}