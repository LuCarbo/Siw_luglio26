package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Giocatore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GiocatoreRepository extends JpaRepository<Giocatore, Long> {
    
    // Trova tutti i giocatori appartenenti a una specifica squadra (tramite l'ID della squadra)
    List<Giocatore> findBySquadraId(Long squadraId);
    
    // Trova tutti i giocatori che fanno un certo ruolo (es. "Attaccante")
    List<Giocatore> findByRuolo(String ruolo);
}