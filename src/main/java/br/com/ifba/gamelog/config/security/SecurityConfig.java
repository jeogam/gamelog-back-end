package br.com.ifba.gamelog.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTLoginFilter jwtLoginFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults()) // Ativa o CORS (essencial para Front-end)
                .csrf(AbstractHttpConfigurer::disable) // Desativa CSRF (padrão para APIs REST)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Sem sessão (Stateless)
                .authorizeHttpRequests(authorize -> authorize
                        // 1. Libera Swagger e Documentação (Essencial para testes)
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // 2. Libera Login (AuthController)
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()

                        // 3. Libera Cadastro de Usuário (UsuarioController)
                        // AQUI ESTAVA O POSSÍVEL ERRO: Tem que bater com o @RequestMapping do Controller
                        .requestMatchers(HttpMethod.POST, "/api/v1/usuarios/usuario").permitAll()

                        // 4. Libera Leitura Pública (Jogos, Avaliações, Health Check)
                        .requestMatchers(HttpMethod.GET, "/api/v1/jogos/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/avaliacoes/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/health").permitAll()

                        // 5. Libera endpoint de erro do Spring (Evita 403 em stacktraces)
                        .requestMatchers("/error").permitAll()

                        // 6. Preflight Requests (CORS)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Qualquer outra rota exige autenticação
                        .anyRequest().authenticated()
                )
                // Adiciona o filtro JWT antes do filtro padrão de usuário/senha
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