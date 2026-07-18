package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Utente;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface UtenteRepository extends CrudRepository<Utente, Long> {

    // Fondamentale quando implementeremo il login o Spring Security
    Optional<Utente> findByUsername(String username);

    Optional<Utente> findByProviderId(String providerId);
}