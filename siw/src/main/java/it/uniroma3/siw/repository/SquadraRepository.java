package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Squadra;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SquadraRepository extends CrudRepository<Squadra, Long> {

    // Trova tutte le squadre di una specifica città
    List<Squadra> findByCitta(String citta);
}