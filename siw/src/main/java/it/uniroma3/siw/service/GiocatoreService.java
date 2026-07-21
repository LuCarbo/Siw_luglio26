package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Giocatore;
import it.uniroma3.siw.repository.GiocatoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GiocatoreService {

    private final GiocatoreRepository giocatoreRepository;

    public GiocatoreService(GiocatoreRepository giocatoreRepository) {
        this.giocatoreRepository = giocatoreRepository;
    }

    @Transactional
    public Giocatore save(Giocatore giocatore) {
        return giocatoreRepository.save(giocatore);
    }

    @Transactional
    public void deleteById(Long id) {
        giocatoreRepository.deleteById(id);
    }

    public Iterable<Giocatore> findAll() {
        return giocatoreRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Giocatore findById(Long id) {
        return giocatoreRepository.findById(id).orElseThrow(() -> new RuntimeException("Giocatore non trovato"));
    }

    public Iterable<Giocatore> findBySquadraId(Long squadraId) {
        return giocatoreRepository.findBySquadraId(squadraId);
    }

    public Iterable<Giocatore> findSvincolati() {
        return giocatoreRepository.findBySquadraIsNull();
    }
}