package it.uniroma3.siw.service;

import it.uniroma3.siw.model.RigaClassifica;
import it.uniroma3.siw.model.Partita;
import it.uniroma3.siw.model.enums.StatoPartita;
import it.uniroma3.siw.model.Torneo;
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

    public List<RigaClassifica> generaClassifica(Long torneoId) {
        Torneo torneo = torneoService.findById(torneoId);
        List<Partita> partite = partitaService.findByTorneoId(torneoId);

        Map<Long, RigaClassifica> mappaClassifica = new HashMap<>();

        // 1. Inizializziamo la classifica con le squadre del torneo (se la lista esiste)
        if (torneo.getSquadre() != null) {
            torneo.getSquadre().forEach(squadra -> 
                mappaClassifica.put(squadra.getId(), new RigaClassifica(squadra))
            );
        }

        // 2. Leggiamo i risultati delle partite
        for (Partita p : partite) {
            if (p.getStato() == StatoPartita.PLAYED) {
                
                // DIFESA CONTRO L'ERRORE 500: Se la squadra non era iscritta al torneo, la aggiungiamo alla classifica ora!
                mappaClassifica.putIfAbsent(p.getSquadraCasa().getId(), new RigaClassifica(p.getSquadraCasa()));
                mappaClassifica.putIfAbsent(p.getSquadraTrasferta().getId(), new RigaClassifica(p.getSquadraTrasferta()));

                RigaClassifica rigaCasa = mappaClassifica.get(p.getSquadraCasa().getId());
                RigaClassifica rigaTrasferta = mappaClassifica.get(p.getSquadraTrasferta().getId());

                int golCasa = p.getGoalsHome();
                int golTrasferta = p.getGoalsAway();

                if (golCasa > golTrasferta) {
                    rigaCasa.aggiungiVittoria(golCasa, golTrasferta);
                    rigaTrasferta.aggiungiSconfitta(golTrasferta, golCasa);
                } else if (golCasa < golTrasferta) {
                    rigaTrasferta.aggiungiVittoria(golTrasferta, golCasa);
                    rigaCasa.aggiungiSconfitta(golCasa, golTrasferta);
                } else {
                    rigaCasa.aggiungiPareggio(golCasa);
                    rigaTrasferta.aggiungiPareggio(golTrasferta);
                }
            }
        }

        // 3. Convertiamo in lista e ordiniamo
        List<RigaClassifica> classificaFinale = new ArrayList<>(mappaClassifica.values());
        classificaFinale.sort((r1, r2) -> {
            if (r1.getPunti() != r2.getPunti()) return Integer.compare(r2.getPunti(), r1.getPunti());
            if (r1.getDifferenzaReti() != r2.getDifferenzaReti()) return Integer.compare(r2.getDifferenzaReti(), r1.getDifferenzaReti());
            return Integer.compare(r2.getGolFatti(), r1.getGolFatti());
        });

        return classificaFinale;
    }
}