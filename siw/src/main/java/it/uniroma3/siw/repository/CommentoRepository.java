package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Commento;
import it.uniroma3.siw.model.Partita;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface CommentoRepository extends CrudRepository<Commento, Long> {

    List<Commento> findByPartita(Partita partita);

    List<Commento> findByPartitaOrderByDataCreazioneDesc(Partita partita);
}
