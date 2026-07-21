package it.uniroma3.siw.repository;

import java.util.*;
import it.uniroma3.siw.model.Squadra;
import org.springframework.data.repository.CrudRepository;

public interface SquadraRepository extends CrudRepository<Squadra, Long> {

    List<Squadra> findByTorneiId(Long torneoId);

}