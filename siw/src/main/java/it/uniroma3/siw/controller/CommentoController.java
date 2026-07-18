package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.service.CommentoService;
// Se usi CredentialsService importalo qui e cambialo nel costruttore
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

    /* =========================================================================
     * METODO PRIVATO DI SUPPORTO: RECUPERA L'UTENTE DAL PRINCIPAL
     * ========================================================================= */
    private Utente recuperaUtenteLoggato(Principal principal) {
        if (principal == null) {
            return null;
        }
        
        // Cerca l'utente nel database tramite lo username contenuto nel Principal
        return utenteRepository.findByUsername(principal.getName()).orElse(null);
    }

    /* =========================================================================
     * 1. INSERIMENTO DI UN NUOVO COMMENTO
     * ========================================================================= */
    @PostMapping("/partita/{id}/commento")
    public String aggiungiCommento(@PathVariable("id") Long partitaId, 
                                   @RequestParam("testo") String testo, 
                                   @RequestParam(value = "immagine", required = false) MultipartFile immagine,
                                   Principal principal) {
        
        try {
            if (principal != null) {
                Utente utenteLoggato = recuperaUtenteLoggato(principal);
                if (utenteLoggato != null) {
                    // Passiamo l'immagine al service
                    commentoService.aggiungiCommento(partitaId, utenteLoggato.getId(), testo, immagine);
                }
            }
        } catch (IOException e) {
            // Gestione errore (opzionale)
            System.err.println("Errore caricamento immagine: " + e.getMessage());
        }
        
        return "redirect:/partita/" + partitaId;
    }

    /* =========================================================================
     * 2. MODIFICA DI UN COMMENTO ESISTENTE
     * ========================================================================= */
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
                    commentoService.modificaCommento(commentoId, utenteLoggato.getId(), nuovoTesto, nuovaImmagine, rimuoviImmagine);
                }
            }
        } catch (IOException e) {
            System.err.println("Errore durante la modifica dell'immagine: " + e.getMessage());
        }
        
        return "redirect:/partita/" + partitaId;
    }

}