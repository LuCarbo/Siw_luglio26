package it.uniroma3.siw.security;

import it.uniroma3.siw.model.enums.RuoloUtente;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

// METODO NUOVO: Dice a Spring Security di ignorare le risorse statiche
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // 1. ROTTE PUBBLICHE 
                .requestMatchers(
                        "/", 
                        "/tornei", 
                        "/torneo/**", 
                        "/partita/**", 
                        "/css/**", 
                        "/js/**", 
                        "/login", 
                        "/api/esperimento",
                        "/squadra/**",
                        "/api/partite/**",
                        "/api/giocatori/**", // <-- ECCO QUELLA CHE MANCAVA! Sblocca React
                        "/style.css",
                        "/*.css"
                ).permitAll()
                
                // 2. ROTTE ADMIN
                .requestMatchers("/admin/**").hasAuthority(RuoloUtente.ADMIN.name())
                
                // 3. TUTTO IL RESTO RICHIEDE IL LOGIN 
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login") 
                .defaultSuccessUrl("/", true) 
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll()
            )
            // Disabilitiamo il CSRF (fondamentale per le chiamate React)
            .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}