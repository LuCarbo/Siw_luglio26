package it.uniroma3.siw.controller.rest;

import it.uniroma3.siw.service.PartitaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EsperimentoController {

    private final PartitaService partitaService;

    public EsperimentoController(PartitaService partitaService) {
        this.partitaService = partitaService;
    }

    // Aggiungiamo produces = "text/html" per far capire al browser che stiamo inviando una pagina web
    @GetMapping(value = "/api/esperimento", produces = "text/html")
    public String avviaEsperimento() {
        StringBuilder html = new StringBuilder();

        // Aggiungiamo un po' di stile CSS per renderlo bello da vedere
        html.append("<html><head><style>")
            .append("body { font-family: Arial, sans-serif; margin: 40px; background-color: #f4f7f6; }")
            .append(".report-box { background-color: white; border: 1px solid #ddd; padding: 30px; border-radius: 10px; max-width: 600px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); }")
            .append(".highlight { color: #d9534f; font-weight: bold; font-size: 1.2em; }")
            .append(".success { color: #28a745; font-weight: bold; font-size: 1.2em; }")
            .append("hr { border: 0; border-top: 1px solid #eee; margin: 20px 0; }")
            .append("</style></head><body>");

        html.append("<div class='report-box'>");
        html.append("<h2 style='color: #333;'>📊 Risultati Esperimento Prestazionale</h2><hr>");

        // 1. TEST N+1 (Strategia LAZY)
        long inizioLazy = System.currentTimeMillis();
        partitaService.eseguiTestLazy();
        long tempoLazy = System.currentTimeMillis() - inizioLazy;
        html.append("<p>🔴 1. Strategia <b>LAZY</b> (N+1 Problem): <br><span class='highlight'>")
            .append(tempoLazy).append(" ms</span></p>");

        // 2. TEST EAGER (Strategia con EntityGraph)
        long inizioEager = System.currentTimeMillis();
        partitaService.eseguiTestEager();
        long tempoEager = System.currentTimeMillis() - inizioEager;
        html.append("<p>🟠 2. Strategia <b>EAGER</b> (@EntityGraph): <br><span class='highlight'>")
            .append(tempoEager).append(" ms</span></p>");

        // 3. TEST OTTIMIZZATO (Strategia JOIN FETCH)
        long inizioOttimizzato = System.currentTimeMillis();
        partitaService.eseguiTestOttimizzato();
        long tempoOttimizzato = System.currentTimeMillis() - inizioOttimizzato;
        html.append("<p>🟢 3. Strategia <b>OTTIMIZZATA</b> (JOIN FETCH): <br><span class='success'>")
            .append(tempoOttimizzato).append(" ms</span></p>");

        // CONCLUSIONI
        html.append("<hr><h3 style='color: #0056b3;'>📈 Conclusioni</h3>");
        
        // Calcolo per evitare divisioni per zero se il test è stato troppo veloce
        long tempoSicuro = tempoOttimizzato > 0 ? tempoOttimizzato : 1; 
        long ratio = tempoLazy > 0 ? (tempoLazy / tempoSicuro) : 0;
        
        html.append("<p>Il metodo <b>JOIN FETCH</b> è risultato circa <b>")
            .append(ratio)
            .append(" volte più veloce</b> del caricamento LAZY standard.</p>");

        html.append("</div></body></html>");

        return html.toString();
    }
}