package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.model.Torneo;
import it.uniroma3.siw.repository.SquadraRepository;
import it.uniroma3.siw.repository.TorneoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service // Dice a Spring di creare un'istanza di questa classe all'avvio
public class TorneoService {

    private final TorneoRepository torneoRepository;
    private final SquadraRepository squadraRepository;

    // Injection tramite costruttore
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

    // --- LOGICA DI BUSINESS ---

    @Transactional // Se una delle due operazioni fallisce, non viene salvato nulla
    public void iscriviSquadra(Long torneoId, Long squadraId) {
        Torneo torneo = findById(torneoId);
        Squadra squadra = squadraRepository.findById(squadraId)
                .orElseThrow(() -> new RuntimeException("Squadra non trovata"));

        // Aggiungiamo la squadra alla lista del torneo
        if (!torneo.getSquadre().contains(squadra)) {
            torneo.getSquadre().add(squadra);
            // La squadra.getTornei().add(torneo) si aggiornerebbe in automatico se crei un
            // metodo helper,
            // ma con Hibernate l'owner della relazione (Torneo) è sufficiente per salvare
            // il dato.
            torneoRepository.save(torneo);
        }

    }

    @Transactional
    public void deleteById(Long id) {
        torneoRepository.deleteById(id);
    }
}