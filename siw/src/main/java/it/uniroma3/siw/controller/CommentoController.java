package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Commento;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.service.CommentoService;
import it.uniroma3.siw.service.UtenteService; // Aggiunto per recuperare l'utente
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class CommentoController {

    private final CommentoService commentoService;
    private final UtenteService utenteService;

    public CommentoController(CommentoService commentoService, UtenteService utenteService) {
        this.commentoService = commentoService;
        this.utenteService = utenteService;
    }

    // ==============================================
    // 1. INSERIMENTO NUOVO COMMENTO
    // ==============================================
    @PostMapping("/partite/{partitaId}/commenti")
    public String aggiungiCommento(
            @PathVariable Long partitaId, 
            @RequestParam("testo") String testo,
            Principal principal) {
            
        // 1. Recupero l'utente loggato in modo sicuro tramite il suo username
        Utente autore = utenteService.findByUsername(principal.getName());
        
        // 2. Passo l'ID dell'utente al service (lavoriamo per ID come hai chiesto)
        commentoService.aggiungiCommento(partitaId, autore.getId(), testo);
        
        return "redirect:/partite/" + partitaId; 
    }

    // ==============================================
    // 2. MOSTRA IL FORM DI MODIFICA
    // ==============================================
    @GetMapping("/commenti/{commentoId}/modifica")
    public String mostraFormModifica(@PathVariable Long commentoId, Model model, Principal principal) {
        
        Commento commento = commentoService.findById(commentoId);
        Utente utenteLoggato = utenteService.findByUsername(principal.getName());

        // Controllo di sicurezza: confrontiamo l'ID dell'autore con l'ID dell'utente loggato
        if (!commento.getAutore().getId().equals(utenteLoggato.getId())) {
            return "redirect:/"; // Non sei l'autore, ti sbatto fuori
        }

        model.addAttribute("commento", commento);
        return "formModificaCommento";
    }

    // ==============================================
    // 3. SALVATAGGIO DELLA MODIFICA
    // ==============================================
    @PostMapping("/commenti/{commentoId}/modifica")
    public String salvaModificaCommento(
            @PathVariable Long commentoId,
            @RequestParam("testo") String nuovoTesto,
            Principal principal) {
            
        // Recupero di nuovo l'ID dell'utente per validare la richiesta nel service
        Utente utenteLoggato = utenteService.findByUsername(principal.getName());
            
        Commento commentoAggiornato = commentoService.modificaCommento(commentoId, utenteLoggato.getId(), nuovoTesto);
        
        return "redirect:/partite/" + commentoAggiornato.getPartita().getId();
    }
}