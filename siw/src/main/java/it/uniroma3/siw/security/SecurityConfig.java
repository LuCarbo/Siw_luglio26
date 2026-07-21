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

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(auth -> auth
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

                                                .requestMatchers("/admin/**").hasAuthority(RuoloUtente.ADMIN.name())

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
                                .csrf(csrf -> csrf.disable());

                return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}