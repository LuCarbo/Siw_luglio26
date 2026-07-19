package it.uniroma3.siw;

import it.uniroma3.siw.model.Partita;
import it.uniroma3.siw.model.Torneo;
import it.uniroma3.siw.repository.TorneoRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class JpaExperimentTest {

    @Autowired
    private TorneoRepository torneoRepository;

    @Autowired
    private EntityManager em;

    // TODO: Inserisci qui l'ID di un torneo presente nel tuo DB che contenga
    // diverse partite!
    private static final Long TORNEO_ID_TEST = 1L;

    // Numero di volte che ripeteremo ogni test per avere una media affidabile
    private static final int ITERAZIONI = 20;

    @Test
    @Transactional
    public void runAllStrategiesAndSummarize() {
        System.out.println("\n\n=======================================================");
        System.out.println("INIZIO ESPERIMENTO JPA (" + ITERAZIONI + " iterazioni per strategia)");
        System.out.println("Attendere prego, esecuzione query in corso...");
        System.out.println("=======================================================\n\n");

        long timeLazy = executeLazyStrategy();
        long timeJoinFetch = executeJoinFetchStrategy();
        long timeEntityGraph = executeEntityGraphStrategy();

        System.out.println("\n\n\n\n=======================================================");
        System.out.println("                 RISULTATI FINALI MEDI                 ");
        System.out.println("                   ITERAZIONI: " + ITERAZIONI);
        System.out.println("=======================================================");
        System.out.println(String.format("%-25s | %s", "STRATEGIA", "TEMPO MEDIO"));
        System.out.println("-------------------------------------------------------");
        System.out.println(String.format("%-25s | %d ms", "1. LAZY (Default)", timeLazy));
        System.out.println(String.format("%-25s | %d ms", "2. JOIN FETCH", timeJoinFetch));
        System.out.println(String.format("%-25s | %d ms", "3. ENTITY GRAPH", timeEntityGraph));
        System.out.println("=======================================================\n\n\n\n");
    }

    private long executeLazyStrategy() {
        long totalTime = 0;
        for (int i = 0; i < ITERAZIONI; i++) {
            em.clear();
            long startTime = System.currentTimeMillis();
            Optional<Torneo> optTorneo = torneoRepository.findById(TORNEO_ID_TEST);
            if (optTorneo.isPresent()) {
                Torneo torneo = optTorneo.get();
                for (Partita p : torneo.getPartite()) {
                    String nomeCasa = p.getSquadraCasa().getNome();
                    String nomeTrasferta = p.getSquadraTrasferta().getNome();
                }
            } else {
                System.out.println("ERRORE: Torneo con ID " + TORNEO_ID_TEST + " non trovato.");
                return 0;
            }
            long endTime = System.currentTimeMillis();
            totalTime += (endTime - startTime);
        }
        return totalTime / ITERAZIONI;
    }

    private long executeJoinFetchStrategy() {
        long totalTime = 0;
        for (int i = 0; i < ITERAZIONI; i++) {
            em.clear();
            long startTime = System.currentTimeMillis();
            Optional<Torneo> optTorneo = torneoRepository.findByIdWithJoinFetch(TORNEO_ID_TEST);
            if (optTorneo.isPresent()) {
                Torneo torneo = optTorneo.get();
                for (Partita p : torneo.getPartite()) {
                    String nomeCasa = p.getSquadraCasa().getNome();
                    String nomeTrasferta = p.getSquadraTrasferta().getNome();
                }
            }
            long endTime = System.currentTimeMillis();
            totalTime += (endTime - startTime);
        }
        return totalTime / ITERAZIONI;
    }

    private long executeEntityGraphStrategy() {
        long totalTime = 0;
        for (int i = 0; i < ITERAZIONI; i++) {
            em.clear();
            long startTime = System.currentTimeMillis();
            Optional<Torneo> optTorneo = torneoRepository.findByIdWithEntityGraph(TORNEO_ID_TEST);
            if (optTorneo.isPresent()) {
                Torneo torneo = optTorneo.get();
                for (Partita p : torneo.getPartite()) {
                    String nomeCasa = p.getSquadraCasa().getNome();
                    String nomeTrasferta = p.getSquadraTrasferta().getNome();
                }
            }
            long endTime = System.currentTimeMillis();
            totalTime += (endTime - startTime);
        }
        return totalTime / ITERAZIONI;
    }
}
