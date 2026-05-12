package com.agenda.itic.controller;

import org.springframework.security.core.Authentication;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agenda.itic.model.Usuari;
import com.agenda.itic.service.CorreoPermitidoService;
import com.agenda.itic.service.UsuariService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/oauth/google")
@CrossOrigin(origins = "*")
public class OAuthController {
    
    @Autowired
    UsuariService usuariService;

    @Autowired
    CorreoPermitidoService correoPermitidoService;
    
    @Value("${app.frontend.url}")
    private String frontendUrl;
    
    /*@GetMapping("/home")
    public void home(Authentication authentication, HttpServletResponse response) {
        
        OAuth2User user = (OAuth2User) authentication.getPrincipal();

        String email = user.getAttribute("email");

        if (correoPermitidoService.getCorreoPermitido(email) == null) {
            try {
                response.sendRedirect(frontendUrl + "/login.html?error=unauthorized");
            } catch (IOException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
            return;
        }

        UsuariRequestDTO dto = new UsuariRequestDTO();
        dto.setEmail(email);
        dto.setNom(user.getAttribute("name"));  
        String providerName = "google";
        dto.setProvider(providerName);
        String providerId = user.getName();
        dto.setProviderId(providerId);
        Usuari usuari = usuariService.createOrUpdateOAuthUsuari(dto);


        if (usuari == null) {
            try {
                response.sendRedirect(frontendUrl + "/home");
            } catch (IOException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
            return;
        }

    
        try {
            response.sendRedirect(frontendUrl + "?token=");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }  */ 

    
    
}
