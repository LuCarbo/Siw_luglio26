package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Giocatore;
import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.repository.GiocatoreRepository;
import it.uniroma3.siw.repository.SquadraRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SquadraService {

    private final SquadraRepository squadraRepository;
    private final GiocatoreRepository giocatoreRepository;

    public SquadraService(SquadraRepository squadraRepository, GiocatoreRepository giocatoreRepository) {
        this.squadraRepository = squadraRepository;
        this.giocatoreRepository = giocatoreRepository;
    }

    public Iterable<Squadra> findAll() {
        return squadraRepository.findAll();
    }

    public Squadra findById(Long id) {
        return squadraRepository.findById(id).orElseThrow(() -> new RuntimeException("Squadra non trovata"));
    }

    @Transactional
    public Squadra save(Squadra squadra) {
        return squadraRepository.save(squadra);
    }

    @Transactional
    public void deleteById(Long id) {
        squadraRepository.deleteById(id);
    }

}