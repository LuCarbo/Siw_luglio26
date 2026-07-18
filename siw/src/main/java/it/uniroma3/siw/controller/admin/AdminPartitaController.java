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
    public String mostraFormNuovaPartita(Model model) {
        Partita partita = new Partita();
        partita.setStato(StatoPartita.SCHEDULED); // Di default è "da giocare"
        
        model.addAttribute("partita", partita);
        model.addAttribute("squadre", squadraService.findAll());
        model.addAttribute("tornei", torneoService.findAll());
        
        return "admin/partita-form";
    }

    // 2. Salva la nuova partita nel database
    @PostMapping("/salva")
    public String salvaPartita(@ModelAttribute("partita") Partita partita) {
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