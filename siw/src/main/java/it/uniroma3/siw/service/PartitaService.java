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

    public List<Partita> findPartiteDaGiocare() {
        return partitaRepository.findByStato(StatoPartita.SCHEDULED);
    }

    public Partita findById(Long id) {
        return partitaRepository.findById(id).orElseThrow(() -> new RuntimeException("Partita non trovata"));
    }
    
    // --- LOGICA DI BUSINESS ---
    
    @Transactional
    public void save(Partita partita) {
        partitaRepository.save(partita);
    }

    @Transactional
    public void deleteById(Long id) {
        partitaRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    public void eseguiTestLazy() {
        List<Partita> partite = partitaRepository.findAll();
        // Simuliamo l'accesso ai dati che avverrebbe nella vista Thymeleaf o in React
        for (Partita p : partite) {
            p.getSquadraCasa().getNome();
            p.getSquadraTrasferta().getNome();
            p.getTorneo().getNome();
        }
    }

    @Transactional(readOnly = true)
    public List<Partita> findByTorneoId(Long torneoId) {
        return partitaRepository.findByTorneoIdOrderByDataOraAsc(torneoId);
    }

    @Transactional(readOnly = true)
    public void eseguiTestOttimizzato() {
        List<Partita> partite = partitaRepository.findAllOttimizzato(); // Usa la query personalizzata
        // Simuliamo l'accesso: questa volta NON verranno generate nuove query
        for (Partita p : partite) {
            p.getSquadraCasa().getNome();
            p.getSquadraTrasferta().getNome();
            p.getTorneo().getNome();
        }
    }
    
    @Transactional(readOnly = true)
    public List<Partita> findAllOttimizzato() {
        // Chiama la query super-veloce che abbiamo scritto nel Repository
        return partitaRepository.findAllOttimizzato();
    }

    @Transactional
    public Partita impostaRisultato(Long partitaId, int goalsHome, int goalsAway) {
        // 1. Recuperiamo la partita dal database
        Partita partita = partitaRepository.findById(partitaId)
                .orElseThrow(() -> new RuntimeException("Partita non trovata con ID: " + partitaId));

        // 2. Aggiorniamo i dati della partita
        partita.setGoalsHome(goalsHome);
        partita.setGoalsAway(goalsAway);
        partita.setStato(StatoPartita.PLAYED); // Cambia lo stato in "Giocata"

        // 3. Salva le modifiche
        Partita partitaSalvata = partitaRepository.save(partita);

        // [Opzionale per il futuro]: Qui potresti chiamare classificaService.aggiorna(partita.getTorneo())
        // Grazie a @Transactional, se l'aggiornamento della classifica fallisse, 
        // anche il risultato della partita verrebbe annullato nel DB automaticamente.

        return partitaSalvata;
    }

    @Transactional(readOnly = true)
    public void eseguiTestEager() {
        // Usa la query con EntityGraph (EAGER dinamico)
        List<Partita> partite = partitaRepository.findAllEager(); 
        
        // I dati sono già in memoria, non farà altre query qui dentro
        for (Partita p : partite) {
            p.getSquadraCasa().getNome();
            p.getSquadraTrasferta().getNome();
            p.getTorneo().getNome();
        }
    }
    
}