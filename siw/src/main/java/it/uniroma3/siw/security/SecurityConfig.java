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

        private final CustomOAuth2UserService customOAuth2UserService;

        public SecurityConfig(CustomOAuth2UserService customOAuth2UserService) {
                this.customOAuth2UserService = customOAuth2UserService;
        }

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
                                                                "/squadre",
                                                                "/api/partite/**",
                                                                "/api/giocatori/**",
                                                                "/style.css",
                                                                "/*.css",
                                                                "/error")
                                                .permitAll()

                                                // 2. ROTTE ADMIN
                                                .requestMatchers("/admin/**").hasAuthority(RuoloUtente.ADMIN.name())

                                                // 3. TUTTO IL RESTO RICHIEDE IL LOGIN
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .defaultSuccessUrl("/", true)
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/")
                                                .permitAll())
                                .oauth2Login(oauth2 -> oauth2
                                                .loginPage("/login")
                                                .userInfoEndpoint(userInfo -> userInfo
                                                                .userService(customOAuth2UserService))
                                                .defaultSuccessUrl("/", true))
                                // Disabilitiamo il CSRF (fondamentale per le chiamate React)
                                .csrf(csrf -> csrf.disable());

                return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}