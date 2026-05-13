package com.agenda.itic.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agenda.itic.dto.UsuariResponseDto;
import com.agenda.itic.dto.UsuariTokenDto;
import com.agenda.itic.exception.BadRequestException;
import com.agenda.itic.exception.ResourceNotFoundException;
import com.agenda.itic.model.Rol;
import com.agenda.itic.model.Usuari;
import com.agenda.itic.repository.UsuariRepository;

@Service
public class UsuariService {

    @Autowired
    UsuariRepository usuariRepository;

    @Autowired
    CorreoPermitidoService correoPermitidoService;

    private UsuariResponseDto toDTO(Usuari usuari) {
        return new UsuariResponseDto(
                usuari.getId(),
                usuari.getNom(),
                usuari.getEmail(),
                usuari.getRol().name(),
                usuari.getRol().getPermisos().stream().toList(),
                usuari.getFotoPerfil()
        );
    }

    public List<UsuariResponseDto> getUsuaris() {
        return usuariRepository.findAll().stream().map(user -> toDTO(user)).toList();
    }

    public List<UsuariResponseDto> getUsuarisActius(boolean actiu) {
        return usuariRepository.findByActiu(actiu).stream().map(user -> toDTO(user)).toList();
    }

    public List<UsuariResponseDto> getUsuarisProfes() {
        return usuariRepository.findByRol(Rol.PROFESSOR).stream().map(user -> toDTO(user)).toList();
    }


    // ELIMINAR PARA FINAL
    private Usuari mapToUsuari(UsuariTokenDto usuariRequestDTO) {
        Usuari usuari = new Usuari();
        usuari.setNom(usuariRequestDTO.getNom());
        usuari.setEmail(usuariRequestDTO.getEmail());
        usuari.setRol(getRol(usuariRequestDTO.getEmail()));
        usuari.setActiu(true);
        usuari.setProvider(usuariRequestDTO.getProvider() != null ? usuariRequestDTO.getProvider() : "local");
        usuari.setProviderId(usuariRequestDTO.getProviderId());
        usuari.setFotoPerfil(usuariRequestDTO.getFotoPerfil());
        return usuari;
    }

    private Rol getRol(String email) {
        if (!email.contains("@iticbcn.cat") && !email.equals("josephabanto07@gmail.com")) {
            throw new BadRequestException("Només s'accepten correus de l'ITIC BCN.");
        }

        // Lista de administradores maestros (coincide con SecurityConfig)
        java.util.Set<String> adminEmails = java.util.Set.of(
            "2223_joseph.abanto@iticbcn.cat",
            "josephabanto07@gmail.com"
        );

        if (adminEmails.contains(email)) {
            return Rol.ADMIN;
        }

        try {
            if (correoPermitidoService.getCorreoPermitido(email) != null) {
                return Rol.ADMIN;
            }
        } catch (ResourceNotFoundException e) {
            // No está en la lista blanca de la DB
        }
            
        if (!email.split("@")[0].contains("_")) {
            return Rol.PROFESSOR;
        }

        return Rol.USUARI;
    }   

    // ELIMINAR PARA FINAL
    public UsuariResponseDto createUsuari(UsuariTokenDto usuariRequestDTO) {
        Rol rol = getRol(usuariRequestDTO.getEmail());
        Usuari usuari = mapToUsuari(usuariRequestDTO);
        usuari.setRol(rol);
        return toDTO(usuariRepository.save(usuari));
    }

