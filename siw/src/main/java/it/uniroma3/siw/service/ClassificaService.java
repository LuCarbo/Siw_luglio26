package it.uniroma3.siw.service;

import it.uniroma3.siw.model.ClassificaSquadra;
import it.uniroma3.siw.model.Partita;
import it.uniroma3.siw.model.Torneo;
import it.uniroma3.siw.model.enums.StatoPartita;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ClassificaService {

    private final TorneoService torneoService;
    private final PartitaService partitaService;

    public ClassificaService(TorneoService torneoService, PartitaService partitaService) {
        this.torneoService = torneoService;
        this.partitaService = partitaService;
    }

    public List<ClassificaSquadra> generaClassifica(Long torneoId) {
        Torneo torneo = torneoService.findById(torneoId);
        List<Partita> partite = partitaService.findByTorneoId(torneoId);

        Map<Long, ClassificaSquadra> mappaClassifica = new HashMap<>();

        // 1. Inizializziamo la classifica (tutti a 0 punti)
        if (torneo.getSquadre() != null) {
            torneo.getSquadre().forEach(squadra -> 
                mappaClassifica.put(squadra.getId(), new ClassificaSquadra(squadra))
            );
        }

        // 2. Assegniamo SOLO i punti in base ai gol
        for (Partita p : partite) {
            if (p.getStato() == StatoPartita.PLAYED) {
                
                mappaClassifica.putIfAbsent(p.getSquadraCasa().getId(), new ClassificaSquadra(p.getSquadraCasa()));
                mappaClassifica.putIfAbsent(p.getSquadraTrasferta().getId(), new ClassificaSquadra(p.getSquadraTrasferta()));

                ClassificaSquadra rigaCasa = mappaClassifica.get(p.getSquadraCasa().getId());
                ClassificaSquadra rigaTrasferta = mappaClassifica.get(p.getSquadraTrasferta().getId());

                int golCasa = p.getGoalsHome();
                int golTrasferta = p.getGoalsAway();

                if (golCasa > golTrasferta) {
                    // La squadra in casa ha vinto: +3 punti
                    rigaCasa.aggiungiPunti(3);
                    // La squadra in trasferta ha perso: +0 punti (non facciamo nulla)
                } 
                else if (golCasa < golTrasferta) {
                    // La squadra in trasferta ha vinto: +3 punti
                    rigaTrasferta.aggiungiPunti(3);
                } 
                else {
                    // Pareggio: +1 punto a testa
                    rigaCasa.aggiungiPunti(1);
                    rigaTrasferta.aggiungiPunti(1);
                }
            }
        }

        // 3. Estraiamo i valori dalla mappa e li ordiniamo
        List<ClassificaSquadra> classificaFinale = new ArrayList<>(mappaClassifica.values());
        
        // Questo comando usa il tuo metodo "compareTo" per mettere i punteggi in ordine decrescente
        Collections.sort(classificaFinale);

        return classificaFinale;
    }
}