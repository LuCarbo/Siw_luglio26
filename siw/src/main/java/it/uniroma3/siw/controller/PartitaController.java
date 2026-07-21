package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Commento;
import it.uniroma3.siw.model.Partita;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.service.CommentoService;
import it.uniroma3.siw.service.PartitaService;
import it.uniroma3.siw.repository.UtenteRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class PartitaController {

    private final PartitaService partitaService;
    private final CommentoService commentoService;
    private final UtenteRepository utenteRepository;

    public PartitaController(PartitaService partitaService,
            CommentoService commentoService,
            UtenteRepository utenteRepository) {
        this.partitaService = partitaService;
        this.commentoService = commentoService;
        this.utenteRepository = utenteRepository;
    }

    // Visualizzazione pagina
    @GetMapping("/partita/{id}")
    public String getPartita(@PathVariable("id") Long id, Model model, Principal principal) {
        Partita partita = partitaService.findById(id);

        List<Commento> commenti = commentoService.findByPartitaId(partita);

        model.addAttribute("partita", partita);
        model.addAttribute("commenti", commenti);

        // id utente loggato
        if (principal != null) {
            Utente utenteLoggato = recuperaUtenteLoggato(principal);
            if (utenteLoggato != null) {
                model.addAttribute("utenteLoggatoId", utenteLoggato.getId());
            }
        }

        return "partita";
    }

    // Recupera utente loggato
    private Utente recuperaUtenteLoggato(Principal principal) {
        return utenteRepository.findByUsername(principal.getName()).orElse(null);

    }
}