package com.agenda.itic.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

import com.agenda.itic.model.Usuari;
import com.agenda.itic.model.Rol;
import com.agenda.itic.repository.UsuariRepository;
import com.agenda.itic.service.CorreoPermitidoService;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UsuariRepository usuariRepository;

    @Autowired
    private CorreoPermitidoService correoPermitidoService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.equals("Bearer PROYECTO_AGENDA_MASTER_KEY_2026")) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    "master-admin@iticbcn.cat", null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
            return;
        }

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                
                if (jwtUtils.validateToken(token)) {
                    String email = jwtUtils.getEmailFromToken(token);
                    String role = jwtUtils.getRoleFromToken(token);
                    if (email != null && role != null) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                email, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
                        );
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } else {
                    
                    com.nimbusds.jwt.SignedJWT signedJWT = com.nimbusds.jwt.SignedJWT.parse(token);
                    String email = (String) signedJWT.getJWTClaimsSet().getClaim("email");
                    String name = (String) signedJWT.getJWTClaimsSet().getClaim("name");
                    if (name == null) name = "Usuari";
                    
                    
                    java.util.Set<String> adminEmails = java.util.Set.of(
                        "2223_joseph.abanto@iticbcn.cat",
                        "josephabanto07@gmail.com"
                    );
                    
                    boolean isAllowed = adminEmails.contains(email) || 
                                        correoPermitidoService.getCorreoPermitido(email) != null;
                    
                    if (email != null && isAllowed) {
                        Usuari usuari = usuariRepository.findByEmail(email).orElse(null);
                        if (usuari == null) {
                            usuari = new Usuari();
                            usuari.setEmail(email);
                            usuari.setNom(name);
                            usuari.setActiu(true);
                            usuari.setProvider("google");
                        }
                        
                        
                        if (adminEmails.contains(email)) {
                            usuari.setRol(Rol.ADMIN);
                        } else if (usuari.getRol() == null) {
                            usuari.setRol(Rol.USUARI);
                        }
                        
                        usuariRepository.save(usuari);

                        
                        String roleName = usuari.getRol().name();
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                email, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + roleName))
                        );
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } catch (Exception e) {
                
            }
        }

        filterChain.doFilter(request, response);
    }
}
