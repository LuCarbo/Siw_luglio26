package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Partita;
import it.uniroma3.siw.model.enums.StatoPartita;
import it.uniroma3.siw.repository.PartitaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PartitaService {

    private final PartitaRepository partitaRepository;

    public PartitaService(PartitaRepository partitaRepository) {
        this.partitaRepository = partitaRepository;
    }

    public Partita findById(Long id) {
        return partitaRepository.findById(id).orElseThrow(() -> new RuntimeException("Partita non trovata"));
    }

    @Transactional
    public void save(Partita partita) {
        partitaRepository.save(partita);
    }

    @Transactional
    public void deleteById(Long id) {
        partitaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Partita> findByTorneoId(Long torneoId) {
        return partitaRepository.findByTorneoIdOrderByDataOraAsc(torneoId);
    }

    @Transactional(readOnly = true)
    public List<Partita> findAllOttimizzato() {
        return partitaRepository.findAllOttimizzato();
    }

    @Transactional
    public Partita impostaRisultato(Long partitaId, int goalsHome, int goalsAway) {

        Partita partita = partitaRepository.findById(partitaId)
                .orElseThrow(() -> new RuntimeException("Partita non trovata con ID: " + partitaId));

        partita.setGoalsHome(goalsHome);
        partita.setGoalsAway(goalsAway);
        partita.setStato(StatoPartita.PLAYED);

        Partita partitaSalvata = partitaRepository.save(partita);

        return partitaSalvata;
    }

}