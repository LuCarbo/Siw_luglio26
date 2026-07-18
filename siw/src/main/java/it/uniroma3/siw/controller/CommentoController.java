package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.service.CommentoService;
// Se usi CredentialsService importalo qui e cambialo nel costruttore
import it.uniroma3.siw.repository.UtenteRepository; 

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
     * 1. INSERIMENTO DI UN NUOVO COMMENTO
     * ========================================================================= */
    @PostMapping("/partita/{id}/commento")
    public String aggiungiCommento(@PathVariable("id") Long partitaId, 
                                   @RequestParam("testo") String testo, 
                                   Principal principal) {
        
        if (principal != null) {
            Utente utenteLoggato = recuperaUtenteLoggato(principal);
            if (utenteLoggato != null) {
                commentoService.aggiungiCommento(partitaId, utenteLoggato.getId(), testo);
            }
        }
        
        // Torna alla pagina della partita
        return "redirect:/partita/" + partitaId;
    }

    /* =========================================================================
     * 2. MODIFICA DI UN COMMENTO ESISTENTE
     * ========================================================================= */
    @PostMapping("/partita/{partitaId}/commento/{commentoId}/modifica")
    public String modificaCommento(@PathVariable("partitaId") Long partitaId, 
                                   @PathVariable("commentoId") Long commentoId,
                                   @RequestParam("nuovoTesto") String nuovoTesto, 
                                   Principal principal) {
        
        if (principal != null) {
            Utente utenteLoggato = recuperaUtenteLoggato(principal);
            if (utenteLoggato != null) {
                commentoService.modificaCommento(commentoId, utenteLoggato.getId(), nuovoTesto);
            }
        }
        
        return "redirect:/partita/" + partitaId;
    }

    /* =========================================================================
     * METODO PRIVATO DI SUPPORTO: RECUPERA L'UTENTE DAL PRINCIPAL
     * ========================================================================= */
    private Utente recuperaUtenteLoggato(Principal principal) {
        // Se usi la ricerca diretta nel repository:
        return utenteRepository.findByUsername(principal.getName()).orElse(null);
        
        /* 
         * Se usi il CredentialsService:
         * return credentialsService.getCredentials(principal.getName()).getUtente();
         */
    }
}