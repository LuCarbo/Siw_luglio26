package it.uniroma3.siw.bootstrap;

import it.uniroma3.siw.model.Giocatore;
import it.uniroma3.siw.model.Partita;
import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.model.Torneo;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.model.enums.StatoPartita;
import it.uniroma3.siw.repository.GiocatoreRepository;
import it.uniroma3.siw.repository.PartitaRepository;
import it.uniroma3.siw.repository.SquadraRepository;
import it.uniroma3.siw.repository.TorneoRepository;
import it.uniroma3.siw.repository.UtenteRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.security.crypto.password.PasswordEncoder;

@Component
public class DataInitializer implements CommandLineRunner {

    private final TorneoRepository torneoRepository;
    private final SquadraRepository squadraRepository;
    private final PartitaRepository partitaRepository;
    private final UtenteRepository utenteRepository;
    private final GiocatoreRepository giocatoreRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(TorneoRepository torneoRepository,
            SquadraRepository squadraRepository,
            PartitaRepository partitaRepository,
            UtenteRepository utenteRepository,
            GiocatoreRepository giocatoreRepository,
            PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.utenteRepository = utenteRepository;
        this.torneoRepository = torneoRepository;
        this.squadraRepository = squadraRepository;
        this.partitaRepository = partitaRepository;
        this.giocatoreRepository = giocatoreRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        // Verifica se il database è vuoto
        if (torneoRepository.count() == 0) {
            System.out.println("Inizializzazione dati di test in corso...");

            // 0. Creazioni Utenti
            if (utenteRepository.count() == 0) {
                System.out.println("Creazione utenti di sistema...");

                Utente admin = new Utente();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123")); // Cripta la password!
                admin.setRuolo(it.uniroma3.siw.model.enums.RuoloUtente.ADMIN);

                Utente user = new Utente();
                user.setUsername("mario");
                user.setPassword(passwordEncoder.encode("user123"));
                user.setRuolo(it.uniroma3.siw.model.enums.RuoloUtente.USER);

                utenteRepository.save(admin);
                utenteRepository.save(user);
            }

            // 1. Creazione delle Squadre
            Squadra sq1 = new Squadra();
            sq1.setNome("Atletico Roma");
            sq1.setCitta("Roma");
            sq1.setAnnoFondazione(2015);

            Squadra sq2 = new Squadra();
            sq2.setNome("Real Milano");
            sq2.setCitta("Milano");
            sq2.setAnnoFondazione(2018);

            Squadra sq3 = new Squadra();
            sq3.setNome("Napoli United");
            sq3.setCitta("Napoli");
            sq3.setAnnoFondazione(2020);

            squadraRepository.save(sq1);
            squadraRepository.save(sq2);
            squadraRepository.save(sq3);

            // 1.5 Creazione dei Giocatori
            System.out.println("Creazione giocatori per le squadre...");

            // Giocatori Atletico Roma
            giocatoreRepository
                    .save(creaGiocatore("Marco", "Rossi", "Attaccante", LocalDate.of(1995, 6, 12), 1.82, sq1));
            giocatoreRepository
                    .save(creaGiocatore("Andrea", "Bianchi", "Centrocampista", LocalDate.of(1998, 3, 22), 1.78, sq1));
            giocatoreRepository.save(creaGiocatore("Luca", "Verdi", "Difensore", LocalDate.of(1992, 11, 5), 1.88, sq1));

            // Giocatori Real Milano
            giocatoreRepository
                    .save(creaGiocatore("Giovanni", "Brambilla", "Portiere", LocalDate.of(1990, 1, 15), 1.92, sq2));
            giocatoreRepository
                    .save(creaGiocatore("Matteo", "Colombo", "Attaccante", LocalDate.of(1997, 8, 30), 1.85, sq2));
            giocatoreRepository.save(
                    creaGiocatore("Alessandro", "Ferrari", "Centrocampista", LocalDate.of(1999, 12, 10), 1.75, sq2));

            // Giocatori Napoli United
            giocatoreRepository
                    .save(creaGiocatore("Ciro", "Esposito", "Attaccante", LocalDate.of(1996, 5, 20), 1.77, sq3));
            giocatoreRepository
                    .save(creaGiocatore("Gennaro", "Russo", "Difensore", LocalDate.of(1994, 9, 14), 1.86, sq3));
            giocatoreRepository
                    .save(creaGiocatore("Antonio", "Romano", "Centrocampista", LocalDate.of(2001, 2, 28), 1.80, sq3));

            // 2. Creazione dei Tornei
            Torneo t1 = new Torneo();
            t1.setNome("Torneo di Primavera");
            t1.setAnno(2026);
            t1.setDescrizione("Il torneo amatoriale più atteso della capitale.");
            t1.getSquadre().add(sq1);
            t1.getSquadre().add(sq3);

            Torneo t2 = new Torneo();
            t2.setNome("Coppa Italia Amatori");
            t2.setAnno(2026);
            t2.setDescrizione("Competizione nazionale per le migliori squadre dilettantistiche.");
            t2.getSquadre().add(sq1);
            t2.getSquadre().add(sq2);
            t2.getSquadre().add(sq3);

            Torneo tSperimentale = new Torneo();
            tSperimentale.setNome("Torneo Test JPA");
            tSperimentale.setAnno(2026);
            tSperimentale.setDescrizione("Torneo con 200 partite per testare le prestazioni JPA.");
            tSperimentale.getSquadre().add(sq1);
            tSperimentale.getSquadre().add(sq2);
            tSperimentale.getSquadre().add(sq3);

            torneoRepository.save(t1);
            torneoRepository.save(t2);
            torneoRepository.save(tSperimentale);

            // 3. Generazione di 20 Partite per i tornei standard
            System.out.println("Generazione di 20 partite di test...");
            Random random = new Random();
            Squadra[] squadre = { sq1, sq2, sq3 };
            Torneo[] tornei = { t1, t2 };

            for (int i = 1; i <= 20; i++) {
                Partita p = new Partita();
                p.setTorneo(tornei[i % 2]); // Alterna tra t1 e t2

                // Seleziona due squadre diverse in modo ciclico
                int idxCasa = i % 3;
                int idxTrasferta = (i + 1) % 3;
                p.setSquadraCasa(squadre[idxCasa]);
                p.setSquadraTrasferta(squadre[idxTrasferta]);

                // Imposta date da 10 giorni fa a 10 giorni nel futuro
                p.setDataOra(LocalDateTime.now().plusDays(i - 10));
                p.setLuogo("Campo Comunale " + (i % 3 + 1));

                // Prime 10 partite già giocate, ultime 10 programmate
                if (i <= 10) {
                    p.setStato(StatoPartita.PLAYED);
                    p.setGoalsHome(random.nextInt(5)); // Goal da 0 a 4
                    p.setGoalsAway(random.nextInt(5));
                } else {
                    p.setStato(StatoPartita.SCHEDULED);
                }

                partitaRepository.save(p);
            }

            // 4. Generazione di 200 Partite per l'esperimento N+1 nel Torneo Sperimentale
            System.out.println("Generazione di partite per il Torneo Sperimentale...");
            for (int i = 1; i <= 200; i++) {
                Partita p = new Partita();
                p.setTorneo(tSperimentale);
                int idxCasa = i % 3;
                int idxTrasferta = (i + 1) % 3;
                p.setSquadraCasa(squadre[idxCasa]);
                p.setSquadraTrasferta(squadre[idxTrasferta]);
                p.setDataOra(LocalDateTime.now().plusDays(i));
                p.setStato(StatoPartita.SCHEDULED);
                partitaRepository.save(p);
            }

            System.out.println("Inizializzazione completata con successo!");
        } else {
            System.out.println("Il database contiene già dei dati. Inizializzazione saltata.");
        }
    }

    // Metodo privato di supporto per creare un giocatore in modo pulito e veloce
    private Giocatore creaGiocatore(String nome, String cognome, String ruolo, LocalDate dataNascita, Double altezza,
            Squadra squadra) {
        Giocatore g = new Giocatore();
        g.setNome(nome);
        g.setCognome(cognome);
        g.setRuolo(ruolo);
        g.setDataNascita(dataNascita);
        g.setAltezza(altezza);
        g.setSquadra(squadra);
        return g;
    }
}