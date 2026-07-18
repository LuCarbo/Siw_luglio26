package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Torneo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TorneoRepository extends CrudRepository<Torneo, Long> {

    // ESEMPIO DI "QUERY METHOD" (Spring genera la query automaticamente)
    // Cerca tutti i tornei di un determinato anno
    List<Torneo> findByAnno(Integer anno);

    // Cerca un torneo ignorando le maiuscole/minuscole
    List<Torneo> findByNomeContainingIgnoreCase(String nome);
}