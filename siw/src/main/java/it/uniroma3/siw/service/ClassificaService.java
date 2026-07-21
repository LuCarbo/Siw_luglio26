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

        if (torneo.getSquadre() != null) {
            torneo.getSquadre()
                    .forEach(squadra -> mappaClassifica.put(squadra.getId(), new ClassificaSquadra(squadra)));
        }

        for (Partita p : partite) {
            if (p.getStato() == StatoPartita.PLAYED) {

                mappaClassifica.putIfAbsent(p.getSquadraCasa().getId(), new ClassificaSquadra(p.getSquadraCasa()));
                mappaClassifica.putIfAbsent(p.getSquadraTrasferta().getId(),
                        new ClassificaSquadra(p.getSquadraTrasferta()));

                ClassificaSquadra rigaCasa = mappaClassifica.get(p.getSquadraCasa().getId());
                ClassificaSquadra rigaTrasferta = mappaClassifica.get(p.getSquadraTrasferta().getId());

                int golCasa = p.getGoalsHome();
                int golTrasferta = p.getGoalsAway();

                if (golCasa > golTrasferta) {

                    rigaCasa.aggiungiPunti(3);

                } else if (golCasa < golTrasferta) {

                    rigaTrasferta.aggiungiPunti(3);
                } else {

                    rigaCasa.aggiungiPunti(1);
                    rigaTrasferta.aggiungiPunti(1);
                }
            }
        }

        List<ClassificaSquadra> classificaFinale = new ArrayList<>(mappaClassifica.values());

        Collections.sort(classificaFinale);

        return classificaFinale;
    }
}