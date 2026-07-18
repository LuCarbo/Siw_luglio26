package it.uniroma3.siw.controller.admin;

import it.uniroma3.siw.model.Torneo;
import it.uniroma3.siw.service.TorneoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/tornei") // Tutte le rotte di questa classe richiedono privilegi ADMIN
public class AdminTorneoController {

    private final TorneoService torneoService;

    public AdminTorneoController(TorneoService torneoService) {
        this.torneoService = torneoService;
    }

    // 1. MOSTRA IL FORM VUOTO PER UN NUOVO TORNEO
    @GetMapping("/nuovo")
    public String mostraFormNuovoTorneo(Model model) {
        model.addAttribute("torneo", new Torneo()); // Passiamo un torneo vuoto per far compilare i campi
        return "admin/torneo-form"; 
    }

    // 2. SALVA IL TORNEO (Nuovo o Modificato)
    @PostMapping("/salva")
    public String salvaTorneo(@ModelAttribute("torneo") Torneo torneo) {
        Torneo savedTorneo = torneoService.save(torneo);
        // Dopo il salvataggio, reindirizza al dettaglio del torneo
        return "redirect:/torneo/" + savedTorneo.getId(); 
    }

    // 3. MOSTRA IL FORM PRE-COMPILATO PER LA MODIFICA
    @GetMapping("/{id}/modifica")
    public String mostraFormModifica(@PathVariable("id") Long id, Model model) {
        Torneo torneo = torneoService.findById(id);
        model.addAttribute("torneo", torneo);
        return "admin/torneo-form";
    }

    // 4. ELIMINA UN TORNEO
    @GetMapping("/{id}/elimina")
    public String eliminaTorneo(@PathVariable("id") Long id) {
        torneoService.deleteById(id);
        return "redirect:/tornei";
    }

    // 5. ISCRIVI SQUADRA AL TORNEO
    @PostMapping("/{id}/iscriviSquadra")
    public String iscriviSquadra(@PathVariable("id") Long torneoId, @RequestParam("squadraId") Long squadraId) {
        torneoService.iscriviSquadra(torneoId, squadraId);
        return "redirect:/torneo/" + torneoId;
    }
}
