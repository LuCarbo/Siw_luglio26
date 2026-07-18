package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Giocatore;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface GiocatoreRepository extends CrudRepository<Giocatore, Long> {

    // Trova tutti i giocatori appartenenti a una specifica squadra (tramite l'ID
    // della squadra)
    List<Giocatore> findBySquadraId(Long squadraId);

    // Trova tutti i giocatori che fanno un certo ruolo (es. "Attaccante")
    List<Giocatore> findByRuolo(String ruolo);
}