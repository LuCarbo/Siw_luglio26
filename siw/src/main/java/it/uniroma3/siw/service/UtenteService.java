package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.repository.UtenteRepository;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UtenteService {

    private final UtenteRepository utenteRepository;
//    private final PasswordEncoder passwordEncoder; // Strumento di Spring Security per criptare le password

    public UtenteService(UtenteRepository utenteRepository /*,PasswordEncoder passwordEncoder*/) {
        this.utenteRepository = utenteRepository;
//        this.passwordEncoder = passwordEncoder;
    }

    // ==============================================
    // OPERAZIONI DI LETTURA
    // ==============================================

    @Transactional(readOnly = true)
    public Utente findById(Long id) {
        return utenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
    }

    @Transactional(readOnly = true)
    public Utente findByUsername(String username) {
        return utenteRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utente non trovato con username: " + username));
    }

    @Transactional(readOnly = true)
    public List<Utente> findAll() {
        return utenteRepository.findAll();
    }

    // ==============================================
    // OPERAZIONI DI SCRITTURA (REGISTRAZIONE E GESTIONE)
    // ==============================================

    /*@Transactional
    public Utente registraUtente(Utente utente) {
        // 1. Cripta la password in formato BCrypt prima di salvarla nel Database
        String passwordCriptata = passwordEncoder.encode(utente.getPassword());
        utente.setPassword(passwordCriptata);

        // 2. Assegna il ruolo di base. Chi si registra dal sito è sempre un "USER".
        // (Gli ADMIN li creerai tu a mano dal DB oppure tramite una funzione dedicata).
        if (utente.getRuolo() == null || utente.getRuolo().isBlank()) {
            utente.setRuolo("USER");
        }

        // 3. Salva nel database
        return utenteRepository.save(utente);
    }

    // Un metodo utile se vuoi creare un pannello per promuovere gli utenti
    @Transactional
    public void promuoviAdAdmin(Long utenteId) {
        Utente utente = findById(utenteId);
        utente.setRuolo("ADMIN");
        utenteRepository.save(utente);
    }
    */
}