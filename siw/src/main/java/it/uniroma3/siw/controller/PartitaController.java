package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Commento;
import it.uniroma3.siw.model.Partita;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.service.CommentoService;
import it.uniroma3.siw.service.PartitaService;

// IMPORTANTE: Decommenta questa riga se nel tuo progetto usi CredentialsService
// import it.uniroma3.siw.service.CredentialsService; 
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
    
    // NOTA: Se usi CredentialsService, iniettalo qui al posto di UtenteRepository
    private final UtenteRepository utenteRepository; 

    public PartitaController(PartitaService partitaService, 
                             CommentoService commentoService, 
                             UtenteRepository utenteRepository) {
        this.partitaService = partitaService;
        this.commentoService = commentoService;
        this.utenteRepository = utenteRepository;
    }

    /* =========================================================================
     * 1. VISUALIZZAZIONE DELLA PAGINA DELLA PARTITA (E DEI SUOI COMMENTI)
     * ========================================================================= */
    @GetMapping("/partita/{id}")
    public String getPartita(@PathVariable("id") Long id, Model model, Principal principal) {
        Partita partita = partitaService.findById(id);
        
        // Usiamo il metodo ottimizzato del tuo CommentoService (readOnly)
        List<Commento> commenti = commentoService.findByPartitaId(partita);

        model.addAttribute("partita", partita);
        model.addAttribute("commenti", commenti);

        // Se l'utente è loggato, cerchiamo il suo ID e lo passiamo a Thymeleaf.
        // Questo serve per far mostrare in HTML il tasto "Modifica" SOLO sui suoi commenti.
        if (principal != null) {
            Utente utenteLoggato = recuperaUtenteLoggato(principal);
            if (utenteLoggato != null) {
                model.addAttribute("utenteLoggatoId", utenteLoggato.getId());
            }
        }

        return "partita"; // Cerca il file partita.html in templates
    }

    /* =========================================================================
     * METODO PRIVATO DI SUPPORTO: RECUPERA L'UTENTE DAL PRINCIPAL
     * ========================================================================= */
    private Utente recuperaUtenteLoggato(Principal principal) {
        // Se nel tuo progetto cerchi l'utente direttamente tramite repository:
        return utenteRepository.findByUsername(principal.getName()).orElse(null);
        
        /* 
         * SE INVECE NEL TUO PROGETTO USI IL CREDENTIALS SERVICE (Metodo classico Roma Tre),
         * CANCELLA LA RIGA SOPRA E USA QUESTE DUE:
         * 
         * Credentials credentials = credentialsService.getCredentials(principal.getName());
         * return credentials.getUtente();
         */
    }
}