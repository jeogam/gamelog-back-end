package br.com.ifba.gamelog.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JWTLoginFilter jwtLoginFilter;

    public SecurityConfig(JWTLoginFilter jwtLoginFilter) {
        this.jwtLoginFilter = jwtLoginFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        // Endpoints Públicos
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/usuarios/usuario").permitAll()

                        // Leitura pública (opcional, remova se quiser privado)
                        .requestMatchers(HttpMethod.GET, "/api/v1/jogos/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/avaliacoes/**").permitAll()

                        // --- EXEMPLO DE PERMISSÃO POR ROLE ---
                        // Apenas ADMIN pode deletar usuários.
                        // hasRole adiciona "ROLE_" automaticamente, então verifica "ROLE_ADMINISTRADOR"
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/usuarios/**").hasRole("ADMINISTRADOR")

                        // Qualquer outra requisição precisa estar autenticada
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtLoginFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}