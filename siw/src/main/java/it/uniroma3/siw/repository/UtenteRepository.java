package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long> {
    
    // Fondamentale quando implementeremo il login o Spring Security
    Optional<Utente> findByUsername(String username);

    Optional<Utente> findByProviderId(String providerId);
}