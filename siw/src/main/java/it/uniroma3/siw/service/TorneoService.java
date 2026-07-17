package it.uniroma3.siw.service;

import it.uniroma3.siw.model.ClassificaSquadra;
import it.uniroma3.siw.model.Partita;
import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.model.Torneo;
import it.uniroma3.siw.repository.SquadraRepository;
import it.uniroma3.siw.repository.TorneoRepository;

import java.util.*;

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

    public List<Torneo> findAll() {
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
            // La squadra.getTornei().add(torneo) si aggiornerebbe in automatico se crei un metodo helper, 
            // ma con Hibernate l'owner della relazione (Torneo) è sufficiente per salvare il dato.
            torneoRepository.save(torneo);
        }
    }

    // --- CLASSIFICA ----
    // IL CODICE ORA È CORRETTAMENTE RACCHIUSO IN UN METODO
    public List<ClassificaSquadra> calcolaClassifica(Torneo torneo, List<Partita> partiteGiocate) {
        
        Map<Long, ClassificaSquadra> mappaClassifica = new HashMap<>();

        // 1. Inizializza la mappa con tutte le squadre a zero punti
        for (Squadra s : torneo.getSquadre()) {
            mappaClassifica.put(s.getId(), new ClassificaSquadra(s));
        }

        // 2. Assegna i punti
        for (Partita p : partiteGiocate) {
            // Nota: Se i tuoi metodi in Partita.java si chiamano diversamente 
            // (es. getSquadraHome e getSquadraAway), cambiali qui!
            ClassificaSquadra casa = mappaClassifica.get(p.getSquadraCasa().getId());
            ClassificaSquadra trasferta = mappaClassifica.get(p.getSquadraTrasferta().getId());

            // Evita un NullPointerException se una squadra non è nella mappa
            if (casa == null || trasferta == null) continue;

            if (p.getGoalsHome() > p.getGoalsAway()) {
                casa.aggiungiPunti(3);
            } else if (p.getGoalsHome() < p.getGoalsAway()) {
                trasferta.aggiungiPunti(3);
            } else {
                casa.aggiungiPunti(1);
                trasferta.aggiungiPunti(1);
            }
        }

        // 3. Converti in lista e ordina!
        List<ClassificaSquadra> classificaFinale = new ArrayList<>(mappaClassifica.values());
        Collections.sort(classificaFinale);
        
        return classificaFinale; // Restituisce la classifica calcolata al Controller
    }
    
    @Transactional
    public void deleteById(Long id) {
        torneoRepository.deleteById(id);
    }
}