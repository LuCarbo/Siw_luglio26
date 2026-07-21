package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.service.CommentoService;
import it.uniroma3.siw.repository.UtenteRepository;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Controller
public class CommentoController {

    private final CommentoService commentoService;
    private final UtenteRepository utenteRepository;

    public CommentoController(CommentoService commentoService, UtenteRepository utenteRepository) {
        this.commentoService = commentoService;
        this.utenteRepository = utenteRepository;
    }

    private Utente recuperaUtenteLoggato(Principal principal) {
        if (principal == null) {
            return null;
        }

        // Cerca l'utente nel database tramite lo username contenuto nel Principal
        return utenteRepository.findByUsername(principal.getName()).orElse(null);
    }

    // Nuovo Commento
    @PostMapping("/partita/{id}/commento")
    public String aggiungiCommento(@PathVariable("id") Long partitaId,
            @RequestParam("testo") String testo,
            @RequestParam(value = "immagine", required = false) MultipartFile immagine,
            Principal principal) {

        try {
            if (principal != null) {
                Utente utenteLoggato = recuperaUtenteLoggato(principal);
                if (utenteLoggato != null) {
                    // Passo l'immagine al service
                    commentoService.aggiungiCommento(partitaId, utenteLoggato.getId(), testo, immagine);
                }
            }
        } catch (IOException e) {
            System.err.println("Errore caricamento immagine: " + e.getMessage());
        }

        return "redirect:/partita/" + partitaId;
    }

    // Modifica Commento
    @PostMapping("/partita/{partitaId}/commento/{commentoId}/modifica")
    public String modificaCommento(@PathVariable("partitaId") Long partitaId,
            @PathVariable("commentoId") Long commentoId,
            @RequestParam("nuovoTesto") String nuovoTesto,
            @RequestParam(value = "nuovaImmagine", required = false) MultipartFile nuovaImmagine,
            @RequestParam(value = "rimuoviImmagine", defaultValue = "false") boolean rimuoviImmagine,
            Principal principal) {

        try {
            if (principal != null) {
                Utente utenteLoggato = recuperaUtenteLoggato(principal);
                if (utenteLoggato != null) {
                    commentoService.modificaCommento(commentoId, utenteLoggato.getId(), nuovoTesto, nuovaImmagine,
                            rimuoviImmagine);
                }
            }
        } catch (IOException e) {
            System.err.println("Errore durante la modifica dell'immagine: " + e.getMessage());
        }

        return "redirect:/partita/" + partitaId;
    }

}