package it.uniroma3.siw.controller;

import it.uniroma3.siw.dto.RigaClassifica;
import it.uniroma3.siw.model.Partita;
import it.uniroma3.siw.model.Torneo;
import it.uniroma3.siw.service.ClassificaService;
import it.uniroma3.siw.service.PartitaService;
import it.uniroma3.siw.service.TorneoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class TorneoController {

    private final TorneoService torneoService;
    private final PartitaService partitaService; 
    private final ClassificaService classificaService;

    public TorneoController(TorneoService torneoService, PartitaService partitaService, ClassificaService classificaService) {
        this.classificaService = classificaService;
        this.torneoService = torneoService;
        this.partitaService = partitaService;
    }

    // 1. Rotta per la lista di tutti i tornei
    @GetMapping({"/", "/tornei"})
    public String getTornei(Model model) {
        // ... il tuo codice esistente per recuperare i tornei ...
        model.addAttribute("tornei", torneoService.findAll());
        return "tornei"; // o il nome del tuo file html
    }

    // 2. UNICO METODO per mostrare il dettaglio del torneo e il suo calendario
    @GetMapping("/torneo/{id}")
    public String getTorneoDettaglio(@PathVariable("id") Long id, Model model) {
        Torneo torneo = torneoService.findById(id);
        List<Partita> partite = partitaService.findByTorneoId(id);
        
        // CHIAMIAMO LA LOGICA DI BUSINESS
        List<RigaClassifica> classifica = classificaService.generaClassifica(id);
        
        model.addAttribute("torneo", torneo);
        model.addAttribute("partite", partite);
        model.addAttribute("classifica", classifica); // Passiamo la classifica all'HTML
        
        return "torneo"; 
    }

}