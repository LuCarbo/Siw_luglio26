package it.uniroma3.siw.controller.admin;

import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.service.SquadraService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/squadre")
public class AdminSquadraController {

    private final SquadraService squadraService;

    public AdminSquadraController(SquadraService squadraService) {
        this.squadraService = squadraService;
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
        return "admin/squadra-form";
    }

    @GetMapping("/{id}/elimina")
    public String eliminaSquadra(@PathVariable("id") Long id) {
        squadraService.deleteById(id);
        return "redirect:/tornei";
    }
}