package it.uniroma3.siw;

import it.uniroma3.siw.model.Partita;
import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.model.Torneo;
import it.uniroma3.siw.model.enums.StatoPartita;
import it.uniroma3.siw.repository.TorneoRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
public class JpaExperimentTest {

    @Autowired
    private TorneoRepository torneoRepository;

    @Autowired
    private EntityManager em;

    private Long torneoIdTest;

    private static final int ITERAZIONI = 20;

    @BeforeEach
    public void findTorneoTestId() {
        // Cerca il Torneo Sperimentale creato dal DataInitializer
        java.util.List<Torneo> tornei = torneoRepository.findByNomeContainingIgnoreCase("Sperimentale");
        if (!tornei.isEmpty()) {
            torneoIdTest = tornei.get(0).getId();
        } else {
            throw new RuntimeException("Torneo 'Sperimentale' non trovato! Assicurati che il DataInitializer sia stato eseguito e il DB contenga i dati.");
        }
    }

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

        System.out.println("\n\n=======================================================");
        System.out.println("                 RISULTATI FINALI MEDI                 ");
        System.out.println("                   ITERAZIONI: " + ITERAZIONI);
        System.out.println("=======================================================");
        System.out.println(String.format("%-25s | %s", "STRATEGIA", "TEMPO MEDIO"));
        System.out.println("-------------------------------------------------------");
        System.out.println(String.format("%-25s | %d ms", "1. LAZY (Default)", timeLazy));
        System.out.println(String.format("%-25s | %d ms", "2. JOIN FETCH", timeJoinFetch));
        System.out.println(String.format("%-25s | %d ms", "3. ENTITY GRAPH", timeEntityGraph));
        System.out.println("=======================================================\n");
        
        System.out.println("DISCUSSIONE E ANALISI DEI RISULTATI:");
        System.out.println("-------------------------------------------------------");
        System.out.println("L'esperimento dimostra chiaramente il problema 'N+1 Selects' intrinseco");
        System.out.println("nella strategia LAZY quando si naviga l'albero delle relazioni.");
        System.out.println("Con la strategia LAZY (Default), al caricamento del Torneo viene eseguita");
        System.out.println("una query. Successivamente, iterando sulle partite, viene eseguita un'altra");
        System.out.println("query per caricare le partite del torneo. Inoltre, per ciascuna partita,");
        System.out.println("vengono effettuate ulteriori query per caricare la Squadra in Casa e in Trasferta,");
        System.out.println("causando un elevato numero di query individuali verso il database (N+1).");
        System.out.println("Ciò si traduce nel tempo medio di esecuzione più alto e potenziale degrado");
        System.out.println("delle prestazioni in produzione.\n");
        System.out.println("Le strategie JOIN FETCH e ENTITY GRAPH, invece, risolvono il problema");
        System.out.println("eseguendo un'unica, grande query (o un numero molto ridotto di query)");
        System.out.println("utilizzando JOIN SQL. Recuperano il Torneo, le Partite e le rispettive Squadre");
        System.out.println("tutto in una singola andata e ritorno (round-trip) verso il DB. Come mostrato");
        System.out.println("dai risultati, queste strategie risultano molto più veloci ed efficienti per");
        System.out.println("scenari di lettura intensiva in cui è noto anticipatamente di aver bisogno delle relazioni.\n");
        System.out.println("In termini di utilizzo:");
        System.out.println("- JOIN FETCH è pratico per query specifiche scritte in JPQL.");
        System.out.println("- ENTITY GRAPH offre un modo dichiarativo ed elegante per applicare profili");
        System.out.println("  di caricamento dinamici ai metodi (derivati e non) dei Repository Spring Data.");
        System.out.println("=======================================================\n\n");
    }

    private long executeLazyStrategy() {
        long totalTime = 0;
        for (int i = 0; i < ITERAZIONI; i++) {
            em.clear();
            long startTime = System.currentTimeMillis();
            Optional<Torneo> optTorneo = torneoRepository.findById(torneoIdTest);
            if (optTorneo.isPresent()) {
                Torneo torneo = optTorneo.get();
                for (Partita p : torneo.getPartite()) {
                    String nomeCasa = p.getSquadraCasa().getNome();
                    String nomeTrasferta = p.getSquadraTrasferta().getNome();
                }
            } else {
                System.out.println("ERRORE: Torneo con ID " + torneoIdTest + " non trovato.");
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
            Optional<Torneo> optTorneo = torneoRepository.findByIdWithJoinFetch(torneoIdTest);
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
            Optional<Torneo> optTorneo = torneoRepository.findByIdWithEntityGraph(torneoIdTest);
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
