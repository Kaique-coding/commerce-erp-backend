package com.commerce.apirest.config;

import java.io.IOException;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import com.commerce.apirest.enums.PerfilUsuario;
import com.commerce.apirest.repository.UsuarioRepository;
import com.commerce.apirest.service.TokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            TokenService tokenService,
            UsuarioRepository usuarioRepository) throws Exception {

        return http
                .cors(cors -> {})
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, exception) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED)))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/login", "/api/usuario").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/produtos").permitAll()
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/api/pedido/*/status")
                            .hasAuthority(PerfilUsuario.USUARIO_INTERNO.name())
                        .requestMatchers("/api/pedidos/**", "/api/pedido/**")
                            .hasAnyAuthority(PerfilUsuario.CLIENTE.name(), PerfilUsuario.USUARIO_INTERNO.name())
                        .anyRequest().hasAuthority(PerfilUsuario.USUARIO_INTERNO.name()))
                .addFilterBefore(new JwtAuthenticationFilter(tokenService, usuarioRepository),
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /** Valida o token e carrega o perfil atual do usuário no banco. */
    private static final class JwtAuthenticationFilter extends OncePerRequestFilter {

        private final TokenService tokenService;
        private final UsuarioRepository usuarioRepository;

        private JwtAuthenticationFilter(TokenService tokenService, UsuarioRepository usuarioRepository) {
            this.tokenService = tokenService;
            this.usuarioRepository = usuarioRepository;
        }

        @Override
        protected void doFilterInternal(
                HttpServletRequest request,
                HttpServletResponse response,
                FilterChain filterChain) throws ServletException, IOException {

            String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authorization != null && authorization.startsWith("Bearer ")
                    && SecurityContextHolder.getContext().getAuthentication() == null) {
                String token = authorization.substring(7).trim();
                if (tokenService.validateToken(token)) {
                    String subject = tokenService.extractSubject(token);
                    usuarioRepository.findByEmailPrincipal(subject)
                            .filter(usuario -> Boolean.TRUE.equals(usuario.getAtivo()))
                            .filter(usuario -> usuario.getPerfilUsuario() != null)
                            .ifPresent(usuario -> {
                                var authority = new SimpleGrantedAuthority(usuario.getPerfilUsuario().name());
                                var authentication = new UsernamePasswordAuthenticationToken(
                                        subject, null, List.of(authority));
                                SecurityContextHolder.getContext().setAuthentication(authentication);
                            });
                }
            }

            filterChain.doFilter(request, response);
        }
    }
}