    // Crearemos un usuario o no, a traves de un token
    public UsuariResponseDto createOrUpdateUsuariFromToken(String token) {
        System.out.println("DEBUG: Iniciando autenticación con token...");
        String email = normalizeEmail(getTokenEmail(token));
        System.out.println("DEBUG: Email extraído del token: " + email);

        if (email == null) {
            System.err.println("ERROR: No se pudo extraer el email del token.");
            throw new BadRequestException("No se pudo extraer el email del token de autenticación.");
        }

        Optional<Usuari> usuariOptional = usuariRepository.findByEmail(email);
        Usuari user;
        if (usuariOptional.isPresent()) {
            System.out.println("DEBUG: Usuario ya existente: " + email);
            user = usuariOptional.get();
        } else {
            System.out.println("DEBUG: Creando nuevo usuario: " + email);
            user = new Usuari();
            user.setEmail(email);
            user.setNom(getTokenName(token) == null ? "Usuari" : getTokenName(token));
            user.setFotoPerfil(getTokenPicture(token));
            user.setActiu(true);
            user.setProvider(getTokenProvider(token) == null ? "google" : getTokenProvider(token));
            user.setProviderId(getTokenProviderId(token));
        }
        
        user.setRol(getRol(email));
        user = usuariRepository.save(user);
        System.out.println("DEBUG: Autenticación exitosa para: " + email + " con rol: " + user.getRol());
        return toDTO(user);
    }

    public UsuariResponseDto updateUsuari(Long id, UsuariTokenDto usuariRequestDTO) {
        Usuari usuari = usuariRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuari no trobat"));
            
        usuari.setNom(usuariRequestDTO.getNom());
        usuari.setEmail(usuariRequestDTO.getEmail());
        usuari.setRol(getRol(usuariRequestDTO.getEmail()));
        usuari.setActiu(true);
        usuari.setProvider(
                usuariRequestDTO.getProvider() != null ? usuariRequestDTO.getProvider() : usuari.getProvider());
        usuari.setProviderId(usuariRequestDTO.getProviderId() != null ? usuariRequestDTO.getProviderId()
                : usuari.getProviderId());
        if (usuariRequestDTO.getFotoPerfil() != null) {
            usuari.setFotoPerfil(usuariRequestDTO.getFotoPerfil());
        }
        return toDTO(usuariRepository.save(usuari));
    }

    public void deleteUsuari(Long id) {
        Usuari usuari = usuariRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuari no trobat"));
        usuariRepository.delete(usuari);
    }

    private String getTokenEmail(String token) {
        return extractClaim(token, "email");
    }

    private String getTokenName(String token) {
        return extractClaim(token, "name");
    }

    private String getTokenPicture(String token) {
        return extractClaim(token, "picture");
    }

    private String getTokenProviderId(String token) {
        return extractClaim(token, "providerId");
    }

    private String getTokenProvider(String token) {
        return "google"; 
    }




    private String extractClaim(String token, String claimName) {
        try {
            String payload = decodeTokenPayload(token);
            if (payload == null) return null;
            
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.JsonNode node = mapper.readTree(payload);
            
            if (node.has(claimName)) {
                return node.get(claimName).asText();
            }
            
            // Soporte para claims anidados o alternativos en Cognito
            if (claimName.equals("providerId")) {
                if (node.has("sub")) return node.get("sub").asText();
                if (node.has("cognito:username")) return node.get("cognito:username").asText();
            }
            
            return null;
        } catch (Exception e) {
            System.err.println("ERROR al extraer claim " + claimName + ": " + e.getMessage());
            return null;
        }
    }

    private String decodeTokenPayload(String token) {
        try {
            String normalizedToken = token.startsWith("Bearer ") ? token.substring(7).trim() : token;
            String[] parts = normalizedToken.split("\\.");
            if (parts.length < 2) {
                System.err.println("ERROR: Token malformado (menos de 2 partes)");
                return null;
            }
            byte[] decoded = Base64.getUrlDecoder().decode(parts[1]);
            return new String(decoded, StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.err.println("ERROR al decodificar payload del token: " + e.getMessage());
            return null;
        }
    }


    private String normalizeEmail(String email) {
        if (email == null) {
            return null;
        }
        String normalized = email.trim().toLowerCase(Locale.ROOT);
        return normalized.isBlank() ? null : normalized;
    }

}
