package it.uniroma3.siw.controller.rest;

import it.uniroma3.siw.service.GiocatoreService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/giocatori")
@CrossOrigin(origins = "*") // Permette a React di leggere i dati
public class GiocatoreRestController {

    private final GiocatoreService giocatoreService;

    public GiocatoreRestController(GiocatoreService giocatoreService) {
        this.giocatoreService = giocatoreService;
    }

    @GetMapping
    public List<GiocatoreDTO> getAllGiocatori() {
        // Recupera tutti i giocatori dal DB e li trasforma in un formato leggero per React
        return giocatoreService.findAll().stream()
                .map(g -> new GiocatoreDTO(
                        g.getId(),
                        g.getNome(),
                        g.getCognome(),
                        g.getRuolo(),
                        g.getSquadra() != null ? g.getSquadra().getNome() : "Svincolato"
                ))
                .collect(Collectors.toList());
    }

    // --- CLASSE DTO (Data Transfer Object) ---
    // Serve a inviare a React solo le stringhe ed evitare che Spring vada in loop 
    // infinito cercando di convertire l'intera entità Squadra
    public static class GiocatoreDTO {
        public Long id;
        public String nome;
        public String cognome;
        public String ruolo;
        public String squadra;

        public GiocatoreDTO(Long id, String nome, String cognome, String ruolo, String squadra) {
            this.id = id;
            this.nome = nome;
            this.cognome = cognome;
            this.ruolo = ruolo;
            this.squadra = squadra;
        }
    }
}