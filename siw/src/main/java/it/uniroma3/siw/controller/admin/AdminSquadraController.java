package it.uniroma3.siw.controller.admin;

import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.model.Giocatore;
import it.uniroma3.siw.service.SquadraService;
import it.uniroma3.siw.service.GiocatoreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/squadre")
public class AdminSquadraController {

    private final SquadraService squadraService;
    private final GiocatoreService giocatoreService;

    public AdminSquadraController(SquadraService squadraService, GiocatoreService giocatoreService) {
        this.squadraService = squadraService;
        this.giocatoreService = giocatoreService;
    }

    @GetMapping("/nuova")
    public String mostraFormNuovaSquadra(Model model) {
        model.addAttribute("squadra", new Squadra());
        return "admin/squadra-form";
    }

    @PostMapping("/salva")
    public String salvaSquadra(@ModelAttribute("squadra") Squadra squadra) {
        squadraService.save(squadra);
        return "redirect:/tornei";
    }

    @GetMapping("/{id}/modifica")
    public String mostraFormModifica(@PathVariable("id") Long id, Model model) {
        model.addAttribute("squadra", squadraService.findById(id));
        model.addAttribute("giocatoriSvincolati", giocatoreService.findSvincolati());
        return "admin/squadra-form";
    }

    @GetMapping("/{id}/elimina")
    public String eliminaSquadra(@PathVariable("id") Long id) {
        squadraService.deleteById(id);
        return "redirect:/tornei";
    }

    @PostMapping("/{squadraId}/giocatori/esistente")
    public String aggiungiGiocatoreEsistente(@PathVariable("squadraId") Long squadraId,
            @RequestParam("giocatoreId") Long giocatoreId) {
        Squadra squadra = squadraService.findById(squadraId);
        Giocatore giocatore = giocatoreService.findById(giocatoreId);
        giocatore.setSquadra(squadra);
        giocatoreService.save(giocatore);
        return "redirect:/admin/squadre/" + squadraId + "/modifica";
    }

    @GetMapping("/{squadraId}/giocatori/{giocatoreId}/rimuovi")
    public String rimuoviGiocatore(@PathVariable("squadraId") Long squadraId,
            @PathVariable("giocatoreId") Long giocatoreId) {
        Giocatore giocatore = giocatoreService.findById(giocatoreId);
        giocatore.setSquadra(null);
        giocatoreService.save(giocatore);
        return "redirect:/admin/squadre/" + squadraId + "/modifica";
    }
}