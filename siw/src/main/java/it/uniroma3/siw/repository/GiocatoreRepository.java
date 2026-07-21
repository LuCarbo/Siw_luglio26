package it.uniroma3.siw.repository;

import java.util.List;
import it.uniroma3.siw.model.Giocatore;
import org.springframework.data.repository.CrudRepository;

public interface GiocatoreRepository extends CrudRepository<Giocatore, Long> {

    List<Giocatore> findBySquadraId(Long squadraId);

    List<Giocatore> findBySquadraIsNull();
}