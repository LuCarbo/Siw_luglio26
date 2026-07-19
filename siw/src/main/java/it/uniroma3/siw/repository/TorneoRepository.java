package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Torneo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TorneoRepository extends CrudRepository<Torneo, Long> {

    // ESEMPIO DI "QUERY METHOD" (Spring genera la query automaticamente)
    // Cerca tutti i tornei di un determinato anno
    List<Torneo> findByAnno(Integer anno);

    // Cerca un torneo ignorando le maiuscole/minuscole
    List<Torneo> findByNomeContainingIgnoreCase(String nome);

    // --- METODI PER L'ANALISI SPERIMENTALE ---
    
    // Strategia B: Join Fetch
    @Query("SELECT t FROM Torneo t " +
           "LEFT JOIN FETCH t.partite p " +
           "LEFT JOIN FETCH p.squadraCasa " +
           "LEFT JOIN FETCH p.squadraTrasferta " +
           "WHERE t.id = :id")
    Optional<Torneo> findByIdWithJoinFetch(@Param("id") Long id);

    // Strategia C: EntityGraph
    @EntityGraph(attributePaths = {"partite", "partite.squadraCasa", "partite.squadraTrasferta"})
    @Query("SELECT t FROM Torneo t WHERE t.id = :id")
    Optional<Torneo> findByIdWithEntityGraph(@Param("id") Long id);
}