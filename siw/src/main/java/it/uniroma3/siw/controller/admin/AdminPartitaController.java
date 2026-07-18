package it.uniroma3.siw.controller.admin;

import it.uniroma3.siw.model.Partita;
import it.uniroma3.siw.model.enums.StatoPartita;
import it.uniroma3.siw.service.PartitaService;
import it.uniroma3.siw.service.SquadraService;
import it.uniroma3.siw.service.TorneoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/partita")
public class AdminPartitaController {

    private final PartitaService partitaService;
    private final SquadraService squadraService;
    private final TorneoService torneoService;

    public AdminPartitaController(PartitaService partitaService, SquadraService squadraService, TorneoService torneoService) {
        this.partitaService = partitaService;
        this.squadraService = squadraService;
        this.torneoService = torneoService;
    }

    // 1. Mostra il form per pianificare una nuova partita
    @GetMapping("/nuova")
    public String mostraFormNuovaPartita(@RequestParam("torneoId") Long torneoId, Model model) {
        Partita partita = new Partita();
        partita.setStato(StatoPartita.SCHEDULED); // Di default è "da giocare"
        
        it.uniroma3.siw.model.Torneo torneo = torneoService.findById(torneoId);
        partita.setTorneo(torneo);
        
        model.addAttribute("partita", partita);
        model.addAttribute("squadre", torneo.getSquadre());
        model.addAttribute("torneoCorrente", torneo);
        
        return "admin/partita-form";
    }

    // 1.5. Mostra il form per modificare una partita esistente
    @GetMapping("/{id}/modifica")
    public String mostraFormModificaPartita(@PathVariable("id") Long id, Model model) {
        Partita partita = partitaService.findById(id);
        
        it.uniroma3.siw.model.Torneo torneo = partita.getTorneo();
        
        model.addAttribute("partita", partita);
        model.addAttribute("squadre", torneo.getSquadre());
        model.addAttribute("torneoCorrente", torneo);
        
        return "admin/partita-form";
    }

    // 2. Salva la nuova partita nel database
    @PostMapping("/salva")
    public String salvaPartita(@ModelAttribute("partita") Partita partita, Model model) {
        
        // Validazione: Squadra casa e trasferta non possono essere la stessa
        if (partita.getSquadraCasa() != null && partita.getSquadraTrasferta() != null && 
            partita.getSquadraCasa().getId().equals(partita.getSquadraTrasferta().getId())) {
            
            it.uniroma3.siw.model.Torneo torneo = partita.getTorneo();
            model.addAttribute("erroreSquadre", "La squadra in casa non può essere uguale alla squadra in trasferta.");
            model.addAttribute("squadre", torneo.getSquadre());
            model.addAttribute("torneoCorrente", torneo);
            return "admin/partita-form";
        }
        
        // Se i gol non sono inseriti, mettiamoli a 0 di default
        if(partita.getStato() == StatoPartita.SCHEDULED) {
            partita.setGoalsHome(0);
            partita.setGoalsAway(0);
        }
        partitaService.save(partita);
        return "redirect:/tornei"; // Rimanda alla lista dei tornei dopo il salvataggio
    }

    // 3. Elimina una partita
    @GetMapping("/{id}/elimina")
    public String eliminaPartita(@PathVariable("id") Long id) {
        Partita partita = partitaService.findById(id);
        Long torneoId = partita.getTorneo().getId();
        partitaService.deleteById(id);
        return "redirect:/torneo/" + torneoId; 
    }
}