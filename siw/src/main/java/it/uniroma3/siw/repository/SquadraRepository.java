package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Squadra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SquadraRepository extends JpaRepository<Squadra, Long> {
    
    // Trova tutte le squadre di una specifica città
    List<Squadra> findByCitta(String citta);
}