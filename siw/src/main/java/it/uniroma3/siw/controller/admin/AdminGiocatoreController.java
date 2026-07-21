package it.uniroma3.siw.controller.admin;

import it.uniroma3.siw.model.Giocatore;
import it.uniroma3.siw.service.GiocatoreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/giocatori")
public class AdminGiocatoreController {

    private final GiocatoreService giocatoreService;

    public AdminGiocatoreController(GiocatoreService giocatoreService) {
        this.giocatoreService = giocatoreService;
    }

    @GetMapping("/nuovo")
    public String mostraFormNuovoGiocatore(Model model) {
        model.addAttribute("giocatore", new Giocatore());
        return "admin/giocatore-form";
    }

    @PostMapping("/salva")
    public String salvaGiocatore(@ModelAttribute("giocatore") Giocatore giocatore) {
        giocatoreService.save(giocatore);
        return "redirect:/tornei"; 
    }
}
