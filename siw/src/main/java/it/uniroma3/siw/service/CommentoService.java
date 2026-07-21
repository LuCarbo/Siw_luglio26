package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Commento;
import it.uniroma3.siw.model.Partita;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.repository.CommentoRepository;
import it.uniroma3.siw.repository.PartitaRepository;
import it.uniroma3.siw.repository.UtenteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class CommentoService {

    private final CommentoRepository commentoRepository;
    private final PartitaRepository partitaRepository;
    private final UtenteRepository utenteRepository;

    public CommentoService(CommentoRepository commentoRepository,
            PartitaRepository partitaRepository,
            UtenteRepository utenteRepository) {
        this.commentoRepository = commentoRepository;
        this.partitaRepository = partitaRepository;
        this.utenteRepository = utenteRepository;
    }

    @Transactional(readOnly = true)
    public List<Commento> findByPartitaId(Partita partita) {
        return commentoRepository.findByPartitaOrderByDataCreazioneDesc(partita);
    }

    @Transactional
    public Commento aggiungiCommento(Long partitaId, Long utenteId, String testo, MultipartFile fileImmagine)
            throws IOException {
        Partita partita = partitaRepository.findById(partitaId)
                .orElseThrow(() -> new RuntimeException("Partita non trovata"));
        Utente autore = utenteRepository.findById(utenteId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        Commento commento = new Commento();
        commento.setTesto(testo);
        commento.setPartita(partita);
        commento.setAutore(autore);

        if (fileImmagine != null && !fileImmagine.isEmpty()) {
            commento.setImmagine(fileImmagine.getBytes());
        }

        return commentoRepository.save(commento);
    }

    @Transactional
    public Commento modificaCommento(Long commentoId, Long utenteIdRichiedente, String nuovoTesto,
            MultipartFile nuovaImmagine, boolean rimuoviImmagine) throws IOException {
        Commento commento = commentoRepository.findById(commentoId)
                .orElseThrow(() -> new RuntimeException("Commento non trovato"));

        if (!commento.getAutore().getId().equals(utenteIdRichiedente)) {
            throw new RuntimeException(
                    "Operazione non autorizzata: non puoi modificare il commento di un altro utente.");
        }

        commento.setTesto(nuovoTesto);

        if (rimuoviImmagine) {
            commento.setImmagine(null);
        } else if (nuovaImmagine != null && !nuovaImmagine.isEmpty()) {
            commento.setImmagine(nuovaImmagine.getBytes());
        }

        return commentoRepository.save(commento);
    }

}