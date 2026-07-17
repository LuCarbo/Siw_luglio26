package it.uniroma3.siw.controller.rest;

import it.uniroma3.siw.model.Partita;
import it.uniroma3.siw.service.PartitaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/partite")
// Consente a React (che di solito gira su una porta diversa, es. http://localhost:5173) 
// di fare chiamate verso il backend Spring Boot senza blocchi di sicurezza del browser
@CrossOrigin(origins = "*") 
public class PartitaRestController {

    private final PartitaService partitaService;

    public PartitaRestController(PartitaService partitaService) {
        this.partitaService = partitaService;
    }

    // 1. Endpoint per dare a React la lista di tutte le partite del calendario
    // GET http://localhost:8080/api/partite
    @GetMapping
    public List<Partita> getTutteLePartite() {
        // Usiamo il metodo ottimizzato JOIN FETCH che abbiamo creato per l'esperimento prestazionale!
        return partitaService.findAllOttimizzato();
    }

    // 2. Endpoint per aggiornare il risultato (la chiamata che farà il form di React)
    // PUT http://localhost:8080/api/partite/1/risultato
    // Riceve i dati come JSON nel corpo della richiesta (RequestBody)
    @PutMapping("/{id}/risultato")
    public ResponseEntity<?> aggiornaRisultato(
            @PathVariable("id") Long id,
            @RequestBody RisultatoDTO risultato) {
        
        try {
            Partita partitaAggiornata = partitaService.impostaRisultato(
                    id, 
                    risultato.getGoalsHome(), 
                    risultato.getGoalsAway()
            );
            return ResponseEntity.ok(partitaAggiornata); // Restituisce HTTP 200 e la partita aggiornata
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage()); // Restituisce HTTP 404 se la partita non esiste
        }
    }
}

// Un piccolo record o classe di supporto (DTO - Data Transfer Object) 
// per mappare il JSON in arrivo da React
class RisultatoDTO {
    private int goalsHome;
    private int goalsAway;

    // Getter e Setter
    public int getGoalsHome() { return goalsHome; }
    public void setGoalsHome(int goalsHome) { this.goalsHome = goalsHome; }
    public int getGoalsAway() { return goalsAway; }
    public void setGoalsAway(int goalsAway) { this.goalsAway = goalsAway; }
}