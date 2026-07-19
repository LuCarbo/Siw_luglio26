package it.uniroma3.siw.security;

import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.model.enums.AuthProvider;
import it.uniroma3.siw.model.enums.RuoloUtente;
import it.uniroma3.siw.repository.UtenteRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UtenteRepository utenteRepository;

    public CustomOAuth2UserService(UtenteRepository utenteRepository) {
        this.utenteRepository = utenteRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        try {
            return processOAuth2User(userRequest, oAuth2User);
        } catch (Exception ex) {
            // Log the exception
            throw new OAuth2AuthenticationException(ex.getMessage());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        String providerId = oAuth2User.getAttribute("sub"); // Google returns "sub" as the unique ID
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name"); // Google name (e.g. Mario Rossi)

        if (email == null) {
            throw new RuntimeException("Email non trovata dall'OAuth2 provider");
        }

        // Se il nome non è presente, usiamo l'email (o la prima parte dell'email)
        if (name == null || name.trim().isEmpty()) {
            name = email.split("@")[0];
        }

        Optional<Utente> userOptional = utenteRepository.findByProviderId(providerId);
        Utente utente;

        if (userOptional.isPresent()) {
            utente = userOptional.get();
            // Aggiorniamo l'username con il nome Google per mostrarlo al posto della mail
            utente.setUsername(name);
            utente = utenteRepository.save(utente);
        } else {
            // Controlliamo se esiste già un utente con questa email registrato localmente
            Optional<Utente> localUser = utenteRepository.findByUsername(email);
            if (localUser.isPresent()) {
                // L'utente esiste già con password locale, potremmo collegare l'account
                utente = localUser.get();
                utente.setProvider(AuthProvider.GOOGLE);
                utente.setProviderId(providerId);
                utente.setUsername(name); // Sovrascriviamo l'email con il nome
                utente = utenteRepository.save(utente);
            } else {
                // Registriamo un nuovo utente
                utente = new Utente();
                // Usiamo il nome di Google come username per la visualizzazione
                utente.setUsername(name);
                utente.setProvider(AuthProvider.GOOGLE);
                utente.setProviderId(providerId);
                utente.setRuolo(RuoloUtente.USER);
                utente = utenteRepository.save(utente);
            }
        }

        GrantedAuthority authority = new SimpleGrantedAuthority(utente.getRuolo().name());

        Map<String, Object> attributes = new java.util.HashMap<>(oAuth2User.getAttributes());
        attributes.put("name", name);

        return new DefaultOAuth2User(
                Collections.singletonList(authority),
                attributes,
                "name" // Usa 'name' come chiave principale così sec:authentication="name" mostra il
                       // nome!
        );
    }
}
