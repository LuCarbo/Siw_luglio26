package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.service.SquadraService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class SquadraController {

    private final SquadraService squadraService;

    public SquadraController(SquadraService squadraService) {
        this.squadraService = squadraService;
    }

    @GetMapping("/squadre")
    public String getAllSquadre(Model model) {
        model.addAttribute("squadre", squadraService.findAll());
        return "squadre";
    }

    // visualizzazione squadra e giocatori
    @GetMapping("/squadra/{id}")
    public String getSquadra(@PathVariable("id") Long id, Model model) {
        Squadra squadra = squadraService.findById(id);
        model.addAttribute("squadra", squadra);
        return "squadra";
    }
}