package it.uniroma3.siw.controller.rest;

import it.uniroma3.siw.service.SquadraService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/squadre")
@CrossOrigin(origins = "*") 
public class SquadraRestController {

    private final SquadraService squadraService;

    public SquadraRestController(SquadraService squadraService) {
        this.squadraService = squadraService;
    }

    @GetMapping
    public List<SquadraDTO> getAllSquadre() {
        // Presuppone che tu abbia un metodo findAll() nel tuo SquadraService
        return squadraService.findAll().stream()
                .map(s -> new SquadraDTO(
                        s.getId(), 
                        s.getNome(), 
                        // ATTENZIONE: sostituisci s.getCitta() con il getter reale 
                        // che usi nella tua entità Squadra (es. getIndirizzo, getSede, ecc.)
                        s.getCitta() 
                ))
                .collect(Collectors.toList());
    }

    // DTO (Data Transfer Object) per inviare solo i dati necessari a React
    public static class SquadraDTO {
        public Long id;
        public String nome;
        public String citta;

        public SquadraDTO(Long id, String nome, String citta) {
            this.id = id;
            this.nome = nome;
            this.citta = citta;
        }
    }
}