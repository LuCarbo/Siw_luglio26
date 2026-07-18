package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Arbitro;
import org.springframework.data.repository.CrudRepository;

public interface ArbitroRepository extends CrudRepository<Arbitro, Long> {
    // Utile per controllare che non si inseriscano arbitri doppi
    boolean existsByCodiceArbitrale(String codiceArbitrale);
}