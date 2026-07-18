package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Partita;
import it.uniroma3.siw.model.enums.StatoPartita;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartitaRepository extends JpaRepository<Partita, Long> {
    
    // Trova tutte le partite di un determinato torneo
    List<Partita> findByTorneoId(Long torneoId);
    
    // Trova le partite che devono ancora essere giocate o che sono finite
    List<Partita> findByStato(StatoPartita stato);
    
    // Trova tutte le partite in cui gioca una certa squadra (sia in casa che in trasferta)
    List<Partita> findBySquadraCasaIdOrSquadraTrasfertaId(Long squadraIdCasa, Long squadraIdTrasferta);

    // Torva le partite in un determinato torneo
    List<Partita> findByTorneoIdOrderByDataOraAsc(Long torneoId);

    // SOLUZIONE AL PROBLEMA N+1
    @Query("SELECT p FROM Partita p " +
           "JOIN FETCH p.squadraCasa " +
           "JOIN FETCH p.squadraTrasferta " +
           "JOIN FETCH p.torneo")
    List<Partita> findAllOttimizzato();

    @EntityGraph(attributePaths = {"squadraCasa", "squadraTrasferta", "torneo"})
    @Query("SELECT p FROM Partita p")
    List<Partita> findAllEager();
}