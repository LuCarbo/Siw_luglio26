package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Commento;
import it.uniroma3.siw.model.Partita;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface CommentoRepository extends CrudRepository<Commento, Long> {
    
    // Per cercare i commenti di una partita, usa il nome esatto della variabile Java "partita"
    List<Commento> findByPartita(Partita partita);
    
    // Se vuoi i commenti ordinati dal più recente al più vecchio (opzionale ma consigliato):
    // Usa "DataCreazione" tutto attaccato (camelCase), senza underscore!
    List<Commento> findByPartitaOrderByDataCreazioneDesc(Partita partita);
}