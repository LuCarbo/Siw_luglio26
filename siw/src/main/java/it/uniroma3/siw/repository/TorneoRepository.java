package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Torneo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // Segnala a Spring che questo è un componente che gestisce l'accesso ai dati
public interface TorneoRepository extends JpaRepository<Torneo, Long> {


    // ESEMPIO DI "QUERY METHOD" (Spring genera la query automaticamente)
    // Cerca tutti i tornei di un determinato anno
    List<Torneo> findByAnno(Integer anno);
    
    // Cerca un torneo ignorando le maiuscole/minuscole
    List<Torneo> findByNomeContainingIgnoreCase(String nome);
}