package it.uniroma3.siw.controller.admin;

import it.uniroma3.siw.model.Giocatore;
import it.uniroma3.siw.service.GiocatoreService;
import it.uniroma3.siw.service.SquadraService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/giocatori")
public class AdminGiocatoreController {

    private final GiocatoreService giocatoreService;
    private final SquadraService squadraService;

    // Iniettiamo entrambi i servizi
    public AdminGiocatoreController(GiocatoreService giocatoreService, SquadraService squadraService) {
        this.giocatoreService = giocatoreService;
        this.squadraService = squadraService;
    }

    // 1. MOSTRA FORM NUOVO GIOCATORE
    @GetMapping("/nuovo")
    public String mostraFormNuovoGiocatore(Model model) {
        model.addAttribute("giocatore", new Giocatore());
        // Passiamo la lista di tutte le squadre presenti nel DB per il menu a tendina
        model.addAttribute("squadre", squadraService.findAll());
        return "admin/giocatore-form";
    }

    // 2. SALVA GIOCATORE
    @PostMapping("/salva")
    public String salvaGiocatore(@ModelAttribute("giocatore") Giocatore giocatore) {
        giocatoreService.save(giocatore);
        return "redirect:/tornei";
    }

    // 3. MOSTRA FORM MODIFICA
    @GetMapping("/{id}/modifica")
    public String mostraFormModifica(@PathVariable("id") Long id, Model model) {
        model.addAttribute("giocatore", giocatoreService.findById(id));
        model.addAttribute("squadre", squadraService.findAll());
        return "admin/giocatore-form";
    }

    // 4. ELIMINA GIOCATORE
    @GetMapping("/{id}/elimina")
    public String eliminaGiocatore(@PathVariable("id") Long id) {
        giocatoreService.deleteById(id);
        return "redirect:/tornei";
    }
}