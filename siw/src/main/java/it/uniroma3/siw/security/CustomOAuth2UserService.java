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
            throw new OAuth2AuthenticationException(ex.getMessage());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        String providerId = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        if (email == null) {
            throw new RuntimeException("Email non trovata dall'OAuth2 provider");
        }

        // Se il nome non è presente, uso l'email
        if (name == null || name.trim().isEmpty()) {
            name = email.split("@")[0];
        }

        Optional<Utente> userOptional = utenteRepository.findByProviderId(providerId);
        Utente utente;

        if (userOptional.isPresent()) {
            utente = userOptional.get();
            // Username con il nome Google
            utente.setUsername(name);
            utente = utenteRepository.save(utente);
        } else {
            // L'utente esiste già con password locale
            Optional<Utente> localUser = utenteRepository.findByUsername(email);
            if (localUser.isPresent()) {
                // L'utente esiste già con password locale
                utente = localUser.get();
                utente.setProvider(AuthProvider.GOOGLE);
                utente.setProviderId(providerId);
                utente.setUsername(name);
                utente = utenteRepository.save(utente);
            } else {
                // Registriamo un nuovo utente
                utente = new Utente();
                //Nome di Google come username per la visualizzazione
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
                "name");
    }
}
