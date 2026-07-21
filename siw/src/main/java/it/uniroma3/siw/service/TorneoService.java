package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.model.Torneo;
import it.uniroma3.siw.repository.SquadraRepository;
import it.uniroma3.siw.repository.TorneoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TorneoService {

    private final TorneoRepository torneoRepository;
    private final SquadraRepository squadraRepository;

    public TorneoService(TorneoRepository torneoRepository, SquadraRepository squadraRepository) {
        this.torneoRepository = torneoRepository;
        this.squadraRepository = squadraRepository;
    }

    public Iterable<Torneo> findAll() {
        return torneoRepository.findAll();
    }

    public Torneo findById(Long id) {
        return torneoRepository.findById(id).orElseThrow(() -> new RuntimeException("Torneo non trovato"));
    }

    @Transactional
    public Torneo save(Torneo torneo) {
        return torneoRepository.save(torneo);
    }

    @Transactional
    public void iscriviSquadra(Long torneoId, Long squadraId) {
        Torneo torneo = findById(torneoId);
        Squadra squadra = squadraRepository.findById(squadraId)
                .orElseThrow(() -> new RuntimeException("Squadra non trovata"));

        if (!torneo.getSquadre().contains(squadra)) {
            torneo.getSquadre().add(squadra);
            torneoRepository.save(torneo);
        }

    }

    @Transactional
    public void deleteById(Long id) {
        torneoRepository.deleteById(id);
    }
}