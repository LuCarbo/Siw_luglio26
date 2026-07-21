package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Partita;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PartitaRepository extends CrudRepository<Partita, Long> {

    List<Partita> findByTorneoIdOrderByDataOraAsc(Long torneoId);

    @Query("SELECT p FROM Partita p " +
            "JOIN FETCH p.squadraCasa " +
            "JOIN FETCH p.squadraTrasferta " +
            "JOIN FETCH p.torneo")
    List<Partita> findAllOttimizzato();

}